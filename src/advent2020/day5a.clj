(ns advent2020.day5a
  (:require [clojure.java.io :as io])
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

(defn seat->id [seat]
  (let [[row col] (seat->row-column seat)]
    (+ (* row 8) col)))

(defn load-seats []
  (with-open [rdr (io/reader "./src/advent2020/day5.txt")]
    (->> (line-seq rdr)
         (vec))))

(defn solve []
  (let [ids (map seat->id (load-seats))]
    (apply max ids)))
