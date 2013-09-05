(defproject infoq-podcast "0.1.0"
  :description "InfoQ podcast feed"
  :url "http://infoq-podcast.heroku.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0.0"
  :dependencies [[org.clojure/clojure "1.5.1"]
                 [org.clojars.scsibug/feedparser-clj "0.4.0"]]
  :main infoq-podcast.core
  :uberjar-name "infoq-podcast-standalone.jar")
