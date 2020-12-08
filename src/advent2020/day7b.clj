(ns advent2020.day7b
  (:require [clojure.java.io :as io]
            [clojure.set]
            [clojure.string :as string])
  (:gen-class))

(defn parse-bag-count [text]
  (let [[_ c n] (re-find #"^(\d+) (.*) bags?$" text)]
    {:count (Integer. c)
     :bag n}))

(defn parse-rule [line]
  (let [[_ bag-name contains] (re-find #"^(.*?) bags contain (.*)\.$" line)]
    (case contains
      nil (throw (Exception. (str "Unable to parse line: " line)))
      "no other bags" {:bag bag-name :contains []}
      {:bag bag-name
       :contains (->> (string/split contains #",\s")
                      (map parse-bag-count)
                      (vec))})))

(defn load-data []
  (with-open [rdr (io/reader "./src/advent2020/day7.txt")]
    (->> (line-seq rdr)
         (map parse-rule)
         (vec))))

(def container-bags
  (reduce (fn [acc rule] (let [{:keys [bag contains]} rule]
                           (assoc acc bag contains)))
            {}
            (load-data)))


(defn count-child-bags [bag-name]
  (let [contained-bags (get container-bags bag-name)]
    (apply + (->> contained-bags
                  (map (fn [{b :bag c :count}]
                         (let [descendant-count (* c (count-child-bags b))]
                           (+ c descendant-count))))))))

(defn solve []
  (count-child-bags "shiny gold"))
