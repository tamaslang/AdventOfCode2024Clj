(ns com.day7.Calibration-test
  (:require [clojure.test :refer :all]
            [com.day7.Calibration :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def data-file (io/resource
                "resources/day7/input.txt"))

(deftest should-find-calibration-result-for-example
  (testing "Should find calibration result for example"
    (is (= 3457 (find-calibration-result task-1-functions ["190: 10 19"
                                                           "3267: 81 40 27"
                                                           "83: 17 5"
                                                           "156: 15 6"
                                                           "7290: 6 8 6 15"
                                                           "161011: 16 10 13"
                                                           "192: 17 8 14"
                                                           "21037: 9 7 18 13"
                                                           "292: 11 6 16 20"])))))

(deftest should-find-calibration-result-for-input-file
  (testing "Should find calibration result for input file"
    (is (= 4364915411363 (find-calibration-result task-1-functions (str/split-lines (slurp data-file)))))))

(deftest should-find-calibration-result-for-example-task2
  (testing "Should find calibration result for example for task 2"
    (is (= 11387 (find-calibration-result task-2-functions ["190: 10 19"
                                                            "3267: 81 40 27"
                                                            "83: 17 5"
                                                            "156: 15 6"
                                                            "7290: 6 8 6 15"
                                                            "161011: 16 10 13"
                                                            "192: 17 8 14"
                                                            "21037: 9 7 18 13"
                                                            "292: 11 6 16 20"])))))

(deftest should-find-calibration-result-for-input-file-task2
  (testing "Should find calibration result for input file fr task 2"
    (is (= 38322057216320 (find-calibration-result task-2-functions (str/split-lines (slurp data-file)))))))