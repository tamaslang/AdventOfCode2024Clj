(ns com.day19.Towels_test
  (:require [clojure.test :refer :all]
            [com.day19.Towels :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def data-file (io/resource
                "resources/day19/input.txt"))

(deftest should-find-combinations-in-example
  (testing "Should find number of valid combinations"
    (is (= 6 (find-pattern-combinations ["r, wr, b, g, bwu, rb, gb, br"
                                         ""
                                         "brwrr"
                                         "bggr"
                                         "gbbr"
                                         "rrbgbr"
                                         "ubwu"
                                         "bwurrg"
                                         "brgr"
                                         "bbrgwb"])))))

(deftest should-find-combinations-in-input-file
  (testing "Should solve for input file"
    (is (= 308 (find-pattern-combinations (str/split-lines (slurp data-file)))))))

; SOLVED with GOOGLE SHEETS:
;   https://docs.google.com/spreadsheets/d/1mCQ1gWFqAXo9rQvHXZQRZPj41ouTFRGpM0mOXw5sU-E/edit?usp=sharing
(deftest should-find-combinations-in-input-file
  (testing "Should solve for input file"
    (is (= 308 (find-pattern-combinations (str/split-lines (slurp data-file)))))))

(deftest should-find-all-different-ways-in-example
  (testing "Should find all different ways of valid combinations"
    (is (= 16 (find-all-different-ways-for-pattern-combinations ["r, wr, b, g, bwu, rb, gb, br"
                                                                 ""
                                                                 "brwrr"
                                                                 "bggr"
                                                                 "gbbr"
                                                                 "rrbgbr"
                                                                 "ubwu"
                                                                 "bwurrg"
                                                                 "brgr"
                                                                 "bbrgwb"])))))

(deftest should-find-all-different-ways-in-input-file
  (testing "Should solve for input file"
    (is (= 662726441391898 (find-all-different-ways-for-pattern-combinations (str/split-lines (slurp data-file)))))))


