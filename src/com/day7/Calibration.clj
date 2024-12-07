(ns com.day7.Calibration
  (:require [com.utils.InputParsing :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(defn parse-input-line [input-line]
  (let
   [[result number-str] (str/split input-line #":")]
    [(Long/parseLong result) (parse-numbers-in-line (str/trim number-str))]))

(def append (fn [this that] (Long/parseLong (str this that))))

(def task-1-functions [+ *])
(def task-2-functions [+ * append])

(defn try-to-combine [functions result components]
  (def results (reduce
                (fn [temp-results, last-nr]
                  (->>
                   temp-results
                   (map (fn [temp-result] ((apply juxt functions) temp-result last-nr)))
                   (flatten)
                   (filter #(<= %1 result))))
                [(first components)]
                (rest components)))

  (if (not-empty (filter #{result} results)) result 0))

(defn find-calibration-result
  "should find solution"
  [combine-functions data]
  (->>
   data
   (map parse-input-line)
   (map (fn [[result components]] (try-to-combine combine-functions result components)))
   (reduce +)))