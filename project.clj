(defproject infoq-podcast "1.0.0"
  :description "InfoQ podcast feed"
  :url "http://infoq-podcast.heroku.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0.0"
  :dependencies [;; [clj-http-lite "0.2.0"]
                 ;; [http-kit "2.1.10"]
                 [clj-http "0.7.6"]
                 [enlive "1.1.4"]
                 [org.clojars.scsibug/feedparser-clj "0.4.0"]
                 [org.clojure/clojure "1.5.1"]]
  :main infoq-podcast.core
  :uberjar-name "infoq-podcast-standalone.jar")
