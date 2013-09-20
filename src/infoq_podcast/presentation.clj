(ns infoq-podcast.presentation
  (:import [java.text.SimpleDateFormat])
  (:require [clojure.string     :as string]
            [infoq-podcast.html :as html]
            [taoensso.timbre    :as timbre :refer [trace debug info]]))


;;; Globals

(def base-url "http://www.infoq.com")


;;; Utilities

(defn- resource-url [path]
  (str base-url path))

(defn- parse-int [s]
  (. Integer parseInt s))

(defn- parse-date [s]
  (let [format (java.text.SimpleDateFormat. "MMM dd, yyyy")]
    (.parse format s)))

(defn- interval->sec [s]
  (let [units (map parse-int (string/split s #":"))]
    (+ (* (first units) 60)
       (second units))))

(defn first-true [coll]
  (some identity coll))


;;; Internals

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
               #(-> % html/inner-text interval->sec)
               dom))

(defn- video [dom]
  (let [url (html/select [:#video :> :source]
                         (html/attr :src)
                         dom)
        [length type] (html/content-header url)]
    [url length type]))

(defn- date [dom]
  (html/select [:.author_general]
               #(->> % (html/inner-text 2) (re-find #"(?s)on\s*(.*)") second parse-date)
               dom))

(defn- slides [dom]
  (let [mapper #(some->> % html/inner-text (re-find #".*var slides.*"))]
    (->> (html/select-all [:script] mapper dom)
         first-true
         (re-seq #"'(.+?)'")
         (map second)
         (map resource-url))))

(defn- times [dom]
  (let [mapper #(some->> % html/inner-text (re-find #".*var TIMES.*"))]
    (->> (html/select-all [:script] mapper dom)
         first-true
         (re-seq #"(\d+?),")
         (map second)
         (map parse-int))))

(defn- overview-ids [dom]
  (html/select-all [:.news_type_video :> :a] (html/attr :href) dom))


;;; API

(defn metadata [id]
  (let [md-keys [:id :link :poster :keywords :summary :title :authors :date
                 :length :video :slides :times]
        md-vals (juxt (constantly id) (constantly (str base-url id)) poster
                      keywords summary title authors date length video slides
                      times)]
    (debug "Fetching presentation" id)
    (->> (str base-url id)
         html/dom
         md-vals
         (zipmap md-keys))))

(defn latest
  ([] (latest 0))
  ([marker]
     (let [dom (-> (str base-url "/presentations/" marker) html/dom)]
       (debug "Fetching page" marker)
       ;; FIXME: Sequence currently not lazy. But why?!
       (lazy-cat (overview-ids dom) #_(lazy-seq (latest (+ marker 12)))))))
