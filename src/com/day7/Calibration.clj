(ns com.day7.Calibration
  (:require [clojure.set :as set]
            [com.utils.InputParsing :refer :all]
            [clojure.string :as str]))

(defn parse-input-line [input-line]
  (let
   [[result number-str] (str/split input-line #":")]
    [(Long/parseLong result) (parse-numbers-in-line (str/trim number-str))]))

(def append (fn [this that] (Long/parseLong (str this that))))

(def task-1-functions [+ *])
(def task-2-functions [+ * append])

(defn combine-all [functions components threshold]
  (reduce
   (fn [temp-results, last-nr]
     (->>
      temp-results
      (map (fn [temp-result] ((apply juxt functions) temp-result last-nr)))
      (flatten)
      (filter #(<= %1 threshold))
      (into #{})))

   #{(first components)}
   (rest components)))

(defn result-or-zero-if-invalid [functions expected-result components]
  (if (contains? (combine-all functions components expected-result) expected-result) expected-result 0))

(defn find-calibration-result
  "should find solution"
  [combine-functions data]
  (->>
   data
   (map parse-input-line)
   (pmap (fn [[result components]] (result-or-zero-if-invalid combine-functions result components)))
   (reduce +)))