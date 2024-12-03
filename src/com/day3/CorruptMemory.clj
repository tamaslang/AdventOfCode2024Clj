(ns com.day3.CorruptMemory
  (:require [com.utils.InputParsing :refer :all]))

(defn eval-mul [mul-str]
  (let [[_ x y] (first (re-seq #"mul\((\d+),(\d+)\)" mul-str))]  (* (Integer/parseInt x) (Integer/parseInt y))))

(defn parse-instructions [instructions]
  (->>
   instructions
   (map eval-mul)
   (reduce +)))

(defn find-instructions
  "should find mul find instructions"
  [data]
  (parse-instructions (re-seq #"mul\(\d+,\d+\)" data)))

(defn parse-instructions-with-do-and-don't [instructions]
  (let [[sum _] (reduce (fn [[sum do?], current]
                            (cond
                              (= current "don't") [sum false]
                              (= current "do") [sum true]
                              do? [(+ sum (eval-mul current)) do?]
                              :else [sum do?]))
                          [0 true]
                          instructions)]
    sum))

(defn find-instructions-task2
  "should find instructions handling do and don't"
  [data]
  (parse-instructions-with-do-and-don't (re-seq #"(?:don\'t)|(?:do)|(?:mul\(\d+,\d+\))" data)))
