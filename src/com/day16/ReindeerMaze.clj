(ns com.day16.ReindeerMaze
  (:require [com.utils.InputParsing :refer :all]))

(defn adjacents [matrix [x y]]
  (keep identity [;above
                  (matrix->get-char-at-xy matrix [x (dec y)])
                  ; same row
                  (matrix->get-char-at-xy matrix [(dec x) y])
                  (matrix->get-char-at-xy matrix [(inc x) y])
                  ; row below
                  (matrix->get-char-at-xy matrix [x (inc y)])]))

;  (def visited-pos (atom {}))
;  (swap! visited-pos assoc [[10 10] [0 -1]] 1010)
;  (@visited-pos [[10 10] [0 -1]])

(def directions
  [[0, -1]
   [1 0]
   [0 1]
   [-1 0]])

(defn turn-right [direction]
  (nth (cycle directions) (inc (.indexOf directions direction))))

(defn turn-left [direction]
  (nth directions (mod (+ (count directions) (dec (.indexOf directions direction))) (count directions))))

(defn go-forward [direction] direction)

(defn wall? [char] (= char \#))
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

(defn find-reindeer-path [matrix start-pos end-pos]
  (def visited-pos (atom {}))
  (reduce (fn [reindeers _]
            (if (empty? reindeers) (reduced @visited-pos)
                                   (mapcat (fn [{:keys [pos direction score]}]
                                             (let
                                               [reached-end? (end? (matrix->get-xy matrix pos))]
                                               ; UPDATE VISITED
                                               (when (not (was-visited-cheaper @visited-pos pos direction score)) (swap! visited-pos assoc [pos direction] score))
                                               (cond
                                                 reached-end? []
                                                 (was-visited-cheaper @visited-pos pos direction score) []
                                                 :else (all-positions-to-move-from-here matrix pos (go-forward direction) score)))) reindeers)))
          [{:pos start-pos :direction [1 0] :score 0}] (range))
  (remove nil?
          [(@visited-pos [end-pos [0, -1]])
           (@visited-pos [end-pos [1 0]])
           (@visited-pos [end-pos [0 1]])
           (@visited-pos [end-pos [-1 0]])]))

;
;(reduce (fn [next-positions next-slope]
;          (let
;            [adjacents (mapcat #(matrix->adjacents matrix %) next-positions)
;             next-positions (filter (fn [[height _]] (= height next-slope)) adjacents)]
;            next-positions))
;        [starting-position] "123456789"))

(defn find-path-with-lowest-score
  "should find solution"
  [data]
  (let [matrix (create-matrix data)
        start-pos (matrix->find-first matrix \S)
        end-pos (matrix->find-first matrix \E)]
    (apply min (find-reindeer-path matrix start-pos end-pos))))