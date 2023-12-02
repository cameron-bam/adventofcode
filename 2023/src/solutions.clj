(ns solutions
  (:require [day-one]))

(defmacro solutions
  [& body]
  (let [solutions (map (fn [solution]
                         `(time
                           (prn (quote ~solution) ~solution))) body)]
    `(do 
       ~@solutions)))


(defn -main [& _]
  (solutions
   (day-one/-main "./input/day_one.txt" :part-one)
   (day-one/-main "./input/day_one.txt" :part-two)))

(-main)