(ns day-five
  (:require [clojure.string :as str]
            [lib.solution-registry :refer [def-solution]]))

(defn dispatch [part & _] part)

(defmulti solve #'dispatch)

(defn parse-map-str [map-str]
  (->> (str/split map-str #" ")
       (map (fn [n]
              (Long/parseLong n 10)))))

(defn parse-seeds [seed-str]
  (map #(Long/parseLong % 10) (-> seed-str
                                  (subs (count "seeds: "))
                                  (str/split #" "))))

(defn build-shift-interval-map [lines]
  (loop [[line & lines] lines
         map-name nil
         maps {}]
    (cond
      (or (nil? line) (str/blank? line)) (if (nil? lines)
                                           maps
                                           (recur lines nil maps))
      (nil? map-name) (let [map-name (-> line (str/split #" ") first keyword)]
                        (recur lines map-name (assoc maps map-name '())))
      :else (recur lines map-name (update maps map-name conj (parse-map-str line))))))

(defn map-get [maps k]
  (let [getter (fn [[dest src len] k default]
                 (if (and (>= k src)
                          (< k (+ src len)))
                   (+ k (- dest src))
                   default))]
    (reduce (fn [val entry]
              (getter entry k val)) k maps)))


(defn get-seed-location [maps seed]
  (loop [[cur-map & next] maps
         cur-id seed]
    (if (nil? cur-map)
      cur-id
      (recur next (map-get cur-map cur-id)))))

(defmethod solve :part-one [_ maps seeds]
  (->> (map (partial get-seed-location maps) seeds)
       (apply min)))

(defn compare-range [[a a_len] [b b_len]]
  (if (>= b (+ a a_len))
    3 ; "0011"
    (if (> b a)
      (if (>= (+ b b_len) (+ a a_len))
        5 ;; 0101
        6 ;; 0110
        )
      (if (>= (+ b b_len) (+ a a_len))
        9 ;; 1001
        (if (<= (+ b b_len) a)
          12 ;; 1100
          10 ;; 1010
          )))))

(defn split-seed-range
  ([[dest & map-range] seed-range]
   (let [[map-start map-len] map-range
         [seed-start seed-len] seed-range
         shift #(+ % (- dest map-start))
         map-end (+ map-start map-len)
         seed-end (+ seed-start seed-len)]
     (case (compare-range seed-range map-range)
                            ;; no transforms
       (3 12) [seed-range]
                            ;; seed range less than map range but overlaps
       5 [[seed-start (- map-start seed-start)]
          [(shift map-start) (- seed-end map-start)]]
                            ;; seed range encompasses the map range
       6 [[seed-start (- map-start seed-start)]
          [(shift map-start) map-len]
          [map-end (- seed-end map-end)]]
                            ;; map range encompases seed range
       9 [[(shift seed-start) seed-len]]
                            ;; map range less than seed range but overlaps
       10 [[(shift seed-start) (- map-end seed-start)]
           [map-end (- seed-end map-end)]]))))

(defmethod solve :part-two [_ shift-interval-map-vals seeds]
  (->> (partition 2 seeds)
       (mapcat (fn [seed-range]
              (reduce
               (fn [seed-ranges shift-intervals]
                 (reduce
                  (fn [seed-ranges shift-interval]
                    (mapcat (partial split-seed-range shift-interval) seed-ranges))
                  seed-ranges
                  shift-intervals))
               [seed-range]
               shift-interval-map-vals)))
       (map first)
       (reduce min Long/MAX_VALUE)))

(defn -main [filename part & _]
  (let [[seeds _ & maps] (->> (slurp filename)
                              (str/split-lines))
        shift-interval-map (build-shift-interval-map maps)
        seeds (parse-seeds seeds)]
    (solve part (map (partial get shift-interval-map)
                     [:seed-to-soil
                      :soil-to-fertilizer
                      :fertilizer-to-water
                      :water-to-light
                      :light-to-temperature
                      :temperature-to-humidity
                      :humidity-to-location]) seeds)))

(def-solution
  (-main "./input/day_five.txt" :part-one)
  (-main "./input/day_five.txt" :part-two))