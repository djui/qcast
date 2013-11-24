(ns qcast.util
  (:require [clojure.string :refer [split]]))


;;; Interface

;; Core

(defn parse-int [s]
  (when (string? s)
    (Long/parseLong s)))


;; Time

(defn interspaced
  "Repeatedly execute task-fn following a t ms sleep. If arg is given, pass arg
  to the initial execution and its result to subsequent executions. This is
  usually best wrapped in a Future."
  ([t task-fn]
     (loop []
       (task-fn)
       (Thread/sleep t)
       (recur)))
  ([t task-fn arg]
     (loop [_arg arg]
       (let [res (task-fn _arg)]
         (Thread/sleep t)
         (recur res)))))

(defn seconds
  ([] (seconds 1))
  ([n] (* n 1000)))

(defn minutes
  ([] (minutes 1))
  ([n] (* n (seconds 60))))

(defn hours
  ([] (hours 1))
  ([n] (* n (minutes 60))))

(defn days
  ([] (days 1))
  ([n] (* n (hours 24))))

(defn interval->sec [s]
  (->> (split s #":")
       (map parse-int)
       reverse
       (map * (iterate (partial * 60) 1))
       (reduce +)))
