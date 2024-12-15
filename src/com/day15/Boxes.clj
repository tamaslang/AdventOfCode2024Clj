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
  (let [[matrix-part _ instructions-part] (partition-by str/blank? input-lines)
        matrix (create-matrix matrix-part)]
    [matrix (apply str instructions-part)]))

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

; TASK 2
;If the tile is #, the new map contains ## instead.
;If the tile is O, the new map contains [] instead.
;If the tile is ., the new map contains .. instead.
;If the tile is @, the new map contains @. instead.
(defn scale-up-line [line]
  (reduce
   (fn [scaled-up char]
     (cond
       (= char \#) (str scaled-up "##")
       (= char \O) (str scaled-up "[]")
       (= char \.) (str scaled-up "..")
       (= char \@) (str scaled-up "@.")
       :else (throw (Exception. (str "Unrecognised character. Char=" char)))))

   "" line))

(defn parse-input-with-scaling-up [input-lines]
  (let [[matrix-part _ instructions-part] (partition-by str/blank? input-lines)
        scaled-up-matrix-lines (map scale-up-line matrix-part)
        matrix (create-matrix scaled-up-matrix-lines)]
    [matrix (apply str instructions-part)]))

(defn print-area-when-scaled-up [boxes walls robots [X Y]]
  (let [boxes-start-positions (set (map first boxes))
        boxes-end-positions (set (map second boxes))]
    (reduce (fn [_, count]
              (let
               [[x y] [(mod count X) (int (Math/floor (/ count X)))]]
                (when (zero? x) (println ""))
                (cond
                  (not-empty (set/intersection boxes-start-positions #{[x y]})) (print \[)
                  (not-empty (set/intersection boxes-end-positions #{[x y]})) (print \])
                  (not-empty (set/intersection walls #{[x y]})) (print \#)
                  (not-empty (set/intersection robots #{[x y]})) (print \@)
                  :else (print \.))))
            []
            (range 0  (* X Y)))))

(defn process-instructions-when-scaled-up [matrix instructions]
  (println "MATRIX!")
  (print-matrix matrix)
  (let [boxes (map (fn [[box-leftside-x box-leftside-y]] [[box-leftside-x box-leftside-y] [(inc box-leftside-x) box-leftside-y]]) (matrix->find-all matrix \[))
        walls (matrix->find-all matrix \#)
        start-pos (matrix->find-first matrix \@)
        size-y (count matrix)
        size-x (count (first matrix))]
    (println  "matrix size=" (count matrix))
    (print-area-when-scaled-up boxes walls #{start-pos} [size-x size-y]))
  ;
  ;      start-pos (matrix->find-first matrix \@)]
  ;  (reduce
  ;    (fn
  ;      [[current-pos boxes] instruction]
  ;      ;(print-area boxes walls #{current-pos} [(count matrix) (count matrix)])
  ;      ;(println)
  ;      ;(println "instruction: " instruction)
  ;      (let [direction (map-instuction-to-direction instruction)
  ;            [can-move? boxes-on-the-way] (can-move? matrix boxes current-pos direction)
  ;            current-pos* (if can-move? (move current-pos direction) current-pos)
  ;            untouched-boxes (set/difference boxes boxes-on-the-way)
  ;            moved-boxes (map (fn [box] (move box direction)) boxes-on-the-way)
  ;            boxes* (set/union untouched-boxes (set moved-boxes))]
  ;        [current-pos* boxes*]))
  ;
  ;    [start-pos boxes]
  ;    instructions))
  [[] #{}])

(defn find-box-positions-scaled-up
  "should find box positions"
  [data]
  (let [[matrix instructions] (parse-input-with-scaling-up data)
        [_ moved-boxes] (process-instructions-when-scaled-up matrix instructions)]
    (reduce + (map (fn [[x y]] (+ x (* y 100))) moved-boxes))))

