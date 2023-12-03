(ns lib.solution-registry)

(def registry (atom []))

(def ^:dynamic eval-on-def false)

(defmacro def-solution
  [& body]
  (let [solutions `(let [start-ns# (. *ns* -name)]
                     (in-ns (. ~*ns* -name))
                     (eval '(do
                              ~@(map (fn [solution]
                                        `(time
                                          (prn *ns* (quote ~solution) ~solution))) body)))
                     (in-ns start-ns#))]
    `(do
       (swap! registry conj (quote ~solutions))
       (when eval-on-def
         (eval ~solutions)))))

(defn eval-solution [soln]
  (eval soln))

(defn run-solutions []
  (run! eval-solution @registry))

(defn clear-registry []
  (reset! registry []))