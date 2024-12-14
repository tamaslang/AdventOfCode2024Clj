(ns com.day14.Task
  (:require [com.utils.InputParsing :refer :all]))

(defn parse-robot-state [robot-state]
  (map Integer/parseInt (rest (re-find #"p=(-?\d+),(-?\d+)\sv=(-?\d+),(-?\d+)" robot-state))))

(defn move-robot-for-seconds [seconds [px py vx vy] [X Y]]
  [(mod (+ px (* seconds vx)) X)
   (mod (+ py (* seconds vy)) Y)])

(defn solve [seconds [X Y] data]
  (let [robots (map parse-robot-state data)
        robot-position-after-seconds (map #(move-robot-for-seconds seconds % [X Y]) robots)
        quadrant-size-x (int (/ X 2)) ; 5
        quadrant-size-y (int (/ Y 2)) ; 3
        quadrant-1-bots (filter (fn [[px py]] (and (< px quadrant-size-x) (< py quadrant-size-y))) robot-position-after-seconds)
        quadrant-2-bots (filter (fn [[px py]] (and (< quadrant-size-x px) (< py quadrant-size-y))) robot-position-after-seconds)
        quadrant-3-bots (filter (fn [[px py]] (and  (<  quadrant-size-x px) (< quadrant-size-y py))) robot-position-after-seconds)
        quadrant-4-bots (filter (fn [[px py]] (and (<  px quadrant-size-x) (<  quadrant-size-y py))) robot-position-after-seconds)]
    (reduce * (map count [quadrant-1-bots quadrant-2-bots quadrant-3-bots quadrant-4-bots]))))