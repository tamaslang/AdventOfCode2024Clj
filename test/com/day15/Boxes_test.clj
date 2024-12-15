(ns com.day15.Boxes-test
  (:require [clojure.test :refer :all]
            [com.day15.Boxes :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def data-file (io/resource
                "resources/day15/input.txt"))

(deftest should-solve-in-small-example
  (testing "Should find box positions in small example"
    (is (= 2028 (find-box-positions ["########"
                                     "#..O.O.#"
                                     "##@.O..#"
                                     "#...O..#"
                                     "#.#.O..#"
                                     "#...O..#"
                                     "#......#"
                                     "########"
                                     ""
                                     "<^^>>>vv<v>>v<<"])))))

(deftest should-solve-in-large-example
  (testing "Should find box positions in large example"
    (is (= 10092 (find-box-positions ["##########"
                                      "#..O..O.O#"
                                      "#......O.#"
                                      "#.OO..O.O#"
                                      "#..O@..O.#"
                                      "#O#..O...#"
                                      "#O..O..O.#"
                                      "#.OO.O.OO#"
                                      "#....O...#"
                                      "##########"
                                      ""
                                      "<vv>^<v^>v>^vv^v>v<>v^v<v<^vv<<<^><<><>>v<vvv<>^v^>^<<<><<v<<<v^vv^v>^vvv<<^>^v^^><<>>><>^<<><^vv^^<>vvv<>><^^v>^>vv<>v<<<<v<^v>^<^^>>>^<v<v><>vv>v^v^<>><>>>><^^>vv>v<^^^>>v^v^<^^>v^^>v^<^v>v<>>v^v^<v>v^^<^^vv<<<v<^>>^^^^>>>v^<>vvv^><v<<<>^^^vv^<vvv>^>v<^^^^v<>^>vvvv><>>v^<<^^^^^^><^><>>><>^^<<^^v>>><^<v>^<vv>>v>>>^v><>^v><<<<v>>v<v<v>vvv>^<><<>^><^>><>^v<><^vvv<^^<><v<<<<<><^v<<<><<<^^<v<^^^><^>>^<v^><<<^>>^v<v^v<v^>^>>^v>vv>^<<^v<>><<><<v<<v><>v<^vv<<<>^^v^>^^>>><<^v>>v^v><^^>>^<>vv^<><^^>^^^<><vvvvv^v<v<<>^v<v>v<<^><<><<><<<^^<<<^<<>><<><^^^>^^<>^>v<>^^>vv<^v^v<vv>^<><v<^v>^^^>>>^^vvv^>vvv<>>>^<^>>>>>^<<^v>^vvv<>^<><<v>v^^>>><<^^<>>^v^<v^vv<>v^<<>^<^v^v><^<<<><<^<v><v<>vv>>v><v^<vv<>v^<<^"])))))

(deftest should-solve-for-input-file
  (testing "Should find box positions in input file"
    (is (= 1538613 (find-box-positions (str/split-lines (slurp data-file)))))))
; 1st attempt 1538613 (too low)