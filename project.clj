(defproject infoq-podcast "1.0.0"
  :description "InfoQ podcast feed"
  :url "http://infoq-podcast.heroku.com"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0.0"
  :uberjar-name "infoq-podcast-standalone.jar"
  :main infoq-podcast.core
  :dependencies [[clj-http "0.7.7"]
                 [clj-time "0.6.0"]
                 [com.taoensso/timbre "2.6.2"]
                 [compojure "1.1.5"]
                 [enlive "1.1.4"]
                 [hiccup "1.0.4"]
                 [http-kit "2.1.10"]
                 [org.clojure.contrib/djui "1.9"]
                 [org.clojure/clojure "1.5.1"]
                 [org.clojure/java.jdbc "0.3.0-alpha5"]
                 [org.xerial/sqlite-jdbc "3.7.2"]])
