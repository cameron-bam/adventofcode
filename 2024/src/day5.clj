(ns day5
  (:require [clojure.string :as str]
            [clojure.set :as set]
            [clojure.math :as math]))


(def input (let [input (->> (slurp "src/day5.txt")
                            (str/split-lines))]
             (update (split-at (->> (map-indexed vector input)
                                    (some #(let [[i v] %]
                                             (when (= v "") i)))) input) 1 #(->> % (map (fn [p] (str/split p #","))) (drop 1)))))

(defn get-rules [[rules-input]]
  (update-vals (reduce (fn [acc next]
                         (let [[k v] (str/split next #"\|")]
                           (update acc k into [v]))) {} rules-input) set))

(defn validate-page-update [rules pages]
  (loop [[page & pages] pages
         prev-pages #{}]
    (if (nil? page)
      :valid
      (when-not (seq (set/intersection prev-pages (rules page)))
        (recur pages (conj prev-pages page))))))


(defn valid-updates [input]
  (let [rules (get-rules input)]
    (filter (partial validate-page-update rules) (second input))))

(def part-1 (->> input
                 valid-updates
                 (map #(Integer/parseInt (->> %
                                              (drop (math/floor-div (count %) 2))
                                              first) 10))
                 (apply +)))

(defn fix-update [rules pages]
  (loop [pages pages
         attempts #{}]
    (if (validate-page-update rules pages)
      pages
      (if (attempts pages)
        (throw (ex-info "Cycle detected!" {}))
        (let [next-attempt (loop [[page & remaining-pages] pages
                                  prev-pages #{}
                                  reordered-pages pages]
                             (if (nil? page)
                               reordered-pages
                               (let [page-rules (rules page)]
                                 (if-not (seq (set/intersection prev-pages page-rules))
                                   (recur remaining-pages (conj prev-pages page) reordered-pages)
                                   (let [[i p] (->> (map-indexed vector reordered-pages)
                                                    (some #(let [[_ p] %]
                                                             (when (page-rules p) %))))
                                         cur-index (- (count pages) (count remaining-pages) 1)]
                                     (recur remaining-pages (conj prev-pages page) (-> reordered-pages
                                                                                       (assoc i page)
                                                                                       (assoc cur-index p))))))))]
          (recur next-attempt (conj attempts pages)))))))

(defn fixed-updates [input]
  (let [rules (get-rules input)]
    (->> (second input)
         (remove (partial validate-page-update rules))
         (map (partial fix-update rules)))))

(def part-2 (->> input
                 fixed-updates
                 (map #(Integer/parseInt (->> %
                                              (drop (math/floor-div (count %) 2))
                                              first) 10))
                 (apply +)))