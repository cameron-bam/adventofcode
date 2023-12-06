(ns day-five
  (:require [clojure.string :as str]
            [lib.solution-registry :refer [def-solution]]))

(defn parse-map-str [map-str]
  (let [[dest src len] (->> (str/split map-str #" ")
                            (map (fn [n]
                                   (Long/parseLong n 10))))]
    (fn [k]
      (when (and (>= k src)
                 (< k (+ src len)))
        (+ k (- dest src))))))

(defn parse-seeds [seed-str]
  (map #(Long/parseLong % 10) (-> seed-str
                                  (subs (count "seeds: "))
                                  (str/split #" "))))

(defn build-maps [lines]
  (loop [[line & lines] lines
         map-name nil
         maps {}]
    (cond
      (or (nil? line) (str/blank? line)) (if (nil? lines)
                                           maps
                                           (recur lines nil maps))
      (nil? map-name) (let [map-name (-> line (str/split #" ") first keyword)]
                        (recur lines map-name (assoc maps map-name '(identity))))
      :else (recur lines map-name (update maps map-name conj (parse-map-str line))))))

(defn map-get [map k]
  (reduce (fn [val getter]
            (or (getter k) val)) k map))

(defn get-seed-location [maps seed]
  (loop [[cur-map & next] [:seed-to-soil
                           :soil-to-fertilizer
                           :fertilizer-to-water
                           :water-to-light
                           :light-to-temperature
                           :temperature-to-humidity
                           :humidity-to-location]
         cur-id seed]
    (if (nil? cur-map)
      cur-id
      (recur next (map-get (get maps cur-map) cur-id)))))

(defn -main [filename part & _]
  (let [[seeds _ & maps] (->> (slurp filename)
                              (str/split-lines))
        maps (build-maps maps)
        seeds (parse-seeds seeds)]
    (->> (map (partial get-seed-location maps) seeds)
         (apply min))))

(def-solution
  (-main "./input/day_five.txt" :part-one))