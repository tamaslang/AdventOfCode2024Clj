(ns com.day22.MonkeyBusiness-test
  (:require [clojure.test :refer :all]
            [com.day22.MonkeyBusiness :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def data-file (io/resource
                "resources/day22/input.txt"))

(deftest should-calculate-nth-secret-for-number-for-example-1
  (testing "Should calculate nth secret for example 1"
    (is (= 15887950 (nth-secret 1 123)))
    (is (= 16495136 (nth-secret 2 123)))
    (is (= 527345 (nth-secret 3 123)))
    (is (= 704524 (nth-secret 4 123)))
    (is (= 1553684 (nth-secret 5 123)))
    (is (= 12683156 (nth-secret 6 123)))
    (is (= 11100544 (nth-secret 7 123)))
    (is (= 12249484 (nth-secret 8 123)))
    (is (= 7753432 (nth-secret 9 123)))
    (is (= 5908254 (nth-secret 10 123)))))

(deftest should-calculate-nth-secret-for-number-for-example-2
  (testing "Should calculate nth secret for example 2"
    (is (= 8685429 (nth-secret 2000 1)))
    (is (= 4700978 (nth-secret 2000 10)))
    (is (= 15273692 (nth-secret 2000 100)))
    (is (= 8667524 (nth-secret 2000 2024)))))

(deftest should-add-up-generated-numbers-in-example
  (testing "Should add up generated numbers in example"
    (is (= 37327623 (nth-secret-number 2000 ["1"
                                             "10"
                                             "100"
                                             "2024"])))))

(deftest should-add-up-generated-numbers-in-input-file
  (testing "Should add up generated numbers in input file"
    (is (= 13022553808 (nth-secret-number 2000 (str/split-lines (slurp data-file)))))))

(deftest should-get-best-price-for-example-1
  (testing "Should get best price for example-1"
    (is (= 6 (best-price 10 ["123"])))))

(deftest should-get-best-price-for-example-2
  (testing "Should get best price for example-2"
    (is (= 23 (best-price 2000 ["1"
                                "2"
                                "3"
                                "2024"])))))

(deftest should-get-best-price-for-input-file
  (testing "Should get best price for input file"
    (is (= 1605 (best-price 2000 (str/split-lines (slurp data-file)))))))
