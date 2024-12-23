(ns com.day23.LanParty
  (:require [clojure.set :as set]
            [clojure.string :as str]
            [com.utils.InputParsing :refer :all]))

(defn parse-connection [connection]
  (let [[c1 c2] (str/split connection  #"-")] [c1 c2]))

(defn parse-input [computers]
  (->>  computers
        (map parse-connection)))

(defn map-connection-to-all-points [connections]
  (reduce (fn [lanparties? [c1 c2]]
            (let [c1-added (update-in lanparties? [c1] (fn [old-val] (concat [c2] old-val)))
                  c2-added (update-in c1-added [c2] (fn [old-val] (concat [c1] old-val)))]
              c2-added))
          {} connections))

(defn find-intersect [mapped-connections computer network root-computers min-size max-size]
  (let
   [connections (set/difference (set (mapped-connections computer)) network)
    only-valid-if-all-connected (set/intersection connections root-computers)
    network* (conj network computer)
    ]
    ;(println "network " network*)
    ;(println "connections " connections)
    ;(println "only-valid-if-all-connected " only-valid-if-all-connected)
    (cond
      (= max-size (count network*)) network*
      (and (empty? only-valid-if-all-connected) (= max-size (count network*))) network*
      (empty? only-valid-if-all-connected) nil
      :else
      (->>
        only-valid-if-all-connected
       (map #(find-intersect mapped-connections % network* root-computers min-size max-size))
       (remove nil?)
       (flatten)
       (distinct)))))

(defn find-connected-computers
  "should find solution"
  [data]
  (let [connections (parse-input data)
        mapped-connections (map-connection-to-all-points connections)]
    (->>
     mapped-connections
     (filter (fn [[c _]] (= (first c) \t)))
     (map (fn [[c _]] c))
     (mapcat (fn [computer] (find-intersect mapped-connections computer #{computer} (set (mapped-connections computer)) 3 3)))
     distinct
     count
     )
    ))