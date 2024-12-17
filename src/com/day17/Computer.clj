(ns com.day17.Computer
  (:require
    [com.utils.InputParsing :refer :all]
    [clojure.string :as str]
            ))

;(defn compile-program [program]
;  (let [[registers _ instructions] (partition-by str/blank? program)
;        [AX BX CX] (map )]
;
;    )
;)

(defn resolve-combo-operand [operand {:keys [AX BX CX] :as registers}]
  (cond
    (<= 0 operand 3) operand
    (= operand 4) AX
    (= operand 5) BX
    (= operand 6) CX
    (= operand 7) (throw (Exception. (str "operand 7 is reserved and will not appear in valid programs.")))
    :else (throw (Exception. (str "Unrecognised operand. Operand=" operand)))
    )
  )

(defn adv [operand {:keys [AX BX CX] :as registers}]
  (let [
        resolved-operand (resolve-combo-operand operand registers)
        result (int (/ AX (Math/pow 2 resolved-operand)))
        registers* (assoc registers :AX result)
        ]
    {:output nil :registers registers*}
    )
  )

(defn bxl [operand {:keys [AX BX CX] :as registers}]
  (let [
        result (bit-xor BX operand)
        registers* (assoc registers :BX result)
        ]
    {:output nil :registers registers*}
    )
  )

(defn bst [operand {:keys [AX BX CX] :as registers}]
  (let [
        resolved-operand (resolve-combo-operand operand registers)
        result (mod resolved-operand 8)
        registers* (assoc registers :BX result)
        ]
    {:output nil :registers registers*}
    )
  )

(defn jnz [operand {:keys [AX BX CX] :as registers}]
  (if (zero? AX)
    {:output nil :registers registers}
    {:instruction-ptr operand :output nil :registers registers}
    )
  )


(defn bxc [operand {:keys [AX BX CX] :as registers}]
  (let [
        result (bit-xor BX CX)
        registers* (assoc registers :BX result)
        ]
    {:output nil :registers registers*}
    )
  )

(defn out [operand {:keys [AX BX CX] :as registers}]
  (let [
        resolved-operand (resolve-combo-operand operand registers)
        result (mod resolved-operand 8)
        ]
    {:output result :registers registers}
    )
  )

(defn bdv [operand {:keys [AX BX CX] :as registers}]
  (let [
        resolved-operand (resolve-combo-operand operand registers)
        result (int (/ AX (Math/pow 2 resolved-operand)))
        registers* (assoc registers :BX result)
        ]
    {:output nil :registers registers*}
    )
  )

(defn cdv [operand {:keys [AX BX CX] :as registers}]
  (let [
        resolved-operand (resolve-combo-operand operand registers)
        result (int (/ AX (Math/pow 2 resolved-operand)))
        registers* (assoc registers :CX result)
        ]
    {:output nil :registers registers*}
    )
  )

(def operation-set
  {
   0 adv
   1 bxl
   2 bst
   3 jnz
   4 bxc
   5 out
   6 bdv
   7 cdv
   }
  )

(defn execute [instructions {:keys [AX BX CX] :as registers}]
  (println "EXECUTE " instructions)
  (println "REGISTERS " registers)
  (loop
    [instruction-ptr 0
     registers registers
     output []
     ]

    (let [
          [opcode operand] (nth instructions instruction-ptr)
          operation (operation-set opcode)
          {jump-ptr :instruction-ptr output-value :output registers* :registers} (operation operand registers)
          instruction-ptr* (if jump-ptr (/ jump-ptr 2) (inc instruction-ptr))
          output* (if output-value (conj output output-value) output)
          ]
        (if (< instruction-ptr* (count instructions))
          (recur instruction-ptr* registers* output*)
          {:output output* :registers registers*}
         )
      )
    )
  )

(defn parse-instructions [instruction-str]
  (partition 2 2 (parse-numbers-in-line-separator #"," instruction-str))
)

(defn execute-program
  "should find solution"
  [data]
  0)