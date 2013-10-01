(ns infoq-podcast.core
  (:gen-class)
  (:require [clojure.string         :as string]
            [infoq-podcast.cache    :as cache]
            [infoq-podcast.catcher  :as catcher]
            [infoq-podcast.feed.rss :as rss]
            ;;[org.httpkit.client     :as http-kit]
            [taoensso.timbre        :as timbre :refer [trace debug info]]))


;;; Main

(defn- prepare [p]
  (let [guid (:link p)
        date (:date p)]
    (cache/put guid date p)
    [(rss/title (:title p))
     (rss/link guid)
     (rss/description (:summary p))
     (rss/author "info@infoq.com") ;; (string/join ", " (:authors p))
     (rss/pub-date date)
     (rss/guid guid)
     (apply rss/enclosure (:video p))
     (apply rss/category (:keywords p))]))

(defn -main
  "Requires n/12 + 2*n requests (/12 -> overview page, 2* -> page + video)."
  []
  (debug "Starting")
  (cache/init)
  (let [channel [(rss/title "InfoQ Presentations Feed")
                 (rss/link "http://www.infoq.com")
                 (rss/description (str "Facilitating the spread of knowledge and "
                                       "innovation in enterprise software development"))
                 (rss/language "en-US")
                 (rss/generator "InfoQ-Feed-Generator/1.0")]
        extensions [:atom :itunes :feedburner :simple-chapters :content :history]
        items (->> (catcher/latest) (take 4) (map catcher/metadata) (map prepare))
        feed (rss/feed channel items extensions)]
    (prn (cache/latest))
    (clojure.pprint/pprint feed)))
