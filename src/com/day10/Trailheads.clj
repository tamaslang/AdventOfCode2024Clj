(ns com.day10.Trailheads
  (:require [com.utils.InputParsing :refer :all]))

(defn matrix->adjacents [matrix [height [x y]]]
  (keep identity [;above
                  (matrix->get-char-at-xy matrix [x (dec y)])
                  ; same row
                  (matrix->get-char-at-xy matrix [(dec x) y])
                  (matrix->get-char-at-xy matrix [(inc x) y])
                  ; row below
                  (matrix->get-char-at-xy matrix [x (inc y)])]))

(defn find-end-from [matrix starting-position]
  (set (reduce (fn [next-positions next-slope]
                 (println "next slope " next-slope)
                 (println "next-positions " next-positions)
                 (let
                  [adjacents (mapcat #(matrix->adjacents matrix %) next-positions)
                   next-positions (filter (fn [[height _]] (= height next-slope)) adjacents)]
                   next-positions))
               [starting-position] "123456789")))

(defn find-trailheads
  "should find trailheads"
  [data]
  (let
   [trailmap (create-matrix data)
    starting-points (matrix->find-all-nr-with-fmatching trailmap #(= % \0))]
    (->>
     starting-points
     (map #(find-end-from trailmap %1))
     (map count)
     (reduce +))))