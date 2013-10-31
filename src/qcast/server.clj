(ns qcast.server
  (:gen-class)
  (:require [compojure.core        :refer [defroutes GET]]
            [compojure.handler     :as handler]
            [compojure.route       :as route]
            [config]
            [qcast.cache           :as cache]
            [qcast.feed.ext.atom   :as atom]
            [qcast.feed.ext.itunes :as itunes]
            [qcast.feed.ext.simple-chapters :as psc]
            [qcast.feed.rss        :as rss]
            [qcast.infoq           :as infoq]
            [qcast.util            :refer [parse-int]]
            [org.httpkit.server    :as http]
            [taoensso.timbre       :refer :all]))


;;; Internals

(defn- slides
  ([] (slides 1))
  ([n] (cons (str "Slide " n) (lazy-seq (slides (inc n))))))

(defn- feed-item [media-type p]
  (let [link (:link p)]
    [(rss/title (:title p))
     (rss/link link)
     (rss/description (:summary p))
     ;;(rss/author (string/join ", " (:authors p)))
     (rss/author "info@infoq.com (InfoQ)")
     (rss/pub-date (:publish-date p))
     (rss/guid link)
     (apply rss/enclosure (media-type p))
     (apply rss/categories (:keywords p))
     ;;(itunes/author (string/join ", " (:authors p)))
     (itunes/author "info@infoq.com")
     (itunes/summary (:summary p))
     ;;(itunes/image (:poster p))
     (itunes/image (first (:slides p)))
     (itunes/duration (:length p))
     (apply psc/chapters (map vector (:times p) (slides) (:slides p)))]))

(defn- feed-channel [change-date]
  (let [base-url "http://www.infoq.com"
        title "QCast - InfoQ Presentation Podcast"]
    [(rss/title title)
     (rss/link base-url)
     (rss/description (str "Facilitating the spread of knowledge "
                           "and innovation in enterprise software "
                           "development"))
     (rss/image (str base-url "/styles/i/logo-big.jpg") title base-url)
     (rss/language "en-US")
     (rss/generator "InfoQ-Feed-Generator/1.0")
     (rss/last-build-date change-date)
     (atom/link "http://infoqcast.herokuapp.com/feed")
     (itunes/author "InfoQ")
     (itunes/owner "InfoQ" "info@infoq.com")
     (itunes/summary (str "InfoQ.com is a practitioner-driven "
                          "community news site focused on "
                          "facilitating the spread of knowledge and "
                          "innovation in enterprise software "
                          "development."))
     (itunes/categories "Education" "Technology")
     (itunes/keywords "Java" ".NET" "dotnet" "Ruby" "SOA"
                      "Service Oriented Architecture" "Agile"
                      "enterprise" "software development"
                      "development" "architecture" "programming")
     (itunes/image (str base-url "/styles/i/logo-big.jpg"))
     (itunes/block false)
     (itunes/explicit false)]))

(defn- serve-feed [media]
  (let [entries (map :data (cache/latest 50))
        items (map #(feed-item media %) entries)
        channel (feed-channel (:publish-date (first entries)))
        extensions [:atom :itunes :simple-chapters]]
    (rss/feed channel items extensions)))


;;; Main

(defn- rss-response [body]
  {:status 200
   :headers {"Content-Type" "application/rss+xml"}
   :body body})

(defn- redirect [url]
  {:status 302
   :headers {"Location" url}
   :body ""})

(defroutes app-routes
  (GET "/feed"       [] (redirect "feed/audio"))
  (GET "/feed/audio" [] (fn [_req] (rss-response (serve-feed :audio))))
  (GET "/feed/video" [] (fn [_req] (rss-response (serve-feed :video))))
  (GET "/presentations/:filename" [filename]
       (fn [_req] (redirect (infoq/media-link filename))))
  (route/files "/")
  (route/not-found "Not found"))

(defn -main []
  (info "Starting web server")
  (let [port (parse-int (or (System/getenv "PORT") "8080"))]
    (http/run-server (handler/site app-routes) {:port port})))
