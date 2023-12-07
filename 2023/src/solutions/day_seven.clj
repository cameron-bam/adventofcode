(ns day-seven
  (:require [clojure.string :as str]
            [lib.solution-registry :refer [def-solution]]))

(defn vec->ix-lookup [v]
  (->> (map-indexed (fn [ix item]
                      (vector item ix)) v)
       (flatten)
       (apply hash-map)))

(def hand-ranks
  (vec->ix-lookup [{5 1}
                   {4 1
                    1 1}
                   {3 1
                    2 1}
                   {3 1
                    1 2}
                   {2 2
                    1 1}
                   {2 1
                    1 3}
                   {1 5}]))

(def card-ranks (vec->ix-lookup ["A" "K" "Q" "J" "T" "9" "8" "7" "6" "5" "4" "3" "2" "1"]))

(defn hand->rank [hand]
  (let [hand-class (->> (clojure.string/split hand #"")
                        (group-by identity)
                        vals
                        (group-by count)
                        (map (fn [[k v]]
                               (vector k (count v))))
                        (into {}))]
    (get hand-ranks hand-class)))

(defn compare-with [f & vals]
  (->> vals
       (map f)
       (apply compare)))

(defn dispatch [part _] part)

(defmulti solve #'dispatch)

(defmethod solve :part-one [_ hands]
  (->> (sort (fn [{x :cards} {y :cards}]
               (let [res (compare-with hand->rank y x)]
                 (if (not= 0 res)
                   res
                   (compare-with #(->> (str/split % #"")
                                       (map (fn [card]
                                              (get card-ranks card)))
                                       (apply vector)) y x)))) hands)
       (map-indexed (fn [ix {:keys [bid]}]
                      (* (+ 1 ix) bid)))
       (reduce + 0)))

(defn -main [file part & _] 
  (->> (slurp file)
       (str/split-lines)
       (map #(->> (str/split % #" ")
                  (mapcat (fn [k v]
                            (vector k v)) [:cards :bid])
                  (apply hash-map)))
       (map (fn [hand] (update hand :bid #(Integer/parseInt % 10))))
       (solve part)))

(def-solution
  (-main "./input/day_seven.txt" :part-one))