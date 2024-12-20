(ns com.day20.RaceTrack-test
  (:require [clojure.test :refer :all]
            [com.day20.RaceTrack :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def data-file (io/resource
                "resources/day20/input.txt"))

(deftest should-find-cheats-in-example
  (testing "Should find cheats in example"
    (is (= 44 (find-cheats 0 ["###############"
                              "#...#...#.....#"
                              "#.#.#.#.#.###.#"
                              "#S#...#.#.#...#"
                              "#######.#.#.###"
                              "#######.#.#...#"
                              "#######.#.###.#"
                              "###..E#...#...#"
                              "###.#######.###"
                              "#...###...#...#"
                              "#.#####.#.###.#"
                              "#.#...#.#.#...#"
                              "#.#.#.#.#.#.###"
                              "#...#...#...###"
                              "###############"])))))

(deftest should-find-cheats-in-example-with-saved-picosecs-at-least-20
  (testing "Should find cheats in example"
    (is (= 5 (find-cheats 20 ["###############"
                              "#...#...#.....#"
                              "#.#.#.#.#.###.#"
                              "#S#...#.#.#...#"
                              "#######.#.#.###"
                              "#######.#.#...#"
                              "#######.#.###.#"
                              "###..E#...#...#"
                              "###.#######.###"
                              "#...###...#...#"
                              "#.#####.#.###.#"
                              "#.#...#.#.#...#"
                              "#.#.#.#.#.#.###"
                              "#...#...#...###"
                              "###############"])))))

(deftest should-solve-for-input-file
  (testing "Should solve for input file"
    (is (= 1263 (find-cheats 100 (str/split-lines (slurp data-file)))))))
