(ns advent2020.day4b
  (:require [clojure.java.io :as io]
            [clojure.spec.alpha :as s]
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

(s/def ::byr (fn [byr]
              (let [year (Integer. byr)]
               (<= 1920 year 2002))))
(s/def ::iyr (fn [iyr]
               (let [year (Integer. iyr)]
                 (<= 2010 year 2020))))
(s/def ::eyr (fn [eyr]
               (let [year (Integer. eyr)]
                 (<= 2020 year 2030))))
(s/def ::hgt (s/or :cm (s/and string? (fn [hgt]
                                        (if-let [amount (second (re-find #"^(\d+)cm$" hgt))]
                                          (<= 150 (Integer. amount) 193)
                                          false)))
                   :in (s/and string? (fn [hgt]
                                        (if-let [amount (second (re-find #"^(\d+)in$" hgt))]
                                          (<= 59 (Integer. amount) 76)
                                          false)))))
(s/def ::hcl (s/and string? #(re-matches #"^#[0-9a-f]{6}$" %)))
(s/def ::ecl #{"amb" "blu" "brn" "gry" "grn" "hzl" "oth"})
(s/def ::pid (s/and string? #(re-matches #"^\d{9}$" %)))

(s/def ::passport (s/keys :req-un [::byr ::iyr ::eyr ::hgt ::hcl ::ecl ::pid]))

(defn solve []
  (let [data (load-data)]
    (count (filter #(s/valid? ::passport %) data))))
