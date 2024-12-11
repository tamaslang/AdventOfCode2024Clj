(ns com.day11.Stones-test
  (:require [clojure.test :refer :all]
            [com.day11.Stones :refer :all]
            [clojure.java.io :as io]))

(def data-file (io/resource
                "resources/day11/input.txt"))

(deftest should-solve-in-example
  (testing "Should solve"
    (is (= 22 (blink-optimised 6 "125 17")))))

(deftest should-solve-in-example-2
  (testing "Should solve"
    (is (= 55312 (blink-optimised 25 "125 17")))))

(deftest should-solve-for-input-file
  (testing "Should solve for input file"
    (is (= 199946 (blink-optimised 25 (slurp data-file))))))

(deftest should-solve-for-input-file-when-optimised
  (testing "Should solve for input file when optimised"
    (is (= 199946 (blink-optimised 25 (slurp data-file))))))

(deftest should-solve-for-input-file-when-optimised-blink-75-times
  (testing "Should solve for input file when optimised"
    (is (= 237994815702032 (blink-optimised 75 (slurp data-file))))))

(deftest should-solve-for-input-file-when-blink-75-times
  (testing "Should solve for input file when blink 72 times"
    (is (= 237994815702032 (blink-optimised 75 (slurp data-file))))))