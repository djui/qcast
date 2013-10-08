(ns infoq-podcast.main
  (:gen-class)
  (:import [java.sql SQLException])
  (:require [infoq-podcast.cache   :as cache]
            [infoq-podcast.catcher :as catcher]
            [infoq-podcast.server  :as server]))


;;; Main

(defn -main []
  (try (cache/init)
    (catch java.sql.SQLException _))
  (server/-main)
  (catcher/-main))
