(ns lib.grid
  (:require [clojure.string :as str]
            [clojure.math :refer [atan2 PI cos sin]]))

(defn str->grid [s]
  (->> s str/trim str/split-lines (map (comp #(str/split % #"") str/trim)) (apply vector)))

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

(defn get-neighbor [pos vel]
  (into [] (map + pos vel)))

(defn get-neighbors [pos velocities]
  (map #(get-neighbor % pos) velocities))

(defn valid-neighbors? [grid]
  (filter #(get-in grid %)))

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

(defn coords [grid]
  (for [i (range 0 (count grid))
        j (range 0 (count (first grid)))]
    [i j]))

(defn grid-map [f grid]
  (reduce (fn [grid pos]
            (update-in grid pos f))
          grid
          (coords grid)))

(defn radian->angle [r]
  (* r (/ 360 (* 2 PI))))

(defn angle->radian [a]
  (* a (/ (* 2 PI) 360)))

(defn angle [a b]
  (-> (radian->angle (- (apply atan2 (update a 0 (partial * -1))) (apply atan2 (update b 0 (partial * -1)))))))

(defn vels->angles [vels]
  (map angle (rest vels) (butlast vels)))

(defn angle-delta [vels]
  (transduce (map #(cond
                     (> % 180) (- % 360)
                     (< % -180) (+ % 360)
                     :else %)) + 0 (vels->angles vels)))

(defn rotate-velocity [vel rotate]
  (let [basis (angle vel [0 1])
        rads (angle->radian (+ basis rotate))] 
    (mapv int [(* -1 (sin rads)) (cos rads)])))

(defn empty-grid [max-rows max-cols val]
  (let [grid-row (mapv (constantly val) (range 0 (inc max-cols)))]
    (reduce (fn [grid _]
            (conj grid grid-row)) [] (range 0 (inc max-rows)))))