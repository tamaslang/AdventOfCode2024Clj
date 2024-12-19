(ns com.day19.Towels
  (:require [com.utils.InputParsing :refer :all]
            [clojure.string :as str]))

(defn parse-input [input-lines]
  (let [[[patterns] _ towels] (partition-by str/blank? input-lines)]
    [(str/split patterns #", ") towels]))

(defn find-patterns-string-starts-with [patterns test-string]
  (filter #(str/starts-with? test-string %) patterns))

(defn remove-from-beginning [sub-string test-string]
  (cond
    (str/starts-with? test-string sub-string) (apply str (drop (count sub-string) test-string))
    :else test-string))

(def count-reaching-end-with-patterns-memo
  (memoize (fn [patterns test-string]
             (let [matching-patterns (find-patterns-string-starts-with patterns test-string)
                   full-match-count (if (some #{test-string} matching-patterns) 1 0)]
               (cond
                 (empty? matching-patterns) 0
                 :else
                 (+
                  full-match-count
                  (->>
                   matching-patterns
                   (map #(count-reaching-end-with-patterns-memo patterns (remove-from-beginning % test-string)))
                   (reduce +))))))))

(defn find-pattern-combinations
  "Find pattern combinations for towels"
  [data]
  (let [[patterns towels] (parse-input data)]
    (println "pattern " patterns)
    (println "towels " towels)
    (->>
     towels
     (map #(do (println "COUNT MATCHING FOR " %) %))
     (map #(count-reaching-end-with-patterns-memo patterns %))
     (filter #(> % 0))
     (count))))

(defn find-all-different-ways-for-pattern-combinations
  "Find all different ways for part 2"
  [data]
  (let [[patterns towels] (parse-input data)]
    (println "pattern " patterns)
    (println "towels " towels)
    (->>
     towels
     (map #(do (println "COUNT MATCHING FOR " %) %))
     (map #(count-reaching-end-with-patterns-memo patterns %))
     (filter #(> % 0))
     (reduce +))))