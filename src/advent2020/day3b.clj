(ns advent2020.day3b
  (:require [clojure.java.io :as io])
  (:gen-class))

(defn parse-line [line]
  (let [expanded-line (apply str (repeat 100 line))]
    (map (fn [c] (case c
                   \. :open
                   \# :tree)) expanded-line)))

(def puzzle-map (with-open [rdr (io/reader "./src/advent2020/day3.txt")]
                  (->> (line-seq rdr)
                       (map parse-line)
                       (vec))))

(defn traverse
  ([x y slope-x slope-y] (traverse x y slope-x slope-y []))
  ([x y slope-x slope-y path]
   (let [last-y (- (count puzzle-map) 1)
         next-x (+ x slope-x)
         next-y (+ y slope-y)
         value (nth (nth puzzle-map y) x)
         updated-path (conj path value)]
     (if (< last-y next-y)
       updated-path
       (traverse next-x next-y slope-x slope-y updated-path)))))

(defn count-path-trees [slope-x slope-y]
  (let [path (traverse 0 0 slope-x slope-y)]
    (count (filter #(= % :tree) path))))

(defn solve []
  (*
    (count-path-trees 1 1)
    (count-path-trees 3 1)
    (count-path-trees 5 1)
    (count-path-trees 7 1)
    (count-path-trees 1 2)))
