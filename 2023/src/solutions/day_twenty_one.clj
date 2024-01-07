(ns day-twenty-one
  (:require [lib.grid :as g]
            [lib.solution-registry :refer [def-solution]]
            [examples :refer [day-twenty-one]]))


(g/str->grid day-twenty-one)

(defn find-start [g]
  (->> (for [i (range 0 (count g))
             j (range 0 (count (first g)))]
         [[i j] (get-in g [i j])])
       (some #(when (= "S" (second %)) %))))


(defn get-next-steps [{:keys [grid] :as context}]
  (update context :coords (partial into #{} (comp (mapcat #(->> (g/get-neighbors % [[-1 0] [0 -1] [1 0] [0 1]])
                                                                (filter (fn [pos]
                                                                          (let [val (get-in grid pos)]
                                                                            (and (some? val) (not= "#" val)))))))))))

(defn get-next-steps-infinite-map
  [{:keys [grid] :as context}]
  (update context :coords 
          (partial into #{}
                   (comp (mapcat #(->> (g/get-neighbors % [[-1 0] [0 -1] [1 0] [0 1]])
                                       (filter (fn [[i j]]
                                                 (let [val (get-in grid
                                                                   [(mod i (count grid))
                                                                    (mod j (count (first grid)))])]
                                                   (and (some? val) (not= "#" val)))))))))))

(defn part-one [s]
  (let [grid (g/str->grid s)
        [start] (find-start grid)]
    (->> (iterate get-next-steps {:grid grid :coords #{start}}) 
         (drop 64)
         first
         :coords 
         count)))

(defn part-two [s]
  (let [grid (g/str->grid s)
        [start] (find-start grid)]
    (->> (iterate get-next-steps-infinite-map {:grid grid :coords #{start}})
         (drop 500)
         first :coords count)))

(def-solution
  (part-one (slurp "./input/day_twenty_one.txt")))