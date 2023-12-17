(ns day-fourteen
  (:require [lib.grid :refer [str->grid cols->rows rotate grid-diff]]
            [lib.spies :refer [spy-fn]]
            [lib.solution-registry :refer [def-solution]]
            [examples :refer [day-fourteen]]))

(defn calc-load [row]
  (let [row-len (count row)]
    (->> row
            (map-indexed (partial apply vector))
            (reduce
             (fn [weight [i cell]]
               (if (= cell \O)
                 (+ weight (- row-len i))
                 weight)) 0))))

(def sort-row
  (memoize
   (fn sort-row [row]
     (->> (partition-by #{"#"} row)
          (mapcat #(if (-> % first #{"O" "."})
                     (sort-by (fn [%] (.indexOf ["O" "."] %)) %)
                     %))))))

(defn calc-forward-load [g]
  (->> g
       cols->rows
       (map calc-load)
       (reduce + 0)))

(def tilt-forward
  (fn tilt-forward [g]
    (->> g
         cols->rows
         (map sort-row)
         cols->rows)))

(def run-cycle
  (fn run-cycle [g]
    (-> g tilt-forward (rotate 1) tilt-forward (rotate 1) tilt-forward (rotate 1) tilt-forward (rotate 1))))

(defn part-one [s]
  (->> s str->grid tilt-forward calc-forward-load))

(defn part-two [s cycles]
  (let [cache (atom {:g->i {}
                     :i->g {}})]
    (reduce (fn [g i]
              (if-let [last-i (get-in @cache [:g->i g])]
                (let [length (- i last-i)
                      ending-index (+ last-i (mod (- cycles last-i) length))]
                  (reduced (get-in @cache [:i->g ending-index])))
                (do
                  (swap! cache assoc-in [:g->i g] i)
                  (swap! cache assoc-in [:i->g i] g)
                  (run-cycle g)))) (->> s str->grid) (range cycles))))

(def-solution
  (part-one (slurp "./input/day_fourteen.txt"))
  (-> (slurp "./input/day_fourteen.txt") (part-two 1000000000) calc-forward-load))

(defn test-solution [example cycles]
  (grid-diff (->> (nth example cycles) str->grid)
             (part-two (first example) cycles)))

(defn debug-sorting [s]
  (->> s str->grid (spy-fn) cols->rows (map sort-row) cols->rows))

(comment
  (test-solution day-fourteen 3))