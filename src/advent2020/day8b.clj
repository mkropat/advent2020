(ns advent2020.day8b
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
                    :visited #{}
                    :halted? false})

(defn- execute* [program state]
  (let [{:keys [acc pc visited]} state]
    (cond
      (= pc (count program)) (assoc state :halted? true)
      (not (<= 0 pc (count program))) state
      (contains? visited pc) state
      :else (let [{:keys [op arg]} (nth program pc)
                  new-state (case op
                              :nop (assoc state :pc (inc pc))
                              :acc (assoc state :pc (inc pc)
                                                :acc (+ acc arg))
                              :jmp (assoc state :pc (+ pc arg))
                              (throw (Exception. (str "Unrecognized op: " op))))]
              (recur program (assoc new-state :visited (conj visited pc)))))))

(defn execute [program]
  (execute* program initial-state))

(defn swappable-op? [op]
  (case op
    :nop true
    :jmp true
    false))

(defn swap-op [op]
  (case op
    :nop :jmp
    :jmp :nop))

(defn- enumerate-tweaked-programs* [program i]
  (if (>= i (count program))
    ()
    (let [instruction (nth program i)
          {op :op} instruction]
      (if (swappable-op? op)
        (let [new-instruction (assoc instruction :op (swap-op op))
              new-program (assoc program i new-instruction)]
          (lazy-seq (cons new-program (enumerate-tweaked-programs* program (inc i)))))
        (enumerate-tweaked-programs* program (inc i))))))

(defn enumerate-tweaked-programs [program]
  (enumerate-tweaked-programs* program 0))

(defn solve []
  (->> (enumerate-tweaked-programs (load-data))
       (map execute)
       (filter :halted?)
       (first)
       :acc))
