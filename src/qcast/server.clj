(ns qcast.server
  (:gen-class)
  (:require [compojure.core                 :refer [defroutes routes GET]]
            [compojure.handler              :as handler]
            [compojure.route                :as route]
            [config]
            [org.httpkit.server             :as http]
            [qcast.cache                    :as cache]
            [qcast.feed.ext.atom            :as atom]
            [qcast.feed.ext.itunes          :as itunes]
            [qcast.feed.ext.simple-chapters :as psc]
            [qcast.feed.rss                 :as rss]
            [qcast.infoq.site               :as infoq]
            [qcast.ring.middleware.xml      :as xml]
            [qcast.util                     :refer [parse-int]]
            [ring.middleware.json           :as json]
            [ring.util.response             :as response]
            [taoensso.timbre                :refer :all]))

;;; Internals

(defn- slides
  ([] (slides 1))
  ([n] (cons (str "Slide " n) (lazy-seq (slides (inc n))))))

(defn- feed-item [media-type p]
  (let [link (:link p)]
    (remove nil?
      [(rss/title (:title p))
       (rss/link link)
       (rss/description (:summary p))
       ;;(rss/author (string/join ", " (:authors p)))
       (rss/author "info@infoq.com (InfoQ)")
       (rss/pub-date (:publish-date p))
       (rss/guid link)
       (when (contains? p media-type) (apply rss/enclosure (media-type p)))
       (apply rss/categories (:keywords p))
       ;;(itunes/author (string/join ", " (:authors p)))
       (itunes/author "info@infoq.com")
       (itunes/summary (:summary p))
       ;;(itunes/image (:poster p))
       (itunes/image (first (:slides p)))
       (itunes/duration (:length p))
       (apply psc/chapters (map vector (:times p) (slides) (:slides p)))])))

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
        change-date (or (:publish-date (first entries)) 0)
        channel (feed-channel change-date)
        extensions [:atom :itunes :simple-chapters]]
    (rss/feed channel items extensions)))

(defn- respond [body]
  (fn [_req] {:body body}))

;;; Main

(defroutes api-routes
  "Routes for client API"
  (GET "/api/v1/presentations" {{since-id "since"} :query-params}
    (->> (if since-id (cache/latest 20 since-id) (cache/latest 20))
         (map :data)
         (map #(dissoc % :slides :times :video :audio :pdf))
         respond))
  (GET "/api/v1/presentations/:id" [id]
    (->> id
         (str "/presentations/")
         cache/lookup
         (map :data)
         respond)))

(defroutes feed-routes
  "Routes for RSS Feeds"
  (GET "/feed"       [] (response/redirect "feed/audio"))
  (GET "/feed/audio" [] (respond (serve-feed :audio)))
  (GET "/feed/video" [] (respond (serve-feed :video))))

(defroutes files-routes
  "Routes for file handling and downloads"
  (GET "/presentations/:filename" [filename]
    (response/redirect (infoq/media-link filename))))

(defroutes default-routes
  (GET "/" []
    (let [res (response/resource-response "index.html" {:root "public"})]
      (response/content-type res "text/html")))
  (route/resources "/")
  (route/not-found nil))

(defn- site []
  (routes
    ;; API
    (-> (handler/api api-routes)
        json/wrap-json-response)
    ;; RSS Feed
    (-> feed-routes
        xml/wrap-rss-response)
    ;; RSS Feed media
    files-routes
    ;; Static content
    (handler/site default-routes)))

(defn -main []
  (info "Starting web server")
  (let [port (parse-int (or (System/getenv "PORT") "8080"))]
    (http/run-server (site) {:port port})))
