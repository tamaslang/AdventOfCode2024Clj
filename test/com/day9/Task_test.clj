(ns com.day9.Task-test
  (:require [clojure.test :refer :all]
            [com.day9.Task :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def data-file (io/resource
                "resources/day9/input.txt"))

(deftest should-calculate-checksum-for-memory-in-example
  (testing "Should calculate checksum for memory in example"
    (is (= 1928 (calculate-checksum-for-memory "2333133121414131402")))))

(deftest should-calculate-checksum-for-memory-in-input-file
  (testing "Should calculate checksum for memory in input file"
    (is (= 6461289671426 (calculate-checksum-for-memory (slurp data-file))))))
