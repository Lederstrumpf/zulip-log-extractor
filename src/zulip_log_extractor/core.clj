(ns zulip-log-extractor.core
  (:use [clojure.data.json :as json])
  (:gen-class))

(def data (slurp "messages-000001.json"))

(def json (second (first (json/read-str data :key-fn keyword))))

(def meetings (filter #(re-find #"meeting*" %) (distinct (map :subject json))))

(defn extract-messages [subject messages]
  (map :content (filter #(re-matches (re-pattern subject) (:subject %)) messages))
  )

(defn file-name [meeting-name]
  (str (clojure.string/replace meeting-name #" " "-") ".log"))

(map #(spit (file-name %) (clojure.string/join "\n---\n" (extract-messages % json))) meetings)
