(ns com.day21.Numpad
  (:require [com.utils.InputParsing :refer :all]))

(def keypad
  {\7 [0 0]
   \8 [1 0]
   \9 [2 0]
   \4 [0 1]
   \5 [1 1]
   \6 [2 1]
   \1 [0 2]
   \2 [1 2]
   \3 [2 2]
   \X [0 3]
   \0 [1 3]
   \A [2 3]
   \^ [1 3]
   \< [0 4]
   \v [1 4]
   \> [2 4]})

;Least turns (this becomes important when escaping the missing cell in both numeric and directional pads).
;
;moving < over ^ over v over >.
(def move-order
  {\< 0
   \^ 1
   \v 2
   \> 3})

(defn distance [from to] (mapv - to from))

(defn mapout-best-path [from to]
  (let [[from to] [(keypad from) (keypad to)]
        void (keypad \X)
        [dx dy] (distance from to)
        x-instructions (if (< dx 0) (repeat (abs dx) \<) (repeat dx \>))
        y-instructions (if (< dy 0) (repeat (abs dy) \^) (repeat dy \v))
        paths (->> [(when (not= void [(+ (first from) dx) (second from)]) ; can go x first
                      (str (apply str x-instructions) (apply str y-instructions) "A"))
                    (when (not= void [(first from) (+ dy (second from))]) ; can go y first
                      (str (apply str y-instructions) (apply str x-instructions) "A"))]
                   (keep identity))]
    (first (sort-by (fn [path] (move-order (first path))) paths))))

(defn sequence-to-instructions [sequence]
  (reduce (fn [instructions [from to]]
            (conj instructions (mapout-best-path from to)))
          []
          (partition 2 1 (str "A" sequence))))

(def recur-instructions-memo (memoize (fn [input depths]
                                        (cond
                                          (zero? depths) (count input)
                                          :else
                                          (->>
                                           (sequence-to-instructions input)
                                           (map #(recur-instructions-memo % (dec depths)))
                                           (reduce +))))))

(defn numeric-part [input]
  (Integer/parseInt (apply str (filter Character/isDigit input))))

(defn calculate-robot-instructions-length
  "should calculate length of best sequences"
  [robot-level data]
  (->> data
       (map (fn [input] (* (recur-instructions-memo input (inc robot-level)) (numeric-part input))))
       (reduce +)))