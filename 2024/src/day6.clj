(ns day6
  (:require [clojure.string :as str]))

(def input (->> (slurp "src/day6.txt")
                (str/split-lines)
                (map #(str/split % #""))
                vec))

(defn get-start [input]
  (->> input
       (map-indexed (fn [y row] (map-indexed (fn [x v] [v [y x]]) row)))
       (some #(some (fn [[v coords]]
                      (when (#{"^" ">" "v" "<"} v) 
                        [coords (case v
                                  "^" [-1 0]
                                  ">" [0 1]
                                  "v" [1 0]
                                  "<" [0 -1])])) %))))

(defn traverse-grid [input]
  (let [[start direction] (get-start input)
        max-visited (* (count input) (count (first input)) 4)] 
    (loop [cur start
           visited #{[start direction]}
           [direction :as directions] (drop-while #(not= % direction) (cycle [[-1 0] [0 1] [1 0] [0 -1]]))]
      (let [next-coord (mapv + direction cur)
            next-val (get-in input next-coord)
            next-coord (if (= next-val "#") cur next-coord)
            [next-direction :as directions] (cond->> directions (= next-val "#") (drop 1))]
        (if (or (not next-val) (visited [next-coord next-direction]) (> (count visited) max-visited)) [visited (cond (> (count visited) max-visited) :visit-detection-broken next-val :trapped)]
            (recur next-coord (conj visited [next-coord next-direction]) directions))))))


(def part-1 (->> (traverse-grid input)
                 first
                 (map first)
                 set
                 count))


(def part-2 (->> (get-start input)
                 first
                 (disj (->> (traverse-grid input) first (map first) set))
                 (map #(let [new-grid (assoc-in input % "#")]
                         (prn "Testing" %)
                         (when-let [outcome (second (traverse-grid new-grid))]
                           (prn "couldn't escape!" outcome)
                           [% outcome])))
                 (filter some?)
                 count))
