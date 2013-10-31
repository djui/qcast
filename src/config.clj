(ns config
  (:import [java.io PushbackReader]
           [java.lang RuntimeException])
  (:require [clojure.edn     :as edn]
            [clojure.java.io :as io]
            [clojure.string  :as string]
            [clojure.walk    :as walk])
  (:refer-clojure :exclude [load get]))


;;; Globals

(def config (atom {}))


;;; Utilities

(defn- read-resource [resource-path]
  (or (some-> resource-path
              io/resource
              slurp
              edn/read-string)
      {}))


(defn- deep-merge [& maps]
  (if (every? map? maps)
    (apply merge-with deep-merge maps)
    (last maps)))

(defn str-keys-to-map [[k v]]
  (let [ks (map keyword (filter not-empty (string/split k #"[\._]")))]
    (when-not (empty? ks) (assoc-in {} ks v))))

(defn deep-keywordize-keys [m]
  (->> m (map str-keys-to-map) (apply deep-merge)))


;;; Internals

(defn- config-and-env []
  (deep-merge (read-resource "config.default.edn")
              (read-resource "config.edn")
              (deep-keywordize-keys (System/getProperties))
              (deep-keywordize-keys (System/getenv))))


;;; Interface

(defn load []
  (reset! config (config-and-env)))

(defn get [& keys]
  (get-in @config keys))
