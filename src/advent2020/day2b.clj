(ns advent2020.day2b
  (:require [clojure.string :as string]
            [clojure.java.io :as io])
  (:gen-class))

(defn parse-line [line]
  (let [[counts letter password] (string/split line #"\s")
        [min-count max-count] (string/split counts #"-")]
    {:first-position (Integer/parseInt min-count)
     :second-position (Integer/parseInt max-count)
     :letter (first letter)
     :password password}))

(def lines (with-open [rdr (io/reader "./src/advent2020/day2.txt")]
             (->> (line-seq rdr)
                  (map parse-line)
                  (vec))))

(defn xor [x y]
  (and (or x y) (not (and x y))))

(defn valid? [{:keys [first-position second-position letter password]}]
  (let [first-letter (get password (- first-position 1))
        second-letter (get password (- second-position 1))]
    (xor (= first-letter letter) (= second-letter letter))))

(defn solve [] (count (filter valid? lines)))
