(ns com.day6.Guard
  (:require [com.utils.InputParsing :refer :all]))

(defn matrix->lookup [matrix char]
  (let
   [size-y (count matrix)
    size-x (count (first matrix))]
    (reduce (fn [_, count]
              (let
               [pos [(mod count size-x) (int (Math/floor (/ count size-y)))]]
                (if (= (get-x-y matrix pos) char) (reduced pos) ())))
            0
            (range 0 (* size-x size-y)))))

(def directions
  [[0, -1]
   [1 0]
   [0 1]
   [-1 0]])

(defn turn-right [direction]
  (nth (cycle directions) (inc (.indexOf directions direction))))

(defn move [current-pos direction]
  [(+ (first current-pos) (first direction))
   (+ (second current-pos) (second direction))])

(defn walk [matrix start-pos]
  (reduce (fn [[visited current-pos direction] _]
            (let [visited* (conj visited current-pos)
                  whats-ahead (get-x-y matrix (move current-pos direction))
                  direction* (if (= whats-ahead \#) (turn-right direction) direction)]
              (if (nil? whats-ahead) (reduced visited*) [visited* (move current-pos direction*) direction*]))) [#{} start-pos (first directions)] (cycle (range 0 1))))

(defn count-patrol-field
  "should count the fields the guard patrol"
  [data]
  (let [matrix (create-matrix data)
        start-pos (matrix->lookup matrix \^)]
    (count (walk matrix start-pos))))