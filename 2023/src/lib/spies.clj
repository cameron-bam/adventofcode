(ns lib.spies
  (:require [clojure.pprint :refer [pprint]]))

(defn spy-fn [val]
  (pprint val)
  val)

(defn make-thread-body [body]
  (mapcat #(vector % `spy-fn) body))

(defmacro spy->
  [& body]
  `(-> ~@(make-thread-body body)))

(defmacro spy->>
  [& body]
  `(->> ~@(make-thread-body body)))