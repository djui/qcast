(ns qcast.server
  (:gen-class)
  (:require [compojure.core     :as compojure :refer [defroutes GET]]
            [compojure.handler  :as handler]
            [compojure.route    :as route]
            [qcast.cache        :as cache]
            [qcast.feed.rss     :as rss]
            [qcast.util         :as util :refer [parse-int]]
            [org.httpkit.server :as http]
            [taoensso.timbre    :as timbre :refer :all]))


;;; Internals

(defn- prepare-item [p]
  (let [link (:link p)]
    [(rss/title (:title p))
     (rss/link link)
     (rss/description (:summary p))
     ;;(rss/author (string/join ", " (:authors p)))
     (rss/author "info@infoq.com")
     (rss/pub-date (:publish-date p))
     (rss/guid link)
     (apply rss/enclosure (:video p))
     (apply rss/category (:keywords p))]))

(defn- serve-feed [req]
  (let [base-url #(apply str "http://www.infoq.com" %&)
        entries (cache/latest 50)
        items (map (comp prepare-item :data) entries)
        channel [(rss/title "QCast - InfoQ Presentation Podcast")
                 (rss/link (base-url))
                 (rss/description (str "Facilitating the spread of knowledge "
                                       "and innovation in enterprise software "
                                       "development"))
                 (rss/image (base-url "/styles/i/logo-big.jpg") "InfoQ" (base-url))
                 (rss/language "en-US")
                 (rss/generator "InfoQ-Feed-Generator/1.0")
                 (rss/pub-date (get-in (first entries) [:data :publish-date]))]
        extensions [:atom :itunes :feedburner :simple-chapters :content :history]
        feed (rss/feed channel items extensions)]
    feed))


;;; Main

(defroutes app-routes
  (GET "/feed" [] serve-feed)
  (route/files "/")
  (route/not-found "Not found"))

(defn -main []
  (info "Starting web server")
  (let [port (parse-int (or (System/getenv "PORT") "8080"))]
    (http/run-server (handler/site app-routes) {:port port})))
