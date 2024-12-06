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

(defn walk [matrix start-pos direction]
  (reduce (fn [[visited current-pos direction] _]
            (let [visited* (conj visited current-pos)
                  whats-ahead (matrix->get-xy matrix (move current-pos direction))
                  direction* (if (= whats-ahead \#) (turn-right direction) direction)]
              (if (nil? whats-ahead) (reduced visited*) [visited* (move current-pos direction*) direction*]))) [#{} start-pos direction] (cycle (range 0 1))))

(defn count-patrol-field
  "should count the fields the guard patrol"
  [data]
  (let [matrix (create-matrix data)
        start-pos (matrix->find-first matrix \^)]
    (count (walk matrix start-pos (first directions)))))

(defn is-circular? [matrix start-pos direction]
  (reduce (fn [[visited current-pos direction] _]
            (let [visited* (conj visited [current-pos direction])
                  pos-forward (move current-pos direction)
                  whats-ahead (matrix->get-xy matrix pos-forward)
                  direction* (if (= whats-ahead \#) (turn-right direction) direction)]
              (cond
                (nil? whats-ahead) (reduced false)
                (not-empty (set/intersection visited* #{[pos-forward direction*]})) (reduced true)
                :else [visited* (move current-pos direction*) direction*])))
          [#{} start-pos direction]
          (cycle (range 0 1))))

(defn walk-to-find-circular [matrix start-pos direction]
  (->>
   (let
    [possible-blocks (remove #{start-pos (move start-pos (first directions))} (walk matrix start-pos direction))]
     (count (filter (fn [possible-block] (is-circular? (matrix->update-xy matrix possible-block \#) start-pos direction)) possible-blocks)))))

(defn count-circulars-when-adding-block
  "should count the fields the guard patrol"
  [data]
  (let [matrix (create-matrix data)
        start-pos (matrix->find-first matrix \^)
        possible-blocks (remove #{start-pos (move start-pos (first directions))} (walk matrix start-pos (first directions)))]
    (->> possible-blocks
         (map #(matrix->update-xy matrix %1 \#))
         (filter #(is-circular? %1 start-pos (first directions)))
         (count))))
