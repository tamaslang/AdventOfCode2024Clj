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
  (->>
   data
   (map parse-numbers-in-line)
   (filter #(or (numbers-safe-dampener? increasing %1) (numbers-safe-dampener? decreasing %1)))
   (count)))