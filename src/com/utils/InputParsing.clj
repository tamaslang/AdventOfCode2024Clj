(ns com.utils.InputParsing
  (:require [clojure.string :as str]))

(defn digitsToNumber
  "convert array of digits to a number"
  [digits]
  (reduce (fn [acc elem] (+ (* acc 10) elem)) digits))

(defn parse-numbers-in-line
  [number-line]
  (->>
   (str/split number-line #"\s+")
   (map #(Long/parseLong %))))

(defn long-str [& strings] (str/join "\n" strings))

(defn create-matrix [string-lines]
  (into [] (map vec) string-lines))

(defn matrix->arr-w-symbol-and-xy [matrix]
  (for [[i row] (map-indexed vector matrix)
        [j key] (map-indexed vector row)]
    {:symbol key :x j :y i}))

(defn print-matrix [matrix]
  (doseq [line matrix]
    (println (apply str line))))

(defn rotate [matrix]
  (apply map list matrix))

(defn indices [pred coll]
  (keep-indexed #(when (pred %2) %1) coll))