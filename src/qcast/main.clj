(ns qcast.main
  (:gen-class)
  (:import [java.sql SQLException])
  (:require [qcast.cache   :as cache]
            [qcast.catcher :as catcher]
            [qcast.server  :as server]))


;;; Main

(defn -main []
  (try (cache/init) ;; Database might already exist
    (catch java.sql.SQLException _))
  (server/-main)
  (catcher/-main))
