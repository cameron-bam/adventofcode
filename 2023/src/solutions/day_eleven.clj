(ns day-eleven
  (:require [clojure.string :as str]
            [lib.solution-registry :refer [def-solution]]))

(defn nested-vector->grid [coll]
  {:rows (count coll)
   :cols (count (first coll))
   :grid (->> (map-indexed (fn [i row]
                             (map-indexed (fn [j val]
                                            {:key [i j] :val val}) row)) coll)
              flatten
              (mapcat (fn [{:keys [key val]}]
                        (vector key val)))
              (apply hash-map))})

(defn get-expanded-space [{:keys [rows cols grid] :as input}]
  (let [rows (into #{} (range 0 rows))
        cols (into #{} (range 0 cols))]
    (merge input (reduce (fn [acc [k v]]
                           (if (= "#" v)
                             (-> acc
                                 (update :empty-rows disj (-> k first))
                                 (update :empty-cols disj (-> k second)))
                             acc))
                         {:empty-rows rows :empty-cols cols} grid))))

(defn get-galaxies [{:keys [grid] :as input}]
  (assoc input :galaxies (reduce (fn [acc [k v]]
                                   (if (= "#" v)
                                     (conj acc k)
                                     acc)) #{} grid)))

(defn get-pairs [coll]
  (->> (for [x coll
             y coll]
         (when-not (= x y)
           #{x y}))
       (filter some?)
       (reduce conj #{})))

(defn get-galaxy-pairs [{:keys [galaxies] :as input}]
  (assoc input :galaxy-pairs (get-pairs galaxies)))

(defn get-neighbors [grid [i j]]
  (->> [[(dec i) j]
        [(inc i) j]
        [i (dec j)]
        [i (inc j)]]
       (filter (partial contains? grid))))

(defn dijkstras [{:keys [grid empty-rows empty-cols]} expanding-factor start]
  (let [get-increment #(let [[row col] %1
                             [row_i col_i] %2]
                         (if (or
                              (and (not= row row_i)
                                   (empty-rows row_i))
                              (and (not= col col_i)
                                   (empty-cols col_i))) expanding-factor 1))]
    (loop [visited {start 0}
           [{:keys [id distance] :as node} & nodes] (->> (get-neighbors grid start)
                                                         (map #(hash-map :id % :distance (get-increment start %))))]
      (if (nil? node)
        visited
        (let [next-nodes (->> (get-neighbors grid id)
                              (filter (comp not (partial contains? visited)))
                              (map #(hash-map :id % :distance (+ distance (get-increment id %)))))]
          (recur (reduce (fn [visited {:keys [id distance]}]
                           (assoc visited id distance)) visited next-nodes)
                 (concat nodes next-nodes)))))))


(defn compute-shortest-paths [expanding-factor {:keys [galaxies] :as input}]
  (assoc input :dijkstras (->> galaxies
                               (mapcat #(vector % (dijkstras input expanding-factor %)))
                               (apply hash-map))))

(defn sum-shortest-paths [{:keys [dijkstras galaxy-pairs]}]
  (->> (map #(get-in dijkstras (into [] %)) galaxy-pairs)
       (reduce + 0)))


(defn parse-input [s expanding-factor]
  (->> s
       (str/split-lines)
       (map #(str/split % #""))
       nested-vector->grid
       get-expanded-space
       get-galaxies
       get-galaxy-pairs 
       (compute-shortest-paths expanding-factor)
       sum-shortest-paths))

(def-solution
  (parse-input (slurp "./input/day_eleven.txt") 2)
  (parse-input (slurp "./input/day_eleven.txt") 1000000))