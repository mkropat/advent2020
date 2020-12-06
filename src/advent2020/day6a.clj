(ns advent2020.day6a
  (:require [clojure.java.io :as io]
            [clojure.set]
            [clojure.string :as string])
  (:gen-class))

(defn group-blocks [lines]
  (if (empty? lines)
    nil
    (let [[block non-block] (split-with #(not (= % "")) lines)
          remaining (drop 1 non-block)]
      (lazy-seq (cons (string/join "\n" block) (group-blocks remaining))))))

(defn block-seq [reader]
  (group-blocks (line-seq reader)))

(defn parse-block [block]
  (let [answers-by-person (string/split block #"\s")]
    (vec (map set answers-by-person))))
    
(defn load-data []
  (with-open [rdr (io/reader "./src/advent2020/day6.txt")]
    (->> (block-seq rdr)
         (map parse-block)
         (vec))))

(defn combine-answer-group [answer-group]
  (reduce clojure.set/union #{} answer-group))

(defn solve []
  (let [answer-sets (map combine-answer-group (load-data))]
    (apply + (map count answer-sets))))
