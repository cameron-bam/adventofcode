(ns day-three
  (:require [clojure.string :as str]
            [lib.solution-registry :refer [def-solution]]))

(defn get-row-indices [grid i j part-num]
  (for [j (range (max 0 (- j (count part-num))) (min (+ 2 j) (-> grid first count)))]
    [i j]))


(defn get-surrounding-indices [grid i j part-num]
  (let [indexes (atom '())]
    (when (>= (dec i) 0)
      (swap! indexes concat (get-row-indices grid (dec i) j part-num)))
    (when (< (inc i) (count grid))
      (swap! indexes concat (get-row-indices grid (inc i) j part-num)))
    (when (>= (- j (count part-num)) 0)
      (swap! indexes conj [i (- j (count part-num))]))
    (when (< (inc j) (-> grid first count))
      (swap! indexes conj [i (inc j)]))
    @indexes))

(defn validate-part-num [grid i j part-num]
  (when (and
         (seq part-num)
         (->> (get-surrounding-indices grid i j part-num)
              (map (partial get-in grid))
              (some #(and (not= % \.)
                          (not (Character/isDigit %))))))
    (reduce (fn [num c]
              (+ (* 10 num) (Character/getNumericValue c))) 0 part-num)))

(defn get-new-part-nums [grid i j part-nums part-num]
  (if-let [part-num (validate-part-num grid i (dec j) part-num)]
    (conj part-nums part-num)
    part-nums))

(defn get-part-numbers [grid]
  (loop [[row & rows] grid
         i 0
         part-nums []]
    (if (nil? row)
      part-nums
      (recur rows (inc i)
             (loop [[col & cols] row
                    j 0
                    part-nums part-nums
                    part-num []]
               (if (nil? col)
                 (get-new-part-nums grid i j part-nums part-num) ;; validate current part num then return
                 (if-not (Character/isDigit col)
                   ;; validate current part num then recur
                   (recur cols (inc j) (get-new-part-nums grid i j part-nums part-num) [])
                   ;; read next part num
                   (recur cols (inc j) part-nums (conj part-num col)))))))))

(defn -main [filename part & _]
  (let [grid (->> (slurp filename)
                  (str/split-lines)
                  (map (comp (partial apply vector) seq char-array))
                  (apply vector))]
    (reduce + 0 (get-part-numbers grid))))

(def-solution
  (-main "./input/day_three.txt" :part-one))