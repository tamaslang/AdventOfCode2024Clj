(ns com.day15.Boxes
  (:require [clojure.set :as set]
            [com.utils.InputParsing :refer :all]
            [clojure.string :as str]))

;(def directions
;  [[0, -1]
;   [1 0]
;   [0 1]
;   [-1 0]])
;

(defn map-instuction-to-direction [instruction]
  (cond
    (= instruction \^) [0, -1]
    (= instruction \>) [1 0]
    (= instruction \v) [0 1]
    (= instruction \<) [-1 0]
    :else
    (println "Unrecoginised instruction:" instruction)))

(defn parse-input [input-lines]
  (let [[matrix-part _ [instructions-part]] (partition-by str/blank? input-lines)
        matrix (create-matrix matrix-part)]
    [matrix instructions-part]))

(defn wall? [char] (= char \#))
(defn box? [char] (= char \O))

(defn move [[px py] [ix iy]]
  [(+ px ix) (+ py iy)])

(defn can-move? [matrix boxes pos direction]
  (reduce (fn [[boxes-on-the-way current-pos] instruction]
            (let [current-pos* (move current-pos instruction)
                  at-current-pos (matrix->get-xy matrix current-pos*)
                  wall? (wall? at-current-pos)
                  box?  (not-empty (set/intersection boxes #{current-pos*}))]
              (cond
                wall? (reduced [false #{}])
                box? [(conj boxes-on-the-way current-pos*) current-pos*]
                :else ; space
                (reduced [true boxes-on-the-way]))))

          [#{} pos]
          (cycle (list direction))))

(defn print-area [boxes walls robots [X Y]]
  (reduce (fn [_, count]
            (let
             [[x y] [(mod count X) (int (Math/floor (/ count X)))]]
              (when (zero? x) (println ""))
              (cond
                (not-empty (set/intersection boxes #{[x y]})) (print \O)
                (not-empty (set/intersection walls #{[x y]})) (print \#)
                (not-empty (set/intersection robots #{[x y]})) (print \@)
                :else (print \.))))

          []
          (range 0  (* X Y))))

(defn process-instructions [matrix instructions]
  (let [boxes (matrix->find-all matrix \O)
        walls (matrix->find-all matrix \#)
        start-pos (matrix->find-first matrix \@)]
    (reduce
     (fn
       [[current-pos boxes] instruction]
       ;(print-area boxes walls #{current-pos} [(count matrix) (count matrix)])
       ;(println)
       ;(println "instruction: " instruction)
       (let [direction (map-instuction-to-direction instruction)
             [can-move? boxes-on-the-way] (can-move? matrix boxes current-pos direction)
             current-pos* (if can-move? (move current-pos direction) current-pos)
             untouched-boxes (set/difference boxes boxes-on-the-way)
             moved-boxes (map (fn [box] (move box direction)) boxes-on-the-way)
             boxes* (set/union untouched-boxes (set moved-boxes))]
         [current-pos* boxes*]))

     [start-pos boxes]
     instructions)))

(defn find-box-positions
  "should find box positions"
  [data]
  (let [[matrix instructions] (parse-input data)
        [_ moved-boxes] (process-instructions matrix instructions)]
    (reduce + (map (fn [[x y]] (+ x (* y 100))) moved-boxes))))