(ns com.day14.Task-test
  (:require [clojure.test :refer :all]
            [com.day14.Task :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def data-file (io/resource
                "resources/day14/input.txt"))

(deftest should-solve-in-example-0
  (testing "Should solve"
    (is (= 0 (solve 100 [11 7] ["p=2,4 v=2,-3"])))))

(deftest should-solve-in-example-1
  (testing "Should solve"
    (is (= 0 (solve 100 [11 7] ["p=0,4 v=3,-3"
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

(deftest should-solve-for-input-file
  (testing "Should solve for input file"
    (is (= 210587128 (solve 100 [101 103] (str/split-lines (slurp data-file)))))))

(deftest should-solve-for-input-file
  (testing "Should solve for input file"
    (is (= 0 (solve-for-christmas-tree 2570000 Integer/MAX_VALUE [101 103] (str/split-lines (slurp data-file)))))))
