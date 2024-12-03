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
     (map (fn [[_ x y]] (* (Integer/parseInt x) (Integer/parseInt y))))
     (reduce +))))

(defn solve-mul [mul-str]
  (let [[_ x y] (first (re-seq #"mul\((\d+),(\d+)\)" mul-str))]  (* (Integer/parseInt x) (Integer/parseInt y))))

(defn solve2
  "should find solution"
  [data]
  (let [instructions (re-seq #"(?:don\'t)|(?:do)|(?:mul\(\d+,\d+\))" data)]
    (first (reduce (fn [[total do?], current]
                     (cond
                       (= current "don't") [total false]
                       (= current "do") [total true]
                       do? [(+ total (solve-mul current)) do?]
                       :else [total do?]))

                   [0 true]
                   instructions))))
