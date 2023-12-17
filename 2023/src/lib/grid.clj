(ns lib.grid
  (:require [clojure.string :as str]))


(defn str->grid [s]
  (->> s str/trim str/split-lines (map (comp #(str/split % #"") str/trim))))

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