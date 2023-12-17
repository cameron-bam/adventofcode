(ns day-fifteen
  (:require [clojure.string :as str]
            [lib.solution-registry :refer [def-solution]]
            [lib.spies :refer [spy->>]]
            [examples :refer [day-fifteen]]))

(defn chars->hash [chars]
  (->> chars
       (map int)
       (reduce (fn [total val]
                 (-> total
                     (+ val)
                     (* 17)
                     (mod 256))) 0)))

(def str->hash 
  (memoize
   (fn str->hash [s]
     (->> s char-array chars->hash))))

(defn chars->str [chars]
  (reduce str "" chars))

(defn part-one [s]
  (->> (str/split (str/trim s) #",")
       (map str->hash)
       (reduce + 0)))

;; hash corresponds to map
;; label -> string preceding the operators
;; '-' operator -> indicates that we pull labeled lens out of box (list) and compact (array delete)
;; '=' operator -> adds a lens will be followed by a number indicating the focal length. Add the lens to the box
;;;; if the label exists, overwrite the lens
;;;; if the label doesn't exist, add to back of the box

;; At end of processing, add up focusing power of each lens in the boxes
;; fp(lens) = (1 + boxno) (1 + index of lens within box) (focal length)
;; lens = [label focal_length]
;; box = lens[]
;; boxes = {box-no box}

(defn get-focusing-power [lenses]
  (->> (map-indexed #(vector (inc %1) %2) lenses)
       (reduce (fn [tot [slot-no [label foc-len]]]
                 (+ (* (+ 1 (str->hash label)) slot-no foc-len) tot)) 0)))

(defn dispatch [_ [_ [op] _]] op)

(defmulti box-op' #'dispatch)

(defmethod box-op' \- [b [label]]
  (update (or b {}) (chars->hash label) #(->> (filter (comp (partial not= %2) first) (or %1 []))
                                              (apply vector)) (chars->str label)))

(defmethod box-op' \= [b [label _ [focal-len]]]
  (let [b (or b {})
        box-no (chars->hash label)
        b (if (contains? b box-no) b (assoc b box-no []))
        label (chars->str label)
        focal-len (Character/getNumericValue focal-len)
        new-b (update b box-no #(->> (map (fn [[label focal-len]]
                                            [label (if (= %2 label) %3 focal-len)]) %1)
                                     (apply vector)) label focal-len)]
    (if (not= new-b b)
      new-b
      (update b box-no conj [label focal-len]))))

(defn box-op
  ([b s]
   (box-op' b (partition-by #{\- \=} s)))
  ([b op & ops]
   (reduce box-op b (cons op ops))))

(defn part-two [s]
  (->> (str/split s #",")
          (apply box-op {})
          vals
          (map get-focusing-power)
          (reduce + 0)))

(def-solution
  (part-one (slurp "./input/day_fifteen.txt"))
  (part-two (slurp "./input/day_fifteen.txt")))