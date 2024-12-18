(ns com.day18.RamRun
  (:require [clojure.set :as set]
            [clojure.string :as str]
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

(defn log-memory [step-count traversers visited-pos blocks dimension]
  (println)
  (println)
  (println (format "STEP %d. Traversers=%s" step-count traversers))
  (print-area traversers visited-pos blocks dimension))

(defn traverse-with-blocks [logging? dimension start-pos end-pos blocks]
  (def visited-pos (atom {}))
  (loop
   [traversers #{start-pos}
    step-count 0]
    (when logging? (log-memory step-count traversers @visited-pos blocks dimension))
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
  [logging? dimension nr-of-box-fallen data]
  (let [blocks (parse-positions data)
        blocks-fallen (set (take nr-of-box-fallen blocks))
        start-pos [0 0]
        end-pos [(dec dimension) (dec dimension)]
        [_ step-count] (traverse-with-blocks logging? dimension start-pos end-pos blocks-fallen)]
    step-count))

(defn test-escaped-for [dimension start-pos end-pos blocks nr-of-blocks-fallen]
  (first (traverse-with-blocks false dimension start-pos end-pos (set (take nr-of-blocks-fallen blocks)))))

(defn find-element [f-test-escaped-for? start end]
  (loop
   [interval-start start
    interval-end end]
    (let [interval-middle (int (/ (+ interval-start interval-end) 2))
          [interval-start* interval-end*] (if (and (f-test-escaped-for? interval-start) (not (f-test-escaped-for? interval-middle))) [interval-start (dec interval-middle)] [(inc interval-middle) interval-end])]
      (println (format "Intervals [%d %d] [%d %d]" interval-start interval-middle (inc interval-middle) interval-end))
      (cond
        (= interval-start* interval-end*) interval-start
        :else (recur interval-start* interval-end*)))))

(defn first-byte-that-blocks
  "should find shortest-path-to-exit"
  [dimension data]
  (let [blocks (parse-positions data)
        start-pos [0 0]
        end-pos [(dec dimension) (dec dimension)]
        f-test-escaped-for? (partial test-escaped-for dimension start-pos end-pos blocks)
        byte-index (find-element f-test-escaped-for? 0 (dec (count blocks)))
        byte (nth blocks byte-index)]
    (str/join "," byte)))
