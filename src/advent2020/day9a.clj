(ns advent2020.day9a
  (:require [clojure.java.io :as io]
            [clojure.math.combinatorics :as combo])
  (:gen-class))

(defn load-data []
  (with-open [rdr (io/reader "./src/advent2020/day9.txt")]
    (->> (line-seq rdr)
         (map bigint)
         (vec))))

(defn find-invalid [preceding numbers]
  (if (seq numbers)
    (let [[n & remaining] numbers
          valid? (some #(= n (apply + %)) (combo/combinations preceding 2))
          new-preceding (conj (vec (drop 1 preceding)) n)]
      (if valid?
        (recur new-preceding remaining)
        n))
    nil))

(defn solve
  ([] (solve 25))
  ([n]
   (let [[preamble data] (split-at n (load-data))]
     (find-invalid preamble data))))
