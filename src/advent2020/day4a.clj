(ns advent2020.day4a
  (:require [clojure.java.io :as io]
            [clojure.set :as set]
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
  (let [pairs (string/split block #"\s")]
    (reduce (fn [acc pair] (let [[k v] (string/split pair #":")]
                             (assoc acc (keyword k) v))) {} pairs)))
  
(defn load-data []
  (with-open [rdr (io/reader "./src/advent2020/day4.txt")]
    (->> (block-seq rdr)
         (map parse-block)
         (vec))))

(def required-keys #{:byr :iyr :eyr :hgt :hcl :ecl :pid})

(defn has-required-keys? [passport]
  (set/subset? required-keys passport))

(defn solve []
  (let [data (load-data)]
    (count (filter has-required-keys? data))))
