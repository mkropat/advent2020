(ns advent2020.day9b
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

(defn solve-part1
  ([numbers] (solve-part1 numbers 25))
  ([numbers n]
   (let [[preamble data] (split-at n numbers)]
     (find-invalid preamble data))))

(defn enumerate-subsequences-at [s i]
  (let [full-subsequence (drop i s)]
    (map #(take (inc %) full-subsequence) (range (count full-subsequence)))))

(defn enumerate-subsequences [s]
  (apply concat (map #(enumerate-subsequences-at s %) (range (count s)))))

(defn solve []
  (let [numbers (load-data)
        target-number (solve-part1 numbers)
        target-sequence (->> (enumerate-subsequences numbers)
                             (filter #(<= 2 (count %)))
                             (filter #(= target-number (apply + %)))
                             (first))
        max-number (apply max target-sequence)
        min-number (apply min target-sequence)]
    (+ max-number min-number)))
