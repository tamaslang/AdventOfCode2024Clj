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

(defn adjacents-with-distance [matrix [x y] distance]
  (keep identity [;above
                  (matrix->get-char-at-xy matrix [x (- y distance)])
                  ; same row
                  (matrix->get-char-at-xy matrix [(- x distance) y])
                  (matrix->get-char-at-xy matrix [(+ x distance) y])
                  ; row below
                  (matrix->get-char-at-xy matrix [x (+ y distance)])]))

(defn jumps-from [matrix pos step jump-size visited]
  (->>
   (adjacents-with-distance matrix pos jump-size)
   (filter (fn [[symbol _]] (or (end? symbol) (start? symbol) (space? symbol))))
   (map second)
   (map (fn [adjacent-pos] (visited adjacent-pos)))
   (remove nil?)
   (map (fn [adjacent-step] (- step adjacent-step jump-size)))
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

(defn map-jumps-from [matrix start-pos]
  (reduce (fn [[current-pos last-pos visited jumps] step-count]
            (let
             [visited* (assoc visited current-pos step-count)
              jumps* (doall (concat jumps (jumps-from matrix current-pos step-count 2 visited)))
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
  [min-saved-picosec data]
  (let
   [matrix (create-matrix data)
    start-pos (matrix->find-first matrix \S)
    jumps (map-jumps-from matrix start-pos)]
    (->> jumps
         (filter #(>= % min-saved-picosec))
         count)))