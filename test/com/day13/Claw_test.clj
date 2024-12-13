(ns com.day13.Claw-test
  (:require [clojure.test :refer :all]
            [com.day13.Claw :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def data-file (io/resource
                "resources/day13/input.txt"))

(deftest should-find-cheapest-price-to-win-all-prize-for-the-example
  (testing "Should find cheapest price to win all prize for the example"
    (is (= 480 (find-prize-cheapest
                ["Button A: X+94, Y+34"
                 "Button B: X+22, Y+67"
                 "Prize: X=8400, Y=5400"
                 ""
                 "Button A: X+26, Y+66"
                 "Button B: X+67, Y+21"
                 "Prize: X=12748, Y=12176"
                 ""
                 "Button A: X+17, Y+86"
                 "Button B: X+84, Y+37"
                 "Prize: X=7870, Y=6450"
                 ""
                 "Button A: X+69, Y+23"
                 "Button B: X+27, Y+71"
                 "Prize: X=18641, Y=10279"])))))

(deftest should-find-cheapest-price-to-win-all-prize-for-the-input-file
  (testing "Should find cheapest price to win all prize for the input file"
    (is (= 28059 (find-prize-cheapest (str/split-lines (slurp data-file)))))))
