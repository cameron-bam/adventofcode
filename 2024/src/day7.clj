(ns day7
  (:require [clojure.string :as str]))

(def input (->> (slurp "src/day7.txt")
                (str/split-lines)
                (map #(let [[output input] (str/split % #": ")
                            input (str/split input #" ")]
                        [(Long/parseLong output 10) (map (fn [i] (Long/parseLong i 10)) input)]))))

(defn apply-ops [params ops]
  (let [[init & params] params]
    (reduce (fn [acc [param op]] (op acc param)) init (map vector params ops))))

(defn permutations [values size]
  (let [seqs (map vector values)]
    (if (= size 1)
      seqs
      (mapcat #(map (fn [seq] (into seq %)) (permutations values (dec size))) seqs))))

(defn producible? [ops [test-val inputs]]
  (->> (permutations ops (-> inputs count dec))
       (map (partial apply-ops inputs))
       (some #{test-val})))

(def part-one (->> input
                   (map (partial producible? [+ *]))
                   (filter some?)
                   (apply +)))

(defn combine-nums [a b]
  (Long/parseLong (str a b)))

(def part-two (->> input
                   (map (partial producible? [+ * combine-nums]))
                   (filter some?)
                   (apply +)))