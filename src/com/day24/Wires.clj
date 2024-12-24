(ns com.day24.Wires
  (:require [com.utils.InputParsing :refer :all]
            [clojure.string :as str]))

(def operations {:AND (fn [x y] (and x y))
                 :OR (fn [x y] (or x y))
                 :XOR (fn [x y] (not= x y))})

(defn parse-wires-input [input-lines]
  (let [[wires-part _ gates-part] (partition-by str/blank? input-lines)
        initialised-wires (->> wires-part
                               (map #(str/split % #":\s"))
                               (map (fn [[name value]] [name (= value "1")]))
                               (into {}))
        gates (->>
               gates-part
               (map #(str/split % #"[\s]"))
               (map (fn [[w1 op w2 _ wr]] [wr {:op (operations (keyword op)) :w1 w1 :w2 w2}]))
               (into {}))
        all-wires (->>
                   (keys gates)
                   flatten
                   (map (fn [wire] [wire nil]))
                   (into {}))]

    {:wires (merge all-wires initialised-wires) :gates gates}))

(defn resolve-a-wire [wires-state gates wire-name]
  (let [{:keys [w1 w2 op]} (gates wire-name)
        w1 (if (nil? (@wires-state w1)) (resolve-a-wire wires-state gates w1) (@wires-state w1))
        w2 (if (nil? (@wires-state w2)) (resolve-a-wire wires-state gates w2) (@wires-state w2))
        result (op w1 w2)]
    (swap! wires-state assoc wire-name result)
    result))

(defn resolve-wires [wires gates]
  (def wires-state (atom wires))
  (let
   [zs-for-result (->>
                   wires
                   keys
                   (filter (fn [wire] (str/starts-with? wire "z")))
                   sort
                   reverse)
    result (reduce (fn [result z-to-resolve]
                     (+ (* result 2) (if (resolve-a-wire wires-state gates z-to-resolve) 1 0)))
                   0
                   zs-for-result)]
    result))

(defn calculate-wires-output
  "should find solution"
  [data]
  (let [{:keys [wires gates]} (parse-wires-input data)]
    (resolve-wires wires gates)))