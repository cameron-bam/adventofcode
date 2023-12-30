(ns day-eighteen
  (:require [examples :refer [day-eighteen]]
            [lib.grid :refer [angle-delta rotate-velocity empty-grid angle]]
            [lib.solution-registry :refer [def-solution]]
            [clojure.string :as str]
            [clojure.pprint :refer [pprint *print-right-margin*]]
            [clojure.java.io :as jio]))

(defn dir->vel [dir]
  (case dir
    "R" [0 1]
    "D" [1 0]
    "L" [0 -1]
    "U" [-1 0]))

(defn str->dig-plan [s]
  (->> (str/split-lines s)
       (map str/trim)
       (map #(str/split % #" "))
       (map #(let [[dir steps color] %]
               (hash-map :vel (dir->vel dir) :steps (Integer/parseInt steps) :color color)))
       (assoc {} :dig-plan)))

(defn get-path-segment [pos {:keys [vel steps]}]
  (reduce (fn [acc _]
            (let [prev (or (peek acc) pos)]
              (conj acc (mapv + prev vel)))) [] (range 0 steps)))

(defn get-path [{:keys [dig-plan] :as context}]
  (loop [[step & dig-plan] dig-plan 
         path {}
         pos [0 0]
         dirs []]
    (if (empty? step)
      (let [dirs (conj dirs (first dirs))]
        (assoc context :path path :rotational-direction (/ (angle-delta dirs) 360)))
      (let [path-segment (get-path-segment pos step)]
        (recur dig-plan
               (reduce (fn [path [pos next]]
                         (assoc path pos {:next next :vel (:vel step)}))
                       (assoc path pos {:next (first path-segment)
                                           :vel (:vel step)})
                       (map vector (butlast path-segment) (rest path-segment)))
               (last path-segment)
               (conj dirs (:vel step)))))))

(defn path-seq
  ([path start]
   (path-seq path start (get path start)))
  ([path start cur]
   (cond
     (nil? cur) (list)
     (= (:next cur) start) (list cur)
     :else (cons cur (lazy-seq (path-seq path start (get path (:next cur))))))))

(defn fill-seq [path vel cur]
  (let [next (->> (map + cur vel)
                  (mapv int))]
    (if (contains? path next)
      (list)
      (cons next (lazy-seq (fill-seq path vel next))))))

(defn get-trench-area [{:keys [path rotational-direction] :as context}] 
  (loop [[cur & path-list] (path-seq path [0 0])
         basin #{}
         iters 0]
    (if (nil? cur)
      (assoc context :basin basin)
      (let [{:keys [vel next]} cur
            fill-vel (rotate-velocity vel (* rotational-direction 90))
            is-inside-corner? (some-> path-list first :vel (angle fill-vel) abs int (= 180))
            nodes (cond-> (fill-seq path fill-vel next)
                    is-inside-corner? (concat (fill-seq path vel next)))]
        (recur path-list
               (into basin nodes)
               (inc iters))))))

(defn get-min-max [coll]
  (reduce (fn [[a b] step]
            [(min a step) (max b step)])
          [Integer/MAX_VALUE 0] coll))

(defn render-trench-and-basin [{:keys [path basin] :as context}]
  (let [path-coords (keys path)
        [min-rows max-rows] (get-min-max (map first path-coords))
        [min-cols max-cols] (get-min-max (map second path-coords))
        row-offset (max (* -1 min-rows) 0)
        col-offset (max (* -1 min-cols) 0)
        apply-offset #(vector (+ (first %) row-offset) (+ (second %) col-offset))
        grid (empty-grid (+ max-rows row-offset) (+ col-offset max-cols) '.)
        grid (reduce #(assoc-in %1 %2 'P) grid (map apply-offset path-coords))
        grid (reduce #(assoc-in %1 %2 '*) grid (map apply-offset basin))]
    (with-open [w (jio/writer "./output/day_eighteen.txt")]
      (binding [*print-right-margin* (* 3 (+ col-offset max-cols))]
        (pprint grid w)))
    context))

(defn part-one [s]
  (-> s
      str->dig-plan
      get-path
      get-trench-area
      #_render-trench-and-basin
      (#(+ (-> % :basin count) (-> % :path count)))))

(def-solution
  (part-one (slurp "./input/day_eighteen.txt")))

(part-one day-eighteen)