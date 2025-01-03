(ns com.day9.MoveFileBlocks
  (:require [com.utils.InputParsing :refer :all]))

; https://www.educative.io/answers/vectors-vs-lists-in-clojure

(defn checksum-for-memory [memory]
  (loop
   [memory memory ; [1 2 3 4 5]
    logical-idx 0
    idx 0
    move-idx (dec (count memory))
    total 0]
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
  (let [memory (into [] (map #(- (int %1) (int \0))) data)]
    (checksum-for-memory memory)))

; TASK 2
(defn calculate-checksum-for-memory-move-blocks
  "should find solution"
  [data]
  (let [memory (into [] (map #(- (int %1) (int \0))) data)
        memory-mapped (->> memory
                           (map-indexed vector)
                           (map (fn [[id size]] (if (even? id) [(/ id 2) size] [-1 size])))
                           (into []))]

    (def writeable-memory (java.util.ArrayList. memory-mapped))

    (reduce (fn [_ [file-id file-size]]
              (let
               [next-free-space (first (filter (fn [[space-id space-size]] (and (= space-id -1) (>= space-size file-size))) writeable-memory))
                space-position (.indexOf writeable-memory next-free-space)
                file-position (.lastIndexOf writeable-memory [file-id file-size])]
                (when (> file-position space-position -1)
                  ; remove file
                  (.remove #^java.util.List writeable-memory [file-id file-size])
                  ; remove space
                  (.remove #^java.util.List writeable-memory (int space-position))
                  ; add file to where space pos
                  (.add writeable-memory space-position [file-id file-size])
                  ; insert remaining space back
                  (.add writeable-memory (inc space-position) [-1 (- (second next-free-space) file-size)])
                  ; add space to where file was
                  (.add writeable-memory file-position [-1 file-size]))))
            [] (take-nth 2 (reverse memory-mapped))))
  (->>
   writeable-memory
   (map (fn [[id size]] (if (> id -1) (repeat size id) (repeat size 0))))
   (flatten)
   (map-indexed (fn [index id] (* index id)))
   (reduce +)))



