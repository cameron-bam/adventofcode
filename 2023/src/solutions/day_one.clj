(ns day-one
  (:require [clojure.string :as str]
            [lib.solution_registry :refer [def-solution]]))

(defmulti make-num-resolver identity)
(defmulti num-resolver (fn [part _] part))

(defmethod make-num-resolver :part-one [part]
  (partial num-resolver part))

(defmethod num-resolver :part-one
  [_ c]
  (when (Character/isDigit c)
    (Character/getNumericValue c)))

(def digit-map
  {"one" 1
   "two" 2
   "three" 3
   "four" 4
   "five" 5
   "six" 6
   "seven" 7
   "eight" 8
   "nine" 9})

(def digits (map (comp seq char-array) (keys digit-map)))

(def acc (atom []))

(defmethod make-num-resolver :part-two [part]
  (partial num-resolver part))

(defn arr-starts-with [a b]
  (loop [[a_next & a] a
         [b_next & b] b]
    (if-not (= a_next b_next)
      false
      (if (nil? b)
        true
        (recur a (or b []))))))

(defmethod num-resolver :part-two [_ c]
  (if-let [digit (num-resolver :part-one c)]
    (do (reset! acc [])
        digit)
    (loop [chars (swap! acc conj c)]
      (if-let [digit (get digit-map (reduce (fn [s c] (str s (Character/toString c))) "" chars))]
        (do (reset! acc [c])
            digit)
        (when-not (or (empty? chars)
                      (some #(arr-starts-with % chars) digits))
          (recur (swap! acc (comp (partial apply vector) rest))))))))


(defn line->number [num-resolver line]
  (let [chars (seq (char-array line))]
    (loop [[c & chars] chars
           first nil
           last nil]
      (if (nil? c)
        (+ (* 10 first) last)
        (if-let [num (num-resolver c)]
          (recur chars (if (nil? first) num first) num)
          (recur chars first last))))))

(defn solve [input part & _]
  (->> (slurp input)
       (str/split-lines)
       (map #(line->number (make-num-resolver part) %))
       (reduce +)))

(def-solution
  (day-one/solve "./input/day_one.txt" :part-one)
  (day-one/solve "./input/day_one.txt" :part-two))