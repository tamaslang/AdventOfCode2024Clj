(ns com.day16.ReindeerMaze
  (:require [clojure.set :as set]
            [com.utils.InputParsing :refer :all]))

(def directions
  { :NORTH [0, -1]
    :EAST [1 0]
   :SOUTH [0 1]
   :WEST [-1 0]})

(defn turn-right [direction]
  (let [direction-vals (vals directions)]
    (nth (cycle direction-vals) (inc (.indexOf direction-vals direction))))
  )

(defn turn-left [direction]
  (let [direction-vals (vals directions)
        direction-count (count directions)
        ]
    (nth direction-vals (mod (+ direction-count (dec (.indexOf direction-vals direction))) direction-count)))
  )

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

(defn apply-upper-score-limit [upper-limit positions]
  (filter (fn [{:keys [score]}] (<= score upper-limit)) positions))

(defn find-reindeer-path [upper-limit matrix start-pos end-pos]
  (def visited-pos (atom {}))
  (reduce (fn [reindeers step-count]
            (if (empty? reindeers) (reduced [reindeers step-count])
                (mapcat (fn [{:keys [pos direction score]}]
                          (let
                           [reached-end? (end? (matrix->get-xy matrix pos))]
                            (when (not (was-visited-cheaper @visited-pos pos direction score)) (swap! visited-pos assoc [pos direction] score))
                            (cond
                              reached-end? []
                              (was-visited-cheaper @visited-pos pos direction score) []
                              :else
                              (apply-upper-score-limit upper-limit
                                                       (all-positions-to-move-from-here matrix pos (go-forward direction) score)))))

                        reindeers)))
          [{:pos start-pos :direction [1 0] :score 0}] (range))
  (remove nil?
          [(@visited-pos [end-pos [0, -1]])
           (@visited-pos [end-pos [1 0]])
           (@visited-pos [end-pos [0 1]])
           (@visited-pos [end-pos [-1 0]])]))

(defn find-path-with-lowest-score
  "should find solution"
  [upper-limit data]
  (let [matrix (create-matrix data)
        start-pos (matrix->find-first matrix \S)
        end-pos (matrix->find-first matrix \E)]
    (apply min (find-reindeer-path upper-limit matrix start-pos end-pos))))

(defn all-reindeers-reached-end? [matrix reindeers]
  (every? (fn [{:keys [pos]}] (end? (matrix->get-xy matrix pos))) reindeers))

(defn count-fields-best-path [upper-limit matrix start-pos end-pos]
  (def visited-pos (atom {}))
  (let [[reindeers _] (reduce (fn [reindeers step-count]
                                (if (all-reindeers-reached-end? matrix reindeers) (reduced [reindeers step-count])
                                    (mapcat (fn [{:keys [pos direction score visited]}]
                                              (when (not (was-visited-cheaper @visited-pos pos direction score)) (swap! visited-pos assoc [pos direction] score))
                                              (cond
                                                (was-visited-cheaper @visited-pos pos direction score) []
                                                :else
                                                (->>
                                                 (all-positions-to-move-from-here matrix pos (go-forward direction) score)
                                                 (filter (fn [{:keys [score]}] (<= score upper-limit)))
                                                 (map #(assoc % :visited (conj visited pos (:pos %)))))))

                                            reindeers)))
                              [{:pos start-pos :direction [1 0] :score 0 :visited #{}}] (range))]
    (count (apply set/union (map (fn [{:keys [_ visited]}] visited) reindeers)))))

(defn find-tiles-for-seats
  "should find solution"
  [upper-limit data]
  (let [matrix (create-matrix data)
        start-pos (matrix->find-first matrix \S)
        end-pos (matrix->find-first matrix \E)]
    (count-fields-best-path upper-limit matrix start-pos end-pos)))

