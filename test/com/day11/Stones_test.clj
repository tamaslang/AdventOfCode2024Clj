(ns com.day11.Stones-test
  (:require [clojure.test :refer :all]
            [com.day11.Stones :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def data-file (io/resource
                "resources/day11/input.txt"))

(deftest should-solve-in-example
  (testing "Should solve"
    (is (= 22 (blink 6 "125 17")))))

(deftest should-solve-in-example
  (testing "Should solve"
    (is (= 55312 (blink 25 "125 17")))))

(deftest should-solve-for-input-file
  (testing "Should solve for input file"
    (is (= 199946 (blink 25 (slurp data-file))))))
