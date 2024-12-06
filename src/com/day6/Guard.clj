(ns com.day6.Guard
  (:require [clojure.set :as set]
            [com.utils.InputParsing :refer :all]))

(def directions
  [[0, -1]
   [1 0]
   [0 1]
   [-1 0]])

(defn turn-right [direction]
  (nth (cycle directions) (inc (.indexOf directions direction))))

(defn move [current-pos direction]
  [(+ (first current-pos) (first direction))
   (+ (second current-pos) (second direction))])

(defn walk [matrix start-pos]
  (reduce (fn [[visited current-pos direction] _]
            (let [visited* (conj visited current-pos)
                  whats-ahead (matrix->get-xy matrix (move current-pos direction))
                  direction* (if (= whats-ahead \#) (turn-right direction) direction)]
              (if (nil? whats-ahead) (reduced visited*) [visited* (move current-pos direction*) direction*]))) [#{} start-pos (first directions)] (cycle (range 0 1))))

(defn is-circular? [start-pos matrix]
  (reduce (fn [[visited current-pos direction] _]
            ;(println (if (not-empty (set/intersection visited #{[current-pos direction]})) "CIRCULAR!" "NO" ))
            (let [visited* (conj visited [current-pos direction])
                  whats-ahead (matrix->get-xy matrix (move current-pos direction))
                  direction* (if (= whats-ahead \#) (turn-right direction) direction)
                  new-point [(move current-pos direction*) direction*]]
              (cond
                (not-empty (set/intersection visited* #{new-point})) (reduced true)
                (nil? (matrix->get-xy matrix (move current-pos direction*))) (reduced false)
                :else [visited* (move current-pos direction*) direction*])))
          [#{} start-pos (first directions)]
          (cycle (range 0 1))))

(defn count-patrol-field
  "should count the fields the guard patrol"
  [data]
  (let [matrix (create-matrix data)
        start-pos (matrix->find-first matrix \^)]
    (count (walk matrix start-pos))))

(defn count-circulars-when-adding-block
  "should count how many ways we can add a block so path becomes circular"
  [data]
  (let [matrix (create-matrix data)
        start-pos (matrix->find-first matrix \^)
        all-dots (matrix->find-all matrix \.)]
    (->> all-dots
         (map (fn [pos-for-block] (update-in matrix pos-for-block (fn [_] \#))))
         (map (fn [updated-matrix] (is-circular? start-pos updated-matrix)))
         (map #(if %1 1 0))
         (reduce +))))

(defn get-all-from-pos-until-block-or-end [matrix pos direction]
  (let
   [[found _] (reduce (fn [[found current-pos], _]
                        (let [next-pos (move current-pos direction)
                              whats-ahead (matrix->get-xy matrix next-pos)]
                          (if (or (nil? whats-ahead) (= whats-ahead \#))
                            (reduced [found next-pos])
                            [(conj found [next-pos direction]) next-pos])))

                      [#{} pos]
                      (cycle (range 0 1)))]
    found))

(defn walk-to-find-circular [matrix start-pos]
  (reduce (fn [[visited current-pos direction can-block] _]
            (let [visited* (conj visited [current-pos direction])
                  whats-ahead (matrix->get-xy matrix (move current-pos direction))
                  whats-right (get-all-from-pos-until-block-or-end matrix current-pos (turn-right direction))
                  direction* (if (= whats-ahead \#) (turn-right direction) direction)
                  can-block*  (if (and (= whats-ahead \.) (not-empty (set/intersection whats-right visited*))) (inc can-block) can-block)]
              (if (nil? whats-ahead) (reduced can-block*) [visited* (move current-pos direction*) direction*  can-block*])))
          [#{} start-pos (first directions) 0] (cycle (range 0 1))))

(defn count-circulars-when-adding-block
  "should count the fields the guard patrol"
  [data]
  (let [matrix (create-matrix data)
        start-pos (matrix->find-first matrix \^)]
    (walk-to-find-circular matrix start-pos)))