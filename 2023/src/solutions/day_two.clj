(ns day-two
  (:require [clojure.string :as str]
            [lib.solution-registry :refer [def-solution]]))

(defn parse-game [game-str]
  (let [[game-id result] (str/split game-str #":")
        game-id (Integer/parseInt (str/trim (subs game-id 4)))
        sets (str/split result #";")
        sets (map (fn [set]
                    (->> (str/split set #",")
                         (mapcat (fn [cubes]
                                   (let [[num color] (str/split (str/trim cubes) #" ")]
                                     [(keyword color) (Integer/parseInt num)])))
                         (apply hash-map))) sets)
        ]
    {:id game-id :result sets}))

(defn get-max-colors [game]
  (reduce (fn [acc set]
            (reduce (fn [acc [color count]]
                      (update acc color max count)) acc set))
          {:green 0 :red 0 :blue 0} (:result game)))

(defn game-valid? [max-colors game]
  (let [game-max-colors (get-max-colors game)]
    (reduce (fn [valid? color]
              (and valid?
                   (>= (get max-colors color)
                       (get game-max-colors color))))
            true
            [:red :green :blue])))

(defn dispatch [part _] part)

(defmulti solve #'dispatch)

(defmethod solve :part-one [_ games]
  (->> games
       (filter (partial game-valid? {:red 12 :green 13 :blue 14}))
       (map :id)
       (reduce + 0)))

(defmethod solve :part-two [_ games]
  (->> games
       (map #(assoc % :max-colors (get-max-colors %)))
       (map #(assoc % :power (reduce * 1 (-> % :max-colors vals))))
       (map :power)
       (reduce + 0)))

(defn -main [filename part & _]
  (->> (slurp filename)
       (str/split-lines)
       (map parse-game)
       (solve part)))

(def-solution
  (-main "./input/day_two.txt" :part-one)
  (-main "./input/day_two.txt" :part-two))