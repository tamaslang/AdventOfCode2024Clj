(ns com.day21.Numpad-test
  (:require [clojure.test :refer :all]
            [com.day21.Numpad :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def data-file (io/resource
                "resources/day21/input.txt"))

(deftest should-solve-in-example
  (testing "Should solve"
    (is (= 126384 (calculate-robot-instructions-length 2 ["029A"
                                                          "980A"
                                                          "179A"
                                                          "456A"
                                                          "379A"])))))

(deftest should-solve-for-input-file
  (testing "Should solve for input file"
    (is (= 184180 (calculate-robot-instructions-length 2 (str/split-lines (slurp data-file)))))))

(deftest should-solve-for-input-file-part2
  (testing "Should solve for input file"
    (is (= 231309103124520 (calculate-robot-instructions-length 25 (str/split-lines (slurp data-file)))))))
