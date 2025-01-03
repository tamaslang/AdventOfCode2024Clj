(ns com.day2.SafeReports
  (:require [com.utils.InputParsing :refer :all]))

(defn increasing [this next] (< this next (+ this 4)))
(defn decreasing [this next] (> this next (- this 4)))

(defn numbers-safe? [direction numbers]
  (every? #(apply direction %1) (partition 2 1 numbers))) ; use pred

(defn number-of-safe-reports
  [data]
  (->>
   data
   (map parse-numbers-in-line)
   (filter #(or (numbers-safe? increasing %1) (numbers-safe? decreasing %1)))
   (count)))

(defn all-combinations-with-one-removed [numbers]
  (->>
   (range 0 (count numbers))
   (map (fn [index] (flatten (keep-indexed #(when-not (= index %1) %2) numbers))))))

(defn has-a-valid-combination [lists-of-numbers]
  (some (fn [combinations-for-one] (or (numbers-safe? increasing combinations-for-one) (numbers-safe? decreasing combinations-for-one))) lists-of-numbers))

(defn number-of-safe-reports-with-dampener-brute-force
  [data]
  (let [{safe-rows true unsafe-rows false} (->>
                                            data
                                            (map parse-numbers-in-line)
                                            (group-by #(or (numbers-safe? increasing %1) (numbers-safe? decreasing %1))))
        safe-when-dampener (filter (fn [unsafe-row] (has-a-valid-combination (all-combinations-with-one-removed unsafe-row))) unsafe-rows)]
    (+ (count safe-rows) (count safe-when-dampener))))