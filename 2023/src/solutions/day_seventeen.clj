(ns day-seventeen
  (:require [clojure.data.priority-map :refer [priority-map]]
            [lib.grid :as grid]
            [lib.solution-registry :refer [def-solution]]
            [examples :refer [day-seventeen]]))

(def reverse-velocity (comp (partial apply vector)
                   (partial map (partial * -1))))

(def rotate-velocity (comp (partial apply vector) reverse))

(defn get-min-heat-lost [visited pos]
  (try (->> (:heat-loss (get visited pos))
            #_vals
            #_(mapcat vals)
            #_(apply min))
       (catch Exception _ Integer/MAX_VALUE)))

(defn should-visit? [visited last-pos {:keys [pos heat-loss]}]
  (let [last-heat-loss (:heat-loss (get visited pos))]
    (or (nil? last-heat-loss)
        (< (+ heat-loss (:heat-loss (get visited last-pos))) last-heat-loss))))

(defn make-queue
  ([] (clojure.lang.PersistentQueue/EMPTY))
  ([coll]
   (reduce conj clojure.lang.PersistentQueue/EMPTY coll)))

(defn get-next-nodes [grid paths pos vel start end]
  (reduce (fn ([acc _]
               (let [{:keys [node heat-loss consec-count]} (or (peek acc)
                                                               {:node pos
                                                                :heat-loss 0
                                                                :consec-count (if (= (get-in paths [pos :vel]) vel)
                                                                                (get-in paths [pos :consec-count])
                                                                                0)})
                     next-pos (grid/get-neighbor node vel)]
                 (conj acc {:node next-pos
                            :visited-from node
                            :vel vel
                            :consec-count (inc consec-count)
                            :heat-loss (when-let [next-heat-loss (get-in grid next-pos)]
                                         (+ heat-loss next-heat-loss))}))))
          [] (range start (inc end))))

(defn get-path [paths pos]
  (let [visited-from (get-in paths [pos :visited-from])]
    (if visited-from
      (lazy-seq
       (cons pos (get-path paths visited-from)))
      (list pos))))

(def velocities [[1 0] [0 1] [-1 0] [0 -1]])

(defn plot-path [grid paths start]
  (reduce (fn [grid step]
            (assoc-in grid step (if (= start step)'S 'X))) grid (get-path paths start)))

(defn find-min-heat-loss [min-distance max-distance grid]
  (prn "Starting! Dims:" (count grid) (-> grid first count))
  (loop [distances (assoc (reduce (fn [acc coord]
                                    (assoc acc coord Integer/MAX_VALUE))
                                  (priority-map)
                                  (grid/coords grid))
                          [0 0] 0)
         paths {}
         visited {}
         iters 0]
    (let [[pos heat-loss] (first distances)]
      (if (or (nil? pos) (= 'i heat-loss) (> iters 4))
        (do
          (prn "Done! Iters:" iters)
          (clojure.pprint/pprint
           #_(plot-path grid paths [12 12])
             (reduce (fn [grid start]
                       (plot-path grid paths start))
                     grid
                     [[3 1]
                      [1 3]]
                     #_(->> distances 
                          (filter #(not= (second %) Integer/MAX_VALUE))
                          (map first))))
          (clojure.pprint/pprint
           #_grid
           (reduce (fn [grid step]
                     (let [distance (if-let [distance (get visited step)]
                                      [distance]
                                      (let [distance (get distances step)]
                                        (if (= distance Integer/MAX_VALUE) 'i distance)))]
                       (assoc-in grid step distance)))
                   grid (grid/coords grid)))
          (get visited [12 12]))
        (let [#_#_{:keys [pos vel]} (peek queue)
              _ (when (= (mod iters 100000) 0)
                  (prn "Progress report. Iters:" iters "Visited:" (count visited))) 
              [distances paths] (transduce
                                 (comp
                                  (mapcat #(get-next-nodes grid paths pos % min-distance max-distance))
                                  (filter (comp not visited :node))
                                  (filter (comp (partial get-in grid) :node))
                                  (filter #(<= (:consec-count %) 3)))
                                 (fn
                                   ([acc] acc)
                                   ([[distances paths] {node :node vel :vel visited-from :visited-from consec-count :consec-count heat-loss-inc :heat-loss}]
                                    (let [last-distance (get distances node)
                                          distance (+ heat-loss heat-loss-inc)]
                                      (if (< distance last-distance)
                                        [(assoc distances node distance)
                                         (assoc paths node {:visited-from visited-from
                                                            :vel vel
                                                            :consec-count consec-count})]
                                        [distances paths]))))
                                 [(dissoc distances pos) paths] velocities)]
          (recur distances paths (assoc visited pos heat-loss) (inc iters)))))))

(defn part-one [s]
  (->> s
       grid/str->grid
       (grid/grid-map #(Integer/parseInt % 10))
       (find-min-heat-loss 1 10)))

(def-solution
  (part-one (slurp "./input/day_seventeen.txt")))

(time (part-one day-seventeen))

