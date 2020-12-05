(ns advent2020.day5b
  (:require [clojure.java.io :as io]
            [clojure.math.combinatorics :as combo]
            [clojure.set])
  (:gen-class))

(def seat "FBFBBFFRLR")

(defn row-specification-seq [seat]
  (->> seat
    (filter #(contains? #{\F \B} %))
    (map #(case %
            \F :lower
            \B :upper))))

(defn col-specification-seq [seat]
  (->> seat
    (filter #(contains? #{\L \R} %))
    (map #(case %
            \L :lower
            \R :upper))))

(defn split-range [lower-bound upper-bound]
  (let [difference (- upper-bound lower-bound)]
    (if (<= difference 1)
      [[lower-bound lower-bound] [upper-bound upper-bound]]
      (let [mid-point (+ lower-bound (int (/ difference 2)))]
        [[lower-bound mid-point] [(inc mid-point) upper-bound]]))))

(defn specification->num [specification min-num max-num]
   (if (= min-num max-num)
     min-num
     (let [[lower-or-upper & remaining] specification
           [lower-range upper-range] (split-range min-num max-num)
           [new-min-num new-max-num] (case lower-or-upper
                                       :lower lower-range
                                       :upper upper-range)]
       (recur remaining new-min-num new-max-num))))

(defn seat->row-column [seat]
  (let [row (specification->num (row-specification-seq seat) 0 127)
        col (specification->num (col-specification-seq seat) 0 7)]
    [row col]))

(defn row-column->id [[row col]]
  (+ (* row 8) col))

(defn load-seats []
  (with-open [rdr (io/reader "./src/advent2020/day5.txt")]
    (->> (line-seq rdr)
         (vec))))

(defn all-seats [occupied-rows]
  (let [cols (range 0 (inc 7))
        available-rows (clojure.set/difference occupied-rows #{(apply max occupied-rows)})]
    (set (combo/cartesian-product available-rows cols))))

(def occupied-seats
  (set (map seat->row-column (load-seats))))

(defn solve []
  (let [occupied-rows (set (map (fn [[row]] row) occupied-seats))
        all-seats (all-seats occupied-rows)
        solution (clojure.set/difference all-seats occupied-seats)]
    (row-column->id (first solution))))
