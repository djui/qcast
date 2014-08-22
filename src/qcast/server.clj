(ns qcast.server
  (:gen-class)
  (:require [clojure.string            :as s]
            [compojure.core            :refer [defroutes routes GET]]
            [compojure.handler         :as handler]
            [compojure.route           :as route]
            [config]
            [org.httpkit.server        :as http]
            [qcast.cache               :as cache]
            [qcast.feed.feed           :as feed]
            [qcast.infoq.site          :as infoq]
            [qcast.ring.middleware.xml :as xml]
            [qcast.util                :refer [parse-int]]
            [ring.middleware.json      :as json]
            [ring.middleware.ratelimit :refer [wrap-ratelimit]]
            [ring.util.response        :as response]
            [taoensso.timbre           :refer :all]))


;;; Internals

(defn wrap-request-logging [handler]
  (fn [{:keys [request-method uri] :as req}]
    (debug request-method uri)
    (handler req)))

(defn- respond [body]
  (if body
    (fn [_req] {:body body})
    (route/not-found nil)))

(defn- json-keyword [kw]
  (-> kw name (s/replace "-" "_")))


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
         first
         :data
         respond)))

(defroutes feed-routes
  "Routes for RSS Feeds"
  (GET "/feed"       [] (response/redirect "feed/audio"))
  (GET "/feed/audio" [] (respond (feed/serve :audio (cache/latest 50))))
  (GET "/feed/video" [] (respond (feed/serve :video (cache/latest 50)))))

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
    (-> api-routes
        wrap-request-logging
        handler/api
        (json/wrap-json-response {:key-fn json-keyword})
        wrap-ratelimit)
    ;; RSS Feed
    (-> feed-routes
        wrap-request-logging
        xml/wrap-rss-response
        wrap-ratelimit)
    ;; RSS Feed media
    (-> files-routes
        wrap-request-logging
        wrap-ratelimit)
    ;; Static content
    (-> default-routes
        wrap-request-logging
        handler/site)))

(defn -main []
  (let [port (parse-int (or (System/getenv "PORT") "8080"))]
    (info "Starting web server on port" port)
    (http/run-server (site) {:port port})))
