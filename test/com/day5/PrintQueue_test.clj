(ns com.day5.PrintQueue-test
  (:require [clojure.test :refer :all]
            [com.day5.PrintQueue :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def data-file (io/resource
                "resources/day5/input.txt"))

(deftest should-find-valid-queue-in-example
  (testing "Should find valid queue in example"
    (is (= 143 (find-valid-queue ["47|53"
                                  "97|13"
                                  "97|61"
                                  "97|47"
                                  "75|29"
                                  "61|13"
                                  "75|53"
                                  "29|13"
                                  "97|29"
                                  "53|29"
                                  "61|53"
                                  "97|53"
                                  "61|29"
                                  "47|13"
                                  "75|47"
                                  "97|75"
                                  "47|61"
                                  "75|61"
                                  "47|29"
                                  "75|13"
                                  "53|13"
                                  ""
                                  "75,47,61,53,29"
                                  "97,61,53,29,13"
                                  "75,29,13"
                                  "75,97,47,61,53"
                                  "61,13,29"
                                  "97,13,75,29,47"])))))

(deftest should-find-valid-queue-in-input-file
  (testing "Should find valid queue for input file"
    (is (= 6505 (find-valid-queue (str/split-lines (slurp data-file)))))))

(deftest should-fix-invalid-queues-in-example
  (testing "Should fix invalid queues"
    (is (= 123 (fix-invalid-queues ["47|53"
                                    "97|13"
                                    "97|61"
                                    "97|47"
                                    "75|29"
                                    "61|13"
                                    "75|53"
                                    "29|13"
                                    "97|29"
                                    "53|29"
                                    "61|53"
                                    "97|53"
                                    "61|29"
                                    "47|13"
                                    "75|47"
                                    "97|75"
                                    "47|61"
                                    "75|61"
                                    "47|29"
                                    "75|13"
                                    "53|13"
                                    ""
                                    "75,47,61,53,29"
                                    "97,61,53,29,13"
                                    "75,29,13"
                                    "75,97,47,61,53"
                                    "61,13,29"
                                    "97,13,75,29,47"])))))

(deftest should-fix-invalid-queues-in-input-file
  (testing "Should fix invalid queues for input file"
    (is (= 6897 (fix-invalid-queues (str/split-lines (slurp data-file)))))))