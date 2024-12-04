(ns com.day4.WorldSearch
  (:require [com.utils.InputParsing :refer :all]))

(defn get-x-y [matrix [x y]]
  (when-let [symbol (get-in matrix [y x])] {:symbol symbol :x x :y y}))

(defn adjacents [matrix [x y]]
  (keep identity [; row above
                  (get-x-y matrix [(dec x) (dec y)])
                  (get-x-y matrix [x (dec y)])
                  (get-x-y matrix [(inc x) (dec y)])
                  ; same row
                  (get-x-y matrix [(dec x) y])
                  (get-x-y matrix [(inc x) y])
                  ; row below
                  (get-x-y matrix [(dec x) (inc y)])
                  (get-x-y matrix [x (inc y)])
                  (get-x-y matrix [(inc x) (inc y)])]))

(defn find-word [word matrix pos-data]
  (println "find word=" word)
  (print-matrix matrix)
  (println "data-pos-data= " pos-data)
  (first (reduce (fn [[_ pos-data] char]
            (println "lookup=" char)
            (println "pos-data[" (count pos-data) "]=" pos-data )
            (let
              [position-with-match (filter #(= (:symbol %1) char) pos-data)
               adjacents (flatten (map #(adjacents matrix [(:x %1) (:y %1)]) position-with-match))
               ]
              (println "position-with-match[" (count position-with-match) "]=" position-with-match)
              (println "adjacents[" (count adjacents) "]=" pos-data)
              [(count position-with-match) adjacents]
              )
            )
          [0 pos-data] word)))


(defn world-search
  "should find XMS in matrix"
  [data]
  (let
   [world-search-matrix (create-matrix data)
    world-search-posdata (matrix->arr-w-symbol-and-xy world-search-matrix)
    occurences (find-word "XMAS" world-search-matrix world-search-posdata)]
    occurences
    ))