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


(defn get-winning-count [card]
  (let [winning-set (->> card :winning (into #{}))]
    (->> card
         :mine
         (filter winning-set)
         count)))

(defn count-card-copies [card other-cards]
  (let [matches (:winning-count card)
        start (+ 1 (:id card))]
    (->> (range start (+ start matches))
         (map (partial get @other-cards))
         (reduce + 1)
         (swap! other-cards assoc (:id card)))))

(defn dispatch [part _]
  part)

(defmulti solve #'dispatch)

(defmethod solve :part-one [_ cards]
  (->> cards
       (map (comp floor (partial pow 2) dec get-winning-count))
       (reduce + 0)))

(defmethod solve :part-two [_ cards]
  (let [card-scores (atom {})]
    (->> cards
         (reverse)
         (map #(assoc % :winning-count (get-winning-count %)))
         (run! #(count-card-copies % card-scores)))
    (->> @card-scores
         vals
         (reduce + 0))))

(defn -main [filename part & _]
  (->> (slurp filename)
       (str/split-lines)
       (map parse-card)
       (solve part)))


(def-solution
  (-main "./input/day_four.txt" :part-one)
  (-main "./input/day_four.txt" :part-two))