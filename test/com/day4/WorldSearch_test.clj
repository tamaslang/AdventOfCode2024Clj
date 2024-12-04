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
  (testing "Should find XMAS in example 2"
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
  (testing "Should find XMAS in example 2"
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
    (is (= 0 (world-search (str/split-lines (slurp data-file)))))))
