(ns com.day10.Task-test
  (:require [clojure.test :refer :all]
            [com.day10.Task :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def data-file (io/resource
                "resources/day10/input.txt"))

(deftest should-solve-in-example
  (testing "Should solve"
    (is (= 0 (solve ["3   4"])))))

(deftest should-solve-for-input-file
  (testing "Should solve for input file"
    (is (= 0 (solve (str/split-lines (slurp data-file)))))))
