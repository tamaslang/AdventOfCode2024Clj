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
        result (int (/ AX (Math/pow 2 resolved-operand)))
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
        result (int (/ AX (Math/pow 2 resolved-operand)))
        registers* (assoc registers :BX result)]
    {:output nil :registers registers*}))

(defn cdv [operand {:keys [AX BX CX] :as registers}]
  (let [resolved-operand (resolve-combo-operand operand registers)
        result (int (/ AX (Math/pow 2 resolved-operand)))
        registers* (assoc registers :CX result)]
    {:output nil :registers registers*}))

(def operation-set
  {0 ["ADV" adv]
   1 ["BXL" bxl]
   2 ["BST" bst]
   3 ["JNZ" jnz]
   4 ["BXC" bxc]
   5 ["OUT" out]
   6 ["BDV" bdv]
   7 ["CDV" cdv]})

(defn log-operation [operation-name opcode registers output]
  (println (format "%3s %d      Registers: %-30s Output: %s" operation-name opcode registers output))
)

(defn log-program [registers]
  (println "EXECUTE PROGRAM")
  (println  (format "           Registers: %-30s" registers))
  )

(defn execute [logging instructions {:keys [AX BX CX] :as registers} ]
  (when logging (log-program registers))
  (loop
   [instruction-ptr 0
    registers registers
    output []]
    (let [[opcode operand] (nth instructions instruction-ptr)
          [operation-name operation] (operation-set opcode)
          {jump-ptr :instruction-ptr output-value :output registers* :registers} (operation operand registers)
          instruction-ptr* (if jump-ptr (/ jump-ptr 2) (inc instruction-ptr))
          output* (if output-value (conj output output-value) output)]
      (when logging (log-operation operation-name opcode registers* output*))
      (if (< instruction-ptr* (count instructions))
        (recur instruction-ptr* registers* output*)
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
        {output :output registers* :registers} (execute true instructions registers )]
    (str/join "," output)))

; PART 2 brute force
(defn find-AX-register-value-for-program-to-output-itself-brute-force
  "should find solution"
  [upper-limit data]
  (let [{:keys [instructions registers]} (compile-program data)
        expected-instructions  (flatten instructions)]
    (reduce (fn [_ AX]
              (let [{output :output} (execute false instructions (assoc registers :AX AX))]
                (when (zero? (mod AX 100000)) (println "COUNT: " AX))
                (when (= output expected-instructions) (reduced AX)))) 0 (range 0 upper-limit))))

