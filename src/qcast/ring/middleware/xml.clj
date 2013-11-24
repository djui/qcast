(ns qcast.ring.middleware.xml
  (:require [hiccup.core        :refer [html]]
            [hiccup.page        :refer [xml-declaration]]
            [ring.util.response :refer [content-type]]))

(defn- as-xml
  "Convert a clojure xml structure into a XML string. Accepts the following options:
    :encoding - encoding for xml manifest"
  [tree & [{:keys [encoding] :or {encoding "utf-8"} :as options}]]
  (html (xml-declaration encoding) tree))

(defn- wrap-response
  [handler type encoding]
  (fn [request]
    (let [response (handler request)]
      (if (vector? (:body response))
        (-> response
            (content-type (format "%s; charset=%s" type encoding))
            (update-in [:body] as-xml {:encoding encoding}))
        response))))

(defn wrap-xml-response
  "Middleware that converts responses with a clojure xml structure for a body into a
  XML response."
  [handler]
  (wrap-response handler "application/xml" "utf-8"))

(defn wrap-rss-response
  "Middleware that converts responses with a clojure xml structure for a body into a
  RSS response."
  [handler]
  (wrap-response handler "application/rss+xml" "utf-8"))
