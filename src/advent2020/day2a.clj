(ns advent2020.day2a
  (:require [clojure.string :as string]
            [clojure.java.io :as io])
  (:gen-class))

(defn parse-line [line]
  (let [[counts letter password] (string/split line #"\s")
        [min-count max-count] (string/split counts #"-")]
    {:min-count (Integer/parseInt min-count)
     :max-count (Integer/parseInt max-count)
     :letter (first letter)
     :password password}))

(def lines (with-open [rdr (io/reader "./src/advent2020/day2.txt")]
             (->> (line-seq rdr)
                  (map parse-line)
                  (vec))))

(defn valid? [{:keys [min-count max-count letter password]}]
  (let [actual-count (count (filter #(= letter %) password))]
    (and (<= min-count actual-count) (<= actual-count max-count))))

(defn solve [] (count (filter valid? lines)))
