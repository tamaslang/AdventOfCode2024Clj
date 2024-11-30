(ns com.util.Utils
  (:require [clojure.string :as str]))

(defn long-str [& strings] (str/join "\n" strings))

(defn create-matrix [string-lines]
  (into [] (map vec) string-lines))