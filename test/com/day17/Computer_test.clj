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
  (testing "Should perform adv"
    (is (= {:output nil :registers {:AX 1 :BX 0 :CX 0}} (adv 2 {:AX 4 :BX 0 :CX 0})))
    (is (= {:output nil :registers {:AX 1 :BX 0 :CX 0}} (adv 2 {:AX 6 :BX 0 :CX 0}))) ; handle non integers
    (is (= {:output nil :registers {:AX 1 :BX 3 :CX 0}} (adv 5 {:AX 8 :BX 3 :CX 0}))) ; use BX for combo operand
    )
)

;The bxl instruction (opcode 1) calculates the bitwise XOR of register B
; and the instruction's literal operand, then stores the result in register B.
(deftest should-perform-bxl
  (testing "Should perform bxl"
    (is (= {:output nil :registers {:AX 0 :BX 1 :CX 0}} (bxl 6 {:AX 0 :BX 7 :CX 0})))
    )
  )

;The bst instruction (opcode 2) calculates the value of its combo operand modulo 8
; (thereby keeping only its lowest 3 bits), then writes that value to the B register.
(deftest should-perform-bst
  (testing "Should perform bst"
    (is (= {:output nil :registers {:AX 0 :BX 3 :CX 0}} (bst 3 {:AX 0 :BX 0 :CX 0})))
    (is (= {:output nil :registers {:AX 255 :BX 7 :CX 0}} (bst 4 {:AX 255 :BX 0 :CX 0}))) ; use register
    )
  )

;The jnz instruction (opcode 3) does nothing if the A register is 0. However,
; if the A register is not zero, it jumps by setting the instruction pointer to the value of its literal operand;
; if this instruction jumps, the instruction pointer is not increased by 2 after this instruction.
(deftest should-perform-jnz
  (testing "Should perform jnz"
    (is (= {:output nil :registers {:AX 0 :BX 0 :CX 0}} (jnz 0 {:AX 0 :BX 0 :CX 0}))) ; does nothing with AX=0
    (is (= {:instruction-ptr 7 :output nil :registers {:AX 1 :BX 0 :CX 0}} (jnz 7 {:AX 1 :BX 0 :CX 0}))) ; sets instruction if AX!=0
    )
  )

;The bxc instruction (opcode 4) calculates the bitwise XOR of register B and register C,
; then stores the result in register B. (For legacy reasons, this instruction reads an operand but ignores it.)
(deftest should-perform-bxc
  (testing "Should perform bxc"
    (is (= {:output nil :registers {:AX 0 :BX 15 :CX 9}} (bxc 0 {:AX 0 :BX 6 :CX 9})))
    )
  )

;The out instruction (opcode 5) calculates the value of its combo operand modulo 8,
; then outputs that value. (If a program outputs multiple values, they are separated by commas.)
(deftest should-perform-out
  (testing "Should perform out"
    (is (= {:output 3 :registers {:AX 0 :BX 0 :CX 0}} (out 3 {:AX 0 :BX 0 :CX 0})))
    (is (= {:output 7 :registers {:AX 255 :BX 0 :CX 0}} (out 4 {:AX 255 :BX 0 :CX 0}))) ; use register
    )
  )

;The numerator is the value in the A register.
; The denominator is found by raising 2 to the power of the instruction's combo operand.
; (So, an operand of 2 would divide A by 4 (2^2); an operand of 5 would divide A by 2^B.)
; The result of the division operation is truncated to an integer and then written to the B register.
(deftest should-perform-bdv
  (testing "Should perform bdv"
    (is (= {:output nil :registers {:AX 4 :BX 1 :CX 0}} (bdv 2 {:AX 4 :BX 0 :CX 0})))
    (is (= {:output nil :registers {:AX 6 :BX 1 :CX 0}} (bdv 2 {:AX 6 :BX 0 :CX 0}))) ; handle non integers
    (is (= {:output nil :registers {:AX 8 :BX 1 :CX 0}} (bdv 5 {:AX 8 :BX 3 :CX 0}))) ; use BX for combo operand
    )
  )

; The cdv instruction (opcode 7) works exactly like the adv instruction except that the result is stored in the C register.
; (The numerator is still read from the A register.)
(deftest should-perform-cdv
  (testing "Should perform cdv"
    (is (= {:output nil :registers {:AX 4 :BX 0 :CX 1}} (cdv 2 {:AX 4 :BX 0 :CX 0})))
    (is (= {:output nil :registers {:AX 6 :BX 0 :CX 1}} (cdv 2 {:AX 6 :BX 0 :CX 0}))) ; handle non integers
    (is (= {:output nil :registers {:AX 8 :BX 3 :CX 1}} (cdv 5 {:AX 8 :BX 3 :CX 0}))) ; use BX for combo operand
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
