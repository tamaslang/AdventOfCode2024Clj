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

(defn valid-pos? [dimension [x y]]
  (and (<= 0 x (dec dimension)) (<= 0 y (dec dimension))))

(defn find-antinodes-for-locations [dimension [_ locations]]
  (->>
   locations
   combine-all-items-in-list
   (mapcat find-antinodes)
   set
   (filter #(valid-pos? dimension %1))))

(defn unique-antinode-locations
  "should find unique antinode locations"
  [data]
  (let [matrix (create-matrix data)
        dimension (count matrix)
        antennas (matrix->find-all-with-fmatching matrix #(not= \. %1))
        antennas-grouped-by-id (group-by #(first %1) antennas)
        antenna-positions-by-id (map (fn [[key values]] [key (map second values)]) antennas-grouped-by-id)]
    (->>
     antenna-positions-by-id
     (mapcat #(find-antinodes-for-locations dimension %))
     set
     count)))

; TASK 2
(defn collect-antinodes-loop [f-valid-pos? [dx dy] [x y]]
  (reduce (fn [antinodes, _]
            (let [[last-x last-y] (last antinodes)
                  ax (+ last-x dx) ay (+ last-y dy)]
              (if (f-valid-pos? [ax ay]) (conj antinodes [ax ay]) (reduced antinodes)))) [[x y]] (range)))

(defn find-antinodes-with-loop [f-valid-pos? [[l1x l1y] [l2x l2y]]]
  (let [dx (- l2x l1x)
        dy (- l2y l1y)]
    (set/union
     (collect-antinodes-loop f-valid-pos? [(* -1 dx) (* -1 dy)] [l1x l1y])
     (collect-antinodes-loop f-valid-pos? [dx dy] [l2x l2y]))))

(defn find-antinodes-for-locations-task2 [f-valid-pos? [_ locations]]
  (->>
   locations
   combine-all-items-in-list
   (mapcat #(find-antinodes-with-loop f-valid-pos? %))
   set))

(defn unique-antinode-locations-with-resonance
  "should find unique antinode locations with resonance"
  [data]
  (let [matrix (create-matrix data)
        dimension (count matrix)
        antennas (matrix->find-all-with-fmatching matrix #(not= \. %1))
        antennas-grouped-by-id (group-by #(first %1) antennas)
        antenna-positions-by-id (map (fn [[key values]] [key (map second values)]) antennas-grouped-by-id)]
    (->>
     antenna-positions-by-id
     (mapcat #(find-antinodes-for-locations-task2 (partial valid-pos? dimension) %))
     set
     count)))