(ns day1
  (:require [clojure.string :as str]))


(def input (->> (slurp "./src/day1.txt")
                (str/split-lines)
                (map #(let [[a b] (str/split % #"   ")] [a b]))
                (reduce #(let [[a b] %1 [c d] %2] [(conj a c) (conj b d)]) [[] []])
                (map #(map (fn [i] (Integer/parseInt i 10)) %))))

(def part-1 (->> input
                 (map sort)
                 (apply map #(abs (- %1 %2)))
                 (reduce +)))

(def part-2 ((fn [[left right]]
               (let [freq (reduce (fn [acc next]
                                    (update acc next (fn [cur] (if (int? cur) (inc cur) 1)))) {} right)]
                 (reduce (fn [acc next]
                           (+ acc (* next (get freq next 0)))) 0 left))) input))