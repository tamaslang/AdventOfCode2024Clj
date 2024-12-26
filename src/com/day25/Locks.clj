(ns com.day25.Locks
  (:require [com.utils.InputParsing :refer :all]
            [clojure.string :as str]))

(defn to-heights [schema-lines]
  (->> schema-lines
       (map char-array) ; map all lines to char array
       (apply map vector) ; create columns based on indexes
       (map (fn [column] (count (filter #(= % \#) column)))) ; count # in each column creating the key value e.g. (1 6 4 5 4)
       ))

(defn key? [group] (every? #{\#} (last group))) ; lines are parsed as key if all character of the last line is \#

(defn parse-locks-and-keys [input-lines]
  (let [{keys true locks false} (->> (partition-by str/blank? input-lines)
                                     (remove #(every? str/blank? %))
                                     (group-by key?))]
    {:locks (map to-heights locks) :keys (map to-heights keys)}))

(defn find-matching-keys [data]
  (let [{:keys [locks keys]} (parse-locks-and-keys data)]
    (reduce (fn [acc lock]
              (+ acc
                 (->> keys
                      (map (fn [key] (every? #(<= % 7) (map + key lock))))
                      (map #(get {false 0 true 1} %))
                      (reduce +))))
            0 locks)))