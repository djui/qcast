(ns qcast.db
  (:require [clj-time.coerce       :as time]
            [clojure.edn           :as edn]
            [clojure.java.jdbc     :as jdbc]
            [clojure.java.jdbc.ddl :as ddl]
            [clojure.java.jdbc.sql :as dml]))


;;; Interface

;; SPECS

(defn sqlite
  ([] (sqlite "sqlite.db"))
  ([location]
     {:subprotocol "sqlite"
      :classname "org.sqlite.JDBC"
      :subname location}))


;; DDL

(defn create-table [db name & specs]
  (let [stmt (apply ddl/create-table (conj specs name))]
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
  [db sql-params & opts]
  (apply jdbc/query (cons db (cons sql-params opts))))

(defn select
  "Create SELECT statement."
  [& args]
  (apply dml/select args))

(defn select'
  "Create SELECT statement and query it."
  [db & select-args]
  (jdbc/query db (apply dml/select select-args)))


;; SQL

(def join dml/join)
(def where dml/where)
(def order-by dml/order-by)

(defn limit [n]
  (str "LIMIT " n))

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
