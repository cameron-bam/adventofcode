(ns day-twelve
  (:require [clojure.string :as str]
            [lib.solution-registry :refer [def-solution]]))

(def input "???.### 1,1,3
.??..??...?##. 1,1,3
?#?#?#?#?#?#?#? 1,3,1,6
????.#...#... 4,1,1
????.######..#####. 1,6,5
?###???????? 3,2,1")

(def solution "#.#.### 1,1,3
.#...#....###. 1,1,3
.#.###.#.###### 1,3,1,6
####.#...#... 4,1,1
#....######..#####. 1,6,5
.###.##....# 3,2,1")

(defn parse-groups [s]
  (->> (str/split s #",")
       (map #(Integer/parseInt % 10))))

(defn parse-conds [s]
  (str/split s #""))

(defn parse-line [line]
  (let [[springs groups] (str/split line #" ")]
    [(parse-conds springs) (parse-groups groups)]))

(defn in-group? [s] (= s "#"))

(defn new-group [groups s group-size]
  (if (and (> group-size 0) (not (in-group? s)))
    (conj groups group-size)
    groups))

(defn new-group-size [s group-size]
  (if (in-group? s) (inc group-size) 0))

(defn find-groups [springs]
  (loop [[spring & springs] springs
         group-size 0
         groups []]
    (if (nil? spring)
      (new-group groups spring group-size)
      (recur springs
             (new-group-size spring group-size)
             (new-group groups spring group-size)))))

(defn count-possible-groups
  ([[springs groups]]
   (count-possible-groups springs groups 0))
  ([[spring & springs] groups group-size]
   (if (nil? spring)
    (do
      #_(prn "reached end of row!" group-size groups)
      (if (or (and (= group-size (first groups)) (= 1 (count groups)))
              (and (= 0 group-size) (nil? (seq groups))))
        1 0))
    (if (= "?" spring)
     (do 
       #_(prn "Handling unknown value" spring springs groups group-size)
       (+ (count-possible-groups (cons "." springs) groups group-size)
        (count-possible-groups (cons "#" springs) groups group-size)))
     (let [new-group-size (new-group-size spring group-size)
           cur-group (or (first groups) 0)
           exited-group? (and (= new-group-size 0) (not= 0 group-size))]
       #_(prn "Checking if new-group-size is valid" new-group-size groups)
       (if (or (> new-group-size cur-group)
               (and exited-group? (not= cur-group group-size)))
         0 (recur springs (if exited-group? (rest groups) groups) new-group-size)))))))

(defn solve-part-one [s]
  (->> s
       (str/split-lines)
       (map parse-line)
       (map count-possible-groups)
       (reduce + 0)))

(def-solution
  (solve-part-one (slurp "./input/day_twelve.txt")))