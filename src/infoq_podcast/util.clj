(ns infoq-podcast.util
  (:require [clojure.string :as string]))


;;; API

(defn interspaced
  "Repeatedly execute task-fn following a t ms sleep. If arg is given, pass arg
  to the initial execution and its result to subsequent executions."
  ([t task-fn]
     (future
       (loop []
         (task-fn)
         (Thread/sleep t)
         (recur))))
  ([t task-fn arg]
     (future
       (loop [arg' arg]
         (let [res (task-fn arg')]
           (Thread/sleep t)
           (recur res))))))

(defn seconds
  ([] (seconds 1))
  ([n] (* n 1000)))

(defn parse-int [s]
  (. Integer parseInt s))

(defn parse-date [s]
  (let [format (java.text.SimpleDateFormat. "MMM dd, yyyy")]
    (.parse format s)))

(defn interval->sec [s]
  (let [units (map parse-int (string/split s #":"))]
    (+ (* (first units) 60)
       (second units))))

(defn first-true [coll]
  (some identity coll))
