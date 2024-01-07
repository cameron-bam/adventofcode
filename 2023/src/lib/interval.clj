(ns lib.interval)

(defn compare-range
  "Compares ranges and returns a numeric value indicating the nature of the overlap. The 
   numeric result corresponds to the binary number representing the order of the interval, where 0
   corresponds to a and 1 corresponds to b."
  [[a a_len] [b b_len]]
  (if (>= b (+ a a_len))
    3 ;; "0011"
    (if (> b a)
      (if (>= (+ b b_len) (+ a a_len))
        5 ;; 0101
        6 ;; 0110
        )
      (if (>= (+ b b_len) (+ a a_len))
        9 ;; 1001
        (if (<= (+ b b_len) a)
          12 ;; 1100
          10 ;; 1010
          )))))