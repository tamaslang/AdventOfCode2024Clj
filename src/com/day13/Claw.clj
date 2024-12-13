(ns com.day13.Claw
  (:require [com.utils.InputParsing :refer :all]
            [clojure.string :as str]))

(defn can-win-prize? [a b [ax ay] [bx by] [tx ty]]
  (= [(+ (* a ax) (* b bx)) (+ (* a ay) (* b by))] [tx ty]))

(defn tokens [a b]
  (+ (* a 3) b))

(defn find-cheapest [[[ax ay] [bx by] [tx ty]]]
  (let [max-a (min (int (Math/ceil (/ tx ax))) (int (Math/ceil (/ ty ay))))
        max-b (min (int (Math/ceil (/ tx bx))) (int (Math/ceil (/ ty by))))]
    (reduce (fn [cheapest, count]
              (let
               [[a b] [(mod count max-a) (int (Math/floor (/ count max-a)))]]
                (if (and (can-win-prize? a b [ax ay] [bx by] [tx ty]) (or (zero? cheapest) (< (tokens a b) cheapest))) (tokens a b) cheapest)))

            (range 0 (* (inc max-a) (inc max-b))))))

(defn parse-machine-configuration [machine-config]
  (let [[button-a-config button-b-config price-location] machine-config
        [ax ay] (map Integer/parseInt (rest (re-find #"Button A: X\+(\d+), Y\+(\d+)" button-a-config))) ;"Button A: X+69, Y+23"
        [bx by] (map Integer/parseInt (rest (re-find #"Button B: X\+(\d+), Y\+(\d+)" button-b-config))) ;"Button B: X+27, Y+71"
        [tx ty] (map Integer/parseInt (rest (re-find #"Prize: X=(\d+), Y=(\d+)" price-location))) ; Prize: X=8400, Y=5400
        ]
    [[ax ay] [bx by] [tx ty]]))

(defn find-prize-cheapest
  "should find solution"
  [data]
  (let [claw-machines (filter #(> (count %) 1) (partition-by str/blank? data))]
    (->>
     claw-machines
     (pmap parse-machine-configuration)
     (pmap find-cheapest)
     (reduce +))))