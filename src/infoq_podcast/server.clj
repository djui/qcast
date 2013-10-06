(ns infoq-podcast.server
  (:gen-class)
  (:require [compojure.core         :as compojure :refer [defroutes GET]]
            [compojure.handler      :as handler]
            [compojure.route        :as route]
            [infoq-podcast.cache    :as cache]
            [infoq-podcast.feed.rss :as rss]
            [infoq-podcast.util     :as util]
            [org.httpkit.server     :as http]
            [taoensso.timbre        :as timbre :refer [trace debug info]]))


;;; Internals

(defn- prepare-item [p]
  (let [link (:link p)]
    [(rss/title (:title p))
     (rss/link link)
     (rss/description (:summary p))
     (rss/author "info@infoq.com") ;; (string/join ", " (:authors p))
     (rss/pub-date (:date p))
     (rss/guid link)
     (apply rss/enclosure (:video p))
     (apply rss/category (:keywords p))]))

(defn- serve-feed [req]
  (let [base-url #(apply str "http://www.infoq.com" %&)
        items (map (comp prepare-item :data) (cache/latest 50))
        channel [(rss/title "InfoQ Presentations")
                 (rss/link (base-url))
                 (rss/description (str "Facilitating the spread of knowledge "
                                       "and innovation in enterprise software "
                                       "development"))
                 (rss/image (base-url "/styles/i/logo-big.jpg")
                            "InfoQ Presentations" (base-url))
                 (rss/language "en-US")
                 (rss/generator "InfoQ-Feed-Generator/1.0")]
        extensions [:atom :itunes :feedburner :simple-chapters :content :history]
        feed (rss/feed channel items extensions)]
    feed))

(defn- index [req]
  (str "<html>"
       "<head>"
       "  <title>InfoQ Presentations Podcast</title>"
       "</head>"
       "<body>"
       "  <h1>InfoQ Presentation podcast</h1>"
       "  <a href=\"podcast://donots.local:8090/feed\">Subscribe (Instacast)</a><br/>"
       "  <a href=\"pcast://donots.local:8090/feed\">Subscribe (Podcast)</a><br/>"

       "  <a href=\"pktc://donots.local:8090/feed\">Subscribe (Pocket Casts)</a><br/>"
       "  <a href=\"pktc://open\">Subscribe (Pocket Casts open)</a><br/>"
       "  <a href=\"pktc://open/http://donots.local:8090/feed\">Subscribe (Pocket Casts open)</a><br/>"
       "  <a href=\"pktc://open?http://donots.local:8090/feed\">Subscribe (Pocket Casts open)</a><br/>"
       "  <a href=\"pktc://open?url=http://donots.local:8090/feed\">Subscribe (Pocket Casts open)</a><br/>"
       "  <a href=\"pktc://play\">Subscribe (Pocket Casts play)</a><br/>"
       "  <a href=\"pktc://play/http://donots.local:8090/feed\">Subscribe (Pocket Casts play)</a><br/>"
       "  <a href=\"pktc://play?http://donots.local:8090/feed\">Subscribe (Pocket Casts play)</a><br/>"
       "  <a href=\"pktc://play?url=http://donots.local:8090/feed\">Subscribe (Pocket Casts play)</a><br/>"
       
       "  <a href=\"itpc://donots.local:8090/feed\">Subscribe (iTunes)</a><br/>"
       "  <a href=\"http://donots.local:8090/feed\">Subscribe</a><br/>"
       "</body>"
       "</html>"))


;;; Main

(defroutes app-routes
  (GET "/" [] index)
  (GET "/feed" [] serve-feed)
  (route/files "/static/")
  (route/not-found "Not found"))

(defn -main []
  (info "Starting web server")
  (http/run-server (handler/site app-routes) {}))
