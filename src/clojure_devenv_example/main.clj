(ns clojure-devenv-example.main
  (:require [taoensso.telemere :as t]))

(defn return-5
  "Returns the number 5"
  []
  (t/log! "returning 5!")
  5)

(defn -main
  "Entry point for JAR"
  []
  (println "This is the result of our amazing engineering so far: " (return-5)))
