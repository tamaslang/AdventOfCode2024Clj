(ns com.day25.Locks-test
  (:require [clojure.test :refer :all]
            [com.day25.Locks :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def data-file (io/resource
                "resources/day25/input.txt"))

(deftest should-find-mathching-keys
  (testing "Should solve"
    (is (= 0 (find-matching-keys ["#####"
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

(deftest should-solve-for-input-file
  (testing "Should solve for input file"
    (is (= 0 (find-matching-keys (str/split-lines (slurp data-file)))))))
