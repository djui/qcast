(ns qcast.feed.feed
  (:require [qcast.feed.ext.atom            :as atom]
            [qcast.feed.ext.itunes          :as itunes]
            [qcast.feed.ext.simple-chapters :as psc]
            [qcast.feed.rss                 :as rss]))


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
  (let [base-url "https://www.infoq.com"
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
     (atom/link "https://infoqcast.herokuapp.com/feed")
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


;;; Interface

(defn serve [media-type data]
  (let [entries (map :data data)
        items (map #(feed-item media-type %) entries)
        change-date (or (:publish-date (first entries)) 0)
        channel (feed-channel change-date)
        extensions [:atom :itunes :simple-chapters]]
    (rss/feed channel items extensions)))
