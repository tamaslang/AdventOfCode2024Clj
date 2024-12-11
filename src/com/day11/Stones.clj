(ns com.day11.Stones
  (:require [com.utils.InputParsing :refer :all]))


(defn count-digits [number]
  (loop
    [remaining number
     digit-count 1]
    (if (= (long (/ remaining 10)) 0) digit-count (recur (long (/ remaining 10)) (inc digit-count)))
    )
  )

(defn split-nr-at-digit [number digit]
  [(long (/ number (Math/pow 10 digit))) (long (mod number (Math/pow 10 digit)))]
  )


(defn apply-rule [stone]
  (cond
    (= stone 0) [1]
    (even? (count-digits stone))  (split-nr-at-digit stone (/ (count-digits stone) 2))
    :else [(* 2024 stone)]
    )
  )

(defn blink
  "should find solution"
  [blink stones]
  (let [stones (parse-numbers-in-line stones)]
    (count (reduce (fn [all-stones blink]
              (println "blink=" blink " hell? " (count all-stones))
              (flatten (map apply-rule all-stones))
              )
            (into [] stones) (range 0 blink))))
  )