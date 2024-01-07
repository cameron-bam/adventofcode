(ns day-eight
  (:require [clojure.string :as str]
            [lib.math :refer [lcm]]
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
  (loop [node start
         [instruction & rem-instructions] instructions
         matches []
         steps 0]
    (if (nil? instruction)
      {:matches matches
       :next (:id node)}
      (let [next-node (get graph (get node instruction))]
        (recur next-node rem-instructions (if (pred node) (conj matches {:node node :steps steps}) matches) (inc steps))))))

(defmethod solve :part-one
  [_ {:keys [graph instructions] :as input}]
  (loop [cur-node (get graph "AAA")
         steps 0]
    (let [{:keys [matches next]} (find-matches input cur-node (partial = (get graph "ZZZ")))]
      (if (seq matches)
        (+ (-> matches first :steps) steps)
        (recur (get graph next) (+ (count instructions) steps))))))

(defmethod solve :part-two
  [_ {:keys [graph nodes instructions] :as input}]
  (let [id-ends-with #(comp (fn [id] (str/ends-with? id %)) :id)
        start (filter (id-ends-with "A") nodes)
        end (into #{} (filter (id-ends-with "Z") nodes))
        traverse (fn traverse [g d {:keys [id]}]
                   (let [graph-get (partial get g)]
                     (->> (get (graph-get id) d)
                          (map graph-get))))
        traverse-many (fn [g d nodes]
                        (->> (mapcat (partial traverse g d) nodes)
                             (filter some?)))
        graph (reduce
               (fn [new-graph {:keys [id] :as node}]
                 (let [{:keys [next matches]} (find-matches input node end)]
                   (-> new-graph
                       (update id merge node {:next [next]} (when (seq matches) {:distance (-> matches first :steps)
                                                                                 :match (-> matches first :node :id)}))
                       (update-in [next :previous] conj id))))
               graph (vals graph))]
    (loop [nodes (->> graph
                      vals
                      (filter :distance)
                      (traverse-many graph :previous))
           graph graph
           step 1]
      (if (empty? nodes)
        ;; Solution is problem set specific. We exploit the following about the graph
        ;; - no asymmetric cycles are present (meaning we don't travel from one XXZ node to another and back)
        ;; - by inspecting the start nodes, we know they are one away from the beginning of each XXZ cycle. This allows
        ;;   us to use the distance from each start node in our LCM calculation
        (->> start
             (map (comp :distance (partial get graph) :id))
             (apply lcm)
             (* (count instructions)))
        (recur (traverse-many graph :previous (filter (comp not :distance) nodes))
               (reduce (fn [new-graph {:keys [id] :as node}]
                         (if (:distance node)
                           (let [next (first (traverse new-graph :next node))]
                             (prn "Found cycle!" node next)
                             (when-not (= (:match next) (:match node))
                               (prn "Asymmetric cycle detected!" (:match next) (:match node)))
                             new-graph)
                           (-> new-graph
                               (assoc-in [id :distance] step)
                               (assoc-in [id :match] (->> node (traverse new-graph :next) (map :match) first)))))
                       graph nodes)
               (inc step))))))

(defn -main [file part & _]
  (solve part (parse-input file)))

(def-solution
  (-main "./input/day_eight.txt" :part-one)
  (-main "./input/day_eight.txt" :part-two)
  )
