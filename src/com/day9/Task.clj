(ns com.day9.Task
  (:require [com.utils.InputParsing :refer :all]))

(defn checksum-for-memory [memory]
  (loop
   [memory memory
    logical-idx 0
    idx 0
    move-idx (dec (count memory))
    total 0]
    (println inc "|" move-idx)
    (cond
      (> idx move-idx) total
      (even? idx) ; FILE!
      (let [file-size (nth memory idx)
            logical-idx* (+ logical-idx file-size)
            file-id (/ idx 2)
            checksum (reduce + (map #(* file-id %1) (range logical-idx logical-idx*)))]
        (recur memory logical-idx* (inc idx) move-idx (+ total checksum)))
      (odd? idx) ; SPACE!
      (let [file-id (/ move-idx 2)
            space-size (nth memory idx)
            move-size (nth memory move-idx)
            logical-idx* (min (+ logical-idx space-size) (+ logical-idx move-size))
            checksum (reduce + (map #(* file-id %1) (range logical-idx logical-idx*)))
            move-idx* (if (>= space-size move-size) (- move-idx 2) move-idx)
            remaining* (if (<= space-size move-size) (- move-size space-size) 0)
            idx* (if (> space-size move-size) idx (inc idx))
            space-size-remaining (if (> space-size move-size) (- space-size move-size) 0)
            memory* (assoc memory move-idx remaining* idx space-size-remaining)]
        (recur memory* logical-idx* idx* move-idx* (+ total checksum))))))

(defn calculate-checksum-for-memory
  "should find solution"
  [data]

  (println "data=" data)
  (let [memory (into [] (map #(- (int %1) (int \0))) data)]
    (checksum-for-memory memory)))