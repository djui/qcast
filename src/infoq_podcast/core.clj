(ns infoq-podcast.core
  (:gen-class)
  (:require [infoq-podcast.feed         :as feed]
            [infoq-podcast.presentation :as presentation]
            ;;[org.httpkit.client     :as http-kit]
            [taoensso.timbre            :as timbre :refer [trace debug info]]))


;;; Main

(defn presentations->rss-items
  "<channel>
     <title>InfoQ Personalized Feed for ...</title>
     <link>http://www.infoq.com</link>
     <description>This RSS feed is a personalized ...</description>
     <language>en-US</language> 
   </channel>
   <item>
     <title>Presentation: The Future of the JVM</title>
     <link>http://www.infoq.com/presentations/jvm-future-parallelism-cores</link>
     <description>The panelists discuss the future of the JVM in the context of parallelism and high concurrency of tomorrow’s thousands of cores. &lt;i&gt;By Jamie Allen, Cliff Click, Charlie Hunt, Doug Lea, Michael Pilquist&lt;/i&gt;</description>
     <pubDate>Tue, 17 Sep 2013 01:35:00 GMT</pubDate>
     <guid>http://www.infoq.com/presentations/jvm-future-parallelism-cores</guid>
     <category>JVM</category>
     <category>Concurrency</category>
     <category>Parallel Programming</category>
     <category>Multi-core</category>
     <category>Architecture &amp; Design</category>
     <category>Panel</category>
     <category>ETE 2013</category>
     <category>content.presentations.category</category>
     <dc:creator>Jamie Allen, Cliff Click, Charlie Hunt, Doug Lea, Michael Pilquist</dc:creator>
     <dc:date>2013-09-17T01:35:00Z</dc:date>
     <dc:identifier>/presentations/jvm-future-parallelism-cores</dc:identifier>
   </item>"
  [presentation]
  (let [from-keys [:id :poster :keywords :summary :title :authors :date
                   (:video :length) (:slides :times) (base-url :id)]
        to-keys [:guid :? :category :description :title :author :pubDate :?
                 :enclosure :link]]
    ))

(defn -main []
  (debug "Starting")
  (let [presentations (map presentation/metadata (take 4 (presentation/latest)))
        meta {}
        items [{}]
        feed (feed/create :rss meta items)]
    (clojure.pprint/pprint feed)
    ))
