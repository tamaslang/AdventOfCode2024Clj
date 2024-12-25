(ns com.day25.Locks-test
  (:require [clojure.test :refer :all]
            [com.day25.Locks :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def data-file (io/resource
                "resources/day25/input.txt"))

(deftest should-find-matching-keys-in-example
  (testing "Should find matching keys in example"
    (is (= 3 (find-matching-keys ["#####"
                                  ".####"
                                  ".####"
                                  ".####"
                                  ".#.#."
                                  ".#..."
                                  "....."
                                  ""
                                  "#####"
                                  "##.##"
                                  ".#.##"
                                  "...##"
                                  "...#."
                                  "...#."
                                  "....."
                                  ""
                                  "....."
                                  "#...."
                                  "#...."
                                  "#...#"
                                  "#.#.#"
                                  "#.###"
                                  "#####"
                                  ""
                                  "....."
                                  "....."
                                  "#.#.."
                                  "###.."
                                  "###.#"
                                  "###.#"
                                  "#####"
                                  ""
                                  "....."
                                  "....."
                                  "....."
                                  "#...."
                                  "#.#.."
                                  "#.#.#"
                                  "#####"])))))

(deftest should-find-matching-keys-in-input-file
  (testing "Should find matching keys in input file"
    (is (= 2900 (find-matching-keys (str/split-lines (slurp data-file)))))))
