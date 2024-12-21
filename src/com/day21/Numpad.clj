(ns com.day21.Numpad
  (:require [com.utils.InputParsing :refer :all]))

(defn distance [from to]
  (mapv - to from))

(def numpad
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
   \A [2 3]})

(def dirpad
  {\X [0 0]
   \^ [1 0]
   \A [2 0]
   \< [0 1]
   \v [1 1]
   \> [2 1]})

;Least turns (this becomes important when escaping the missing cell in both numeric and directional pads).
;
;moving < over ^ over v over >.
(def move-order
  {\< 0
   \^ 1
   \v 2
   \> 3})

(defn instructions-from-to [keypad-mapped from to]
  (let [[from to] [(keypad-mapped from) (keypad-mapped to)]
        void (keypad-mapped \X)
        [dx dy] (distance from to)
        x-instructions (if (< dx 0) (repeat (abs dx) \<) (repeat dx \>))
        y-instructions (if (< dy 0) (repeat (abs dy) \^) (repeat dy \v))
        paths    (->> [(when (not= void [(+ (first from) dx) (second from)]) ; can go x first
                         (str (apply str x-instructions) (apply str y-instructions) "A"))
                       (when (not= void [(first from) (+ dy (second from))]) ; can go y first
                         (str (apply str y-instructions) (apply str x-instructions) "A"))]
                      (keep identity))]
    (first (sort-by (fn [path] (move-order (first path))) paths))))

(defn robot-sequence [keypad-mapped sequence]
  (reduce (fn [instructions [from to]]
            (str instructions (instructions-from-to keypad-mapped from to)))
          ""
          (partition 2 1 (str "A" sequence))))

(defn recur-instructions [input depths limit]
  (println "depths " depths "limit" limit "input=" input)
  (cond
    (= depths limit) (count input)
    :else
    (recur-instructions (robot-sequence dirpad input) (inc depths) limit)))

(defn count-instruction-length [input limit]
  (recur-instructions (robot-sequence numpad input) 0 limit))

(defn numeric-part [input]
  (Integer/parseInt (apply str (filter Character/isDigit input))))

(defn calculate-robot-sequences
  "should calculate root sequences"
  [depths data]
  (->> data
       (map (fn [input] (* (count-instruction-length input depths) (numeric-part input))))
       (reduce +)))