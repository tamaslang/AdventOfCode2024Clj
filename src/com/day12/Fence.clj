(ns com.day12.Fence
  (:require [clojure.set :as set]
            [com.utils.InputParsing :refer :all]))

(defn adjacents [matrix [x y]]
  (keep identity [;above
                  (matrix->get-char-at-xy matrix [x (dec y)])
                  ; same row
                  (matrix->get-char-at-xy matrix [(dec x) y])
                  (matrix->get-char-at-xy matrix [(inc x) y])
                  ; row below
                  (matrix->get-char-at-xy matrix [x (inc y)])]))

(defn find-a-plot-from [matrix pos]
  (reduce (fn [[found next-positions]  iteration]
            (let
             [adjacents (mapcat #(adjacents matrix %) next-positions)
              adjacents-same-plot (filter (fn [[id _]] (= id (matrix->get-xy matrix pos))) adjacents)
              next-positions*  (set/difference (set (map second adjacents-same-plot)) found)
              found* (set/union found next-positions*)]
              (if (empty? next-positions*)
                (reduced {:id (matrix->get-xy matrix pos) :area found})
                [found* next-positions*])))

          [#{pos} #{pos}] (range (* (count matrix) (count matrix)))))

(defn find-all-plots [matrix]
  (let [all-locations (matrix->all-locations matrix)]
    (reduce (fn [[not-visited-locations mapped-plots] iteration]
              (if (empty? not-visited-locations)
                (reduced mapped-plots)
                (let [next-location-to-visit (first not-visited-locations)
                      a-plot (find-a-plot-from matrix next-location-to-visit)
                      not-visited-locations* (set/difference not-visited-locations (:area a-plot))
                      mapped-plots* (conj mapped-plots a-plot)]
                  [not-visited-locations* mapped-plots*])))

            [all-locations []]
            (range (* (count matrix) (count matrix))))))

(defn calculate-fence-needed [matrix plot-id plot-area]
  (->>
   plot-area
   (map (fn [position] (- 4 (count (filter #(= (first %) plot-id) (adjacents matrix position))))))
   (reduce +)))

(defn fencing-price
  "should find fencing price"
  [data]
  (let [garden (create-matrix data)
        plots (find-all-plots garden)
        plots-mapped-size (map (fn [plot] {:id (:id plot) :area-size (count (:area plot)) :fence-needed (calculate-fence-needed garden (:id plot) (:area plot))}) plots)]
    (->>
     plots-mapped-size
     (map (fn [plot] (* (:area-size plot) (:fence-needed plot))))
     (reduce +))))

; TASK 2
(defn count-corners-at-pos [matrix id [x y]]
  (reduce + [; ┌ ( Upper left corner )
             (if (and
                  (not= id (matrix->get-xy matrix [x (dec y)]))
                  (not= id (matrix->get-xy matrix [(dec x) y]))) 1 0)
   ; ┐ (Upper right corner)
             (if (and
                  (not= id (matrix->get-xy matrix [x (dec y)]))
                  (not= id (matrix->get-xy matrix [(inc x) y]))) 1 0)
   ; ┘ ( Lower right corner)
             (if (and
                  (not= id (matrix->get-xy matrix [(inc x) y]))
                  (not= id (matrix->get-xy matrix [x (inc y)]))) 1 0)

   ; └  (Lower left corner)
             (if (and
                  (not= id (matrix->get-xy matrix [x (inc y)]))
                  (not= id (matrix->get-xy matrix [(dec x) y]))) 1 0)

  ; ┌ ( Upper left corner INSIDE)
             (if (and
                  (= id (matrix->get-xy matrix [x (inc y)]))
                  (= id (matrix->get-xy matrix [(inc x) y]))
                  (not= id (matrix->get-xy matrix [(inc x) (inc y)]))) 1 0)
  ; ┐ (Upper right corner INSIDE)
             (if (and
                  (= id (matrix->get-xy matrix [(dec x) y]))
                  (= id (matrix->get-xy matrix [x (inc y)]))
                  (not= id (matrix->get-xy matrix [(dec x) (inc y)]))) 1 0)
  ; ┘ ( Lower right corner INSIDE)
             (if (and
                  (= id (matrix->get-xy matrix [x (dec y)]))
                  (= id (matrix->get-xy matrix [(dec x) y]))
                  (not= id (matrix->get-xy matrix [(dec x) (dec y)]))) 1 0)
  ; └  (Lower left corner INSIDE)
             (if (and
                  (= id (matrix->get-xy matrix [x (dec y)]))
                  (= id (matrix->get-xy matrix [(inc x) y]))
                  (not= id (matrix->get-xy matrix [(inc x) (dec y)]))) 1 0)]))

(defn count-all-corners-for-areas [matrix id areas]
  (->>
   areas
   (map #(count-corners-at-pos matrix id %))
   (reduce +)))

(defn fencing-price-bulk
  "should find fencing price when bulk buying"
  [data]
  (let [garden (create-matrix data)
        plots (find-all-plots garden)
        plots-mapped-size (map (fn [plot] {:id (:id plot) :area-size (count (:area plot)) :fence-needed (count-all-corners-for-areas garden (:id plot) (:area plot))}) plots)]
    (->>
     plots-mapped-size
     (map (fn [plot] (* (:area-size plot) (:fence-needed plot))))
     (reduce +))))