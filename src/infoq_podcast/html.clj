(ns infoq-podcast.html
  (:require [clj-http.client        :as http]
            [clojure.string         :as string]
            [infoq-podcast.util     :as util]
            [net.cgrand.enlive-html :as css]
            [taoensso.timbre        :as timbre :refer [trace debug info]])
  (:refer-clojure :exclude [meta]))


;;; Globals

(def ios-user-agent
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
  (let [header (-> (HEAD url) :headers)
        length (util/parse-int (get header "content-length"))
        type (get header "content-type")]
    [length type]))

(defn dom [url]
  (-> (GET url) :body css/html-resource))

(defn select-all [selector mapper dom]
  (->> (css/select dom selector) (map mapper)))

(defn select [selector mapper dom]
  (-> (css/select dom selector) first mapper))

(defn attr [name]
  #(get-in % [:attrs name]))

(defn meta
  ([key dom] (meta :name key dom))
  ([key value dom]
     (select [:head [:meta (css/attr= key value)]] (attr :content) dom)))

(defn inner-text
  ([node] (inner-text 0 node))
  ([n node] (some-> (:content node) (nth n) string/trim)))
