(ns com.day12.Fence-test
  (:require [clojure.test :refer :all]
            [com.day12.Fence :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def data-file (io/resource
                "resources/day12/input.txt"))

(deftest should-find-price-for-simple-example-1
  (testing "Should find price for example 1"
    (is (= 772 (fencing-price ["OOOOO"
                               "OXOXO"
                               "OOOOO"
                               "OXOXO"
                               "OOOOO"])))))

(deftest should-find-price-for-simple-example-2
  (testing "Should find price for example 2"
    (is (= 140 (fencing-price ["AAAA"
                               "BBCD"
                               "BBCC"
                               "EEEC"])))))

(deftest should-find-price-for-complex-example-3
  (testing "Should find price for example 3"
    (is (= 1930 (fencing-price ["RRRRIICCFF"
                                "RRRRIICCCF"
                                "VVRRRCCFFF"
                                "VVRCCCJFFF"
                                "VVVVCJJCFE"
                                "VVIVCCJJEE"
                                "VVIIICJJEE"
                                "MIIIIIJJEE"
                                "MIIISIJEEE"
                                "MMMISSJEEE"])))))

(deftest should-find-price-for-input-file
  (testing "Should find price for input file"
    (is (= 1402544 (fencing-price (str/split-lines (slurp data-file)))))))

; TASK 2
(deftest should-find-bulk-price-for-simple-example-1
  (testing "Should find bulk price for example 1"
    (is (= 436 (fencing-price-bulk ["OOOOO"
                                    "OXOXO"
                                    "OOOOO"
                                    "OXOXO"
                                    "OOOOO"])))))

(deftest should-find-bulk-price-for-simple-example-2
  (testing "Should find bulk price for example 2"
    (is (= 80 (fencing-price-bulk ["AAAA"
                                   "BBCD"
                                   "BBCC"
                                   "EEEC"])))))

(deftest should-find-bulk-price-for-example-3
  (testing "Should find bulk price for example 3"
    (is (= 236 (fencing-price-bulk ["EEEEE"
                                    "EXXXX"
                                    "EEEEE"
                                    "EXXXX"
                                    "EEEEE"])))))

(deftest should-find-bulk-price-for-example-4
  (testing "Should find bulk price for example 4"
    (is (= 368 (fencing-price-bulk ["AAAAAA"
                                    "AAABBA"
                                    "AAABBA"
                                    "ABBAAA"
                                    "ABBAAA"
                                    "AAAAAA"])))))

(deftest should-find-bulk-price-for-example-5
  (testing "Should find bulk price for example 5"
    (is (= 946 (fencing-price-bulk ["AAAAAAAA"
                                    "AACBBDDA"
                                    "AACBBAAA"
                                    "ABBAAAAA"
                                    "ABBADDDA"
                                    "AAAADADA"
                                    "AAAAAAAA"])))))

(deftest should-find-bulk-price-for-input-file
  (testing "Should find bulk price for input file"
    (is (= 862486 (fencing-price-bulk (str/split-lines (slurp data-file)))))))