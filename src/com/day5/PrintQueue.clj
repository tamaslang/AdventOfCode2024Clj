(ns com.day5.PrintQueue
  (:require [com.utils.InputParsing :refer :all]
            [clojure.string :as str]
            [clojure.set :as set]))

(defn parse-input [input-lines]
  (let [[rules-part _ print-queue-part] (partition-by str/blank? input-lines)
        print-queues (map #(parse-numbers-in-line-separator #"," %1) print-queue-part)
        rules-list (map #(parse-numbers-in-line-separator #"\|" %1) rules-part)
        rules-grouped (group-by (fn [[before _]] before) rules-list)
        rules-mapped (into {} (map (fn [[key values]] [key (map second values)]) rules-grouped))]
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
  (let [[rules queues] (parse-input data)
        is-valid-check (partial is-queue-valid? rules)]
    (->> queues
         (filter is-valid-check)
         (map mid)
         (reduce +))))

(defn compare-with-rules [rules x y]
  (empty? (set/intersection #{y} (set (rules x)))))

(defn fix-invalid-queues
  "should fix invalid print queue orders"
  [data]
  (let [[rules queues] (parse-input data)
        comparator-based-on-rules (partial compare-with-rules rules)]
    (->> queues
         (filter #(not (is-queue-valid? rules %1)))
         (map #(sort comparator-based-on-rules %1))
         (map mid)
         (reduce +))))
