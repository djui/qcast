;; TODO: Replace everything SQL with HoneySQL
(ns qcast.cache
  (:gen-class)
  (:import [java.sql SQLException])
  (:require [qcast.db        :as db]
            [taoensso.timbre :refer :all]))


;;; Globals
(defonce ^:private db-spec (db/sqlite))


;;; Internals
(defn- pre-process [row]
  (-> row
      (update-in [:data]         db/from-edn)
      (update-in [:publish_date] db/from-inst)))

(defn- post-process [row]
  (-> row
      (update-in [:publish_date] db/to-inst)
      (update-in [:data]         db/to-edn)))


;;; Interface

(defn init []
  (info "Initializing cache")
  (db/create-table db-spec :presentations
    [:id            :TEXT     "NOT NULL" "PRIMARY KEY"]
    [:creation_date :DATETIME "NOT NULL" "DEFAULT CURRENT_TIMESTAMP"]
    [:publish_date  :DATETIME "NOT NULL"]
    [:data          :BLOB     "NOT NULL"]))

(defn put [item]
  (try ;; Protect against existing entries
    (->> {:id           (:id item)
          :publish_date (:publish-date item)
          :data         item}
         pre-process
         (db/insert db-spec :presentations))
    (catch SQLException e
      (warn "Presentation already exists" item))))

(defn lookup [id]
  (map post-process (db/select' db-spec * :presentations (db/where {:id id}))))

(defn latest
  ([] (first (latest 1)))
  ([n]
    (map post-process (db/select' db-spec * :presentations
                                  (db/order-by [{:publish_date  :desc}
                                                {:creation_date :desc}])
                                  (db/limit n))))
  ([n since-id]
     (let [since-date (:publish_date (pre-process (first (lookup since-id))))
           sql-stmt "SELECT * FROM presentations WHERE publish_date < ? ORDER BY publish_date DESC, creation_date DESC LIMIT ?"]
       (map post-process (db/query db-spec [sql-stmt since-date n])))))
