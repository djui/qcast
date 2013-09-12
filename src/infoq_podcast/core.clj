(ns infoq-podcast.core
  (:gen-class)
  (:require [clj-http.client :as http]
             [feedparser-clj.core :as feedparser]
             [net.cgrand.enlive-html :as css]))


(defn user-token
  []
  (let [url "http://www.infoq.com"
        cookie-store (clj-http.cookies/cookie-store)]
    (binding [clj-http.core/*cookie-store* (clj-http.cookies/cookie-store)]
      (http/head url)
      (http/get url))
    (http/head url {:cookie-store cookie-store
                    :headers {"User-Agent" "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_0 like Mac OS X; en-us) AppleWebKit/532.9 (KHTML, like Gecko) Version/4.0.5 Mobile/8A293 Safari/6531.22.7"}})
    (http/get url {:cookie-store cookie-store
                   :headers {"User-Agent" "Mozilla/5.0 (iPhone; U; CPU iPhone OS 4_0 like Mac OS X; en-us) AppleWebKit/532.9 (KHTML, like Gecko) Version/4.0.5 Mobile/8A293 Safari/6531.22.7"}})))

(defn rss-feed-url
  []
  (let [url "http://www.infoq.com"
        response @(http/get url)
        html (:body response)
        token-pattern #"(?s).*updateRssLinks\('([a-zA-Z0-9]*)'\).*"
        matches (re-matches token-pattern html)]
    (second matches)))

(defn create-feed
  []
  (let [url "http://www.infoq.com"
        sel [:#headerRssLink]
        response @(http/get url {:as :stream})
        body (:body response)
        html (css/html-resource body)
        rss-link (-> html (css/select sel) first :attrs :href)]
    rss-link))

(defn -main []
  (pr
   (user-token)))

; on receive service url with credentials:
;   log into infq
;   check for success
;   continue with on receive service url without credentials / alternative:

; on receive serivce url without credentials:
;   go to start page
;   store cookie
;   get rss feed url
;   parse rsss feed
;   filter presentations
;   continue with on receive presentation list

; on receive service url without credentials alternative:
;   go to presentation overview start page
;   parse last x presentation overview pages
;   continue with on receive presentation list

; on receive presentation list:
;   go to presentation sites
;   extract presentation details
;   re-create rss feed with presentation info
;   modify audio and pdf urls to point to service instead

; on receive audio or pdf url:
;   login to infoq
;   check for sucess
;   got to presentation page
;   get file url
;   redirect
