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
  :dependencies [[clj-http "0.9.2"]
                 [clj-time "0.7.0"]
                 [com.taoensso/timbre "3.2.1"]
                 [compojure "1.1.8"]
                 [enlive "1.1.5"]
                 [hiccup "1.0.5"]
                 [http-kit "2.1.16"]
                 [javax.servlet/servlet-api "2.5"]
                 [org.clojure/clojure "1.6.0"]
                 [org.clojure/java.jdbc "0.3.0-alpha5"]
                 [org.xerial/sqlite-jdbc "3.7.2"]
                 [ring-ratelimit "0.2.2"]
                 [ring/ring-core "1.3.0"]
                 [ring/ring-json "0.3.1"]])
