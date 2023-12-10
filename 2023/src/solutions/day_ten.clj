(ns day-ten
  (:require [clojure.string :as str]
            [lib.solution-registry :refer [def-solution]]))

(defn tile->node [i j tile]
  (let [north [(dec i) j]
        south [(inc i) j]
        east [i (inc j)]
        west [i (dec j)]]
    {:id [i j]
     :tile tile
     :pointers (case tile
                 "." #{}
                 "S" #{}
                 "|" #{north south}
                 "-" #{east west}
                 "L" #{north east}
                 "J" #{north west}
                 "7" #{south west}
                 "F" #{south east})}))

(defn connect-start [{:keys [grid start] :as pipe-map}]
  (assoc-in pipe-map [:grid start :pointers]
            (->> grid
                 vals
                 (filter #((:pointers %) start))
                 (map :id)
                 (apply hash-set))))

(defn safe-max [& args]
  (reduce max 0 (filter some? args)))

(defn parse-input [input]
  (->> input
       (str/split-lines)
       (map #(str/split % #""))
       (map-indexed
        (fn [i row]
          (map-indexed
           (fn [j tile]
             (tile->node i j tile)) row)))
       (flatten)
       (reduce (fn [acc {:keys [id tile] :as node}]
                 (-> acc
                     (assoc-in [:grid id] node)
                     (update :max-row safe-max (inc (first id)))
                     (update :max-col safe-max (inc (second id)))
                     (merge (when (= tile "S")
                              {:start id})))) {:grid {}})
       connect-start))

(defn add-distances [{:keys [grid start] :as pipe-map}]
  (let [grid (atom grid)
        _ (swap! grid assoc-in [start :distance] 0)]
    (loop [[node-id & queue] [start]]
      (when node-id
        (let [{:keys [distance pointers]} (get @grid node-id)
              next-distance (inc distance)
              pointers (filter (comp #(or
                                       (nil? %)
                                       (> % next-distance)) #(get-in @grid [% :distance])) pointers)]
          (run! #(swap! grid assoc-in [% :distance] next-distance) pointers)
          (recur (into (or queue []) pointers)))))
    (assoc pipe-map :grid @grid)))

(defn get-max-distance [{:keys [grid]}]
  (->> grid vals (map :distance) (apply safe-max)))

(defn pipe-map->str [{:keys [grid max-row max-col]}]
  (let [string-acc (atom "")]
    (doall
     (for [i (range 0 max-row)]
       (swap! string-acc str (->> (range 0 max-col)
                                  (map (comp
                                        #(or (:distance %)
                                             (:tile %))
                                        (partial get grid)
                                        #(vector i %)))
                                  (apply str)) "\n")))
    @string-acc))


(defn- main [filename part & _]
  (->> (slurp filename)
       parse-input
       add-distances
       get-max-distance))


(def-solution
  (main "./input/day_ten.txt" :part-one))