(ns com.day25.Locks
  (:require [com.utils.InputParsing :refer :all]
            [clojure.string :as str]))

(defn parse-as-number [line]
  (reduce (fn [result char] (+ (* result 10) (if (= char \#) 1 0))) 0 line))

(defn to-heights [schema-lines]
  (->>
   schema-lines
   (map parse-as-number)
   (reduce +)))

(defn key? [group] (every? #{\#} (last group)))

(defn lock? [group] (every? #{\#} (first group)))

(defn parse-locks-and-keys [input-lines]
  (let [keys_and_locks (->>
                        (partition-by str/blank? input-lines)
                        (remove #(= 1 (count %))))
        keys (filter key? keys_and_locks)
        locks (filter lock? keys_and_locks)]
    {:locks (map to-heights locks) :keys (map to-heights keys)}))

(defn try-match [length width this that]
  (reduce (fn [[key-remaining, lock-remaining] _]
            (if (> (+ (mod key-remaining 10) (mod lock-remaining 10)) length) (reduced false)
                [(int (/ key-remaining 10)) (int (/ lock-remaining 10))])) [this that] (range 0 width)))

(defn find-matching-keys
  "should find overlapping keys"
  [data]
  (let [{:keys [locks keys]} (parse-locks-and-keys data)
        matcher (partial try-match 7 5)]
    (reduce (fn [acc lock]
              (+ acc
                 (->> keys
                      (map (fn [key] (if (matcher lock key) 1 0)))
                      (reduce +))))
            0 locks)))