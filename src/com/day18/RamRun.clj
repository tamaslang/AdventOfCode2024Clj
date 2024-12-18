(ns com.day18.RamRun
  (:require [clojure.set :as set]
            [com.utils.InputParsing :refer :all]))

(defn parse-positions [positions]
  (map #(parse-numbers-in-line-separator #"," %) positions))

(defn any-traverser-reached-end [end-pos traversers]
  (some #{end-pos} traversers))

(def directions
  {:NORTH [0, -1]
   :EAST [1 0]
   :SOUTH [0 1]
   :WEST [-1 0]})

(defn move [current-pos direction]
  [(+ (first current-pos) (first direction))
   (+ (second current-pos) (second direction))])

(defn adjacent [traverser dimension]
  (->>
   (vals directions)
   (map (fn [direction] (move traverser direction)))
   (filter (fn [[x y]] (and
                        (>= (dec dimension) x 0)
                        (>= (dec dimension) y 0))))
   set))

(defn update-state [visited-pos-state traverser step]
  (swap! visited-pos-state assoc traverser step)
  traverser)

(defn print-area [traversers visited-pos blocks  dimension]
  (reduce (fn [_, count]
            (let
             [[x y] [(mod count dimension) (int (Math/floor (/ count dimension)))]]
              (when (zero? x) (println ""))
              (cond
                (traversers [x y]) (print \@)
                (visited-pos [x y]) (print \O)
                (blocks [x y]) (print \#)
                :else (print \.))))
          []
          (range 0  (* dimension dimension))))

(defn traverse-with-blocks [dimension start-pos end-pos blocks]
  (def visited-pos (atom {}))
  (loop
   [traversers #{start-pos}
    step-count 0]
    ;(println)
    ;(println)
    ;(println "STEP " step-count ". Traversers=" traversers)
    ;(println step-count ". Visited pos=" @visited-pos)
    ;(print-area traversers @visited-pos blocks dimension)
    (cond
      (any-traverser-reached-end end-pos traversers) [true step-count]
      (empty? traversers) [false step-count]
      :else
      (let [traversers* (->> traversers
                             (map (fn [traverser] (update-state visited-pos traverser step-count)))
                             (mapcat (fn [traverser] (adjacent traverser dimension)))
                             (filter (fn [traverser] (nil? (@visited-pos traverser))))
                             (filter (fn [traverser] (nil? (blocks traverser))))
                             set)]
        (recur traversers* (inc step-count))))))

(defn shortest-path-to-exit
  "should find shortest-path-to-exit"
  [dimension nr-of-box-fallen data]
  (let [blocks (parse-positions data)
        blocks-fallen (set (take nr-of-box-fallen blocks))
        start-pos [0 0]
        end-pos [(dec dimension) (dec dimension)]
        [escaped? step-count] (traverse-with-blocks dimension start-pos end-pos blocks-fallen)]
    step-count))

(defn first-byte-that-blocks
  "should find shortest-path-to-exit"
  [dimension data]
  (let [blocks (parse-positions data)
        start-pos [0 0]
        end-pos [(dec dimension) (dec dimension)]]
    (reduce (fn [_ nr-of-box-fallen]
              (println "CHECKING " nr-of-box-fallen)
              (let [blocks-fallen (set (take nr-of-box-fallen blocks))
                    [escaped? _] (traverse-with-blocks dimension start-pos end-pos blocks-fallen)]
                (when (not escaped?) (reduced nr-of-box-fallen))))
            (range 0 (count blocks)))))