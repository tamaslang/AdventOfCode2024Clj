(ns com.day9.Task
  (:require [com.utils.InputParsing :refer :all]))

(defn expand-memory [memory]
  (loop
   [memory memory
    idx 0
    move-idx (dec (count memory))
    expanded-memory []]
    (cond
      (> idx move-idx) expanded-memory
      (even? idx) ; FILE!
      (let [file-size (nth memory idx)
            file-id (/ idx 2)
            expanded (repeat file-size file-id)]
        (recur memory (inc idx) move-idx (concat expanded-memory expanded)))
      (odd? idx) ; SPACE!
      (let [file-id (/ move-idx 2)
            space-size (nth memory idx)
            move-size (nth memory move-idx)
            movable-size (min space-size move-size)
            expanded (repeat movable-size file-id)
            move-idx* (if (>= space-size move-size) (- move-idx 2) move-idx)
            remaining* (if (<= space-size move-size) (- move-size space-size) 0)
            idx* (if (> space-size move-size) idx (inc idx))
            space-size-remaining (if (> space-size move-size) (- space-size move-size) 0)
            memory* (assoc memory move-idx remaining* idx space-size-remaining)]
        (recur memory* idx* move-idx* (concat expanded-memory expanded))))))

(defn calculate-checksum-for-memory
  "should find solution"
  [data]
  (println "data=" data)
  (let [memory (into [] (map #(- (int %1) (int \0))) data)]
    (->>
     (expand-memory memory)
     (map-indexed vector)
     (map (fn [[idx id]] (* idx id)))
     (reduce +))))