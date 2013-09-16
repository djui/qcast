(ns infoq-podcast.core
  (:gen-class)
  (:import [java.text.SimpleDateFormat])
  (:require [clj-http.client :as http]
            [clojure.string :as string]
            ;;[feedparser-clj.core :as feedparser]
            ;;[org.httpkit.client :as http-kit]
            [net.cgrand.enlive-html :as css]
            [taoensso.timbre :as timbre :refer [trace debug info]]))

;;; Globals

(def base-url "http://www.infoq.com")

(def ios-user-agent (str "Mozilla/5.0"
                         " (iPhone; CPU iPhone OS 7_0 like Mac OS X)"
                         " AppleWebKit/537.51.1 (KHTML, like Gecko)"
                         " Version/7.0 Mobile/11A465 Safari/9537.53"))

;;; HTML Helper

(defn http-get [url]
  (let [options {:headers {"User-Agent" ios-user-agent}, :as :stream}]
    (http/get url options)))

(defn html-dom [url]
  (-> (http-get url) :body css/html-resource))

(defn select-all [selector mapper dom]
  (->> (css/select dom selector) (map mapper)))

(defn select [selector mapper dom]
  (-> (css/select dom selector) first mapper))

(defn tag-attr [name]
  #(get-in % [:attrs name]))

(defn meta-tag
  ([key dom] (meta-tag :name key dom))
  ([key value dom]
     (select [:head [:meta (css/attr= key value)]] (tag-attr :content) dom)))

(defn inner-text
  ([node] (inner-text 0 node))
  ([n node] (some-> (:content node) (nth n) string/trim)))

;;; Helper

(defn parse-int [s]
  (. Integer parseInt s))

(defn parse-date [s]
  (let [formatter (java.text.SimpleDateFormat. "MMM dd, yyyy")]
    (.parse formatter s)))

(defn interval->sec [s]
  (let [units (map parse-int (string/split s #":"))]
    (+ (* (first units) 60)
       (second units))))

(defn first-true [coll]
  (some identity coll))

(defn keywords [s]
  (let [[lower-keywords & upper-keywords] (string/split s #",")
        lower-keywords (string/split lower-keywords #" ")]
    (concat lower-keywords upper-keywords)))

(defn resource-url [path]
  (str base-url path))

;;; Main

(defn metadata-poster [dom]
  (-> (meta-tag :property "og:image" dom)
      resource-url))

(defn metadata-keywords [dom]
  (-> (meta-tag "keywords" dom)
      keywords))

(defn metadata-summary [dom]
  (meta-tag "description" dom))

(defn metadata-title [dom]
  (select [:head :title] inner-text dom))

(defn metadata-authors [dom]
  (-> (select [:.author_general :> :a] inner-text dom)
      (string/split #"\s*(and|,)\s*")))

(defn metadata-length [dom]
  (select [:.videolength2] #(-> % inner-text interval->sec) dom))

(defn metadata-video [dom]
  (select [:#video :> :source] (tag-attr :src) dom))

(defn metadata-date [dom]
  (let [mapper #(->> % (inner-text 2) (re-find #"(?s)on\s*(.*)") second parse-date)]
    (select [:.author_general] mapper dom)))

(defn metadata-slides [dom]
  (let [mapper #(some->> % inner-text (re-find #".*var slides.*"))]
    (->> (select-all [:script] mapper dom)
         first-true
         (re-seq #"'(.+?)'")
         (map second)
         (map resource-url))))

(defn metadata-times [dom]
  (let [mapper #(some->> % inner-text (re-find #".*var TIMES.*"))]
    (->> (select-all [:script] mapper dom)
         first-true
         (re-seq #"(\d+?),")
         (map second)
         (map parse-int))))

(defn metadata [id]
  (let [md-keys [:id :poster :keywords :summary :title :authors :date :length
                 :video :slides :times]
        md-vals (juxt (constantly id) metadata-poster metadata-keywords
                      metadata-summary metadata-title metadata-authors
                      metadata-date metadata-length metadata-video
                      metadata-slides metadata-times)]
    (debug "Fetching presentation" id)
    (->> (str base-url id)
         html-dom
         md-vals
         (zipmap md-keys))))

(defn get-ids [dom]
  (select-all [:.news_type_video :> :a] (tag-attr :href) dom))

(defn presentations
  ([] (presentations 0))
  ([marker]
     (let [dom (-> (str base-url "/presentations/" marker) html-dom)]
       (debug "Fetching page" marker)
       #_(lazy-cat (get-ids dom) (lazy-seq (presentations (+ marker 12))))
       (get-ids dom))))


(defn -main []
  (debug "Starting")
  (let [p (map metadata (take 4 (presentations)))]
    ;(clojure.pprint/pprint p)
    ))
