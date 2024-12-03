(ns com.day3.Task
  (:require [com.utils.InputParsing :refer :all]))

(defn solve
  "should find solution"
  [data]
  (let [instructions (re-seq #"mul\(\d+,\d+\)" data)]
    (->>
     instructions
     (map #(re-seq #"mul\((\d+),(\d+)\)" %1))
     (map first)
     (map (fn [[_ left right]] (* (Integer/parseInt left) (Integer/parseInt right))))
     (reduce +))))

