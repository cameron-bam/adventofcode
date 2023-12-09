(ns day-eight
  (:require [clojure.string :as str]
            [clojure.math :refer [pow]]
            [lib.solution-registry :refer [def-solution]]))


(defn parse-input [file]
  (let [[instructions _ & graph-nodes] (->> (slurp file)
                                            (str/split-lines))
        graph-nodes (map #(let [[id node] (map str/trim (str/split % #"="))
                                [left right] (map str/trim (-> (subs node 1 (dec (count node)))
                                                               (str/split #",")))]
                            {:id id :left left :right right}) graph-nodes)]
    {:instructions (map {"R" :right "L" :left}
                        (-> instructions
                            str/trim
                            (str/split #"")))
     :graph (reduce (fn [graph {:keys [id] :as node}]
                      (assoc graph id node)) {} graph-nodes)
     :nodes graph-nodes}))

(defn dispatch [part _] part)

(defmulti solve #'dispatch)

(defn find-matches [{:keys [graph instructions]} start pred]
  (let [start-nodes (atom #{start})]
    (loop [node start
           [instruction & rem-instructions] instructions
           matches []
           steps 0]
      (if (nil? instruction)
        {:matches matches
         :next (:id node)}
        (let [next-node (get graph (get node instruction))]
          (when-not next-node
            (throw (ex-info "Could not find next node!" {:node node :instruction instruction :steps steps})))
          (when (nil? rem-instructions)
            (when (@start-nodes next-node)
              (throw (ex-info "Cycle detected!" {:node next-node :steps steps})))
            (swap! start-nodes conj next-node))
          (recur next-node rem-instructions (if (pred node) (conj matches steps) matches) (inc steps)))))))

(defn gcd
  ([a b]
   (if (zero? b)
     a
     (recur b (mod a b))))
  ([a b & more]
   (apply gcd (gcd a b) more)))

(defmethod solve :part-one
  [_ {:keys [graph instructions] :as input}]
  (loop [cur-node (get graph "AAA")
         steps 0]
    (let [{:keys [matches next]} (find-matches input cur-node (partial = (get graph "ZZZ")))]
      (if (seq matches)
        (do 
          (prn "found match" matches cur-node)
          (+ (first matches) steps))
        (recur (get graph next) (+ (count instructions) steps))))))

(defmethod solve :part-two
  [_ {:keys [graph nodes instructions] :as input}]
  (let [start (filter (comp #(str/ends-with? % "A") :id) nodes)
        end (into #{} (filter (comp #(str/ends-with? % "Z") :id) nodes))
        traverse (fn traverse [g d {:keys [id]}]
                   (let [graph-get (partial get g)]
                     (->> (get (graph-get id) d)
                          (map graph-get))))
        graph (reduce
               (fn [new-graph {:keys [id] :as node}]
                 (let [{:keys [next matches]} (find-matches input node end)]
                   (-> new-graph
                       (update id merge node {:next [next]} (when (seq matches) {:distance (first matches)}))
                       (update-in [next :previous] conj id))))
               graph (vals graph))]
    (loop [nodes (->> graph
                      vals
                      (filter :distance)
                      (map (partial traverse graph :previous))
                      (flatten)
                      (filter some?))
           graph graph
           step 1]
      (if (empty? nodes)
        (let [distances (->> end
                             (map (comp
                                   (partial traverse graph :next)
                                   (partial get graph) :id))
                             (flatten)
                             (map :distance))
              denom (apply gcd distances)]
          (prn denom distances)
          (->> distances
               (map #(/ % denom))
               (apply *)
               (* (count instructions))
               (+ (count start))))
        (recur (->> nodes
                    (map (partial traverse graph :previous))
                    flatten
                    (filter some?)
                    (filter (comp not :distance)))
               (reduce (fn [new-graph {:keys [id]}]
                         (assoc-in new-graph [id :distance] step))
                       graph nodes)
               (inc step))))))

(defn -main [file part & _]
  (solve part (parse-input file)))

(def-solution
  (-main "./input/day_eight.txt" :part-one)
  (-main "./input/day_eight.txt" :part-two))
