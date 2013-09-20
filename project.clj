(defproject infoq-podcast "1.0.0"
  :description "InfoQ podcast feed"
  :url "http://infoq-podcast.heroku.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0.0"
  :dependencies [[clj-http "0.7.7"]
                 [clj-time "0.6.0"]
                 [com.taoensso/timbre "2.6.2"]
                 [enlive "1.1.4"]
                 [hiccup "1.0.4"]
                 [korma "0.3.0-RC5"]
                 [org.clojure/clojure "1.5.1"]
                 [org.clojure/java.jdbc "0.3.0-alpha5"]
                 [org.xerial/sqlite-jdbc "3.7.2"]]
  :main infoq-podcast.core
  :uberjar-name "infoq-podcast-standalone.jar")
