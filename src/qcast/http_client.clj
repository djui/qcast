(ns qcast.http-client
  (:require [clj-http.client  :as http]
            [clj-http.cookies :as cookies]
            [taoensso.timbre  :refer :all])
  (:refer-clojure :exclude [get]))


;;; Globals

(def ^:private ios-user-agent
  (str "Mozilla/5.0 (iPad; CPU OS 7_0 like Mac OS X) "
       "AppleWebKit/537.51.1 (KHTML, like Gecko) "
       "Version/7.0 "
       "Mobile/11A465 "
       "Safari/9537.53"))

(def ^:private http-options
  {:headers   {"User-Agent" ios-user-agent}
   :insecure? true
   :as        :stream})


;;; Interface

(defn head [url & [opts]]
  (debug :head url opts)
  (http/head url (merge http-options {:follow-redirects false} opts)))

(defn get [url & [opts]]
  (debug :get url opts)
  (http/get url (merge http-options opts)))

(defn post [url & [opts]]
  (debug :post url opts)
  (http/post url (merge http-options opts)))

(defn with-cookies [fn]
  (binding [clj-http.core/*cookie-store* (cookies/cookie-store)]
    (fn)))
