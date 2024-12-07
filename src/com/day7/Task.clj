(ns com.day7.Task
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
;
(defn find-calibration-result
  "should find solution"
  [combine-functions data]
  (->>
   data
   (map parse-input-line)
   (map (fn [[result components]] (try-to-combine combine-functions result components)))
   (reduce +)))
;
;;TASK1 ON EXAMPLE =  3749
;;TASK1 ON FILE =  4364915411363
;
(def data-file (io/resource "resources/task7.txt"))
;
(println "TASK1 ON EXAMPLE = " (find-calibration-result task-1-functions ["190: 10 19"
                                         "3267: 81 40 27"
                                         "83: 17 5"
                                         "156: 15 6"
                                         "7290: 6 8 6 15"
                                         "161011: 16 10 13"
                                         "192: 17 8 14"
                                         "21037: 9 7 18 13"
                                         "292: 11 6 16 20"]))
(println "TASK1 ON FILE = " (find-calibration-result task-1-functions (str/split-lines (slurp data-file))))


(println "TASK2 ON EXAMPLE = " (find-calibration-result task-2-functions ["190: 10 19"
                                                                         "3267: 81 40 27"
                                                                         "83: 17 5"
                                                                         "156: 15 6"
                                                                         "7290: 6 8 6 15"
                                                                         "161011: 16 10 13"
                                                                         "192: 17 8 14"
                                                                         "21037: 9 7 18 13"
                                                                         "292: 11 6 16 20"]))

(println "TASK2 ON FILE = " (find-calibration-result task-2-functions (str/split-lines (slurp data-file))))