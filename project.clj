(defproject qcast "1.0.0"
  :description "QCast - InfoQ presentation podcast"
  :url "http://qcast.herokuapp.com/"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :min-lein-version "2.0.0"
  :uberjar-name "qcast-standalone.jar"
  :main qcast.main
  :dependencies [[clj-http "0.7.7"]
                 [clj-time "0.6.0"]
                 [com.taoensso/timbre "2.6.2"]
                 [compojure "1.1.5"]
                 [enlive "1.1.4"]
                 [hiccup "1.0.4"]
                 [http-kit "2.1.10"]
                 [org.clojure/clojure "1.5.1"]
                 [org.clojure/java.jdbc "0.3.0-alpha5"]
                 [org.xerial/sqlite-jdbc "3.7.2"]]
  :profiles {:production {}})
