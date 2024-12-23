(ns com.day23.LanParty
  (:require [clojure.set :as set]
            [clojure.string :as str]
            [com.utils.InputParsing :refer :all]))

(defn parse-connection [connection]
  (let [[c1 c2] (str/split connection  #"-")] [c1 c2]))

(defn parse-input [computers]
  (->>  computers
        (map parse-connection)))

(defn computers-to-connections [connections]
  (reduce (fn [lanparties? [c1 c2]]
            (let [c1-added (update-in lanparties? [c1] (fn [old-val] (set/union #{c2} old-val)))
                  c2-added (update-in c1-added [c2] (fn [old-val]  (set/union #{c1} old-val)))]
              c2-added))
          {} connections))

(defn find-intersect [mapped-connections computer network root-computers network-size]
  (let
   [connections (set/difference (mapped-connections computer) network)
    only-valid-if-all-connected (set/intersection connections root-computers)
    network* (conj network computer)]
    (cond
      (= network-size (count network*)) network*
      (empty? only-valid-if-all-connected) nil
      :else
      (->>
       only-valid-if-all-connected
       (map #(find-intersect mapped-connections % network* root-computers network-size))
       (remove nil?)
       (flatten)
       (distinct)))))

(defn find-connected-computers
  "should find solution"
  [data]
  (let [connections (parse-input data)
        computers-mapped-to-connections (computers-to-connections connections)]
    (->>
     computers-mapped-to-connections
     (filter (fn [[c _]] (= (first c) \t)))
     (map (fn [[c _]] c))
     (mapcat (fn [computer] (find-intersect computers-mapped-to-connections computer #{computer} (computers-mapped-to-connections computer) 3)))
     distinct
     count)))

(defn max-connected [mapped-connections computer network root-computers]
  (let
   [connections (set/difference (mapped-connections computer) network)
    only-valid-if-all-connected (set/intersection connections root-computers)
    network* (conj network computer)]
    (println "network " network*)
    (println "connections " connections)
    (println "only-valid-if-all-connected " only-valid-if-all-connected)
    (cond
      (empty? only-valid-if-all-connected) network*
      :else
      (->>
       only-valid-if-all-connected
       (map #(max-connected mapped-connections % network* only-valid-if-all-connected))
       (remove nil?)
       (flatten)
       (distinct)))))

(defn count-max-connected [all-network-map]
  (let [[common-parts _] (reduce (fn [[all-common-parts remaining-network-map] network-map]
                                   (let
                                    [any-common-parts (reduce (fn [common-parts to-compare-network-map]
                                                                (conj common-parts (set/intersection network-map to-compare-network-map)))
                                                              []
                                                              remaining-network-map)]

                                     [(concat all-common-parts  (remove empty? any-common-parts)) (rest remaining-network-map)]))

                                 [[] (rest all-network-map)]
                                 (drop-last 1 all-network-map))
        common-parts-groupped-by-count (map (fn [[grp-key values]] [grp-key (count values)]) (group-by identity (remove empty? common-parts)))]
    (first (last (sort-by (fn [[grp-key count]] count) common-parts-groupped-by-count)))))

(defn find-max-connected
  "should find solution"
  [data]
  (let [connections (parse-input data)
        computers-mapped-to-connections (computers-to-connections connections)
        all-network-map (map (fn [[computer connections]] (conj connections computer)) computers-mapped-to-connections)]
    (str/join  "," (sort (count-max-connected all-network-map)))))