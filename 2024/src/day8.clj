(ns day8
  (:require [clojure.string :as str]))

(def input (->> (slurp "src/day8.txt")
                (str/split-lines)
                (mapv #(str/split % #""))))

(def coords-list (->> input
                      (map-indexed (fn [y row] (map-indexed (fn [x v] [v [y x]]) row)))
                      (apply concat)))

(defn antinode [x y]
  (map - (map * [2 2] x) y))

(defn antinodes [& args]
  [(apply antinode args) (apply antinode (reverse args))])

(defn permutations [values size]
  (let [seqs (map vector values)]
    (if (= size 1)
      seqs
      (mapcat #(map (fn [seq] (into seq %)) (permutations values (dec size))) seqs))))

(defn combos [values size]
  (->> (permutations values size) (map set) set (filter #(= size (count %)))))

(def part-1 (->> (-> (group-by first coords-list)
                     (dissoc "."))
                 (mapcat (fn [[_ antennae]] (mapcat (partial apply antinodes) (combos (map second antennae) 2))))
                 (filter #(get-in input %))
                 set
                 count))