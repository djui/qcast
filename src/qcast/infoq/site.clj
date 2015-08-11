(ns qcast.infoq.site
  (:require [qcast.http-client :as http-client]))


;;; Globals

(def ^:private base-url-prefix "https://www.infoq.com")


;;; Internals

(defn- base-url
  ([] base-url-prefix)
  ([& paths]
     (if (.startsWith (first paths) "http") ;; e.g. CDN links
       (reduce str paths)
       (reduce str base-url-prefix paths))))

(defn- resolve-media-url [filename]
  (-> (base-url "/mp3download.action")
      (http-client/head {:query-params {:filename filename}})
      (get-in [:headers "location"])
      ;; (pre-cond not= "http://www.infoq.com/error?sc=404")
      ))

(defn- login! [user pass]
  (http-client/post "https://www.infoq.com/login.action"
             {:form-params {:username user
                            :password pass}}))


;;; Interface

(defn poster-url [file-path]
  (when file-path (base-url file-path)))

(defn slide-url [file-path]
  (when file-path (base-url file-path)))

(defn presentation-url [id]
  (base-url id))

(defn presentation [url]
  (http-client/get url {:follow-redirects false}))

(defn presentations-url [index]
  (base-url "/presentations/" index))

(defn presentations [url]
  (http-client/get url))

(defn media-headers [url]
  (-> url http-client/head :headers))

(defn media-url [filename user pass]
  (let [media-url (str "presentations/" filename)]
    (http-client/with-cookies
      (fn []
        (login! user pass)
        (resolve-media-url media-url)))))
