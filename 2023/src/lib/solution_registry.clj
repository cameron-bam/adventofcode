(ns lib.solution_registry)

(def registry (atom []))

(def ^:dynamic eval-on-def false)

(defmacro def-solution
  [& body]
  (let [solutions (map (fn [solution] 
                         `(time
                           (prn *ns* (quote ~solution) ~solution))) body)]
    `(do
       (when eval-on-def
         (map eval (quote ~solutions)))
       (swap! registry conj (quote ~solutions)))))

(defn run-solutions []
  (run! #(run! eval %) @registry))

(defn clear-registry []
  (reset! registry []))