(ns day-twenty
  (:require [clojure.string :as str]
            [lib.math :refer [lcm]]
            [lib.solution-registry :refer [def-solution]]))

(defn get-incoming-nodes [nodes target] 
  (->> (filter #(let [[_ {:keys [dest]}] %
                      i (.indexOf dest target)]
                  (< -1 i)) nodes)
       (map first)))

(defn parse [s]
  (let [nodes (->> (str/split-lines s)
                   (map (comp (partial zipmap [:modifier :source :dest]) rest #(re-matches #"([\%\&])?(.*) \-\> (.*)" %)))
                   (map #(update % :dest (fn [d] (str/split d #", "))))
                   (reduce (fn [acc {:keys [source] :as node}]
                             (assoc acc source (dissoc node :source))) {}))]
    (reduce (fn [nodes [k {:keys [modifier]}]]
              (cond-> nodes
                (= modifier "&")
                (assoc-in [k :con]
                          (zipmap (get-incoming-nodes nodes k)
                                  (cycle [false]))))) nodes nodes)))

(defn process-node
  [signal from {:keys [dest modifier flip-flop con] :as node-val}]
  (case modifier
    "%" [(assoc node-val :flip-flop (if signal flip-flop (not flip-flop)))
         (not flip-flop)
         (if signal [] dest)]
    "&" (let [con (assoc con from signal)
              signal (not (reduce (fn [acc next]
                                    (and acc next))
                                  true (vals con)))]
          [(assoc node-val :con con) signal dest])
    [node-val signal dest]))

(defn next-step
  [{:keys [nodes queue] :as context}]
  (let [{:keys [node signal from]} (first queue)]
    (when (some? node)
      (let [[node-val signal dest] (process-node signal from (get nodes node))]
        (-> context
            (update :queue (comp (partial apply vector) rest))
            (update :queue into (map #(assoc {:signal signal :from node} :node %)) dest)
            (assoc-in [:nodes node] node-val))))))

(defn send-signal [{:keys [last-iter times]}]
  (let [nodes (-> last-iter last :nodes)]
    {:last-iter (->> {:nodes nodes
                      :queue [{:node "broadcaster"
                               :signal false
                               :from nil}]}
                     (iterate next-step)
                     (take-while (comp seq :queue)))
     :times (inc times)}))

(defn push-button [nodes]
  (->> {:last-iter [{:nodes nodes}]
        :times 0}
       (iterate send-signal)))

(defn part-one [s]
  (->> (push-button (parse s)) 
       (take-while (comp (partial >= 1000) :times))
       (mapcat :last-iter)
       rest
       (map (comp first :queue))
       (group-by :signal)
       (map #(let [[k v] %] [(if k :high :low) (count v)]))
       (into {})
       vals
       (reduce * 1)))

(defn part-two [s]
  (let [nodes (parse s)
        feeder-nodes (->> nodes
                          (filter #(let [[_ {:keys [dest]}] %]
                                     (> (.indexOf dest "rx") -1)))
                          first
                          second
                          :con
                          keys)]
    (->> (transduce
          (mapcat (fn [{:keys [last-iter times]}] (map #(assoc % :times times) last-iter)))
          (fn
            ([acc] acc)
            ([acc {:keys [queue times]}]
             (if (->> acc vals (filter (partial = -1)) seq nil?)
               (reduced acc)
               (let [[{:keys [from signal]}] queue]
                 (cond-> acc (and (acc from) signal) (assoc from times))))))
          (zipmap feeder-nodes (cycle [-1]))
          (->> (parse s)
               (push-button)
               rest))
         vals
         (apply lcm))))

(def-solution
  (part-one (slurp "./input/day_twenty.txt"))
  (part-two (slurp "./input/day_twenty.txt")))