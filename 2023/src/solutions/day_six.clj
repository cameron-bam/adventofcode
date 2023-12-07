(ns day-six
  (:require [clojure.string :as str]
            [clojure.math :refer [ceil floor]]
            [lib.solution-registry :refer [def-solution]]))

(defn dispatch [part _] part)

(defmulti solve #'dispatch)

(defn parse-line [prefix line]
  (->> (-> line
           (subs (count prefix))
           (str/split #" "))
       (filter (comp not str/blank?))
       (map #(Integer/parseInt % 10))))

(defn ways-to-win [[time distance]]
  (let [midpoint (/ time 2)
        start-times ((juxt ceil floor) midpoint)]
    (loop [products start-times
           winning-start-times #{}]
      (let [max-distance (apply * products)]
        (if (< max-distance distance)
          (count winning-start-times)
          (recur (map #(% %2) [inc dec] products) (into winning-start-times products)))))))

(defmethod solve :part-one [_ races]
  (->> races
       (map ways-to-win)
       (reduce * 1)))

(defn -main [filename part & _]
  (->> (slurp filename)
       (str/split-lines)
       (map #(% %2) [(partial parse-line "time:")
                     (partial parse-line "distances:")])
       (apply map vector)
       (solve part)))

(def-solution
  (-main "./input/day_six.txt" :part-one))