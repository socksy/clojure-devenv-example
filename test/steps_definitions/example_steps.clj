(ns steps-definitions.example-steps
  (:require [lambdaisland.cucumber.dsl :refer :all]
            [clojure.test :refer :all]))

(Given "the following currency prices in dollars" [state data-table]
  ;; Write code here that turns the phrase above into concrete actions
  ;; The last argument is a vector of vectors of strings.
  (assoc state
         :conversion-rates
         (into {}
               (map (fn [[k v]]
                      [k (Double/parseDouble v)]))
               data-table)))

(When "I order a {double} {word} coffee from Ben Rahim" [state amount currency]
  ;; Write code here that turns the phrase above into concrete actions
  (-> state
      (assoc :amount amount)
      (assoc :currency currency)))

(Then "I have wasted ${double}" [{:keys [amount currency] :as state} dollar-amount]
  ;; Write code here that turns the phrase above into concrete actions
  (is (= dollar-amount
         (* amount
            (get-in state [:conversion-rates currency]))))
  state)
