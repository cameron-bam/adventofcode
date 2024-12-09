(ns day3
  (:require [clojure.string :as str]))

(def input (slurp "src/day3.txt"))

(def part-1 (->> (re-seq #"mul\([\d]{1,3},[\d]{1,3}\)" input)
                 (map #(map (fn [i] (Integer/parseInt i 10)) (re-seq #"[\d]+" %)))
                 (map (partial apply *))
                 (apply +)))

(defn get-enabled-ops [ops]
  (loop [[op & ops] ops
         out []
         enabled true]
    (if (nil? op)
      out
      (case op
        "do()" (recur ops out true)
        "don't()" (recur ops out false)
        (recur ops (cond-> out enabled (conj op)) enabled)))))


(def part-2 (->> (re-seq #"do\(\)|don't\(\)|mul\([\d]{1,3},[\d]{1,3}\)" input)
                 get-enabled-ops
                 (map #(map (fn [i] (Integer/parseInt i 10)) (re-seq #"[\d]+" %)))
                 (map (partial apply *))
                 (apply +)))