(ns day4
  (:require [clojure.string :as str]))

(def input (->> (slurp "src/day4.txt") 
                str/split-lines))

(defn get-columns [rows]
  (let [columns (take (-> rows first seq count) (repeatedly vector))]
    (->> (reduce (fn [acc next] (map #(conj %1 %2) acc next)) columns rows)
         (map (partial apply str)))))

(defn get-diagonals [rows]
  (let [columns (take (-> rows first seq count dec) (repeatedly vector))]
    (->> (reduce (fn [acc next]
                   (let [acc (conj acc [])
                         next-acc (map #(conj %1 %2) acc next)]
                     (concat next-acc (drop (count next) acc)))) columns rows)
         (map (partial apply str)))))

(defn indexes-of [s value]
  (loop [ix (str/index-of s value)
         indexes []]
    (if (nil? ix)
      indexes
      (recur (str/index-of s value (inc ix)) (conj indexes ix)))))

(defn word-indexes [s word]
  (into (indexes-of s word) (indexes-of s (str/reverse word))))

(def part-1 (->> (concat input
                         (get-columns input)
                         (get-diagonals input)
                         (get-diagonals (map str/reverse input)))
                 (mapcat #(word-indexes % "XMAS"))
                 count))

(defn move [delta start] (map + delta start))
(defn reflect-coords [[y x] size] [y (- size x)])

(defn coordinates-of [start shift s value]
  (let [ixes (indexes-of s value)
        v-len (count value)]
    (map (fn [ix]
           (let [start (move [ix ix] start)]
             (map vector value (take v-len (iterate (partial move shift) start))))) ixes)))

(defn word-coordinates [start shift s value]
  (into (coordinates-of start shift s value)
        (coordinates-of start shift s (str/reverse value))))

(defn get-diagonal-word-coords [input]
  (let [diagonals (get-diagonals input)]
    (loop [[diagonal & diagonals] diagonals
           out []
           move-up? true
           coords [(count input) 0]]
      (if (nil? diagonal)
        out
        (let [coords (move (if move-up? [-1 0] [0 1]) coords)]
          (recur diagonals (into out (word-coordinates coords [1 1] diagonal "MAS")) (and move-up? (not= coords [0 0])) coords))))))

(def part-2 (->> (into (get-diagonal-word-coords input)
                       (map (fn [word]
                              (map (fn [letter]
                                     (update letter 1 reflect-coords (dec (count input)))) word))
                            (get-diagonal-word-coords (map str/reverse input))))
                 (map second)
                 (group-by second)
                 (filter #(> (-> % second count) 1))
                 count))


