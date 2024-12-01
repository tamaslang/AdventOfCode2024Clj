(ns com.day1.Locations
  (:require [com.utils.InputParsing :refer :all]))

(defn findDistance
  "Should find the difference between left and right pairs "
  [data]
  (let
   [location-list (map parse-numbers-in-line data)
    location-mtr   (create-matrix location-list)
    location-cols (rotate location-mtr)
    [left-locations, right-locations] location-cols
    combined-locations (map vector (sort left-locations) (sort right-locations))
    differences (map (fn [[left right]] (abs (- left right))) combined-locations)]
    (reduce + differences)))

(defn findSimilarity
  "Should find the difference between left and right pairs "
  [data]
  (let
   [location-list (map parse-numbers-in-line data)
    location-mtr   (create-matrix location-list)
    location-cols (rotate location-mtr)
    [left-locations, right-locations] location-cols
    location-frequencies (frequencies right-locations)
    similarity (map (fn [loc] (* loc (if-let [fr (location-frequencies loc)] fr 0))) left-locations)]
    (reduce + similarity)))