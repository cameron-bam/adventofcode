(ns day-thirteen
  (:require [clojure.string :as str]
            [lib.solution-registry :refer [def-solution]]
            [clojure.math :refer [floor ceil]]))

(defn test-for-palindrome [line start end]
  (let [->c (partial nth line)]
    (loop [start start
           end end]
      (cond
        (< end start) true
        (not= (->c start)
              (->c end)) false
        :else (recur (inc start) (dec end))))))

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

(defn find-possible-palindromes [g]
  (let [start 0 end (-> g count dec)]
    (loop [cur-start start cur-end end found #{}]
      (if (or (= cur-end start) (= cur-start end))
        found
        (recur (inc cur-start)
               (dec cur-end)
               (->> [[start cur-end] [cur-start end]]
                    (map #(vector % (and (= (get g (first %))
                                            (get g (second %)))
                                         (= 0 (mod (palindrome-length %) 2)))))
                    (reduce #(if (second %2) (conj %1 (first %2)) %1) found)))))))

(defn grid->palindrome [g]
  (->> g find-possible-palindromes (filter (partial apply test-for-palindrome g)) first))

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

(defn print-palindromes [grids row-pals col-pals]
  (->> (add-top-row-indicator row-pals grids)
       (add-left-column-indicator col-pals)
       (grids->string)
       (spit "./output/day_thirteen.txt")))

(defn part-one [s]
  (let [grids (->> s (string->grids))
        row-pals (map grid->palindrome grids)
        col-pals (map (comp grid->palindrome cols->rows) grids)
        sum-cols #(->> % (filter some?) (map columns-to-the-left) (reduce + 0))]
    (print-palindromes grids row-pals col-pals)
    (+ (sum-cols col-pals)
       (* 100 (sum-cols row-pals)))))

(def-solution
  (part-one (slurp "./input/day_thirteen.txt")))