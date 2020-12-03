(ns advent2020.day3a
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
  ([x y] (traverse x y []))
  ([x y path]
   (let [last-y (- (count puzzle-map) 1)
         next-x (+ x 3)
         next-y (+ y 1)
         value (nth (nth puzzle-map y) x)
         updated-path (conj path value)]
     (if (< last-y next-y)
       updated-path
       (traverse next-x next-y updated-path)))))

(defn solve []
  (let [path (traverse 0 0)]
    (count (filter #(= % :tree) path))))
