(defproject advent2020 "0.1.0-SNAPSHOT"
  :dependencies [[org.clojure/clojure "1.9.0"]
                 [org.clojure/math.combinatorics "0.1.6"]]
  :main ^:skip-aot advent2020.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
