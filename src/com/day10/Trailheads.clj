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

(defn find-unique-end-routes-from [matrix starting-position]
  (set (reduce (fn [next-positions next-slope]
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
     (map #(find-unique-end-routes-from trailmap %1))
     (map count)
     (reduce +))))

(defn branch-route-to-next-slope [matrix route next-slope]
  (let [adjacents (matrix->adjacents matrix (last route))
        valid-adjacents (filter (fn [[height _]] (= height next-slope)) adjacents)
        branched-routes (map #(conj route %) valid-adjacents)]
    branched-routes))

(defn find-distinct-routes-from [matrix starting-position]
  (set (reduce
        (fn [routes next-slope] (mapcat (fn [route] (branch-route-to-next-slope matrix route next-slope)) routes))
        [[starting-position]]
        "123456789")))

(defn find-trailheads-scores
  "should find trailheads"
  [data]
  (let
   [trailmap (create-matrix data)
    starting-points (matrix->find-all-nr-with-fmatching trailmap #(= % \0))]
    (->>
     starting-points
     (map #(find-distinct-routes-from trailmap %1))
     (map count)
     (reduce +))))