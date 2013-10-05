(ns infoq-podcast.core
  (:gen-class)
  (:require [clojure.string         :as string]
            [compojure.route        :as route :refer [files not-found]]
            [compojure.handler      :as handler :refer [site]]
            [compojure.core         :as compojure :refer [defroutes GET]]
            [infoq-podcast.cache    :as cache]
            [infoq-podcast.catcher  :as catcher]
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
  (let [items (map (comp prepare-item :data) (cache/latest 50))
        channel [(rss/title "InfoQ Presentations Feed")
                 (rss/link "http://www.infoq.com")
                 (rss/description (str "Facilitating the spread of knowledge and "
                                       "innovation in enterprise software development"))
                 (rss/language "en-US")
                 (rss/generator "InfoQ-Feed-Generator/1.0")]
        extensions [:atom :itunes :feedburner :simple-chapters :content :history]
        feed (rss/feed channel items extensions)]
    feed))

(defn- index [req]
  (str "<html>"
       "<head>"
       "  <title>InfoQ Presentation podcast</title>"
       "</head>"
       "<body>"
       "  <h1>InfoQ Presentation podcast</h1>"
       "</body>"
       "</html>"))

(defn- cache-updates
  "Scrape the overview sites and collect its oughly 12 items per site until
  finding an seen item (since). Scrape a maximum of limit or 100 items. This
  sequence requires additional two requests (page+video) per item, thus n%12 +
  2*n."
  ([] (cache-updates (cache/latest)))
  ([since] (cache-updates since 100))
  ([since limit]
     (let [since-id (or (:id since) :inf)]
       (info "Check for updates since" since-id)
       (->> (catcher/latest)
            (take-while #(not= % since-id))
            (pmap catcher/metadata)
            (map cache/put)
            (dorun (dec limit))))))


;;; Main

(defroutes app-routes
  (GET "/" [] index)
  (GET "/feed" [] serve-feed)
  (route/files "/static/")
  (route/not-found "Not found"))

(defn -main []
  (info "Starting")
  (info "Initializing cache") (cache/init)
  (info "Spawing catcher task") (util/interspaced (util/minutes) cache-updates)
  (info "Spawing web server") (http/run-server (handler/site app-routes) {}))
