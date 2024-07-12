(ns clojure-devenv-example.main-test
  (:require [clojure.test :refer :all]
            [clojure-devenv-example.main :as sut]))

(deftest a-test
  (testing "hopefully correct test"
    (is (= 5 (sut/return-5))))
  (testing "FIXME, I fail."
    (is (= 2 1))))
