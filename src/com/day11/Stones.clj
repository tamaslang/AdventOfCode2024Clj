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
  (reduce + (pmap #(count-mapped-stone-memo % blink) stones)))

(defn blink
  "should find solution"
  [blink stones]
  (recur-blink (parse-numbers-in-line stones) blink))

(defn group-with-count [stones]
  (map (fn [[id, elements]] {:id id :count (count elements)}) (group-by identity stones)))

; OPTIMISED TASK 2 - DIFFERENT APPROACH / NO MEMOIZE
(defn apply-rule-with-count [stone count]
  (cond
    (= stone 0) {:id 1 :count count}
    (even? (count-digits stone))  (map (fn [mapped] {:id mapped :count count}) (split-nr-at-digit stone (/ (count-digits stone) 2)))
    :else {:id (* 2024 stone) :count count}))

(defn group-by-id-merge-counts [groups]
  (map
   (fn [[grp-key values]]
     {:id grp-key
      :count (reduce + (map :count values))}) (group-by :id groups)); '({:id 1, :count 2} {:id 2, :count 1} {:id 3, :count 1} {:id 4, :count 1} {:id 5, :count 3} {:id 5, :count 3}))
  )

(defn loop-with-counting-and-merging [stones blinks]
  (loop
   [stones (group-with-count stones)
    remaining-blinks blinks]
    (if (zero? remaining-blinks)
      (reduce + (map :count stones))
      (let
       [mapped-stones (flatten (map (fn [stone] (apply-rule-with-count (:id stone) (:count stone))) stones))
        groupped (group-by-id-merge-counts mapped-stones)]
        (recur groupped (dec remaining-blinks))))))

(defn blink-optimised
  "should find solution without memoize"
  [blink stones]
  (loop-with-counting-and-merging (parse-numbers-in-line stones) blink))
