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

(defn find-route-ends-from [matrix starting-position]
  (reduce (fn [next-positions next-slope]
            (let
             [adjacents (mapcat #(matrix->adjacents matrix %) next-positions)
              next-positions (filter (fn [[height _]] (= height next-slope)) adjacents)]
              next-positions))
          [starting-position] "123456789"))

(defn find-trailhead-scores
  "should find trailheads with unique ends"
  [data]
  (let
   [trailmap (create-matrix data)
    starting-points (matrix->find-all-with-fmatching trailmap #(= % \0))]
    (->>
     starting-points
     (map #(set (find-route-ends-from trailmap %1))) ; use SET for uniqueness
     (map count)
     (reduce +))))

(defn find-distinct-route-trailheads-scores
  "should find distinct route trailheads scores"
  [data]
  (let
   [trailmap (create-matrix data)
    starting-points (matrix->find-all-nr-with-fmatching trailmap #(= % \0))]
    (->>
     starting-points
     (map #(find-route-ends-from trailmap %1))
     (map count)
     (reduce +))))