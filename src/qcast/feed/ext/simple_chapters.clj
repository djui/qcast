(ns ^{:doc "Simple Chapters extension: http://podlove.org/simple-chapters.
            Specification: http://podlove.org/simple-chapters"}
  qcast.feed.ext.simple-chapters
  (:require [hiccup.util :refer [escape-html]]))


;;; Globals

(def ^:private version "1.2")

;;; Interface

;; Channel

;; Channel or Item

;; Item

(defn chapter
  ([start title]
     [:psc:chapter {:start start, :title (escape-html title)}])
  ([start title link-url]
     (assoc-in (chapter start title) [1 :href] link-url))
  ([start title link-url image-url]
     (assoc-in (chapter start title link-url) [1 :image] image-url)))

(defn chapters [& chapters]
  [:psc:chapters {:version version} (map #(apply chapter %) chapters)])
