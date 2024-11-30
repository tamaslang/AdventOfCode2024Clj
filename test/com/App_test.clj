(ns com.App-test
  (:require [clojure.test :refer :all]
            [com.App :refer :all]))

(deftest app-main-test
  (testing "Greeting should use name passed as parameter."
    (is (= "Hello, Mr Clojure!" (greet {:name "Mr Clojure"}))))
  (testing "Greeting should use default in case no parameter passed"
    (is (= "Hello, World!" (greet {})))))
