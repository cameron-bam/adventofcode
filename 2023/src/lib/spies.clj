(ns lib.spies)

(defn spy-fn [val]
  (prn val)
  val)

(defn make-thread-body [body]
  (mapcat #(vector % `spy-fn) body))

(defmacro spy->
  [& body]
  `(-> ~@(make-thread-body body)))

(defmacro spy->>
  [& body]
  `(->> ~@(make-thread-body body)))