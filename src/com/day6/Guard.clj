(ns com.day6.Guard
  (:require [clojure.set :as set]
            [com.utils.InputParsing :refer :all]))

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

(defn find-next [matrix position direction obsticle]
  (reduce (fn [[position direction], _]
            (let
             [next-position (move position direction)
              next-char (matrix->get-xy matrix next-position)]
              (if (or (= next-position obsticle) (= next-char \#)) [position (turn-right direction)] (reduced [next-position direction])))) [position direction]
          (range 5)))

(defn walk [matrix start-pos direction]
  (reduce (fn [[visited [guard-position guard-direction]] _]
            (if (nil? (matrix->get-xy matrix guard-position))
              (reduced visited)
              [(conj visited guard-position) (find-next matrix guard-position guard-direction [])]))
          [#{} [start-pos direction]] (cycle (range 1))))

(defn count-patrol-field
  "should count the fields the guard patrol"
  [data]
  (let [matrix (create-matrix data)
        start-pos (matrix->find-first matrix \^)]
    (count (walk matrix start-pos (first directions)))))

(defn walk-circular? [matrix start-pos direction obsticle]
  (reduce (fn [[path [guard-position guard-direction]] _]
            (cond
              (not-empty (set/intersection path #{[guard-position guard-direction]})) (reduced true) ; LOOP!
              (nil? (matrix->get-xy matrix guard-position)) (reduced false)
              :else [(conj path [guard-position guard-direction]) (find-next matrix guard-position guard-direction obsticle)]))
          [#{} [start-pos direction]]
          (cycle (range 1))))

(defn count-circulars-when-adding-block
  "should count the fields the guard patrol"
  [data]
  (let [direction-north (first directions)
        matrix (create-matrix data)
        start-pos (matrix->find-first matrix \^)
        possible-obsticle-positions (remove #{start-pos} (walk matrix start-pos direction-north))]
    (reduce + (pmap (fn [possible-obsticle] (if (walk-circular? matrix start-pos direction-north possible-obsticle) 1 0)) possible-obsticle-positions))))