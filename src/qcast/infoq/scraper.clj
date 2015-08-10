(ns qcast.infoq.scraper
  (:gen-class)
  (:require [clj-time.coerce  :as time-coerce]
            [clj-time.format  :as time]
            [clojure.string   :refer [split trim]]
            [qcast.cache      :as cache]
            [qcast.html       :refer :all]
            [qcast.infoq.site :as infoq]
            [qcast.util       :refer :all]
            [taoensso.timbre  :refer :all])
  (:refer-clojure :exclude [meta]))


;;; Internals

(defn- host-url
  "A bit of a hack to get a full url that works for production and
  local testing."
  [& paths]
  (if-let [host (System/getenv "HOST")]
    (apply str "https://" host "/" paths)
    (apply str "http://localhost:8080/" paths)))

;; Scraping Internals

(defn- poster [dom]
  (meta :property "og:image" infoq/poster-url dom))

(defn- keywords [dom]
  (letfn [(split-keywords [s]
            (let [[lowercase-kw & uppercase-kw] (split s #",")
                  lowercase-kw (split lowercase-kw #" ")]
              (map trim (concat lowercase-kw uppercase-kw))))]
    (meta "keywords" split-keywords dom)))

(defn- summary [dom]
  (meta "description" trim dom))

(defn- title [dom]
  (select [:head :title] inner-text dom))

(defn- authors [dom]
  (let [authors-split #(split % #"(\s*[,;&]\s*|\s+and\s+)")
        transformer #(some->> % inner-text authors-split (map trim))]
    (select [:.author_general :> :a] transformer dom)))

(defn- length [dom]
  (let [transformer #(some-> % inner-text interval->sec)]
    (select [:.videolength2] transformer dom)))

(defn- pdf [dom]
  (let [transformer #(some->> % (attr :value) (host-url))
        url (select [:#pdfForm :> [:input (attr= :name "filename")]] transformer dom)]
  (when url
    [url 0 "application/pdf"]))) ;; Size yet unknown

(defn- audio [dom]
  (let [transformer #(some->> % (attr :value) (host-url))
        url (select [:#mp3Form :> [:input (attr= :name "filename")]] transformer dom)]
    (when url
      [url 0 "audio/mpeg"]))) ;; Size yet unknown

(defn- video [dom]
  (let [transformer #(some->> % (attr :src) infoq/media-meta)]
    (select [:#video :> :source] transformer dom)))

(defn- record-date [dom]
  (let [transformer #(some->> % (attr :src)
                              (re-find #"/([0-9]{2}-[a-z]{3})-.*$") second
                              (time/parse (time/formatter "yy-MMM"))
                              time-coerce/to-date)]
    (select [:#video :> :source] transformer dom)))

(defn- publish-date [dom]
  (meta "tprox" (comp time-coerce/to-date parse-int) dom))

(defn- online-date [dom]
  (let [transformer #(some->> % (inner-text 2)
                              (re-find #"(?s)on\s*(.*)") second
                              (time/parse (time/formatter "MMM dd, yyyy"))
                              time-coerce/to-date)]
    (select [:.author_general] transformer dom)))

(defn- slides [dom]
  (let [transformer #(some->> % inner-text (re-find #"var slides.*"))
        filter #(some->> % (some identity) (re-seq #"'(.+?)'")
                         (map (comp infoq/slide-url second)))]
    (select-all [:script] transformer filter dom)))

(defn- times [dom]
  (let [transformer #(some->> % inner-text (re-find #"TIMES.*"))
        filter #(some->> % (some identity) (re-seq #"(\d+?),")
                         (map (comp parse-int second)))]
    (select-all [:script] transformer filter dom)))


;; Scraping API

(defn- metadata [id]
  (let [md-keys [:id :link :poster :keywords :summary :title :authors
                 :record-date :publish-date :online-date
                 :length :pdf :audio :video :slides :times]
        md-vals (juxt (constantly id) (constantly (infoq/presentation-url id))
                      poster keywords summary title authors
                      record-date publish-date online-date
                      length pdf audio video slides times)]
    (debug "Fetching presentation" id)
    (log-errors (some->> id
                         infoq/presentation
                         dom
                         md-vals
                         (zipmap md-keys)))))

(defn- latest
  ([] (latest 0))
  ([marker]
     (debug "Fetching overview from index" marker)
     (let [items (log-errors (infoq/presentations marker))]
       (if (empty? items)
         (if (> marker 0)
           (info "No more items found")
           (warn "No items found. HTML/CSS layout changed?"))
         (lazy-cat items (latest (+ marker (count items))))))))

(defn- cache-updates
  "Scrape the overview sites and collect its (mostly) 12 items per site until
  finding an seen item (`until`). Scrape a maximum of `limit` or a configured
  number or items. This sequence requires one additional GET (`page`) + three
  HEAD (video, audio, pdf) requests per item, thus n%12 + 2*n."
  [limit]
  (let [until (cache/latest)
        until-id (or (:id until) :inf)]
    (info "Check for updates up until" until-id)
    (->> (latest)
         (take-while #(not= % until-id))
         (pmap metadata)
         (filter identity)
         (take limit)
         (map cache/put)
         doall)))


;;; Interface

;; Main

(defn -main [& args]
  (config/load!)
  (info "Starting catcher")
  (let [limit (config/get :catcher :lookback-count)
        interval (config/get :catcher :update-interval)
        task #(debug "Updated" (count (cache-updates limit)))]
    (if (= (first args) "once")
      (do (info "Running once")
          (task))
      (do (info "Running periodically")
          (interspaced (seconds interval) #(logged-future (task)))))))
