(ns qcast.db
  (:require [clj-time.coerce   :as time]
            [clojure.edn       :as edn]
            [clojure.java.jdbc :as jdbc]
            [config]))


;;; Interface

;; SPECS

(defn sqlite []
  {:subprotocol (or (config/get :db :subprotocol) "sqlite")
   :classname   (or (config/get :db :classname)   "org.sqlite.JDBC")
   :subname     (or (config/get :db :subname)     "qcast.db")})


;; DDL

(defn create-table [db name & specs]
  (let [stmt (apply jdbc/create-table-ddl (conj specs name))]
    (jdbc/db-do-commands db stmt)))


;; DML

(defn insert
  "Thin wrapper around jdbc/insert!"
  [db table & opts]
  (apply jdbc/insert! (cons db (cons table opts))))

(defn insert-or-ignore
  "Thin wrapper around jdbc/insert! ignoring failure if unique key already
  exists."
  [db table & opts]
  (throw Exception "Not yet implemented"))

(defn insert-or-replace
  "Thin wrapper around jdbc/insert! replacing the row(s) if unique key already
  exists."
    [db table & opts]
    (throw Exception "Not yet implemented"))

(defn query
  "Query a SELECT statement."
  [db stmt args]
  (jdbc/query db (cons stmt args)))


;; Transformer

(defn from-edn
  "Serialize EDN data structure to string."
  [data]
  (pr-str data))

(defn to-edn
  "Deserialize string to EDN data structure."
  [str]
  (edn/read-string str))

(defn from-inst
  "Convert Clojure instant to SQL date (unix timestamp * 1000)."
  [inst]
  (time/from-sql-date inst))

(defn to-inst
  "Convert SQL date (unix timestamp * 1000) to Clojure instant."
  [timestamp]
  (time/to-sql-date timestamp))
