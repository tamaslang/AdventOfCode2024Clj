(ns com.day22.MonkeyBusiness
  (:require [com.utils.InputParsing :refer :all]))

(def mul_64 (fn [number] (* number 64)))
(def div_32 (fn [number] (int (/ number 32))))
(def mul_2048 (fn [number] (* number 2048)))
(def prune (fn [number] (mod number 16777216)))

(defn do-steo [number]
  (->>
   [number]
   (map #(bit-xor % (mul_64 %)))
   (map prune)
   (map #(bit-xor % (div_32 %)))
   (map prune)
   (map #(bit-xor % (mul_2048 %)))
   (map prune)
   (first)))

(defn nth-secret [n number]
  (reduce (fn [acc _] (do-steo acc)) number (range 0 n)))

(defn nth-secret-number
  "should find solution"
  [n data]
  (->>
   data
   (map Integer/parseInt)
   (map #(nth-secret n %))
   (reduce +)))