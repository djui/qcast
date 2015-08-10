(ns qcast.infoq.site
  (:require [clj-http.cookies :as cookies]
            [qcast.http       :as http]
            [qcast.util       :refer :all]
            [taoensso.timbre  :refer :all])
  (:refer-clojure :exclude [resolve]))


;;; Globals

(def ^:private base-url-prefix "https://www.infoq.com")


;;; Internals

(declare base-url)
(defn- resolve [filename]
  (-> (base-url "/mp3download.action")
      (http/head {:query-params {:filename filename}})
      (get-in [:headers "location"])
      ;; (pre-cond not= "http://www.infoq.com/error?sc=404")
      ))


;;; Interface

(defn base-url
  ([] base-url-prefix)
  ([& paths]
     (if (.startsWith (first paths) "http") ;; e.g. CDN links
       (reduce str paths)
       (reduce str base-url-prefix paths))))

(defn poster-url [file-path]
  (when file-path (base-url file-path)))

(defn slide-url [file-path]
  (when file-path (base-url file-path)))

(defn presentation-url [id]
  (base-url id))

(defn login [user pass]
  (http/post "https://www.infoq.com/login.action"
        {:form-params {:username user
                       :password pass}}))

(defn media-link [filename user pass]
  (binding [clj-http.core/*cookie-store* (cookies/cookie-store)]
    (login user pass)
    (resolve (str "presentations/" filename))))
