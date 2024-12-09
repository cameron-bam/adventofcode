(ns day2
  (:require [clojure.string :as str]))

(def input (->> (slurp "src/day2.txt")
                (str/split-lines)
                (map #(str/split % #" "))
                (map #(map (fn [i] (Integer/parseInt i 10)) %))))

(defn safe? [levels]
  (let [in-range? #(and (>= % 1) (<= % 3))]
    (loop [[cur & levels] levels
           direction 0]
      (if-not (seq levels)
        :safe
        (let [distance (- cur (first levels))
              new-direction (if (not= 0 distance) (/ distance (abs distance)) 0)]
          (when-not
           (or (and (not= new-direction direction) (not= direction 0) (not= new-direction 0))
               (not (in-range? (abs distance))))
            (recur levels new-direction)))))))

(def part-1 (->> (map safe? input)
                 (filter some?)
                 count))

(defn drop-at [i seq]
  (concat (take i seq) (drop (inc i) seq)))

(defn safe-part-2? [levels]
  (if (safe? levels)
    :safe
    (loop [[cur & rest] (range 0 (count levels))]
      (when cur
        (if (safe? (drop-at cur levels))
          :safe
          (recur rest))))))

(def part-2 (->> (map safe-part-2? input)
                 (filter some?)
                 count))