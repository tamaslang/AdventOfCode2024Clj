(ns com.day2.SafeReports-test
  (:require [clojure.test :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]
            [com.day2.SafeReports :refer :all]))

(def data-file (io/resource
                "resources/day2/input.txt"))

(deftest should-find-safe-reports
  (testing "Should find safe reports"
    (is (= 2 (number-of-safe-reports ["7 6 4 2 1"
                                      "1 2 7 8 9"
                                      "9 7 6 2 1"
                                      "1 3 2 4 5"
                                      "8 6 4 4 1"
                                      "1 3 6 7 9"])))))

(deftest should-find-safe-reports-in-input-file
  (testing "Should find safe reports in input file"
    (is (= 549 (number-of-safe-reports (str/split-lines (slurp data-file)))))))

(deftest should-find-safe-reports-with-dampener
  (testing "Should find safe reports with dampener"
    (is (= 4 (number-of-safe-reports-with-dampener ["7 6 4 2 1"
                                                    "1 2 7 8 9"
                                                    "9 7 6 2 1"
                                                    "1 3 2 4 5"
                                                    "8 6 4 4 1"
                                                    "1 3 6 7 9"])))))

(deftest should-find-safe-reports-with-dampener-in-input-file
  (testing "Should find safe reports with dampener in input file"
    (is (= 579 (number-of-safe-reports-with-dampener (str/split-lines (slurp data-file)))))))


(deftest should-find-safe-reports-with-dampener
  (testing "Should find safe reports with dampener"
    (is (= 1 (number-of-safe-reports-with-dampener ["65 66 63 60"])))))