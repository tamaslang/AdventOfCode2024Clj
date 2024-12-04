(ns com.day4.WorldSearch-test
  (:require [clojure.test :refer :all]
            [com.day4.WorldSearch :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def data-file (io/resource
                "resources/day4/input.txt"))

(deftest should-find-xmas-in-small-example
  (testing "Should find XMAS in small example"
    (is (= 4  (world-search ["..X..."
                             ".SAMX."
                             ".A..A."
                             "XMAS.S"
                             ".X...."])))))

(deftest should-solve-in-example-with-dots
  (testing "Should find XMAS in example with dots"
    (is (= 18 (world-search ["....XXMAS."
                             ".SAMXMS..."
                             "...S..A..."
                             "..A.A.MS.X"
                             "XMASAMX.MM"
                             "X.....XA.A"
                             "S.S.S.S.SS"
                             ".A.A.A.A.A"
                             "..M.M.M.MM"
                             ".X.X.XMASX"]))))) +
(deftest should-find-xmas-in-example-with-all
  (testing "Should find XMAS in example with all chars"
    (is (= 18 (world-search ["MMMSXXMASM"
                             "MSAMXMSMSA"
                             "AMXSXMAAMM"
                             "MSAMASMSMX"
                             "XMASAMXAMM"
                             "XXAMMXXAMA"
                             "SMSMSASXSS"
                             "SAXAMASAAA"
                             "MAMMMXMMMM"
                             "MXMXAXMASX"])))))

(deftest should-solve-for-input-file
  (testing "Should solve for input file"
    (is (= 2639 (world-search (str/split-lines (slurp data-file)))))))

(deftest should-find-crossed-mas-in-example
  (testing "Should find crossed XMAS in example"
    (is (= 9  (world-cross-search [".M.S......"
                                   "..A..MSMS."
                                   ".M.S.MAA.."
                                   "..A.ASMSM."
                                   ".M.S.M...."
                                   ".........."
                                   "S.S.S.S.S."
                                   ".A.A.A.A.."
                                   "M.M.M.M.M."
                                   ".........."])))))

(deftest should-find-crossed-mas-in-small-example-1
  (testing "Should find crossed XMAS in small example 1"
    (is (= 1  (world-cross-search ["M.S"
                                   ".A."
                                   "M.S"])))))

(deftest should-find-crossed-mas-in-small-example-2
  (testing "Should find crossed XMAS in small example 2"
    (is (= 1  (world-cross-search ["S.S"
                                   ".A."
                                   "M.M"])))))

(deftest should-find-crossed-mas-in-small-example-3
  (testing "Should find crossed XMAS in small example 3"
    (is (= 1  (world-cross-search ["S.M"
                                   ".A."
                                   "S.M"])))))

(deftest should-find-crossed-mas-in-small-example-4
  (testing "Should find crossed XMAS in small example 4"
    (is (= 1  (world-cross-search ["M.M"
                                   ".A."
                                   "S.S"])))))

(deftest should-find-crossed-mas-in-example
  (testing "Should find crossed XMAS in example"
    (is (= 8  (world-cross-search ["M.S.M.S.M"
                                   ".A.A.A.A."
                                   "M.S.M.S.M"
                                   ".A.A.A.A."
                                   "M.S.M.S.M"])))))

(deftest should-solve-crossed-for-input-file
  (testing "Should solve for input file"
    (is (= 2005 (world-cross-search (str/split-lines (slurp data-file)))))))
