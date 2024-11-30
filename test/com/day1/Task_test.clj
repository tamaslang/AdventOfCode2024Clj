(ns com.day1.Task-test
  (:require [clojure.test :refer :all]
            [com.day1.Task :refer :all]
            [com.util.Utils :as utils]
            [clojure.java.io :as io]))

(def data-file (io/resource
                "resources/day1/input.txt"))

(deftest task-test
  (testing "Should solve for example"
    (is (= 0 (solveTask ["1abc2"])))))

(deftest task-test-input
  (testing "Should solve for input file"
    (is (= 0 (solveTask (slurp data-file))))))




