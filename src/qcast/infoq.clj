(ns qcast.infoq
  (:require [clojure.string   :as string :refer [split]]
            [clj-http.client  :as http]
            [clj-http.cookies :as cookies]
            [qcast.html       :as html]
            [qcast.util       :as util :refer :all])
  (:refer-clojure :exclude [resolve]))


;;; Globals

(def ^:private username "infoqcast@gmail.com")

(def ^:private password "InfoQCast123")

(def ^:private ios-user-agent
  (str "Mozilla/5.0 (iPhone; CPU iPhone OS 7_0 like Mac OS X) "
       "AppleWebKit/537.51.1 (KHTML, like Gecko) "
       "Version/7.0 "
       "Mobile/11A465 "
       "Safari/9537.53"))

(def ^:private http-options
  {:headers {"User-Agent" ios-user-agent}
   :as :stream})


;;; Internals

(defn- HEAD [url & [opts]]
  (http/head url (merge http-options {:follow-redirects false} opts)))

(defn- GET [url & [opts]]
  (http/get url (merge http-options opts)))

(defn- POST [url & [opts]]
  (http/post url (merge http-options opts)))

(defn- overview-ids [dom]
  (html/select-all [:.news_type_video :> :a] #(html/attr :href %) dom))

(declare base-url)
(defn- resolve [filename]
  (-> (base-url "/mp3download.action")
      (HEAD {:query-params {:filename filename}})
      (get-in [:headers "location"])
      ;; (pre-cond not= "http://www.infoq.com/error?sc=404")
      ))

;;; Interface

(defn base-url [& s]
  (apply str "http://www.infoq.com" s))

(defn sbase-url [& s]
  (apply str "https://www.infoq.com" s))

(defn poster-url [file-path]
  (base-url file-path))

(defn slide-url [file-path]
  (base-url file-path))

(defn presentation-url [id]
  (base-url id))

(defn login [user pass]
  (POST (sbase-url "/login.action")
        {:form-params {:username user
                       :password pass}}))

(defn media-link [filename]
  (binding [clj-http.core/*cookie-store* (cookies/cookie-store)]
    (login username password)
    (resolve (str "presentations/" filename))))

(defn media-meta [url]
  (let [headers (:headers (HEAD url))
        length (parse-int (get headers "content-length"))
        type (some-> (get headers "content-type") (split #";") first)]
    (if (= type "text/html")
      [url length nil] ;; discard the usual
      [url length type])))

(defn presentations [index]
  (-> (base-url "/presentations/" index) GET html/dom overview-ids))

(defn presentation [id]
  (-> (presentation-url id) GET))
