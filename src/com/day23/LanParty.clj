(ns com.day23.LanParty
  (:require [clojure.string :as str]
            [com.utils.InputParsing :refer :all]))

(defn parse-connection [connection]
  (let [[c1 c2] (str/split connection  #"-")] [c1 c2]))

(defn parse-input [computers]
  (->>  computers
        (map parse-connection)))

(defn group-connections [connections]
  (reduce (fn [lanparties? [c1 c2]]
            (let [c1-added (update-in lanparties? [c1] (fn [old-val] (concat [c2] old-val)))
                  c2-added (update-in c1-added [c2] (fn [old-val] (concat [c1] old-val)))]
              c2-added))

          {} connections))

(defn count-interconnected-networks-with-size-from [groupped-connections from-computer computer expected-network-size network]
  (cond
    (> (count network) expected-network-size) nil
    (and (= (count network) expected-network-size) (network computer)) network
    :else
    (->>
     (groupped-connections computer)
     (remove #{from-computer})
     (map #(count-interconnected-networks-with-size-from groupped-connections computer % expected-network-size (conj network computer)))
     (remove nil?)
     (flatten)
     (distinct))))

(defn find-connected-computers
  "should find solution"
  [data]
  (let [connections (parse-input data)
        groupped-connections (group-connections connections)]
    (->>
     groupped-connections
     (filter (fn [[c _]] (= (first c) \t)))
     (map (fn [[c _]] c))
     (mapcat (fn [computer] (count-interconnected-networks-with-size-from groupped-connections computer computer 3 #{computer})))
     distinct
     count)))