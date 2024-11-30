(ns com.App
  (:gen-class))

(defn greet
  "Formatting greeting message"
  [data]
  (str "Hello, " (or (:name data) "World") "!"))

(defn -main
  "I don't do a whole lot ... yet."
  [& args]
  (println (greet {:name (first args)})))