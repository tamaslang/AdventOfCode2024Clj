(ns com.day14.Task
  (:require [clojure.set :as set]
            [com.utils.InputParsing :refer :all]))

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

(defn is-symmetric? [quadrant-a quadrant-b [X Y]]
  (every? (fn[[x y]] (not-empty (set/intersection #{[(- X 1 x) y]} quadrant-b))) quadrant-a)
)

(defn is-symmetric-horizontally? [robot-positions X]
  (every? (fn[[x y]] (not-empty (set/intersection #{[(- X 1 x) y]} robot-positions))) robot-positions)
)

(defn is-christmas-tree? [robot-positions [X Y]]
  (let [quadrant-size-x (int (/ X 2))
        quadrant-size-y (int (/ Y 2))
        quadrant-1-bots (filter (fn [[px py]] (and (< px quadrant-size-x) (< py quadrant-size-y))) robot-positions)
        quadrant-2-bots (filter (fn [[px py]] (and (< quadrant-size-x px) (< py quadrant-size-y))) robot-positions)
        ;quadrant-3-bots (filter (fn [[px py]] (and  (<  quadrant-size-x px) (< quadrant-size-y py))) robot-positions)
        ;quadrant-4-bots (filter (fn [[px py]] (and (<  px quadrant-size-x) (<  quadrant-size-y py))) robot-positions)
        ]
    (and
     ;(= (count quadrant-1-bots) (count quadrant-2-bots))
     ;(= (count quadrant-3-bots) (count quadrant-4-bots))
     (is-symmetric? (set quadrant-1-bots) (set quadrant-2-bots) [X Y])
     ;(is-symmetric? (set quadrant-3-bots) (set quadrant-4-bots) [X Y])
     )))

(defn print-tree [coordinates [X Y]]
  (reduce (fn [_, count]
            (let
              [[x y] [(mod count X) (int (Math/floor (/ count X)))]]
              ;(println [x y])
              (when (zero? x) (println ""))
              (if (empty? (set/intersection coordinates #{[x y]})) (print \.) (print \0))
              )
            )
          []
          (range 0 (inc (* X Y))))
  )

(defn solve-for-christmas-tree [from-seconds max-seconds [X Y] data]
  (let [robots (map parse-robot-state data)]
    (reduce (fn [_, second]
              (let
               [robots-moved (map #(move-robot-for-seconds second % [X Y]) robots)
                is-christmas-tree? (is-symmetric-horizontally? (set robots-moved) X)
                quadrant-size-x (int (/ X 2))
                quadrant-size-y (int (/ Y 2))
                ]
                (when (zero? (mod second 10000)) (println "not found at =" second))
                (when is-christmas-tree? (println "FOUND AT SECONDS=" second) )
                (when is-christmas-tree? (print-tree (set robots-moved) [quadrant-size-x quadrant-size-y]))
                (if is-christmas-tree? (reduced second) 0)))
            0
            (range from-seconds max-seconds))))
