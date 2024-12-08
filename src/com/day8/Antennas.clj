(ns com.day8.Antennas
  (:require [clojure.set :as set]
            [com.utils.InputParsing :refer :all]))

(defn combine-all-items-in-list [items]
  (for [i (range (count items)) j (range (inc i) (count items))] [(nth items i) (nth items j)]))

(defn find-antinodes [[[l1x l1y] [l2x l2y]]]
  (let [dx (- l2x l1x)
        dy (- l2y l1y)]
    [[(- l1x dx) (- l1y dy)]
     [(+ l2x dx) (+ l2y dy)]]))

(defn find-antinodes-for-locations [[dimension-x dimension-y] [_ locations]]
  (->>
   locations
   combine-all-items-in-list
   (map find-antinodes)
   (apply concat)
   (into #{})
   (filter #(and (<= 0 (first %1) (dec dimension-x)) (<= 0 (last %1) (dec dimension-y))))))

(defn unique-antinode-locations
  "should find unique antinode locations"
  [data]
  (let [matrix (create-matrix data)
        dimension-y (count matrix)
        dimension-x (count (first matrix))
        antennas (matrix->find-all-with-fmatching matrix #(not= \. %1))
        antennas-grouped-by-id (group-by #(first %1) antennas)
        antenna-positions-by-id (map (fn [[key values]] [key (map second values)]) antennas-grouped-by-id)]
    (->>
     antenna-positions-by-id
     (mapv #(find-antinodes-for-locations [dimension-x dimension-y] %))
     (apply concat)
     (into #{})
     (count))))

; TASK 2
(defn collect-antinodes-loop [loop [dimension-x dimension-y] [dx dy] [x y]]
  (reduce (fn [antinodes, _]
            (let [[last-x last-y] (last antinodes)
                  ax (+ last-x dx) ay (+ last-y dy)]
              (if (and (<= 0 ax (dec dimension-x)) (<= 0 ay (dec dimension-y))) (conj antinodes [ax ay]) (reduced antinodes)))) [[x y]]  (range loop)))
;
(defn find-antinodes-with-loop [loop [dimension-x dimension-y] [[l1x l1y] [l2x l2y]]]
  (let [dx (- l2x l1x)
        dy (- l2y l1y)]
    (set/union
     (collect-antinodes-loop loop [dimension-x dimension-y] [(* -1 dx) (* -1 dy)] [l1x l1y])
     (collect-antinodes-loop loop [dimension-x dimension-y] [dx dy] [l2x l2y]))))

(defn find-antinodes-for-locations-task2 [[dimension-x dimension-y] [_ locations]]
  (->>
   locations
   combine-all-items-in-list
   (map #(find-antinodes-with-loop Integer/MAX_VALUE [dimension-x dimension-y] %))
   (apply concat)
   (into #{})
   (filter #(and (<= 0 (first %1) (dec dimension-x)) (<= 0 (last %1) (dec dimension-y))))))

(defn unique-antinode-locations-with-resonance
  "should find unique antinode locations with resonance"
  [data]
  (let [matrix (create-matrix data)
        dimension-y (count matrix)
        dimension-x (count (first matrix))
        antennas (matrix->find-all-with-fmatching matrix #(not= \. %1))
        antennas-grouped-by-id (group-by #(first %1) antennas)
        antenna-positions-by-id (map (fn [[key values]] [key (map second values)]) antennas-grouped-by-id)]
    (->>
     antenna-positions-by-id
     (mapv #(find-antinodes-for-locations-task2 [dimension-x dimension-y] %))
     (apply concat)
     (into #{})
     (count))))