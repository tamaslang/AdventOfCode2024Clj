(ns com.day11.Stones
  (:require [com.utils.InputParsing :refer :all]))

(defn apply-rule [stone]
  (cond
    (= stone 0) [1]
    (even? (count (str stone))) (map Integer/parseInt (map (partial apply str) (partition-all (/ (count (str stone)) 2) (str stone))))
    :else [(* 2024 stone)]
    )
  )

(defn blink
  "should find solution"
  [blink stones]
  (let [stones (parse-numbers-in-line stones)]
    (count (reduce (fn [all-stones _]
              (flatten (map apply-rule all-stones)))
            stones (range 0 blink) ))
  )
  )