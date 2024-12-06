(ns com.day6.Guard-test
  (:require [clojure.test :refer :all]
            [com.day6.Guard :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def data-file (io/resource
                "resources/day6/input.txt"))

(deftest should-count-the-patrol-fields-in-example
  (testing "Should count the patrol fields in the example"
    (is (= 41 (count-patrol-field ["....#....."
                                   ".........#"
                                   ".........."
                                   "..#......."
                                   ".......#.."
                                   ".........."
                                   ".#..^....."
                                   "........#."
                                   "#........."
                                   "......#..."])))))

(deftest should-count-the-patrol-fields-in-input-file
  (testing "Should count the patrol fields for input file"
    (is (= 5131 (count-patrol-field (str/split-lines (slurp data-file)))))))
