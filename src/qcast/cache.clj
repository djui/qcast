(ns qcast.cache
  (:gen-class)
  (:require [qcast.db        :as db]
            [taoensso.timbre :as timbre :refer :all]))


;;; Globals
(defonce ^:private db-spec (db/sqlite))


;;; Internals
(defn- pre-process [row]
  (-> row
      (update-in [:data] db/from-edn)
      (update-in [:publish_date] db/from-inst)))

(defn- post-process [row]
  (-> row
      (update-in [:publish_date] db/to-inst)
      (update-in [:data] db/to-edn)))


;;; API

(defn init []
  (info "Initializing cache")
  (db/create-table db-spec :presentations
                   [:id :TEXT "PRIMARY KEY" "NOT NULL"]
                   [:creation_date :DATETIME "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"]
                   [:publish_date :DATETIME "NOT NULL"]
                   [:data :BLOB "NOT NULL"]))

(defn put [item]
  (try ;; Protect against existing entries
    (->> {:id (:id item), :publish_date (:publish-date item), :data item}
         pre-process
         (db/insert db-spec :presentations))
    (catch java.sql.SQLException e
      (warn "Presentation already exists" item))))

(defn lookup [id]
  (map post-process (db/select' db-spec :presentations (db/where {:id id}))))

(defn latest
  ([] (first (latest 1)))
  ([n]
    (map post-process (db/select' db-spec * :presentations
                                  (db/order-by {:publish_date :desc})
                                  (db/limit n)))))
