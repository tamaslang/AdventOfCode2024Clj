(ns com.day10.Trailheads-test
  (:require [clojure.test :refer :all]
            [com.day10.Trailheads :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def data-file (io/resource
                "resources/day10/input.txt"))

(deftest should-find-trailheads-in-example
  (testing "Should find trailheads in example"
    (is (= 36 (find-trailheads ["89010123"
                                "78121874"
                                "87430965"
                                "96549874"
                                "45678903"
                                "32019012"
                                "01329801"
                                "10456732"])))))

(deftest should-find-trailheads-in-input-file
  (testing "Should find trailheads in input file"
    (is (= 816 (find-trailheads (str/split-lines (slurp data-file)))))))

(deftest should-find-trailheads-with-distinct-route-in-example-1
  (testing "Should find trailheads with distinct route in example 1"
    (is (= 3 (find-trailheads-scores
              [".....0."
               "..4321."
               "..5..2."
               "..6543."
               "..7..4."
               "..8765."
               "..9...."])))))

(deftest should-find-trailheads-with-distinct-route-in-example-2
  (testing "Should find trailheads with distinct route in example 2"
    (is (= 13 (find-trailheads-scores
               ["..90..9"
                "...1.98"
                "...2..7"
                "6543456"
                "765.987"
                "876...."
                "987...."])))))

(deftest should-find-trailheads-with-distinct-route-in-example-3
  (testing "Should find trailheads with distinct route in example 3"
    (is (= 227 (find-trailheads-scores
                ["012345"
                 "123456"
                 "234567"
                 "345678"
                 "4.6789"
                 "56789."])))))

(deftest should-find-trailheads-with-distinct-route-in-example-4
  (testing "Should find trailheads with distinct route in example 4"
    (is (= 81 (find-trailheads-scores
               ["89010123"
                "78121874"
                "87430965"
                "96549874"
                "45678903"
                "32019012"
                "01329801"
                "10456732"])))))

(deftest should-find-trailheads-in-input-file
  (testing "Should find trailheads in input file"
    (is (= 1960 (find-trailheads-scores (str/split-lines (slurp data-file)))))))