(ns lib.math)

(defn gcd
  ([a b]
   (if (zero? b)
     a
     (recur b (mod a b))))
  ([a b & more]
   (apply gcd (gcd a b) more)))

(defn lcm
  ([a b]
   (*' (/ a (gcd a b)) b))
  ([a b & more]
   (apply *' (/ a (apply gcd a b more)) b more)))