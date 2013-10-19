(ns qcast.server
  (:gen-class)
  (:require [compojure.core        :as compojure :refer [defroutes GET]]
            [compojure.handler     :as handler]
            [compojure.route       :as route]
            [qcast.cache           :as cache]
            [qcast.feed.rss        :as rss]
            [qcast.feed.ext.itunes :as itunes]
            [qcast.util            :as util :refer [parse-int]]
            [org.httpkit.server    :as http]
            [taoensso.timbre       :as timbre :refer :all]))


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
     (apply rss/categories (:keywords p))
     ;;(itunes/author (string/join ", " (:authors p)))
     (itunes/author "info@infoq.com")
     (itunes/summary (:summary p))
     ;;(itunes/image (:poster p))
     (itunes/image (first (:slides p)))
     (itunes/duration (:length p))]))

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
                 (rss/last-build-date (get-in (first entries) [:data :publish-date]))
                 (itunes/author "InfoQ")
                 (itunes/owner "InfoQ" "info@infoq.com")
                 (itunes/summary (str "InfoQ.com is a practitioner-driven "
                                      "community news site focused on "
                                      "facilitating the spread of knowledge and "
                                      "innovation in enterprise software "
                                      "development."))
                 (itunes/categories "development" "architecture & design"
                                    "process & practices"
                                    "operations & infrastructure"
                                    "enterprise architecture")
                 (itunes/keywords "Java" ".NET" "dotnet" "Ruby" "SOA"
                                  "Service Oriented Architecture" "Agile"
                                  "enterprise" "software development"
                                  "development" "architecture" "programming")
                 (itunes/image (base-url "/styles/i/logo-big.jpg"))
                 (itunes/block false)
                 (itunes/explicit false)]
        extensions [:atom :itunes :feedburner :simple-chapters :content :history]
        feed (rss/feed channel items extensions)]
    feed))


;;; Main

(defn- rss-response [body & [status]]
  {:status (or status 200)
   :headers {"Content-Type" "application/rss+xml"}
   :body body})

(defroutes app-routes
  (GET "/feed" [] (comp rss-response serve-feed))
  (route/files "/")
  (route/not-found "Not found"))

(defn -main []
  (info "Starting web server")
  (let [port (parse-int (or (System/getenv "PORT") "8080"))]
    (http/run-server (handler/site app-routes) {:port port})))
