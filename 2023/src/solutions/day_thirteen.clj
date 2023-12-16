(ns day-thirteen
  (:require [clojure.string :as str]
            [lib.solution-registry :refer [def-solution]]
            [clojure.math :refer [floor ceil]]))

(def example "#.##..##.
..#.##.#.
##......#
##......#
..#.##.#.
..##..##.
#.#.##.#.

#...##..#
#....#..#
..##..###
#####.##.
#####.##.
..##..###
#....#..#

##.##.##.##
.#.##.##.##
..#.##..##.
###....#..#
#..#.###...
#..#.###...
###....#..#")

(defn test-for-palindrome [line start end]
  (let [->c (partial nth line)]
    (loop [start start
           end end]
      (cond
        (< end start) true
        (not= (->c start)
              (->c end)) false
        :else (recur (inc start) (dec end))))))

(defn get-longest-palindromes [line start end]
  (let [do-if #(if % (%2 %3) %3)
        search-end? (= start 0)
        search-start? (= end (-> line count dec))]
    (loop [cur-start start
           cur-end end]
      (let [start-pal? (test-for-palindrome line start cur-end)
            end-pal? (test-for-palindrome line cur-start end)]
        (cond
          (or (= cur-end start) (= cur-start end)) [nil]
          (or start-pal? end-pal?) (filter some? (conj #{} (when end-pal? [cur-start end]) (when start-pal? [start cur-end])))
          :else (recur (do-if search-start? inc cur-start) (do-if search-end? dec cur-end)))))))

(defn columns-to-the-left [[start end]]
  (let [length (- (inc end) start)
        distance (-> length (/ 2) floor int)]
    (+ start distance)))

(defn string->grids [s]
  (->> (str/split-lines s)
       (map (comp #(str/split % #"") str/trim))
       (partition-by (partial = [""]))
       (filter (partial not= '([""])))))

(defn palindrome-length [[start end]]
  (if (and start end)
    (- (inc end) start)
    0))

(defn get-shortest-palindromes [pseq]
  (let [by-length (->> pseq
                       (group-by palindrome-length))
        shortest (apply min (keys by-length))]
    (get by-length shortest)))

(defn grid->palindrome [g]
  (loop [[[start end] & others] [[0  (-> g first count dec)]]]
    (let [palindromes (->> (mapcat #(let [res (get-longest-palindromes % start end)] res) g) (into #{}))
          short-pals (get-shortest-palindromes palindromes)]
      (cond
        (= 0 (palindrome-length (first short-pals))) nil
        (= 1 (count palindromes)) (first short-pals)
        :else (recur (concat others short-pals))))))

(defn cols->rows [g]
  (map (fn [col]
         (map #(get % col) g))
       (range 0 (-> g first count))))

(defn get-print-info [pal]
  (let [pal-len (palindrome-length pal)]
    [pal-len (when (> pal-len 0) (dec (int (+ (ceil (/ pal-len 2)) (first pal)))))]))

(defn add-left-column-indicator [col-pals grids]
  (map (fn [pal g]
         (let [[pal-len position] (get-print-info pal)
               marker (if (= 0 (mod pal-len 2)) ">" "V")]
           (if (some? pal)
             (let [indicator-row (->> (range 0 (-> g first count))
                                      (map #(if (= % position) marker " ")))]
               (cons indicator-row g))
             g))) col-pals grids))

(defn add-top-row-indicator [row-pals grids]
  (map (fn [pal g]
         (let [[pal-len position] (get-print-info pal)
               marker (if (= 0 (mod pal-len 2)) "V" "<")]
           (if (some? pal)
           (map-indexed (fn [i row] (conj (apply vector row) (if (= i position) marker " "))) g)
           g))) row-pals grids))

(defn grids->string [grids]
  (->> grids
       (map (fn [g]
              (->> (map str/join g)
                   (str/join "\n"))))
       (str/join "\n\n")))

(defn print-palindromes [grids]
  (let [col-pals (map grid->palindrome grids) 
        row-pals (map (comp grid->palindrome cols->rows) grids)]
    (->> (add-top-row-indicator row-pals grids)
         (add-left-column-indicator col-pals)
         (grids->string)
         (spit "./output/day_thirteen.txt"))
    (+ (->> col-pals
            (filter some?)
            (map columns-to-the-left)
            (reduce + 0))
       (->> row-pals
            (filter some?)
            (map columns-to-the-left)
            (reduce + 0)
            (* 100)))))

(defn cols->rows [g]
  (map (fn [col]
         (map #(get % col) g))
       (range 0 (-> g first count))))

(defn part-one [s]
  (let [grids (->> s (string->grids))] 
    (print-palindromes grids)))

(def-solution
  (part-one (slurp "./input/day_thirteen.txt")))

(part-one (slurp "./input/day_thirteen.txt"))