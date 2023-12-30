(ns lib.grid
  (:require [clojure.string :as str]))


(defn str->grid [s]
  (->> s str/trim str/split-lines (map (comp #(str/split % #"") str/trim)) (apply vector)))

(defn cols->rows [g]
  (map (fn [col]
         (map #(nth % col) g))
       (range 0 (-> g first count))))

(defn rotate [g direction]
  (case direction
    -1 (->> g cols->rows reverse)
    0 g
    1 (->> g cols->rows (map reverse))))

(defn grid->str [g]
  (str/join "\n" (map str/join g)))

(defn get-neighbor [pos vel]
  (into [] (map + pos vel)))

(defn get-neighbors [pos velocities]
  (map #(get-neighbor % pos) velocities))

(defn valid-neighbors? [grid]
  (filter #(get-in grid %)))

(defn row-diff [a* b*]
  (->> (map vector a* b*)
       (map-indexed vector)
       (reduce (fn [diff [i [a b]]]
                 (if (not= a b)
                   (conj diff i)
                   diff)) #{})))

(defn grid-diff [g_a g_b]
  (->> (map vector g_a g_b)
       (map-indexed vector)
       (reduce (fn [diff [i rows]]
                 (into diff (->> rows
                                 (apply row-diff)
                                 (map (partial vector i))))) #{})))

(defn coords [grid]
  (for [i (range 0 (count grid))
        j (range 0 (count (first grid)))]
    [i j]))

(defn grid-map [f grid]
  (reduce (fn [grid pos]
            (update-in grid pos f))
          grid
          (coords grid)))