(ns infoq-podcast.catcher
  (:require [clojure.string     :as string]
            [infoq-podcast.html :as html]
            [infoq-podcast.util :as util]
            [taoensso.timbre    :as timbre :refer [trace debug info]]))


;;; Globals


;;; Internals

(defn- base-url [& s]
  (apply str "http://www.infoq.com" s))


;; Scraping Internals

(defn- resource-url [path]
  (base-url path))

(defn- poster [dom]
  (-> (html/meta :property "og:image" dom)
      resource-url))

(defn- split-keywords [s]
  (let [[lowercase-kw & uppercase-kw] (string/split s #",")
        lowercase-kw (string/split lowercase-kw #" ")]
    (concat lowercase-kw uppercase-kw)))

(defn- keywords [dom]
  (-> (html/meta "keywords" dom)
      split-keywords))

(defn- summary [dom]
  (html/meta "description" dom))

(defn- title [dom]
  (html/select [:head :title]
               html/inner-text
               dom))

(defn- authors [dom]
  (-> (html/select [:.author_general :> :a]
                   html/inner-text
                   dom)
      (string/split #"\s*(and|,)\s*")))

(defn- length [dom]
  (html/select [:.videolength2]
               #(-> % html/inner-text util/interval->sec)
               dom))

(defn- video [dom]
  (let [url (html/select [:#video :> :source]
                         (html/attr :src)
                         dom)
        [length type] (html/content-header url)]
    [url length type]))

(defn- date [dom]
  (html/select [:.author_general]
               #(->> % (html/inner-text 2) (re-find #"(?s)on\s*(.*)") second util/parse-date)
               dom))

(defn- slides [dom]
  (let [mapper #(some->> % html/inner-text (re-find #".*var slides.*"))]
    (->> (html/select-all [:script] mapper dom)
         util/first-true
         (re-seq #"'(.+?)'")
         (map second)
         (map resource-url))))

(defn- times [dom]
  (let [mapper #(some->> % html/inner-text (re-find #".*var TIMES.*"))]
    (->> (html/select-all [:script] mapper dom)
         util/first-true
         (re-seq #"(\d+?),")
         (map second)
         (map util/parse-int))))

(defn- overview-ids [dom]
  (html/select-all [:.news_type_video :> :a] (html/attr :href) dom))


;; Scraping API

(defn metadata [id]
  (let [md-keys [:id :link :poster :keywords :summary :title :authors :date
                 :length :video :slides :times]
        md-vals (juxt (constantly id) (constantly (base-url id)) poster
                      keywords summary title authors date length video slides
                      times)]
    (debug "Fetching presentation" id)
    (->> (base-url id)
         html/dom
         md-vals
         (zipmap md-keys))))

(defn latest
  ([] (latest 0))
  ([marker]
     (debug "Fetching overview from index" marker)
     (let [dom (-> (base-url "/presentations/" marker) html/dom)
           items (overview-ids dom)]
       (lazy-cat items (latest (+ marker (count items)))))))

