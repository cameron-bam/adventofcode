(ns day-eighteen
  (:require [examples :refer [day-eighteen]]
            [lib.grid :refer [angle-delta get-neighbor]]
            [lib.solution-registry :refer [def-solution]]
            [clojure.string :as str]))

(defn dir->vel [dir]
  (case dir
    "R" [0 1]
    "D" [1 0]
    "L" [0 -1]
    "U" [-1 0]))

(defn row->instruction [r]
  (let [[dir steps color] r]
    (hash-map :vel (dir->vel dir) :steps (Integer/parseInt steps) :color color)))

(defn str->dig-plan [s parse-row]
  (->> (str/split-lines s)
       (map str/trim)
       (map #(str/split % #" "))
       (map parse-row)
       (assoc {} :dig-plan)))

(defn get-rotational-direction [{:keys [dig-plan] :as context}]
  (assoc context :rotational-direction (->> dig-plan (mapv :vel) (#(conj % (first %))) (angle-delta) (#(/ % 360)) int)))

(defn get-vertices [{:keys [rotational-direction dig-plan] :as context}] 
  (assoc context :dig-plan
         (reduce (fn [acc [{:keys [steps vel] :as cur-step} next-step]]
                   (let [{cur :end last-vel :vel} (or (-> acc peek) {:vel (:vel (last dig-plan))
                                                                     :end [0 0]})
                         next-rotation (int (/ (angle-delta [last-vel vel (:vel next-step)]) 180))
                         ;; need to make sure that we are using cartesian coordinates that
                         ;; correctly capture the area of the trench and the basin
                         step-transform (cond
                                          (= next-rotation rotational-direction) inc
                                          (= (* -1 next-rotation) rotational-direction) dec
                                          :else identity)]
                     (conj acc (assoc cur-step
                                      :start cur
                                      :end (get-neighbor cur (map (partial * (step-transform steps)) vel))))))
                 [] (map vector dig-plan (conj (apply vector (rest dig-plan)) (first dig-plan))))))

(defn trap-area [[y1 x1] [y2 x2]]
  (/ (* (+ y1 y2)
        (- x1 x2)) 2))

(defn dig-plan-inst->trap-area [{:keys [start end] :as inst}]
  (assoc inst :area (trap-area start end)))

(defn shift-to-positive-coors [{:keys [dig-plan] :as context}]
  (let [coords (into #{} (map :start dig-plan))
        min-row (apply min (map first coords))
        min-col (apply min (map second coords))
        get-off #(if (<= % 0) (+ 1 (* -1 %)) 0)
        roff (get-off min-row)
        coff (get-off min-col)
        apply-offset #(vector (+ (first %) roff) (+ (second %) coff))]
    (assoc context :dig-plan (map #(-> %
                                       (update :start apply-offset)
                                       (update :end apply-offset)) dig-plan))))

(defn get-area [context]
  (->> context
       get-rotational-direction
       get-vertices
       shift-to-positive-coors
       (#(update % :dig-plan (fn [coll] (map dig-plan-inst->trap-area coll))))
       :dig-plan
       (map :area)
       (reduce + 0)))

(defn part-one [s]
  (->> (str->dig-plan s row->instruction)
       get-area))

(defn color->instruction [r]
  (let [[_ _ color] r
        num (Integer/parseInt (subs color 2 (dec (count color))) 16)
        dir (mod num 16)
        distance (/ (- num dir) 16)]
    [color (subs color 2 (dec (count color))) distance dir]
    (hash-map :vel (dir->vel (case dir
                               0 "R"
                               1 "D"
                               2 "L"
                               3 "U")) :steps distance)))

(defn part-two [s]
  (->> (str->dig-plan s color->instruction)
       get-area))

(def-solution
  (part-one (slurp "./input/day_eighteen.txt"))
  (part-two (slurp "./input/day_eighteen.txt")))

(part-one day-eighteen)