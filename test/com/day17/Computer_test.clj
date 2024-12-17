(ns com.day17.Computer-test
  (:require [clojure.test :refer :all]
            [com.day17.Computer :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def data-file (io/resource
                 "resources/day17/input.txt"))

;The adv instruction (opcode 0) performs division. The numerator is the value in the A register.
; The denominator is found by raising 2 to the power of the instruction's combo operand.
; (So, an operand of 2 would divide A by 4 (2^2); an operand of 5 would divide A by 2^B.)
; The result of the division operation is truncated to an integer and then written to the A register.
(deftest should-perform-adv
  (testing "Should perform adv, divide by 4"
    (is (= {:output nil :registers{:AX 1 :BX 2 :CX 3}} (adv 2 {:AX 4 :BX 2 :CX 3})))
    (is (= {:output nil :registers{:AX 1 :BX 2 :CX 3}} (adv 2 {:AX 6 :BX 2 :CX 3}))) ; handle non integers
    (is (= {:output nil :registers{:AX 1 :BX 2 :CX 3}} (adv 5 {:AX 4 :BX 2 :CX 3}))) ; use BX for combo operand
    )
)

;The bst instruction (opcode 2) calculates the value of its combo operand modulo 8
;(thereby keeping only its lowest 3 bits), then writes that value to the B register.
(deftest should-perform-bst
  (testing "Should perform bst"
    ;(is (= {:output nil :registers{:AX 1 :BX 2 :CX 3}} (adv 2 {:AX 4 :BX 2 :CX 3})))
    ;(is (= {:output nil :registers{:AX 1 :BX 2 :CX 3}} (adv 2 {:AX 6 :BX 2 :CX 3})))
    )
  )

; The bxc instruction (opcode 4) calculates the bitwise XOR of register B and register C,
; then stores the result in register B.
; (For legacy reasons, this instruction reads an operand but ignores it.)
(deftest should-perform-bdv
  (testing "Should perform bdv"
    ;(is (= {:output nil :registers{:AX 1 :BX 2 :CX 3}} (adv 2 {:AX 4 :BX 2 :CX 3})))
    ;(is (= {:output nil :registers{:AX 1 :BX 2 :CX 3}} (adv 2 {:AX 6 :BX 2 :CX 3})))
    )
  )

(deftest should-execute-program-in-example
  (testing "Should execute program in example"
    (is (= 0 (execute-program [
                     "Register A: 729"
                     "Register B: 0"
                     "Register C: 0"
                     ""
                     "Program: 0,1,5,4,3,0"])))))


(deftest should-execute-program-input-file
  (testing "Should execute program for input file"
    (is (= 0 (execute-program (str/split-lines (slurp data-file)))))))
