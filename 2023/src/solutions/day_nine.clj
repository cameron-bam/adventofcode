(ns day-nine
  (:require [clojure.string :as str]
            [lib.solution-registry :refer [def-solution]]))

(defn str->int [s]
  (Integer/parseInt s 10))

(defn parse-input [file]
  (->> (-> (slurp file)
           (str/split-lines))
       (map (comp
             (partial apply vector)
             #(map str->int %)
             #(str/split % #" ")))))

(defn diff-seq [nums]
  (loop [[x & nums] nums
         res []]
    (if (-> nums first nil?)
      res
      (recur nums (conj res (- (first nums) x))))))


(defn diff-tree [nums]
  (loop [cur-seq nums
         tree []]
    (let [tree (cons cur-seq tree)]
      (if (every? (partial = 0) cur-seq)
        (apply vector tree)
        (recur (diff-seq cur-seq) tree)))))

(defn extrapolate-right [tree]
  (reduce (fn [new-tree cur-level]
            (conj new-tree
                  (conj cur-level 
                        (if (= new-tree [])
                          0
                          (+ (last cur-level)
                             (->> new-tree last last)))))) [] tree))

(defn extrapolate-left [tree]
  (reduce (fn [new-tree cur-level]
            (conj new-tree
                  (apply vector
                         (cons (if (= new-tree []) 0
                                   (- (first cur-level)
                                      (->> new-tree last first)))
                               cur-level))))
          [] tree))

(defn solve [part input]
  (case part
    :part-one (map (comp last last extrapolate-right diff-tree) input)
    :part-two (map (comp first last extrapolate-left diff-tree) input)))

(defn -main [file part & _]
  (->> (parse-input file) 
       (solve part)
       (reduce + 0)))

(def-solution 
  (-main "./input/day_nine.txt" :part-one)
  (-main "./input/day_nine.txt" :part-two))

