(ns com.day3.CorruptMemory-test
  (:require [clojure.test :refer :all]
            [com.day3.CorruptMemory :refer :all]
            [clojure.java.io :as io]))

(def data-file (io/resource
                "resources/day3/input.txt"))

(deftest should-find-instructions-in-example
  (testing "Should find instructions"
    (is (= 161 (find-instructions "xmul(2,4)%&mul[3,7]!@^do_not_mul(5,5)+mul(32,64]then(mul(11,8)mul(8,5))")))))

(deftest should-find-instructions-in-input-file
  (testing "Should find instructions for input file"
    (is (= 164730528 (find-instructions (slurp data-file))))))

(deftest should-find-instructions-with-do-and-dont-in-example
  (testing "Should find instructions with do's and don'ts"
    (is (= 48 (find-instructions-task2 "xmul(2,4)&mul[3,7]!^don't()_mul(5,5)+mul(32,64](mul(11,8)undo()?mul(8,5))")))))

(deftest should-find-instructions-with-do-and-dont-in-input-file
  (testing "Should find instructions with do's and don'ts for input file"
    (is (= 70478672 (find-instructions-task2 (slurp data-file))))))
