(ns com.day5.PrintQueue
  (:require [com.utils.InputParsing :refer :all]
            [clojure.string :as str]
            [clojure.set :as set]))

(defn parse-input [input-lines]
  (let [[rules-part _ print-queue-part] (partition-by str/blank? input-lines)
        queues (map #(parse-numbers-in-line-separator #"," %1) print-queue-part)
        rules (->> rules-part
                   (map #(parse-numbers-in-line-separator #"\|" %1))
                   (group-by (fn [[before _]] before))
                   (map (fn [[key values]] [key (map second values)]))
                   (into {}))]
    [rules queues]))

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
        is-valid-cond (partial is-queue-valid? rules)]
    (->> queues
         (filter is-valid-cond)
         (map mid)
         (reduce +))))

(defn compare-with-rules [rules x y]
  (empty? (set/intersection #{y} (set (rules x)))))

(defn fix-invalid-queues
  "should fix invalid print queue orders"
  [data]
  (let [[rules queues] (parse-input data)
        comparator-based-on-rules (partial compare-with-rules rules)
        make-it-correct #(sort comparator-based-on-rules %1)
        is-invalid-cond #(not (is-queue-valid? rules %1))]
    (->> queues
         (filter is-invalid-cond)
         (map make-it-correct)
         (map mid)
         (reduce +))))
