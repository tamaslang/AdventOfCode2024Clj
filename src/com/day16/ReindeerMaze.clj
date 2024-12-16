(ns com.day16.ReindeerMaze
  (:require [clojure.set :as set]
            [com.utils.InputParsing :refer :all]))

(def directions
  {:NORTH [0, -1]
   :EAST [1 0]
   :SOUTH [0 1]
   :WEST [-1 0]})

(defn turn-right [direction]
  (let [direction-vals (vals directions)]
    (nth (cycle direction-vals) (inc (.indexOf direction-vals direction)))))

(defn turn-left [direction]
  (let [direction-vals (vals directions)
        direction-count (count directions)]
    (nth direction-vals (mod (+ direction-count (dec (.indexOf direction-vals direction))) direction-count))))

(defn go-forward [direction] direction)

(defn space? [char] (= char \.))
(defn end? [char] (= char \E))

(defn move [current-pos direction]
  [(+ (first current-pos) (first direction))
   (+ (second current-pos) (second direction))])

(defn field-if-can-move [matrix pos direction score]
  (let [char-at-pos (matrix->get-xy matrix (move pos direction))]
    (when (or (space? char-at-pos) (end? char-at-pos)) {:pos (move pos direction) :direction direction :score score})))

(defn was-visited-cheaper [visited-pos pos direction score]
  (and (visited-pos [pos direction]) (< (visited-pos [pos direction]) score)))

(defn all-positions-to-move-from-here [matrix pos direction score]
  (remove nil? [(field-if-can-move matrix pos (go-forward direction) (inc score))
                (field-if-can-move matrix pos (turn-left direction) (+ score 1001))
                (field-if-can-move matrix pos (turn-right direction) (+ score 1001))]))

; RECURSIVE
(defn count-fields-best-path-recursive [visited-pos-state upper-limit matrix end-pos waypoint]
  (cond
    (= (:pos waypoint) end-pos) waypoint
    (not (was-visited-cheaper @visited-pos-state (:pos waypoint) (:direction waypoint) (:score waypoint)))
    (let
     [all-pos-from-here
      (->> (all-positions-to-move-from-here matrix (:pos waypoint) (:direction waypoint) (:score waypoint))
           (filter (fn [{:keys [score]}] (<= score upper-limit)))
           (map #(assoc % :visited (conj (:visited waypoint) (:pos %)))))]
      (swap! visited-pos-state assoc [(:pos waypoint) (:direction waypoint)] (:score waypoint))
      (doall (remove nil? (flatten (map #(count-fields-best-path-recursive visited-pos-state upper-limit matrix end-pos %) all-pos-from-here)))))
    :else nil     ; there is cheaper, stop
    ))

; TASK 1
(defn count-lowest-score [upper-limit matrix start-pos end-pos]
  (def visited-pos (atom {}))
  (let [waypoint {:pos start-pos :direction (directions :EAST) :score 0 :visited #{start-pos}}
        waypoints-reached-end (count-fields-best-path-recursive visited-pos upper-limit matrix end-pos waypoint)]
    (apply min (map #(:score %) waypoints-reached-end))))

(defn find-path-with-lowest-score
  "should find solution"
  [upper-limit data]
  (let [matrix (create-matrix data)
        start-pos (matrix->find-first matrix \S)
        end-pos (matrix->find-first matrix \E)]
    (count-lowest-score upper-limit matrix start-pos end-pos)))

; TASK 2
(defn count-fields-best-path [upper-limit matrix start-pos end-pos]
  (def visited-pos (atom {}))
  (let [waypoint {:pos start-pos :direction (directions :EAST) :score 0 :visited #{start-pos}}
        waypoints-reached-end (count-fields-best-path-recursive visited-pos upper-limit matrix end-pos waypoint)
        smallest-score (apply min (map #(:score %) waypoints-reached-end))
        waypoints-with-smallest-score (filter #(= (:score %) smallest-score) waypoints-reached-end)]
    (count (apply set/union (map (fn [{:keys [_ visited]}] visited) waypoints-with-smallest-score)))))

(defn find-tiles-for-seats
  "should find solution"
  [upper-limit data]
  (let [matrix (create-matrix data)
        start-pos (matrix->find-first matrix \S)
        end-pos (matrix->find-first matrix \E)]
    (count-fields-best-path upper-limit matrix start-pos end-pos)))

