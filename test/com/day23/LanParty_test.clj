(ns com.day23.LanParty-test
  (:require [clojure.test :refer :all]
            [com.day23.LanParty :refer :all]
            [clojure.string :as str]
            [clojure.java.io :as io]))

(def data-file (io/resource
                "resources/day23/input.txt"))

(deftest should-solve-in-example
  (testing "Should solve"
    (is (= 7 (find-connected-computers ["kh-tc"
                                        "qp-kh"
                                        "de-cg"
                                        "ka-co"
                                        "yn-aq"
                                        "qp-ub"
                                        "cg-tb"
                                        "vc-aq"
                                        "tb-ka"
                                        "wh-tc"
                                        "yn-cg"
                                        "kh-ub"
                                        "ta-co"
                                        "de-co"
                                        "tc-td"
                                        "tb-wq"
                                        "wh-td"
                                        "ta-ka"
                                        "td-qp"
                                        "aq-cg"
                                        "wq-ub"
                                        "ub-vc"
                                        "de-ta"
                                        "wq-aq"
                                        "wq-vc"
                                        "wh-yn"
                                        "ka-de"
                                        "kh-ta"
                                        "co-tc"
                                        "wh-qp"
                                        "tb-vc"
                                        "td-yn"])))))

(deftest should-solve-for-input-file
  (testing "Should solve for input file"
    (is (= 1054 (find-connected-computers (str/split-lines (slurp data-file)))))))

(deftest should-find-max-connected-in-example
  (testing "Should find max connected in example"
    (is (= "co,de,ka,ta" (find-max-connected ["kh-tc"
                                              "qp-kh"
                                              "de-cg"
                                              "ka-co"
                                              "yn-aq"
                                              "qp-ub"
                                              "cg-tb"
                                              "vc-aq"
                                              "tb-ka"
                                              "wh-tc"
                                              "yn-cg"
                                              "kh-ub"
                                              "ta-co"
                                              "de-co"
                                              "tc-td"
                                              "tb-wq"
                                              "wh-td"
                                              "ta-ka"
                                              "td-qp"
                                              "aq-cg"
                                              "wq-ub"
                                              "ub-vc"
                                              "de-ta"
                                              "wq-aq"
                                              "wq-vc"
                                              "wh-yn"
                                              "ka-de"
                                              "kh-ta"
                                              "co-tc"
                                              "wh-qp"
                                              "tb-vc"
                                              "td-yn"])))))

(deftest should-find-max-connected-in-input-file
  (testing "Should find max connected in input-file"
    (is (= "ch,cz,di,gb,ht,ku,lu,tw,vf,vt,wo,xz,zk" (find-max-connected (str/split-lines (slurp data-file)))))))