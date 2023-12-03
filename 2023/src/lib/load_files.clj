(ns lib.load-files
  (:require [clojure.java.io :refer [file]]))

(defn load-files [path]
  (let [file  (file path)
        files (.listFiles file)
        load-cache (atom #{})]
    (doseq [x files]
      (when (.isFile x)
        (let [x (.getCanonicalPath x)]
          (when-not (= @load-cache (swap! load-cache conj x))
            (load-file x)))))))