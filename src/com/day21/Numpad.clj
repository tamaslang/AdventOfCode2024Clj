(ns com.day21.Numpad
  (:require [com.utils.InputParsing :refer :all]))


(defn distance [from to]
  (mapv - to from))

(def numpad
  {
   \7 [0 0]
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
   }
  )

(def dirpad
  {
    \X [0 0]
    \^ [1 0]
    \A [2 0]
    \< [0 1]
    \v [1 1]
    \> [2 1]
   }
)

(defn robot-sequence [keypad-mapped sequence]
  (reduce (fn [instructions [from to]]
            (let [[dx dy] (distance (keypad-mapped from) (keypad-mapped to))]
               (str
                         instructions
                         (apply str (if (< dy 0) (repeat (abs dy) \^) (repeat dy \v)))
                         (apply str (if (< dx 0) (repeat (abs dx) \<) (repeat dx \>)))
                         \A
                         )
              )
            )
          ""
          (partition 2 1 (str "A" sequence))
  )
)

(defn to-instructions [input]
  (->> [input]
       (map #(robot-sequence numpad %))
       (map #(robot-sequence dirpad %))
       (map #(robot-sequence dirpad %))
       first
       )
  )

(defn numeric-part [input]
  (Integer/parseInt (apply str (filter Character/isDigit input)))
  )

(defn calculate-robot-sequences
  "should calculate root sequences"
  [data]
  (->> data
       (map (fn[input] (* (count (to-instructions input)) (numeric-part input))))
       (reduce +)
    )
  )
