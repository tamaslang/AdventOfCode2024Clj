(ns com.day11.Stones
  (:require [com.utils.InputParsing :refer :all]))

(defn count-digits [number]
  (loop
   [remaining number
    digit-count 1]
    (if (= (long (/ remaining 10)) 0) digit-count (recur (long (/ remaining 10)) (inc digit-count)))))

(defn split-nr-at-digit [number digit]
  [(long (/ number (Math/pow 10 digit))) (long (mod number (Math/pow 10 digit)))])

(defn apply-rule [stone]
  (cond
    (= stone 0) [1]
    (even? (count-digits stone))  (split-nr-at-digit stone (/ (count-digits stone) 2))
    :else [(* 2024 stone)]))

(def count-mapped-stone-memo
  (memoize
   (fn [stone blinks]
     (loop
      [stones [stone]
       remaining-blinks blinks]
       (if (zero? remaining-blinks)
         (count stones)
         (reduce + (map #(count-mapped-stone-memo % (dec remaining-blinks)) (mapcat apply-rule stones))))))))

(defn recur-blink [stones blink]
  (reduce + (map #(count-mapped-stone-memo % blink) stones)))

(defn blink
  "should find solution"
  [blink stones]
  (recur-blink (parse-numbers-in-line stones) blink))