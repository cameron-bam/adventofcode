(ns day-nineteen
  (:require [clojure.string :as str]
            [lib.solution-registry :refer [def-solution]]
            [lib.interval :refer [compare-range]]
            [examples :refer [day-nineteen]]))

(defn parse-part [s]
  (->> (re-matches #"\{(.*)\}" s) second (#(str/split % #",")) (mapcat (comp #(let [[k v] %] [k (Integer/parseInt v 10)]) #(str/split % #"="))) (apply hash-map)))

(defn build-pred [s]
  (let [pred (zipmap [:part-type :op :compare-to :dest]
                     (rest (re-matches #"([xmas])([\<\>])(\d+)\:(\w+)" s)))]
    (when-not (empty? pred) pred)))

(def ops {"<" <
          ">" >})

(defn parse-rule [s]
  (let [[name rule] (rest (re-matches #"(.*)\{(.*)\}" s))
        rule (->> (str/split rule #",")
                  (map #(if-let [pred (build-pred %)]
                          (update pred :compare-to (fn [v] (Integer/parseInt v 10)))
                          {:dest %})))]
    {:name name :rule rule}))

(defn parse-input [s]
  (let [[rules parts] (->> (str/split-lines s)
                           (partition-by str/blank?)
                           (map (partial filter (comp not str/blank?)))
                           (filter seq)
                           (map (partial map str/trim)))
        parts (map parse-part parts)
        rules (->> (map parse-rule rules)
                   (reduce (fn [acc {:keys [name rule]}]
                             (assoc acc name rule)) {}))]
    [rules parts]))

(defn next-rule [rules rule part]
  (loop [[{:keys [dest part-type op compare-to] :as pred} & rem] (rules rule)]
    (when (nil? dest)
      (throw (ex-info "Invalid rule!" {:pred pred :rule rule :preds (rules rule)})))
    (if-let [dest (if op
                    (when ((ops op) (part part-type) compare-to) dest)
                    dest)]
      dest
      (recur rem))))

(defn process-part [rules part]
  (loop [cur-rule "in"
         last-rule nil
         iters 0]
    (when (> iters 10)
      (throw (ex-info "Failed to process rule!" {:cur-rule cur-rule :last-rule last-rule :rules rules :part part})))
    (if (#{"A" "R"} cur-rule)
      cur-rule
      (recur (next-rule rules cur-rule part) cur-rule (inc iters)))))

(defn part-one [s]
  (let [[rules parts] (parse-input s)]
    (->> (map #(assoc % :result (process-part rules %)) parts)
         (map #(case (:result %)
                 "R" 0
                 "A" (->> (dissoc % :result) vals (reduce + 0))))
         (reduce + 0))))


(defn build-rule-tree [rules cur-rule]
  (->> (get rules cur-rule)
       (map
        (fn [pred] (if (#{"A" "R"} (:dest pred)) pred (update pred :dest (partial build-rule-tree rules)))))))

(defn get-branch-seq [{children :dest :as branch}] 
  (if (#{"A" "R"} children)
    [(filter seq [(dissoc branch :dest) {:dest children}])]
    (if (:op branch)
      (mapcat #(map (partial cons (dissoc branch :dest)) (get-branch-seq %)) children)
      (mapcat get-branch-seq children))))

(defn branch-seq->interval [bs]
  (let [default-range {:start 1 :end 4000}]
    (merge
     (->> (mapcat #(vector % default-range) ["x" "m" "a" "s"])
          (apply hash-map))
     (reduce (fn [acc {:keys [part-type op compare-to]}]
               (assoc acc part-type (merge default-range (if (= "<" op) {:end compare-to} {:start compare-to})))) {} (butlast bs))
     (last bs)))
  )

(defn part-two [s]
  (let [[rules] (parse-input s)
        empty-acc {"x" [] "m" [] "a" [] "s" []}]
    (->> (build-rule-tree rules "in")
         (mapcat get-branch-seq)
         (map branch-seq->interval)
         (map #(assoc % :count (->> (vals (dissoc % :dest))
                                    (map (fn [{:keys [start end]}]
                                           (inc (- end start))))
                                    (reduce * 1))))
         (map :count)
         (reduce + 0))
    
    #_(reduce (fn [acc {:keys [dest] :as next}]
              (update acc dest
                      #(reduce (fn [acc [k v]] (update acc k conj v)) % (dissoc next :dest))))
            {"A" empty-acc "R" empty-acc}
            )))

(def-solution
  (part-one (slurp "./input/day_nineteen.txt")))


#_(part-two day-nineteen)