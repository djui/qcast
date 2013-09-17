(ns infoq-podcast.feed
  (:require [clojure.string  :as string]
            [clojure.xml     :as xml]
            [taoensso.timbre :as timbre :refer [trace debug info]]))


;;; Globals

(def generator "InfoQ-Feed-Generator/1.0 Clojure/1.5.1")

(def rss-spec
  {:channel
   [;; Required (AND)
    :description :link :title
    ;; Optional
    :category :cloud :copyright :docs :generator :image :language :lastBuildDate
    :managingEditor :pubDate :rating :skipDays :skipHours :textInput :ttl
    :webMaster]
   :item
   [;; Required (OR)
    :description :title
    ;; Optional
    :author :category :comments :enclosure :guid :link :pubDate :source]})

(def atom-spec
  {:feed []
   :entry []})


;;; Utilities

(defn- format-inst [inst]
  (let [format (java.text.SimpleDateFormat. "EEE, dd MMM yyyy HH:mm:ss ZZZZ")]
    (.format format inst)))

(defn- xml-str [s]
  (let [escapes {\< "&lt;",
                 \> "&gt;",
                 \& "&amp;",
                 \" "&quot;"}]
    (string/escape s escapes)))

(defn- tag [name attrs content]
  {:tag name, :attrs attrs, :content content})

(defn- tags [tag-map]
  (map #(tag % nil %2) tag-map))


;;; Internals

(defn- extension []
  (throw (UnsupportedOperationException. "Not yet implemented")))

(defn- atom-feed
  "Create an Atom 1.0 feed.
  Specification: http://tools.ietf.org/html/rfc4287"
  [feed entries & [extensions]]
  (throw (UnsupportedOperationException. "Not yet implemented")))

(defn- rss-channel [{:keys [link title description]} meta items]
  (assert (and link title description)
          "RSS channel requires :link, :title, and :description")
  (let [spec-meta (select-keys meta (:channel rss-spec))
        meta-tags (tags (assoc spec-meta :generator generator))
        content (concat meta-tags items)]
    (tag :channel nil content)))

(defn- rss-item [{:keys [title description]} item]
  (assert (or title description)
          "RSS item requires :title or :description")
  (let [spec-item (select-keys item (:item rss-spec))
        content (tags spec-item)]
    (tag :item nil content)))

(defn- rss-feed
  "Create a RSS 2.0 feed.
  Specification: http://cyber.law.harvard.edu/rss/rss.html"
  [meta elements & [extensions]]
  (let [items (map rss-item elements)
        channel (rss-channel meta items)
        feed (tag :rss {:version "2.0"} [channel])]
    (with-out-str
      (xml/emit feed))))


;;; API

(defn create
  "Create a RSS or Atom feed. Extensions can be:
   * [itunes](http://www.itunes.com/dtds/podcast-1.0.dtd)
   * [feedburner](http://rssnamespace.org/feedburner/ext/1.0)
   * [simple-chapters](http://podlove.org/simple-chapters)
   * [content](http://purl.org/rss/1.0/modules/content)
   * [history](http://purl.org/syndication/history/1.0)"
  [type meta elements & [extensions]]
  (case type
    :rss (rss-feed meta elements extensions)
    :atom (atom-feed meta elements extensions)))
