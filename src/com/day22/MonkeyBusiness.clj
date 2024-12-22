(ns com.day22.MonkeyBusiness
  (:require [com.utils.InputParsing :refer :all]))

(def mul_64 (fn [number] (* number 64)))
(def div_32 (fn [number] (int (/ number 32))))
(def mul_2048 (fn [number] (* number 2048)))
(def prune (fn [number] (mod number 16777216)))

(defn do-step [number]
  (->>
   [number]
   (map #(bit-xor % (mul_64 %)))
   (map prune)
   (map #(bit-xor % (div_32 %)))
   (map prune)
   (map #(bit-xor % (mul_2048 %)))
   (map prune)
   (first)))

(defn nth-secret [n number]
  (reduce (fn [acc _] (do-step acc)) number (range 0 n)))

(defn nth-secret-number
  "should add up nth secret number"
  [n data]
  (->>
   data
   (map Integer/parseInt)
   (map #(nth-secret n %))
   (reduce +)))

(def first-step (* (Math/pow 19 3)))
(def second-step (* (Math/pow 19 2)))
(def third-step (* 19))

(defn sequence-to-fours [number]
  (map
   #(- % 9)
   [(int (/ number first-step))
    (int (/ (mod number first-step) second-step))
    (int (/ (mod number second-step) third-step))
    (int (mod number 19))]))

(defn sequence-from-fours [first second third fourth]
  (int (+
        (* (+ first 9) first-step)
        (* (+ second 9) second-step)
        (* (+ third 9) third-step)
        (+ fourth 9))))

(defn add-new-last-step [number new-last-step]
  (int (+ 9 new-last-step (* 19 (mod number first-step)))))

(defn nth-secret-with-best-prices-map [n number]
  (let
   [[_ _ price-map] (reduce (fn [[number last-4-changes price-map] step]
                              (let
                               [new-number (do-step number)
                                last-price (mod number 10)
                                new-price (mod new-number 10)
                                price-change (- new-price last-price)
                                last-4-changes* (add-new-last-step last-4-changes price-change)
                                price-map* (if (and (>= step 3) (nil? (price-map last-4-changes*)))
                                             (assoc price-map last-4-changes* new-price)
                                             price-map)]

                                [new-number last-4-changes* price-map*]))
                            [number 0 {}]
                            (range 0 n))]
    price-map))

(defn best-price
  "should get best price"
  [n data]
  (let
   [best-sequences-merged (->>
                           data
                           (map Integer/parseInt)
                           (map #(nth-secret-with-best-prices-map n %))
                           (apply merge-with +))

    [best-key best-price] (last (sort-by second (into {} best-sequences-merged)))]
    (println "Best price sequence " (sequence-to-fours best-key) " = " best-price)
    best-price))
