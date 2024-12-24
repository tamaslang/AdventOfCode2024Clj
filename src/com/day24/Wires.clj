(ns com.day24.Wires
  (:require [com.utils.InputParsing :refer :all]
            [clojure.string :as str]))

;"x00: 1"
;"x01: 1"
;"x02: 1"
;"y00: 0"
;"y01: 1"
;"y02: 0"
;""
;"x00 AND y00 -> z00"
;"x01 XOR y01 -> z01"
;"x02 OR y02 -> z02"
(def operations {
                  :AND (fn[x y] (and x y))
                  :OR (fn[x y] (or x y))
                  :XOR (fn[x y] (not= x y))
                  }
  )
(defn parse-wires-input [input-lines]
  (let [[wires-part _ gates-part] (partition-by str/blank? input-lines)
        initialised-wires (->> wires-part
                    (map #(str/split % #":\s"))
                    (map (fn[[name value]] [name (= value "1")] ))
                    (into {})
                    )
        gates (->>
                gates-part
                (map #(str/split % #"[\s]"))
                (map (fn[[w1 op w2 _ wr]] [wr {:op (operations (keyword op)) :w1 w1 :w2 w2}] ))
                (into {})
              )
        all-wires (->>
                    (keys gates)
                    flatten
                    (map (fn[wire] [wire nil]))
                    (into {})
                    )

        ;(into {} (map (fn[wire] [wire nil]) (flatten (keys gates))))
        ]
    {:wires (merge all-wires initialised-wires) :gates gates}
    )
  )

(defn resolve-a-wire [wires gates wire-name]
  (println "RESOLVE " wire-name)

  [wires 1]
)

(defn resolve-wires[wires gates]
  (let
    [zs-for-result (->>
                         wires
                         keys
                         (filter (fn[wire] (str/starts-with? wire "z")))
                         sort
                         reverse
                         )]
    (reduce (fn [[updated-wires result] z-to-resolve]
              (println)
              (println "? " updated-wires)
              (println "? " result)
              (let
                [[updated-wires* result-for-wire] (resolve-a-wire updated-wires gates z-to-resolve)
                 result* (+ (* result 2) result-for-wire)
                 ]
                [updated-wires* result*]
                )
              )
              [wires 0]
               zs-for-result)
    )


  ;(reduce [resolved-wires])
  ;(loop
  ;   [wires wires]
  ;  (let
  ;    [to-resolve (first (filter (fn [[[w1 w2] _]]
  ;                        (println "w1 w2" w1 w2)
  ;                        (= 1 (count (remove nil? [(wires w1) (wires w2)])))) gates))]
  ;    (println "TO RESOLVE " to-resolve)
  ;    )
  ;   ;
  ;   ; find wire with nil for which an operation exists where the other wire is not nil
  ;   ; ntg XOR fgs -> mjb
  ;   ;
  ;   ;
  ;  )
  )

(defn calculate-wires-output
  "should find solution"
  [data]
  (let [{:keys [wires gates]} (parse-wires-input data)
        [updated-wires result] (resolve-wires wires gates)
        ]
    result
    )
  )