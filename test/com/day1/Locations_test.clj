(ns com.day1.Locations-test
  (:require [clojure.test :refer :all]
            [com.day1.Locations :refer :all]
            [com.util.Utils :as utils]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def data-file (io/resource
                "resources/day1/input.txt"))

(deftest should-find-distance-for-locations-in-example
  (testing "Should find distance for locations"
    (is (= 11 (findDistance ["3   4"
                             "4   3"
                             "2   5"
                             "1   3"
                             "3   9"
                             "3   3"])))))

(deftest should-find-distance-for-locations-in-input-file
  (testing "Should find distance for input file"
    (is (= 2970687 (findDistance (str/split-lines (slurp data-file)))))))

(deftest should-find-similarity-for-locations-in-example
  (testing "Should find similarity for locations"
    (is (= 31 (findSimilarity ["3   4"
                               "4   3"
                               "2   5"
                               "1   3"
                               "3   9"
                               "3   3"])))))

(deftest should-find-similarity-for-locations-in-input-file
  (testing "Should find similarity for input file"
    (is (= 23963899 (findSimilarity (str/split-lines (slurp data-file)))))))




