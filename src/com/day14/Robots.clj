(ns com.day14.Robots
  (:require [clojure.set :as set]
            [com.utils.InputParsing :refer :all]))

(defn parse-robot-state [robot-state]
  (map Integer/parseInt (rest (re-find #"p=(-?\d+),(-?\d+)\sv=(-?\d+),(-?\d+)" robot-state))))

(defn move-robot-for-seconds [seconds [px py vx vy] [X Y]]
  [(mod (+ px (* seconds vx)) X)
   (mod (+ py (* seconds vy)) Y)])

(defn count-robots-in-quadrants-after [seconds [X Y] data]
  (let [robots (map parse-robot-state data)
        robot-position-after-seconds (map #(move-robot-for-seconds seconds % [X Y]) robots)
        quadrant-size-x (int (/ X 2)) ; 5
        quadrant-size-y (int (/ Y 2)) ; 3
        quadrant-1-bots (filter (fn [[px py]] (and (< px quadrant-size-x) (< py quadrant-size-y))) robot-position-after-seconds)
        quadrant-2-bots (filter (fn [[px py]] (and (< quadrant-size-x px) (< py quadrant-size-y))) robot-position-after-seconds)
        quadrant-3-bots (filter (fn [[px py]] (and  (<  quadrant-size-x px) (< quadrant-size-y py))) robot-position-after-seconds)
        quadrant-4-bots (filter (fn [[px py]] (and (<  px quadrant-size-x) (<  quadrant-size-y py))) robot-position-after-seconds)]
    (reduce * (map count [quadrant-1-bots quadrant-2-bots quadrant-3-bots quadrant-4-bots]))))

(defn find-horizontally-symmetric [quadrant-a quadrant-b X]
  (filter (fn [[x y]] (not-empty (set/intersection #{[(- X 1 x) y]} quadrant-b))) quadrant-a))

(defn beginning-to-look-like-christmas-initial-idea? [robot-positions [X Y]]
  "First idea to look for a christmas tree in the middle position using symmetry"
  (let [half-x-dimension (int (/ X 2))
        left-half-bots (set (filter (fn [[px py]] (and (< px half-x-dimension) (< py 10))) robot-positions))]
    (> (count (find-horizontally-symmetric left-half-bots (set robot-positions) X)) (/ (count left-half-bots) 2))))

(defn beginning-to-look-like-christmas-hack?
  "HACK probably due to the testdata creation all robot has unique position!!!"
  [robot-positions [X Y]]
  (= (count robot-positions) (count (set robot-positions))))

(defn calculate-neighbour-score [distinct-robot-positions [X Y]]
  (reduce (fn [[neighbour-score neighbouring-in-row], count]
            (let
             [[x y] [(mod count X) (int (Math/floor (/ count X)))]]
              (cond
                (zero? x) [(+ neighbour-score neighbouring-in-row) 0]
                (not (empty? (set/intersection distinct-robot-positions #{[x y]}))) [neighbour-score (* (inc neighbouring-in-row) 2)]
                :else [(+ neighbour-score neighbouring-in-row) 0])))
          [0 0]
          (range 0 (* X Y))))

(defn calculate-line-score [distinct-robot-positions-by-line line [from to]]
  (let
   [robots-x-positions-in-line (set (map first (distinct-robot-positions-by-line line)))]
    (reduce + (reduce (fn [[neighbour-score neighbouring-in-row], x-pos]
                        (cond
                          (contains? robots-x-positions-in-line x-pos) [neighbour-score (* (inc neighbouring-in-row) 2)]
                          :else [(+ neighbour-score neighbouring-in-row) 0]))
                      [0 0]
                      (range from to)))))

(defn beginning-to-look-like-christmas?
  "look for a Christmas tree with finding adjacent robot positions"
  [robot-positions [X Y]]
  (let [[score _] (calculate-neighbour-score (set robot-positions) [X Y])]
      ;(println "SCORE= " score)
    (> score 10000)))

(defn beginning-to-look-like-christmas-optimised?
  "look for a Christmas tree with finding adjacent robot positions"
  [robot-positions [X Y]]
  (let [distinct-robot-positions-by-line (group-by second robot-positions)
        score (+
               (calculate-line-score distinct-robot-positions-by-line 20 [20 80])
               (calculate-line-score distinct-robot-positions-by-line 40 [20 80])
               (calculate-line-score distinct-robot-positions-by-line 60 [20 80])
               (calculate-line-score distinct-robot-positions-by-line 80 [20 80]))]
;(println "SCORE= " score)
    (> score 10000)))

(defn print-tree [coordinates [X Y]]
  (reduce (fn [_, count]
            (let
             [[x y] [(mod count X) (int (Math/floor (/ count X)))]]
              (when (zero? x) (println ""))
              (if (empty? (set/intersection coordinates #{[x y]})) (print \.) (print \0))))
          []
          (range 0  (* X Y))))

(defn find-christmas-tree [from-seconds to-seconds [X Y] data]
  (println "FIND Christmas Tree from " from-seconds " to " to-seconds)
  (let [robots (map parse-robot-state data)]
    (reduce (fn [_, second]
              (let
               [robots-moved (map #(move-robot-for-seconds second % [X Y]) robots)
                christmas-tree? (beginning-to-look-like-christmas-optimised? robots-moved [X Y])]
                (when (zero? (mod second 1000)) (println "not found at =" second))
                (when christmas-tree? (println "FOUND AT SECONDS=" second))
                (when christmas-tree? (print-tree (set robots-moved) [X Y]))
                (if christmas-tree? (reduced second) 0)))
            0
            (range from-seconds to-seconds))))
