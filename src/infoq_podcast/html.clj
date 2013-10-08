(ns infoq-podcast.html
  (:require [clj-http.client        :as http]
            [clojure.string         :as string]
            [infoq-podcast.util     :as util :refer [parse-int]]
            [net.cgrand.enlive-html :as css])
  (:refer-clojure :exclude [meta]))


;;; Globals

(def ^:private ios-user-agent
  (str "Mozilla/5.0 (iPhone; CPU iPhone OS 7_0 like Mac OS X) "
       "AppleWebKit/537.51.1 (KHTML, like Gecko) "
       "Version/7.0 "
       "Mobile/11A465 "
       "Safari/9537.53"))


;;; Internals

(defn- GET [url]
  (let [options {:headers {"User-Agent" ios-user-agent}, :as :stream}]
    (http/get url options)))

(defn- HEAD [url]
  (let [options {:headers {"User-Agent" ios-user-agent}, :as :stream}]
    (http/head url options)))


;;; API

(defn content-header [url]
  (let [header (:headers (HEAD url))
        length (parse-int (get header "content-length"))
        type (get header "content-type")]
    [length type]))

(defn dom [url]
  (-> (GET url) :body css/html-resource))

(defn select-all [selector transformer dom]
  (map transformer (css/select dom selector)))

(defn select [selector transformer dom]
  (-> (css/select dom selector) first transformer))

(defn attr [name]
  #(get-in % [:attrs name]))

(defn meta
  ([key dom] (meta :name key dom))
  ([key value dom]
     (select [:head [:meta (css/attr= key value)]] (attr :content) dom)))

(defn inner-text
  ([node] (inner-text 0 node))
  ([n node] (some-> (:content node) (nth n) string/trim)))
