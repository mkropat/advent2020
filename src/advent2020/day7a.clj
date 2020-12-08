(ns advent2020.day7a
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
  (reduce (fn [acc rule] (let [{:keys [bag contains]} rule
                               contained-bags (set (map :bag contains))]
                           (assoc acc bag contained-bags))) {} (load-data)))

(def contained-bags
  (reduce (fn [acc rule]
            (let [{:keys [bag contains]} rule
                  contained-bags (map :bag contains)]
              (reduce (fn [acc contained-bag]
                        (let [s (get acc contained-bag #{})]
                          (assoc acc contained-bag (conj s bag)))) acc contained-bags)))
          {}
          (load-data)))

(defn find-containers
  ([contained-bag] (find-containers contained-bag #{}))
  ([contained-bag all-containers]
   (if-let [my-containers (get contained-bags contained-bag)]
     (let [parent-containers (apply clojure.set/union (map find-containers my-containers))]
       (clojure.set/union my-containers parent-containers))
     all-containers)))

(defn solve []
  (count (find-containers "shiny gold")))
