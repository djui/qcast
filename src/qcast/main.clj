(ns qcast.main
  (:gen-class)
  (:import [java.sql SQLException])
  (:require [qcast.cache         :as cache]
            [qcast.infoq.scraper :as scraper]
            [qcast.server        :as server]))


;;; Main

(defn -main []
  (try (cache/init) ;; Database might already exist
    (catch SQLException _))
  (server/-main)
  (scraper/-main))
