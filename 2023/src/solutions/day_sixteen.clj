(ns day-sixteen
  (:require [lib.grid :as grid]
            [lib.solution-registry :refer [def-solution]]))

(def invert (partial * -1))

(defn get-next-velocities [square cur-velocity]
  (case square
    "." [cur-velocity]
    "$" [(apply vector (reverse cur-velocity))]
    "/" [(->> cur-velocity (map invert) reverse (apply vector))]
    "-" (if (= 0 (first cur-velocity))
          [cur-velocity]
          [[0 1] [0 -1]])
    "|" (if (= 0 (second cur-velocity))
          [cur-velocity]
          [[1 0] [-1 0]])
    nil []))

(defn get-energized-squares
  ([grid] (get-energized-squares grid {} [0 -1] [0 1]))
  ([grid visited position velocity]
   (loop [visited visited
          position position
          velocity velocity]
     (if (get-in visited [position velocity])
       visited
       (let [visited (if (get-in grid position)
                       (update visited position #(apply conj (or %1 #{}) %&) velocity)
                       visited)
             next-position (apply vector (map + position velocity))
             next-val (get-in grid next-position)]
         (if-not next-val
           visited
           (let [next-velocities (get-next-velocities next-val velocity)]
             (case (count next-velocities)
               0 visited
               2 (reduce (fn [acc velocity]
                           (get-energized-squares grid acc next-position velocity)) visited next-velocities)
               1 (recur visited next-position (first next-velocities))))))))))

(defn part-one [input]
  (->> input
       (grid/str->grid)
       get-energized-squares
       count))

(defn get-starts [grid]
  (let [tops (->> (range 0 (-> grid first count))
                  (mapcat #(vector [[-1 %] [1 0]]
                                   [[(-> grid count) %] [-1 0]])))
        sides (->> (range 0 (count grid))
                   (mapcat #(vector [[% -1] [0 1]]
                                    [[% (-> grid first count)] [0 -1]])))]
    (concat tops sides)))

(defn part-two [input]
  (let [grid (grid/str->grid input)]
    (transduce
     (comp
      (map #(get-energized-squares grid {} (first %) (second %)))
      (map count))
     max 0 (get-starts grid))))

(def-solution
  (part-one (slurp "./input/day_sixteen.txt"))
  (part-two (slurp "./input/day_sixteen.txt")))