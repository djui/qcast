(defproject qcast "1.1.1"
  :description "QCast - InfoQ presentation podcast"
  :url "http://infoqcast.herokuapp.com/"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0.0"
  :uberjar-name "qcast-standalone.jar"
  :main qcast.main
  :aot :all
  :profiles {:production {}}
  :dependencies [[clj-http "2.0.0"]
                 [clj-time "0.11.0"]
                 [com.taoensso/timbre "4.1.4"]
                 [compojure "1.4.0"]
                 [enlive "1.1.6"]
                 [hiccup "1.0.5"]
                 [http-kit "2.1.18"]
                 [javax.servlet/servlet-api "2.5"]
                 [org.clojure/clojure "1.7.0"]
                 [org.clojure/java.jdbc "0.4.2"]
                 [org.xerial/sqlite-jdbc "3.8.11.2"]
                 [ring-ratelimit "0.2.2"]
                 [ring/ring-core "1.4.0"]
                 [ring/ring-json "0.4.0"]])
