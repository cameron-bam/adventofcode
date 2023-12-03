(ns main
  (:require [lib.load-files :refer [load-files]]
            [lib.solution_registry :refer [run-solutions clear-registry]]))

(defn -main [& _]
  (clear-registry)
  (load-files "./src/solutions") 
  (run-solutions))

(comment (-main))