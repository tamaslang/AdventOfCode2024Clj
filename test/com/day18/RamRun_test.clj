(ns com.day18.RamRun-test
  (:require [clojure.test :refer :all]
            [com.day18.RamRun :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def data-file (io/resource
                "resources/day18/input.txt"))

(deftest should-find-shortest-path-in-example
  (testing "Should find shortest path in example"
    (is (= 22 (shortest-path-to-exit true 7 12 ["5,4"
                                                "4,2"
                                                "4,5"
                                                "3,0"
                                                "2,1"
                                                "6,3"
                                                "2,4"
                                                "1,5"
                                                "0,6"
                                                "3,3"
                                                "2,6"
                                                "5,1"
                                                "1,2"
                                                "5,5"
                                                "2,5"
                                                "6,5"
                                                "1,4"
                                                "0,4"
                                                "6,4"
                                                "1,1"
                                                "6,1"
                                                "1,0"
                                                "0,5"
                                                "1,6"
                                                "2,0"])))))

(deftest should-find-shortest-path-in-input-file
  (testing "Should find shortest path in input file"
    (is (= 340 (shortest-path-to-exit false 71 1024 (str/split-lines (slurp data-file)))))))

(deftest should-find-blocking-byte-in-input-file
  (testing "Should find blocking byte in input file"
    (is (= "34,32" (first-byte-that-blocks 71 (str/split-lines (slurp data-file)))))))