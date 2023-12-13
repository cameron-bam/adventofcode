(ns day-ten
  (:require [clojure.string :as str]
            [lib.solution-registry :refer [def-solution]]))

(defmacro sym-map
  [& body]
  (let [args# (mapcat #(do
                        (when-not (symbol? %)
                          (throw (ex-info "UnsupportedArgument - sym-map only suports symbol args" {})))
                        (vector (-> % str keyword) %)) body)]
    `(hash-map ~@args#)))

(defn invert-map [m]
  (->> m (mapcat reverse) (apply hash-map)))

(defn start+turns->direction [start turns]
  (let [directions [:north :east :south :west]]
    (get directions (mod (+ turns (.indexOf directions start)) 4))))

(defn get-turns [start end]
  (let [dir-order [:north :south :east :west]
        ->ix #(.indexOf dir-order %)
        diff (- (->ix end) (->ix start))
        diff (if (> diff 2) -1 diff)
        diff (if (< diff -2) 1 diff)]
    diff))

(defn get-directions [i j]
  (let [north [(dec i) j]
        south [(inc i) j]
        east [i (inc j)]
        west [i (dec j)]
        north-east [(dec i) (inc j)]
        south-east [(inc i) (inc j)]
        south-west [(inc i) (dec j)]
        north-west [(dec i) (dec j)]]
    (sym-map north south east west north-east north-west south-east south-west)))

(defn get-relative-directions [forward] 
  {:forward forward
   :back (start+turns->direction forward 2)
   :left (start+turns->direction forward -1)
   :right (start+turns->direction forward 1)})

(defn tile->node [i j tile]
  (let [directions (get-directions i j)
        pick-directions (partial select-keys directions)]
    {:id [i j]
     :tile tile
     :pointers (case tile
                 "." {}
                 "S" {}
                 "|" (pick-directions [:north :south])
                 "-" (pick-directions [:east :west])
                 "L" (pick-directions [:north :east])
                 "J" (pick-directions [:north :west])
                 "7" (pick-directions [:south :west])
                 "F" (pick-directions [:south :east]))}))

(defn connect-start [{:keys [grid start] :as pipe-map}]
  (assoc-in pipe-map [:grid start :pointers]
            (let [lookup-orientation (->> start (apply get-directions) invert-map)]
              (->> grid
                   vals
                   (filter #((-> % :pointers invert-map) start))
                   (mapcat (comp #(vector (get lookup-orientation %) %) :id))
                   (apply hash-map)))))

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
              pointers (->> (vals pointers)
                            (filter (comp #(or (nil? %) (> % next-distance))
                                          #(get-in @grid [% :distance]))))]
          (run! #(swap! grid assoc-in [% :distance] next-distance) pointers)
          (recur (into (or queue []) pointers)))))
    (assoc pipe-map :grid @grid)))

(defn get-path 
  ([grid start-id]
   (let [[direction] (-> (get grid start-id)
                            :pointers
                            seq
                            first)]
     (get-path grid start-id (start+turns->direction direction 2))))
  ([grid start-id heading]
   (let [[direction id] (-> (get grid start-id)
                            :pointers
                            (dissoc (start+turns->direction heading 2))
                            seq
                            first)]
     (lazy-seq (cons [start-id heading id] (get-path grid id direction))))))


(defn get-net-turns [{:keys [grid start] :as input}]
  (loop [[[cur-id heading next-id] & path] (get-path grid start)
         turns 0 
         grid grid]
    (let [[_ next-heading] (first path)
          directions (apply get-directions cur-id)
          turns (+ turns (get-turns heading next-heading))
          relative-nodes (->> (get-relative-directions heading)
                              (mapcat #(let [[k v] %]
                                         [k (get directions v)]))
                              (apply hash-map))]
      (if (= next-id start)
        (assoc input :net-turns turns :grid (update grid cur-id merge relative-nodes))
        (recur path
               turns
               (update grid cur-id merge relative-nodes))))))

(defn get-neighborhood
  ([grid node]
   (get-neighborhood grid node #{node}))
  ([grid node neighbors]
   (let [new-neighbors (->> (apply get-directions node)
                            vals
                            (map (partial get grid))
                            (filter (comp nil? :distance)) 
                            (map :id)
                            (filter (comp nil? neighbors))
                            (filter some?))]
     (reduce (fn [neighbors node]
               (get-neighborhood grid node neighbors))
             (into neighbors new-neighbors) 
             new-neighbors))))

(defn collect-inside-nodes [{:keys [grid start net-turns] :as input}]
  (loop [[{:keys [id left right]} & path] (->> start
                                               (get-path grid)
                                               (map (comp (partial get grid) #(nth % 2))))
         inside-nodes #{}
         outside-nodes #{}]
    (if (or (nil? id) (= id start))
      (assoc input :inside-nodes inside-nodes :outside-nodes outside-nodes)
      (let [inside-node (if (> net-turns 0) right left)
            outside-node (if (> net-turns 0) left right)]
        (recur path 
               (if (or (inside-nodes inside-node) (:distance (get grid inside-node)))
                 inside-nodes
                 (get-neighborhood grid inside-node inside-nodes))
               (if (or (outside-nodes outside-node) (:distance (get grid outside-node)))
                 outside-nodes
                 (get-neighborhood grid outside-node outside-nodes)))))))

(defn get-max-distance [{:keys [grid]}]
  (->> grid vals (map :distance) (apply safe-max)))

(defn pipe-map->str [{:keys [grid max-row max-col inside-nodes outside-nodes start]}] 
  (let [string-acc (atom "")]
    (doall
     (for [i (range 0 max-row)]
       (swap! string-acc str (->> (range 0 max-col)
                                  (map (comp
                                        #(or (when (= (:id %) start) "S")
                                             (when (:distance %) (:tile %))
                                             (when ((or inside-nodes #{}) (:id %)) "I")
                                             (when ((or outside-nodes #{}) (:id %)) "O")
                                             ".")
                                        (partial get grid)
                                        #(vector i %)))
                                  (apply str)) "\n")))
    @string-acc))

(defn print-map [input]
  (prn (dissoc input :grid :inside-nodes))
  (->> input
       pipe-map->str
       (str/split-lines)
       (run! prn)))


(defn- main [filename part & _]
  (->> (slurp filename)
       parse-input
       add-distances
       get-net-turns
       ((if (= part :part-one)
          get-max-distance
          (comp #(spit "./output/day_ten.txt" %) pipe-map->str collect-inside-nodes)))))

(def-solution
  (main "./input/day_ten.txt" :part-one)
  (main "./input/day_ten.txt" :part-two))

(def sample
  #_"...........
.S-------7.
.|F-----7|.
.||.....||.
.||.....||.
.|L-7.F-J|.
.|..|.|..|.
.L--J.L--J.
..........."
  ".F----7F7F7F7F-7....
.|F--7||||||||FJ....
.||.FJ||||||||L7....
FJL7L7LJLJ||LJ.L-7..
L--J.L7...LJS7F-7L7.
....F-J..F7FJ|L7L7L7
....L7.F7||L7|.L7L7|
.....|FJLJ|FJ|F7|.LJ
....FJL-7.||.||||...
....L---J.LJ.LJLJ..."
  #_"FF7FSF7F7F7F7F7F---7
L|LJ||||||||||||F--J
FL-7LJLJ||||||LJL-77
F--JF--7||LJLJ7F7FJ-
L---JF-JLJ.||-FJLJJ7
|F|F-JF---7F7-L7L|7|
|FFJF7L7F-JF7|JL---7
7-L-JL7||F7|L7F-7F7|
L.L7LFJ|||||FJL7||LJ
L7JLJL-JLJLJL--JLJ.L")

(->> sample
     parse-input
     add-distances
     get-net-turns
     collect-inside-nodes
     #_:inside-nodes
     #_count
     print-map)