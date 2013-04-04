(ns behaviors.t-provided-for-parallel-code
  (:require [midje.sweet :refer :all]))

(defn my-func [x]
  (+ x 2))

(defn my-func-caller [data]
  (pmap my-func data))

(defn do-something []
  (my-func-caller [1 2 3]))

(fact "Test some pcalls"
  (do-something) => [2 4 6]
  (provided
    (my-func 1) => 2
    (my-func 2) => 4
    (my-func 3) => 6))

(fact "Test some pcalls 2"
  (do-something) => [2 4 6]
  (provided
    (my-func 1) => 2
    (my-func 2) => 4
    (my-func 3) => 6))

(fact "Test some pcalls 3"
  (do-something) => [2 4 6]
  (provided
    (my-func 1) => 2
    (my-func 2) => 4
    (my-func 3) => 6))