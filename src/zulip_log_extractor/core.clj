(ns zulip-log-extractor.core
  (:use [clojure.java.shell :only [sh]]
        [clojure.data.json :as json])
  (:gen-class))

(def data-files (filter #(re-seq #"messages-\d{6}.json" %) (clojure.string/split-lines (:out (sh "ls")))))

(defn extract-json [raw-data]
  (second (first (json/read-str raw-data :key-fn keyword))))

(def json (reduce (fn [v file] (concat v (extract-json (slurp file)))) [] data-files))

(def meetings (filter #(re-find #"meeting*" %) (distinct (map :subject json))))

(defn extract-messages [subject messages]
  (map :content (filter #(re-matches (re-pattern subject) (:subject %)) messages))
  )

(defn file-name [meeting-name]
  (str (clojure.string/replace meeting-name #" " "-") ".md"))

(map #(spit (file-name %) (clojure.string/join "\n\n" (extract-messages % json))) meetings)
