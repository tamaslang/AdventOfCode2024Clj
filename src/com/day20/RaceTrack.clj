(ns com.day20.RaceTrack
  (:require [com.utils.InputParsing :refer :all]))

(defn adjacents [matrix [x y]]
  (keep identity [;above
                  (matrix->get-char-at-xy matrix [x (dec y)])
                  ; same row
                  (matrix->get-char-at-xy matrix [(dec x) y])
                  (matrix->get-char-at-xy matrix [(inc x) y])
                  ; row below
                  (matrix->get-char-at-xy matrix [x (inc y)])]))

(defn space? [char] (= char \.))
(defn wall? [char] (= char \#))
(defn end? [char] (= char \E))
(defn start? [char] (= char \S))

(defn adjacents-with-distance [[x y] distance dimension]
  (->>
   [[x (- y distance)]
    [(- x distance) y]
    [(+ x distance) y]
    [x (+ y distance)]]
   (filter (fn [[x y]] (and
                        (>= (dec dimension) x 0)
                        (>= (dec dimension) y 0))))))

(defn distance [[x1 y1] [x2 y2]]
  (+ (abs (- x1 x2)) (abs (- y1 y2))))
;(distance [1 3] [3 7])
;6

;0 0 1 2 3 4
;0 X X X X X
;1 X X X X X
;2 X X @ X X
;3 X X X X X
;4 X X X X X
;
;0 0 1 2 3 4
;0 X X A X X
;1 X A A A X
;2 A A @ A A
;3 X A A A X
;4 X X A X X
;
;[2 2]
;->

;###############
;#...#...#.....#
;#.#.#.#.#.###.#
;#S#...#.#.#...#
;#######.#.#.###
;#######.#.#...#
;#######.#.###.#
;###..E#...#...#
;###.#######.###
;#...###...#...#
;#.#####.#.###.#
;#.#...#.#.#...#
;#.#.#.#.#.#.###
;#...#...#...###
;###############
(defn jumps-from-pos-with-with-range-check [pos step visited min-distance max-distance save-at-least]
  (->>
   visited
   (filter (fn [[visited-pos score]] (and (<= score (- step save-at-least)) (<= min-distance (distance pos visited-pos) max-distance))))
   (map (fn [[visited-pos score]] (- step score (distance pos visited-pos))))
   (filter #(> % 0))))

(defn print-area [current-pos  start-pos end-pos visited-pos blocks  dimension]
  (reduce (fn [_, count]

            (let
             [[x y] [(mod count dimension) (int (Math/floor (/ count dimension)))]]
              (when (zero? x) (println ""))
              (cond
                (= current-pos [x y]) (print \@)
                (= start-pos [x y]) (print \S)
                (= end-pos [x y]) (print \E)
                (visited-pos [x y]) (print \O)
                (blocks [x y]) (print \#)
                :else (print \.))))
          []
          (range 0  (* dimension dimension))))

(defn map-jumps-from [matrix start-pos min-distance max-distance save-at-least]
  (def dimensioon (count matrix))
  (reduce (fn [[current-pos last-pos visited jumps] step-count]
            (when (zero? (mod step-count  100)) (println "step " step-count))
            (let
             [visited* (assoc visited current-pos step-count)
              jumps* (doall (concat jumps (jumps-from-pos-with-with-range-check current-pos step-count visited min-distance max-distance save-at-least)))
              next-pos (->>
                        (adjacents matrix current-pos)
                        (filter (fn [[symbol _]] (or (end? symbol) (space? symbol))))
                        (map second)
                        (filter (fn [pos] (not= pos last-pos)))
                        first)]

              (cond
                (not next-pos) (reduced jumps*)
                :else
                [next-pos current-pos visited* jumps*])))
          [start-pos nil {} []] (range 0 (* (count matrix) (count matrix)))))

(defn find-cheats
  "should find solution"
  [min-distance max-distance save-at-least data]
  (let
   [matrix (create-matrix data)
    start-pos (matrix->find-first matrix \S)
    jumps (map-jumps-from matrix start-pos min-distance max-distance save-at-least)]
    (->> jumps
         (filter #(>= % save-at-least))
         count)))