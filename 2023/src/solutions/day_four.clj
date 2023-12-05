(ns day-four
  (:require [clojure.string :as str]
            [clojure.math :refer [pow floor]]
            [lib.solution-registry :refer [def-solution]]))

(defn number-str->vec [number-str]
  (->> (-> number-str str/trim (str/split #"\ "))
       (filter (comp not str/blank?))
       (map #(Integer/parseInt % 10))
       (apply vector)))

(defn parse-card [card]
  (let [[card-id numbers] (str/split card #":")
        card-id (-> card-id (subs 5) (str/trim) (Integer/parseInt 10))
        [winning mine] (map number-str->vec (str/split numbers #"\|"))]
    {:id card-id
     :winning winning
     :mine mine}))


(defn get-points [card]
  (let [winning-set (->> card :winning (into #{}))]
    (->> card
         :mine
         (filter winning-set)
         count
         dec
         (pow 2)
         (floor))))

(defn -main [filename part & _]
  (->> (slurp filename)
       (str/split-lines)
       (map (comp get-points parse-card))
       (reduce + 0)))


(def-solution
  (-main "./input/day_four.txt" :part-one))