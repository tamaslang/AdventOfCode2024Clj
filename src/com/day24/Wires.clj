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

(defn find-all-endwire [wires]
  (->>
   wires
   keys
   (filter (fn [wire] (str/starts-with? wire "z")))
   sort
   reverse))

(defn resolve-wires [wires-state gates]
  (reduce (fn [result z-to-resolve] (+ (* result 2) (if (resolve-a-wire wires-state gates z-to-resolve) 1 0))) 0 (find-all-endwire @wires-state)))

(defn calculate-wires-output
  "should find solution"
  [data]
  (let [{:keys [wires gates]} (parse-wires-input data)]
    (resolve-wires (atom wires) gates)))

; PART 2 ------------------
(defn parse-gates [input-lines]
  (let [[_ _ gates-part] (partition-by str/blank? input-lines)
        gates (->>
               gates-part
               (map #(str/split % #"[\s]"))
               (map (fn [[in1 op in2 _ out]] {:op op :in1 in1 :in2 in2 :out out})))]

    gates))

(defn next-gate [gates wire]
  (->>
   gates
   (filter (fn [{:keys [op in1 in2 out]}] (or (= in1 wire) (= in2 wire))))
   (map #(:op %))
   set))

(defn find-sus-gates [gates]
  (println "ORIG " gates)
  (->>
   gates
   (map (fn [{:keys [op in1 in2 out]}] {:op op :in1 in1 :in2 in2 :out out :out-gate (next-gate gates out)}))
   (filter (fn [{:keys [op in1 in2 out out-gate]}] (or
                                                    (and (not= op "XOR") (and (str/starts-with? out "z") (not (= out "z45")))) ; apart from last (overflow bit) XOR goes to Z
                                                    (and (= op "XOR") (not= #{\x \y} (set [(first in1) (first in2)])) (not (str/starts-with? out "z"))) ; XOR gate input is x y or output is z
                                                    (and (= op "XOR") (not (empty? out-gate)) (not (out-gate "XOR"))) ; XOR gate goes to XOR gate
                                                    (and (= op "AND") (not (empty? out-gate)) (not (out-gate "OR"))) ;AND gate goes to OR gate
                                                    )))
   (remove (fn [{:keys [op in1 in2 out out-gate]}] (#{in1 in2} "x00"))))) ; x00 handled separately as it is in a half adder

(defn detect-wrong-gates
  "should find solution"
  [data]
  (let [gates (parse-gates data)
        sus-gates (find-sus-gates gates)]
    (->> sus-gates
         (map (fn [{:keys [out]}] out))
         flatten
         sort
         (str/join ","))))