(ns infoq-podcast.cache
  (:require [infoq-podcast.db :as db]
            [taoensso.timbre  :as timbre :refer [trace debug info]]))


;;; Globals
(defonce ^:private db-spec (db/sqlite))


;;; Internals
(defn- pre-process [row]
  (-> row
      (update-in [:data] db/from-edn)
      (update-in [:cdate] db/from-inst)))

(defn- post-process [row]
  (-> row
      (update-in [:data] db/to-edn)
      (update-in [:cdate] db/to-inst)))


;;; API

(defn init []
  (db/ensure-table db-spec :presentation
                   [:id :TEXT "NOT NULL" "PRIMARY KEY"]
                   [:cdate :DATETIME "NOT NULL"]
                   [:data :BLOB "NOT NULL"]))
  
(defn put [id date data]
  (try ;; Protect against existing entries
    (->> {:id id, :cdate date, :data data}
         pre-process
         (db/insert db-spec :presentation))
    (catch java.sql.SQLException e)))

(defn lookup [id]
  (->> (db/select' db-spec :presentation
                   (db/where {:id id}))
       (map post-process)))

(defn latest []
  (->> (db/select' db-spec * :presentation
                   (db/order-by {:cdate :desc})
                   (db/limit 1))
       (map post-process)
       first :cdate))

