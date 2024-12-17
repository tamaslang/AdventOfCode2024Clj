(ns com.day17.Computer
  (:require [com.utils.InputParsing :refer :all]))

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
  (println "operand " operand)
  (println "registers" "AX=" AX "BX=" BX "CX=" CX)
  (let [
        resolved-operand (resolve-combo-operand operand registers)
        result (int (/ AX (Math/pow resolved-operand 2)))
        registers* (assoc registers :AX result)
        ]
    {:output nil :registers registers*}
    )
  )

(defn execute-program
  "should find solution"
  [data]
  0)