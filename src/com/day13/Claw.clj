(ns com.day13.Claw
  (:require [com.utils.InputParsing :refer :all]
            [clojure.string :as str]))

(defn can-win-prize? [ta tb [ax ay] [bx by] [x y]]
  (= [(+ (* ta ax) (* tb bx)) (+ (* ta ay) (* tb by))] [x y]))

(defn tokens [ta tb]
  (+ (* ta 3) tb))

(defn find-prize-brute-force [[[ax ay] [bx by] [x y]]]
  (let [max-a (min (int (Math/ceil (/ x ax))) (int (Math/ceil (/ y ay))))
        max-b (min (int (Math/ceil (/ x bx))) (int (Math/ceil (/ y by))))]
    (reduce (fn [cheapest, count]
              (let
               [[a b] [(mod count max-a) (int (Math/floor (/ count max-a)))]]
                (if (and (can-win-prize? a b [ax ay] [bx by] [x y]) (or (zero? cheapest) (< (tokens a b) cheapest))) (reduced (tokens a b)) cheapest)))
            (range 0 (* (inc max-a) (inc max-b))))))

(defn parse-machine-configuration [machine-config]
  (let [[button-a-config button-b-config price-location] machine-config
        [ax ay] (map Integer/parseInt (rest (re-find #"Button A: X\+(\d+), Y\+(\d+)" button-a-config))) ;"Button A: X+69, Y+23"
        [bx by] (map Integer/parseInt (rest (re-find #"Button B: X\+(\d+), Y\+(\d+)" button-b-config))) ;"Button B: X+27, Y+71"
        [tx ty] (map Integer/parseInt (rest (re-find #"Prize: X=(\d+), Y=(\d+)" price-location))) ; Prize: X=8400, Y=5400
        ]
    [[ax ay] [bx by] [tx ty]]))

(defn find-all-prize-cheapest-brute-force
  "should find solution"
  [data]
  (let [claw-machines (filter #(> (count %) 1) (partition-by str/blank? data))]
    (->>
     claw-machines
     (pmap parse-machine-configuration)
     (pmap find-prize-brute-force)
     (reduce +))))

(defn add-task2-delta [[[ax ay] [bx by] [tx ty]]]
  [[ax ay] [bx by] [(+ tx 10000000000000) (+ ty 10000000000000)]])

; Button A: X+94, Y+34
; Button B: X+22, Y+67
; Prize: X=8400, Y=5400
;
; ax * ta + bx * tb = X
; ay * ta + by * tb = Y

; // sort both for ta
; ta = (X - bx * tb) / ax
; ta = (Y - by * tb) / ay
;
; // make it equal
; (X - bx * tb) / ax = (Y - by * tb) / ay
;
; // * ax
; (X - bx * tb)  = (ax * Y - ax * by * tb) / ay
;
; // * ay
; ay * X - ay * bx * tb  = ax * Y - ax * by * tb

; sort for tb
; ay * X - ax * Y = ay * bx * tb - ax * by * tb
; ay * X - ax * Y = tb * (ay * bx - ax * by)
; tb = (ay * X - ax * Y) / (ay * bx - ax * by)
;
;for ta it was
;ta = (X - bx * tb) / ax
;
(defn find-prize-formula [[[ax ay] [bx by] [x y]]]
  (let
   [; tb = (ay * X - ax * Y) / (ay * bx - ax * by)
    tb (long (Math/floor (/ (- (* ay x) (* ax y)) (- (* ay bx) (* ax by)))))
     ;ta = (X - bx * tb) / ax
    ta (long (Math/floor (/ (- x (* bx tb)) ax)))]
    (if (and (= x (+ (* ta ax) (* tb bx))) (= y (+ (* ta ay) (* tb by)))) (+ (* ta 3) tb) 0)))

(defn find-prize-cheapest-task2
  "should find solution"
  [data]
  (let [claw-machines (filter #(> (count %) 1) (partition-by str/blank? data))]
    (->>
     claw-machines
     (pmap parse-machine-configuration)
     (pmap add-task2-delta)
     (pmap find-prize-formula)
     (reduce +))))