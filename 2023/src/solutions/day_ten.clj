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

(defn combine-keys [order & args]
  (->> args
       (sort-by #(.indexOf order %))
       (map name)
       (str/join "-")
       (keyword)))

(def dir-order [:north :south :east :west])

(def ->dir-ix #(.indexOf dir-order %))

(defn get-turns [start end]
  (let [diff (- (->dir-ix end) (->dir-ix start))
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

(def combine-directions (partial combine-keys [:north :south :east :west]))

(def combine-relative-diretions (partial combine-keys [:forward :back :left :right]))

(defn get-relative-directions [forward] 
  (let [dirs {:forward forward
              :back (start+turns->direction forward 2)
              :left (start+turns->direction forward -1)
              :right (start+turns->direction forward 1)}]
    (merge dirs (->> (for [x [:forward :back]
                           y [:left :right]]
                       [x y])
                     (mapcat (fn [keys]
                               (vector (apply combine-relative-diretions keys)
                                       (->> (map (partial get dirs) keys)
                                            (apply combine-directions)))))
                     (apply hash-map)))))

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
  (loop [[[_ heading next-id] & path] (get-path grid start)
         turns 0]
    (let [[_ next-heading] (first path)
          turns (+ turns (get-turns heading next-heading))]
      (if (= next-id start)
        (assoc input :net-turns turns)
        (recur path turns)))))

(defn get-neighborhood
  ([grid neighbors & nodes]
   (loop [[id & nodes] nodes
          neighbors neighbors]
     (let [new-neighbor? (comp #(and (some? %)
                                     (-> % :distance nil?)
                                     (-> % :id neighbors nil?))
                               (partial get grid))]
       (if (nil? id)
         neighbors
         (if (new-neighbor? id)
           (recur (concat nodes (->> (apply get-directions id) vals)) (conj neighbors id))
           (recur nodes neighbors)))))))

(defn get-left-right-nodes [id heading next-heading]
  (let [directions (apply get-directions id)
        {:keys [left right] :as relative-directions} (get-relative-directions heading)
        relative-directions->nodes (partial map (comp (partial get directions)
                                                      (partial get relative-directions)))]
    (condp = next-heading
      left {:left-nodes (relative-directions->nodes [:back-left])
            :right-nodes (relative-directions->nodes [:forward-left :forward :forward-right :right :back-right])}
      right {:left-nodes (relative-directions->nodes [:back-left :left :forward-left :forward :forward-right])
             :right-nodes (relative-directions->nodes [:back-right])}
      heading {:left-nodes (relative-directions->nodes [:back-left :left :forward-left])
               :right-nodes (relative-directions->nodes [:back-right :right :forward-right])})))

(defn collect-inside-nodes [{:keys [grid start net-turns] :as input}]
  (loop [[[cur-id heading] & path] (->> start
                                                (get-path grid))
         is-first true
         inside-nodes #{}
         outside-nodes #{}]
    (if (and (not is-first) (= cur-id start))
      (assoc input :inside-nodes inside-nodes :outside-nodes outside-nodes)
      (let [[_ next-heading] (first path)
            {:keys [left-nodes right-nodes]} (get-left-right-nodes cur-id heading next-heading)
            new-inside-nodes (if (> net-turns 0) right-nodes left-nodes)
            new-outside-nodes (if (> net-turns 0) left-nodes right-nodes)]
        (recur path
               false
               (apply get-neighborhood grid inside-nodes new-inside-nodes)
               (apply get-neighborhood grid outside-nodes new-outside-nodes))))))

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
  (prn (dissoc input :grid :inside-nodes :outside-nodes))
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
          (comp count :inside-nodes collect-inside-nodes)))))

(def-solution
  (main "./input/day_ten.txt" :part-one)
  (main "./input/day_ten.txt" :part-two))