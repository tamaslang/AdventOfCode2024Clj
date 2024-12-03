(ns com.day3.Task-test
  (:require [clojure.test :refer :all]
            [com.day3.Task :refer :all]
            [com.util.Utils :as utils]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def data-file (io/resource
                "resources/day3/input.txt"))

(deftest should-solve-in-example
  (testing "Should solve"
    (is (= 161 (solve "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))")))))

(deftest should-solve-for-input-file
  (testing "Should solve for input file"
    (is (= 164730528 (solve (slurp data-file))))))

(deftest should-solve2-in-example
  (testing "Should solve2"
    (is (= 48 (solve2 "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))")))))

(deftest should-solve2-for-input-file
  (testing "Should solve2 for input file"
    (is (= 164730528 (solve2 (slurp data-file))))))
