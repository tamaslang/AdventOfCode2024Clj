(ns com.day4.WorldSearch
  (:require [com.utils.InputParsing :refer :all]))

(defn direction-north [x y] [x (dec y)])
(defn direction-south [x y] [x (inc y)])
(defn direction-west [x y] [(dec x) y])
(defn direction-east [x y] [(inc x) y])
(defn direction-northeast [x y] [(inc x) (dec y)])
(defn direction-northwest [x y] [(dec x) (dec y)])
(defn direction-southeast [x y] [(inc x) (inc y)])
(defn direction-southwest [x y] [(dec x) (inc y)])

(def directions
  [direction-north
   direction-south,
   direction-west,
   direction-east,
   direction-northeast,
   direction-northwest,
   direction-southeast,
   direction-southwest])

(defn find-word [matrix start-pos direction word]
  (reduce
   (fn [current-pos, char]
     (if (not= char (get-x-y matrix current-pos)) (reduced false) (apply direction current-pos)))
   start-pos
   word))

(defn find-word-all-direction [matrix start-pos word]
  (reduce (fn [total direction] (if (find-word matrix start-pos direction word) (inc total) total)) 0 directions))

(defn world-search
  "should find XMAS in matrix"
  [data]
  (let
   [world-search-matrix (create-matrix data)]
    (reduce +
            (for [[y row] (map-indexed vector world-search-matrix)
                  [x _] (map-indexed vector row)]
              (find-word-all-direction world-search-matrix [x y] "XMAS")))))

(defn find-word-cross [matrix [x y] word]
  (if (or
       (and (find-word matrix [x y] direction-southeast word) (find-word matrix [x (+ y 2)] direction-northeast word))
       (and (find-word matrix [x y] direction-southeast (reverse word)) (find-word matrix [x (+ y 2)] direction-northeast (reverse word)))
       (and (find-word matrix [x y] direction-southeast word) (find-word matrix [(+ x 2) y] direction-southwest word))
       (and (find-word matrix [x y] direction-southeast (reverse word)) (find-word matrix [(+ x 2) y] direction-southwest (reverse word))))
    1 0))

(defn world-cross-search
  "should find X MAS in matrix"
  [data]
  (let
   [world-search-matrix (create-matrix data)]
    (reduce +
            (for [[y row] (map-indexed vector world-search-matrix)
                  [x _] (map-indexed vector row)]
              (find-word-cross world-search-matrix [x y] "MAS")))))