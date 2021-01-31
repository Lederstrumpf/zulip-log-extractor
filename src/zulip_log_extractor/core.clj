(ns zulip-log-extractor.core
  (:use [clojure.java.shell :only [sh]]
        [clojure.data.json :as json])
  (:gen-class))

(defn -main []
  (println "Hello world"))
