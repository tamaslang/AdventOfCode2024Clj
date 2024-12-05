(ns com.day5.PrintQueue
  (:require [com.utils.InputParsing :refer :all]
            [clojure.string :as str]
            [clojure.set :as set]))

(defn parse-input [input-lines]
  (let [[rules-part _ print-queue-part] (partition-by str/blank? input-lines)
        rules (map #(parse-numbers-in-line-separator #"\|" %1) rules-part)
        rules-groupped (group-by (fn [[before after]] before) rules)
        rules-mapped (into {} (map (fn [[key values]] [key (map second values)]) rules-groupped))
        print-queues (map #(parse-numbers-in-line-separator #"," %1) print-queue-part)]
    [rules-mapped print-queues]))

(defn mid [queue] (nth queue (/ (dec (count queue)) 2)))

(defn is-queue-valid? [rules queue]
  (reduce (fn [acc, nr]
            (if
             (not-empty (set/intersection (set (take acc queue)) (set (rules nr))))
              (reduced false)
              (dec acc))) (dec (count queue)) (reverse queue)))

(defn find-valid-queue
  "should find valid print queue orders"
  [data]
  (let [[rules queues] (parse-input data)]
    (->> queues
         (map #(if (is-queue-valid? rules %1) (mid %1) 0))
         (reduce +))))