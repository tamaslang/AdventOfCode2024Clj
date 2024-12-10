(ns com.day10.Trailheads-test
  (:require [clojure.test :refer :all]
            [com.day10.Trailheads :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def data-file (io/resource
                "resources/day10/input.txt"))

(deftest should-solve-in-example
  (testing "Should solve"
    (is (= 36 (find-trailheads ["89010123"
                                "78121874"
                                "87430965"
                                "96549874"
                                "45678903"
                                "32019012"
                                "01329801"
                                "10456732"])))))

(deftest should-solve-for-input-file
  (testing "Should solve for input file"
    (is (= 0 (find-trailheads (str/split-lines (slurp data-file)))))))
