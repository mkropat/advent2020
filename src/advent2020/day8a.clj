(ns advent2020.day8a
  (:require [clojure.java.io :as io]
            [clojure.string :as string])
  (:gen-class))

(defn parse-instruction [instruction]
  (let [[op arg] (string/split instruction #"\s")]
    {:op (keyword op)
     :arg (Integer. arg)}))

(defn load-data []
  (with-open [rdr (io/reader "./src/advent2020/day8.txt")]
    (->> (line-seq rdr)
         (map parse-instruction)
         (vec))))

(def initial-state {:pc 0
                    :acc 0
                    :visited #{}})

(defn- execute* [program state]
  (let [{:keys [acc pc visited]} state
        {:keys [op arg]} (nth program pc)]
    (if (contains? visited pc)
      state
      (let [new-state (case op
                        :nop (assoc state :pc (inc pc))
                        :acc (assoc state :pc (inc pc)
                                          :acc (+ acc arg))
                        :jmp (assoc state :pc (+ pc arg))
                        (throw (Exception. (str "Unrecognized op: " op))))]
        (recur program (assoc new-state :visited (conj visited pc)))))))

(defn execute [program]
  (execute* program initial-state))

(defn solve []
  (let [state (execute (load-data))]
    (:acc state)))
