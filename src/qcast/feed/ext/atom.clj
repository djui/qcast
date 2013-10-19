(ns ^{:doc "Atom Syndication 1.0 extension: http://www.w3.org/2005/Atom.
            Specification: http://tools.ietf.org/html/rfc4287"}
  qcast.feed.ext.atom)


;;; Interface

;; Channel

(defn link [url]
  [:atom10:link {:xmlns:atom10 "http://www.w3.org/2005/Atom"
                 :rel "self"
                 :type "application/rss+xml"
                 :href url}])

;; Channel or Item


;; Item
