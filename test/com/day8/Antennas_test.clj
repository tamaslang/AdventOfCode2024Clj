(ns com.day8.Antennas-test
  (:require [clojure.test :refer :all]
            [com.day8.Antennas :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def data-file (io/resource
                "resources/day8/input.txt"))

(deftest should-find-unique-antiondes-in-example
  (testing "Should find unique antinodes in example"
    (is (= 14 (unique-antinode-locations
               ["............"
                "........0..."
                ".....0......"
                ".......0...."
                "....0......."
                "......A....."
                "............"
                "............"
                "........A..."
                ".........A.."
                "............"
                "............"])))))

(deftest should-find-unique-antiondes-in-input-file
  (testing "Should find unique antinodes in input file"
    (is (= 271 (unique-antinode-locations (str/split-lines (slurp data-file)))))))

; TASK2
(deftest should-find-unique-antiondes-with-resonance-in-example
  (testing "Should find unique antinodes with resonance in example"
    (is (= 34 (unique-antinode-locations-with-resonance
               ["............"
                "........0..."
                ".....0......"
                ".......0...."
                "....0......."
                "......A....."
                "............"
                "............"
                "........A..."
                ".........A.."
                "............"
                "............"])))))

(deftest should-find-unique-antiondes-with-resonnce-in-input-file
  (testing "Should find unique antinodes with resonance in input file"
    (is (= 994 (unique-antinode-locations-with-resonance (str/split-lines (slurp data-file)))))))