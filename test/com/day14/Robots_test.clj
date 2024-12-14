(ns com.day14.Robots-test
  (:require [clojure.test :refer :all]
            [com.day14.Robots :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def data-file (io/resource
                "resources/day14/input.txt"))

(deftest should-find-quadrant-sizes-in-example-1
  (testing "Should find quadrant sizes in example 1"
    (is (= 12 (count-robots-in-quadrants-after 100 [11 7] ["p=0,4 v=3,-3"
                                                           "p=6,3 v=-1,-3"
                                                           "p=10,3 v=-1,2"
                                                           "p=2,0 v=2,-1"
                                                           "p=0,0 v=1,3"
                                                           "p=3,0 v=-2,-2"
                                                           "p=7,6 v=-1,-3"
                                                           "p=3,0 v=-1,-2"
                                                           "p=9,3 v=2,3"
                                                           "p=7,3 v=-1,2"
                                                           "p=2,4 v=2,-3"
                                                           "p=9,5 v=-3,-3"])))))

(deftest should-find-quadrant-sizes-for-input-file
  (testing "Should find quadrant sizes in input file"
    (is (= 210587128 (count-robots-in-quadrants-after 100 [101 103] (str/split-lines (slurp data-file)))))))

(deftest should-find-christmas-tree-for-input-file
  (testing "Should find christmas tree in input file"
    (is (= 7286 (find-christmas-tree 7200 10000 [101 103] (str/split-lines (slurp data-file)))))))