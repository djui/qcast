(ns infoq-podcast.cache
  (:gen-class)
  (:require [infoq-podcast.db :as db]
            [taoensso.timbre  :as timbre :refer [trace debug info warn]]))


;;; Globals
(defonce ^:private db-spec (db/sqlite))


;;; Internals
(defn- pre-process [row]
  (-> row
      (update-in [:data] db/from-edn)
      (update-in [:date] db/from-inst)))

(defn- post-process [row]
  (-> row
      (update-in [:date] db/to-inst)
      (update-in [:data] db/to-edn)))


;;; API

(defn init []
  (info "Initializing cache")
  (db/ensure-table db-spec :presentations
                   [:id :TEXT "PRIMARY KEY" "NOT NULL"]
                   [:cdate :DATETIME "DEFAULT CURRENT_TIMESTAMP"]
                   ;;[:cdate :DATETIME "DEFAULT (datetime ('now','localtime'))"]
                   [:date :DATETIME "NOT NULL"]
                   [:data :BLOB "NOT NULL"]))
  
(defn put [item]
  (try ;; Protect against existing entries
    (->> {:id (:id item), :date (:date item), :data item}
         pre-process
         (db/insert db-spec :presentations))
    (catch java.sql.SQLException e
      (warn "Presentation already exists" item))))

(defn lookup [id]
  (->> (db/select' db-spec :presentations
                   (db/where {:id id}))
       (map post-process)))

(defn latest
  ([] (first (latest 1)))
  ([n]
     (->> (db/select' db-spec * :presentations
                      (db/order-by {:date :desc})
                      (db/limit n))
          (map post-process))))
