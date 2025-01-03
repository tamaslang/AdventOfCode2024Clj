(ns com.utils.InputParsing
  (:require [clojure.set :as set]
            [clojure.string :as str]))

(defn digitsToNumber
  "convert array of digits to a number"
  [digits]
  (reduce (fn [acc elem] (+ (* acc 10) elem)) digits))

(defn parse-numbers-in-line
  [number-line]
  (->>
   (str/split number-line #"\s+")
   (map #(Long/parseLong %))))

(defn parse-numbers-in-line-separator
  [separator number-line]
  (->>
   (str/split number-line separator)
   (map #(Long/parseLong %))))

(defn long-str [& strings] (str/join "\n" strings))

(defn create-matrix [string-lines]
  (into [] (map vec) string-lines))

(defn matrix->arr-w-symbol-and-xy [matrix]
  (for [[i row] (map-indexed vector matrix)
        [j key] (map-indexed vector row)]
    {:symbol key :x j :y i}))

(defn matrix->all-locations [matrix]
  (set (for [[i row] (map-indexed vector matrix)
             [j key] (map-indexed vector row)]
         [j i])))

(defn matrix->get-xy [matrix [x y]]
  (get-in matrix [y x]))

(defn matrix->get-char-at-xy [matrix [x y]]
  (when-let [char (get-in matrix [y x])] [char [x y]]))

(defn matrix->update-xy [matrix [x y] char]
  (update-in matrix [y x] (fn [_] char)))

(defn matrix->find-first [matrix char]
  (let
   [size-y (count matrix)
    size-x (count (first matrix))]
    (reduce (fn [_, count]
              (let
               [pos [(mod count size-x) (int (Math/floor (/ count size-x)))]]
                (if (= (matrix->get-xy matrix pos) char) (reduced pos) ())))
            0
            (range 0 (* size-x size-y)))))

(defn matrix->find-all [matrix char]
  (let
   [size-y (count matrix)
    size-x (count (first matrix))]
    (reduce (fn [found, count]
              (let
               [pos [(mod count size-x) (int (Math/floor (/ count size-x)))]]
                (if (= (matrix->get-xy matrix pos) char) (conj found pos) found)))
            #{}
            (range 0 (* size-x size-y)))))

(defn matrix->find-all-with-fmatching [matrix char-matching?]
  (let
   [size-y (count matrix)
    size-x (count (first matrix))]
    (reduce (fn [found, count]
              (let
               [pos [(mod count size-x) (int (Math/floor (/ count size-x)))]
                char-at-pos (matrix->get-xy matrix pos)]
                (if (char-matching? char-at-pos) (conj found [char-at-pos pos]) found)))
            #{}
            (range 0 (* size-x size-y)))))

(defn matrix->find-all-nr-with-fmatching [matrix char-matching?]
  (let
   [size-y (count matrix)
    size-x (count (first matrix))]
    (reduce (fn [found, count]
              (let
               [pos [(mod count size-x) (int (Math/floor (/ count size-x)))]
                char-at-pos (matrix->get-xy matrix pos)]
                (if (char-matching? char-at-pos) (conj found [(Integer/parseInt (str char-at-pos)) pos]) found)))
            #{}
            (range 0 (* size-x size-y)))))

(defn matrix->find-all-pos-with-fmatching [matrix char-matching?]
  (let
   [size-y (count matrix)
    size-x (count (first matrix))]
    (reduce (fn [found, count]
              (let
               [pos [(mod count size-x) (int (Math/floor (/ count size-x)))]
                char-at-pos (matrix->get-xy matrix pos)]
                (if (char-matching? char-at-pos) (conj found pos) found)))
            #{}
            (range 0 (* size-x size-y)))))

(defn print-matrix [matrix]
  (doseq [line matrix]
    (println (apply str line))))

(defn rotate [matrix]
  (apply map list matrix))

(defn indices [pred coll]
  (keep-indexed #(when (pred %2) %1) coll))