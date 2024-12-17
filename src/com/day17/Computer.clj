(ns com.day17.Computer
  (:require
   [com.utils.InputParsing :refer :all]
   [clojure.string :as str]))

(defn resolve-combo-operand [operand {:keys [AX BX CX] :as registers}]
  (cond
    (<= 0 operand 3) operand
    (= operand 4) AX
    (= operand 5) BX
    (= operand 6) CX
    (= operand 7) (throw (Exception. (str "operand 7 is reserved and will not appear in valid programs.")))
    :else (throw (Exception. (str "Unrecognised operand. Operand=" operand)))))

(defn adv [operand {:keys [AX BX CX] :as registers}]
  (let [resolved-operand (resolve-combo-operand operand registers)
        result (long (/ AX (Math/pow 2 resolved-operand)))
        registers* (assoc registers :AX result)]
    {:output nil :registers registers*}))

(defn bxl [operand {:keys [AX BX CX] :as registers}]
  (let [result (bit-xor BX operand)
        registers* (assoc registers :BX result)]
    {:output nil :registers registers*}))

(defn bst [operand {:keys [AX BX CX] :as registers}]
  (let [resolved-operand (resolve-combo-operand operand registers)
        result (mod resolved-operand 8)
        registers* (assoc registers :BX result)]
    {:output nil :registers registers*}))

(defn jnz [operand {:keys [AX BX CX] :as registers}]
  (if (zero? AX)
    {:output nil :registers registers}
    {:instruction-ptr operand :output nil :registers registers}))

(defn bxc [operand {:keys [AX BX CX] :as registers}]
  (let [result (bit-xor BX CX)
        registers* (assoc registers :BX result)]
    {:output nil :registers registers*}))

(defn out [operand {:keys [AX BX CX] :as registers}]
  (let [resolved-operand (resolve-combo-operand operand registers)
        result (mod resolved-operand 8)]
    {:output result :registers registers}))

(defn bdv [operand {:keys [AX BX CX] :as registers}]
  (let [resolved-operand (resolve-combo-operand operand registers)
        result (long (/ AX (Math/pow 2 resolved-operand)))
        registers* (assoc registers :BX result)]
    {:output nil :registers registers*}))

(defn cdv [operand {:keys [AX BX CX] :as registers}]
  (let [resolved-operand (resolve-combo-operand operand registers)
        result (long (/ AX (Math/pow 2 resolved-operand)))
        registers* (assoc registers :CX result)]
    {:output nil :registers registers*}))

(defn combined-ops [operand {:keys [AX BX CX] :as registers}]
  (let [AX* (long (/ AX (Math/pow 2 3)))
        BX* (bit-xor
             (mod AX 8)
             (long (/ AX (Math/pow 2 (- 7 (mod AX 8))))))
        CX* (long (/ AX (Math/pow 2 (- 7 (mod AX 8)))))
        output (mod BX* 8)]
    (println "AX=" AX " BX=" BX " CX=" CX)
    (println "AX*=" AX* " BX*=" BX* " CX*=" CX*)
    (println "output " (mod BX* 8))
    {:output output :registers {:AX AX* :BX BX* :CX CX*}}))

(defn combined-ops-v2 [AX]
  (let [AX* (long (/ AX (Math/pow 2 3)))
        BX* (bit-xor (mod AX 8) (long (/ AX (Math/pow 2 (- 7 (mod AX 8))))))]
    {:output (mod BX* 8) :AX AX*}))

;
;(let [AX 2037
;      BX 5
;      CX 254
;      AX* (int (/ AX (Math/pow 2 3)))
;      BX* (bit-xor
;            (mod AX 8)
;            (int (/ AX (Math/pow 2 (- 7 (mod AX 8)))))
;            )
;      CX* (int (/ AX (Math/pow 2 (- 7 (mod AX 8)))))
;      ]
;  (println "AX=" AX " BX=" BX " CX=" CX)
;  (println "AX*=" AX* " BX*=" BX* " CX*=" CX*)
;  (println "output " (mod BX* 8))
;  )

(def operation-set
  {0 ["ADV" adv]
   1 ["BXL" bxl]
   2 ["BST" bst]
   3 ["JNZ" jnz]
   4 ["BXC" bxc]
   5 ["OUT" out]
   6 ["BDV" bdv]
   7 ["CDV" cdv]})

(defn log-operation [operation-name operand registers output]
  (println (format "%3s %d      Registers: %-30s Output: %s" operation-name operand registers output)))

(defn log-program [instructions registers]
  (println "EXECUTE PROGRAM")
  (println  (format "Instructions: %s" (into [] instructions))))

(defn can-be-valid-output? [expected-output output]
  (= (take (count output) expected-output) output))

(defn execute [logging instructions {:keys [AX BX CX] :as registers}]
  (when logging (log-program instructions registers))
  (loop
   [instruction-ptr 0
    registers registers
    output []]
    (let [[opcode operand] (nth instructions instruction-ptr)
          [operation-name operation] (operation-set opcode)
          {jump-ptr :instruction-ptr output-value :output registers* :registers} (operation operand registers)
          instruction-ptr* (if jump-ptr (/ jump-ptr 2) (inc instruction-ptr))
          output* (if output-value (conj output output-value) output)]
      (when logging (log-operation operation-name operand registers* output*))
      (cond
        (< instruction-ptr* (count instructions)) (recur instruction-ptr* registers* output*)
        :else
        {:output output* :registers registers*}))))

(defn parse-instructions [instruction-str]
  (partition 2 2 (parse-numbers-in-line-separator #"," instruction-str)))

(defn parse-register-str [register-line]
  (let [[_ register-name register-value] (re-find #"Register ([A|B|C]): (\d+)" register-line)]
    (Integer/parseInt register-value)))

(defn parse-instruction-str [instruction-line]
  (let [[_ instructions-str] (re-find #"Program: (.+)" instruction-line)]
    (parse-instructions instructions-str)))

(defn compile-program [program]
  (let [[registers-str _ [instructions-str]] (partition-by str/blank? program)
        [AX BX CX] (map parse-register-str registers-str)]
    {:registers {:AX AX :BX BX :CX CX} :instructions (parse-instruction-str instructions-str)}))

(defn execute-program
  "should find solution"
  [data]
  (let [{:keys [instructions registers]} (compile-program data)
        {output :output registers* :registers} (execute true instructions registers)]
    (str/join "," output)))

(defn combined-ops [AX]
  (let [AX* (long (/ AX (Math/pow 2 3)))
        BX* (bit-xor (mod AX 8) (long (/ AX (Math/pow 2 (- 7 (mod AX 8))))))]
    {:output (mod BX* 8) :AX AX*}))

; PART 2 brute force
(defn execute-combined [START is-valid-output?]
  (loop
   [AX START
    output []]
    (let [{output-value :output AX* :AX} (combined-ops-v2 AX)
          output* (conj output output-value)]
      (cond
        (not (is-valid-output? output*)) output* ; terminate early
        (zero? AX*) output*
        :else (recur AX* output*)))))

(defn find-AX-register-value-for-expected-n-instructions
  "should find solution"
  [instruction-count data]
  (let [{:keys [instructions _]} (compile-program data)
        expected-instructions (take-last instruction-count (flatten instructions))
        output-validator (partial can-be-valid-output? expected-instructions)]
    (println (format "Finding AX for last %d instructions. Instructions=%s" instruction-count expected-instructions))
    (reduce (fn [_ AX]
              (let [output (execute-combined AX output-validator)]
                (when (zero? (mod AX 100000)) (println "COUNT: " AX))
                (when (= output expected-instructions) (reduced AX)))) 0 (range))))

(defn find-register-value-for-output-copy-of-program
  "should find solution"
  [start-AX data]
  (let [{:keys [instructions registers]} (compile-program data)
        expected-instructions (flatten instructions)]
    (loop
      [AX start-AX]
      (let [{output* :output} (execute false instructions (assoc registers :AX AX))
            output-matches-from-end (= (take-last (count output*) expected-instructions) output*)
            AX* (if output-matches-from-end (* AX 8) (inc AX))
            ]
        (cond
          (= output* expected-instructions) AX
          :else (recur AX*)
          )
        )
      )
    )
)

