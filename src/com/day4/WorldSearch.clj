(ns com.day4.WorldSearch
  (:require [com.utils.InputParsing :refer :all]))

(defn get-x-y [matrix [x y]]
  (when-let [symbol (get-in matrix [y x])] symbol))

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
   direction-southwest]
)

(defn find-word [matrix start-pos direction word]
  (if (reduce
    (fn [current-pos, char]
      (if (not= char (get-x-y matrix current-pos)) (reduced false) (apply direction current-pos))
      )
    start-pos
    word
    ) 1 0)
)

(defn find-word-all-direction [matrix start-pos word]
  (reduce (fn[total direction] (+ total (find-word matrix start-pos direction word))) 0 directions)
)

(defn world-search
  "should find XMS in matrix"
  [data]
  (let
    [world-search-matrix (create-matrix data)
     world-search-posdata (matrix->arr-w-symbol-and-xy world-search-matrix)
     ]
    (->>
      world-search-posdata
      (map (fn [posdata] (find-word-all-direction world-search-matrix [(:x posdata) (:y posdata)] "XMAS")))
      (reduce +)
      )
    )
  )