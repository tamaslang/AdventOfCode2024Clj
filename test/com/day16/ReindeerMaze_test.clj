(ns com.day16.ReindeerMaze-test
  (:require [clojure.test :refer :all]
            [com.day16.ReindeerMaze :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def data-file (io/resource
                "resources/day16/input.txt"))

(deftest should-find=path-with-lowest-score-in-small-example
  (testing "Should find path with lowest score"
    (is (= 1004 (find-path-with-lowest-score Integer/MAX_VALUE ["#####"
                                                                "#..E#"
                                                                "#.#.#"
                                                                "#S..#"
                                                                "#####"])))))

(deftest should-find=path-with-lowest-score-in-example-1
  (testing "Should find path with lowest score"
    (is (= 7036 (find-path-with-lowest-score Integer/MAX_VALUE  ["###############"
                                                                 "#.......#....E#"
                                                                 "#.#.###.#.###.#"
                                                                 "#.....#.#...#.#"
                                                                 "#.###.#####.#.#"
                                                                 "#.#.#.......#.#"
                                                                 "#.#.#####.###.#"
                                                                 "#...........#.#"
                                                                 "###.#.#####.#.#"
                                                                 "#...#.....#.#.#"
                                                                 "#.#.#.###.#.#.#"
                                                                 "#.....#...#.#.#"
                                                                 "#.###.#.#.#.#.#"
                                                                 "#S..#.....#...#"
                                                                 "###############"])))))

(deftest should-find=path-with-lowest-score-in-example-2
  (testing "Should find path with lowest score"
    (is (= 11048 (find-path-with-lowest-score Integer/MAX_VALUE  ["#################"
                                                                  "#...#...#...#..E#"
                                                                  "#.#.#.#.#.#.#.#.#"
                                                                  "#.#.#.#...#...#.#"
                                                                  "#.#.#.#.###.#.#.#"
                                                                  "#...#.#.#.....#.#"
                                                                  "#.#.#.#.#.#####.#"
                                                                  "#.#...#.#.#.....#"
                                                                  "#.#.#####.#.###.#"
                                                                  "#.#.#.......#...#"
                                                                  "#.#.###.#####.###"
                                                                  "#.#.#...#.....#.#"
                                                                  "#.#.#.#####.###.#"
                                                                  "#.#.#.........#.#"
                                                                  "#.#.#.#########.#"
                                                                  "#S#.............#"
                                                                  "#################"])))))

(deftest should-find-path-with-lowest-score-in-input-file
  (testing "Should find path with lowest score in input file"
    (is (= 85396 (find-path-with-lowest-score 100000 (str/split-lines (slurp data-file)))))))

(deftest should-find-seats-in-small-example
  (testing "Should find seats in small example"
    (is (= 5 (find-tiles-for-seats Integer/MAX_VALUE ["#####"
                                                      "#..E#"
                                                      "#.#.#"
                                                      "#S..#"
                                                      "#####"])))))

(deftest should-find-saets-in-example-1
  (testing "Should find path with lowest score"
    (is (= 45 (find-tiles-for-seats Integer/MAX_VALUE  ["###############"
                                                        "#.......#....E#"
                                                        "#.#.###.#.###.#"
                                                        "#.....#.#...#.#"
                                                        "#.###.#####.#.#"
                                                        "#.#.#.......#.#"
                                                        "#.#.#####.###.#"
                                                        "#...........#.#"
                                                        "###.#.#####.#.#"
                                                        "#...#.....#.#.#"
                                                        "#.#.#.###.#.#.#"
                                                        "#.....#...#.#.#"
                                                        "#.###.#.#.#.#.#"
                                                        "#S..#.....#...#"
                                                        "###############"])))))

(deftest should-find-saets-in-example-2
  (testing "Should find path with lowest score"
    (is (= 64 (find-tiles-for-seats Integer/MAX_VALUE ["#################"
                                                       "#...#...#...#..E#"
                                                       "#.#.#.#.#.#.#.#.#"
                                                       "#.#.#.#...#...#.#"
                                                       "#.#.#.#.###.#.#.#"
                                                       "#...#.#.#.....#.#"
                                                       "#.#.#.#.#.#####.#"
                                                       "#.#...#.#.#.....#"
                                                       "#.#.#####.#.###.#"
                                                       "#.#.#.......#...#"
                                                       "#.#.###.#####.###"
                                                       "#.#.#...#.....#.#"
                                                       "#.#.#.#####.###.#"
                                                       "#.#.#.........#.#"
                                                       "#.#.#.#########.#"
                                                       "#S#.............#"
                                                       "#################"])))))

(deftest should-find-seats-in-input-file
  (testing "Should find seats in input file"
    (is (= 428 (find-tiles-for-seats 100000 (str/split-lines (slurp data-file)))))))