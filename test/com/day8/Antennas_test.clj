(ns com.day8.Antennas-test
  (:require [clojure.test :refer :all]
            [com.day8.Antennas :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def data-file (io/resource
                "resources/day8/input.txt"))

(deftest should-find-unique-antiondes-in-example
  (testing "Should find unique antinodes"
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

(deftest should-solve-for-input-file
  (testing "Should solve for input file"
    (is (= 271 (unique-antinode-locations (str/split-lines (slurp data-file)))))))
