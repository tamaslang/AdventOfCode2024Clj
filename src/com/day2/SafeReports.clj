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

(defn numbers-safe-dampener? [direction numbers]
  (let
   [[_ errors] (reduce (fn [[last-nr errors], current-nr]
                         (let
                          [safe? (direction last-nr current-nr)
                           errors* (if safe? errors (inc errors))
                           last-nr* (if safe? current-nr last-nr)]
                           [last-nr* errors*])) [(first numbers) 0] (rest numbers))]
    (<= errors 1)))

(defn number-of-safe-reports-with-dampener
  [data]
  (println "data = " data)
  (->>
   data
   (map parse-numbers-in-line)
   (filter #(or (numbers-safe-dampener? increasing %1) (numbers-safe-dampener? decreasing %1)))
   (count)))

(defn all-combinations-with-one-removed [numbers]
  (->>
   (range 0 (count numbers))
   (map (fn [index] (flatten (keep-indexed #(when-not (= index %1) %2) numbers))))))

(defn has-a-valid-combination [lists-of-numbers]
  (some (fn [combinations-for-1] (or (numbers-safe? increasing combinations-for-1) (numbers-safe? decreasing combinations-for-1))) lists-of-numbers))

(defn number-of-safe-reports-with-dampener-brute-force
  [data]
  (def groupped-by-safe (->>
                         data
                         (map parse-numbers-in-line)
                         (group-by #(or (numbers-safe? increasing %1) (numbers-safe? decreasing %1)))))

  (def not-safe (groupped-by-safe false))
  (def safe (groupped-by-safe true))

  ; try to combine and make it safe by removing
  (def safe-with-dampener (->> not-safe
                               (map all-combinations-with-one-removed)
                               (map has-a-valid-combination)
                               (remove nil?)))
  (+ (count safe) (count safe-with-dampener)))