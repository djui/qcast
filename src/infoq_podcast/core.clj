(ns infoq-podcast.core
  (:gen-class)
  (:require [clojure.string         :as string]
            [infoq-podcast.cache    :as cache]
            [infoq-podcast.catcher  :as catcher]
            [infoq-podcast.feed.rss :as rss]
            ;;[org.httpkit.client     :as http-kit]
            [taoensso.timbre        :as timbre :refer [trace debug info]]))


;;; Utilities

(defn- interspaced
  "Repeatedly execute task-fn following a t ms sleep. If arg is given, pass arg
  to the initial execution and its result to subsequent executions."
  ([t task-fn]
     (future
       (loop []
         (task-fn)
         (Thread/sleep t)
         (recur))))
  ([t task-fn arg]
     (future
       (loop [arg' arg]
         (let [res (task-fn arg')]
           (Thread/sleep t)
           (recur res))))))

(defn- secs [n]
  (* n 1000))


;;; Internals

(defn- prepare-item [p]
  (let [link (:link p)]
    [(rss/title (:title p))
     (rss/link link)
     (rss/description (:summary p))
     (rss/author "info@infoq.com") ;; (string/join ", " (:authors p))
     (rss/pub-date (:date p))
     (rss/guid link)
     (apply rss/enclosure (:video p))
     (apply rss/category (:keywords p))]))

(defn- serve-feed
  "Create and serve the presentation feed."
  [coll]
  (let [channel [(rss/title "InfoQ Presentations Feed")
                 (rss/link "http://www.infoq.com")
                 (rss/description (str "Facilitating the spread of knowledge and "
                                       "innovation in enterprise software development"))
                 (rss/language "en-US")
                 (rss/generator "InfoQ-Feed-Generator/1.0")]
        extensions [:atom :itunes :feedburner :simple-chapters :content :history]
        items (map prepare-item coll)
        feed (rss/feed channel items extensions)]
    (clojure.pprint/pprint feed)))

(defn- cache-updates
  "Scrape the overview sites and collect its oughly 12 items per site until
  finding an seen item (since). Scrape a maximum of limit or 100 items. This
  sequence requires additional two requests (page+video) per item, thus n%12 +
  2*n."
  ([since] (cache-updates since 100))
  ([since limit]
     (info "Check for updates since:" (or since :inf))
     (->> (catcher/latest)
          (take-while #(not= % since))
          (pmap catcher/metadata)
          (map cache/put)
          (dorun (dec limit)))
     (prn "Done.")))


;;; Main

(defn -main []
  (let [task #(cache-updates (cache/latest) 13)]
    (info "Starting")
    (info "Initializing cache")
    (cache/init)
    (info "Spawing update task")
    (interspaced (secs 30) task)))
