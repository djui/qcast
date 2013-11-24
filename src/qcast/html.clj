(ns qcast.html
  (:require [clojure.string         :refer [trim]]
            [net.cgrand.enlive-html :as css])
  (:refer-clojure :exclude [meta]))


;;; Interface

(defn dom [http-response]
  (-> http-response :body css/html-resource))

(defn select-all
  ([selector dom] (select-all selector identity dom))
  ([selector transformer dom] (select-all selector transformer identity dom))
  ([selector transformer filter dom]
    (->> (css/select dom selector) (map transformer) filter)))

(defn select
  ([selector dom] (select selector identity dom))
  ([selector transformer dom]
    (-> (css/select dom selector) first transformer)))

(defn attr [name dom]
  (get-in dom [:attrs name]))

(def attr= css/attr=)

(defn meta
  ([value dom] (meta value identity dom))
  ([value transformer dom] (meta :name value transformer dom))
  ([key value transformer dom]
    (let [transformer' #(->> % (attr :content) transformer)]
      (select [:head [:meta (css/attr= key value)]] transformer' dom))))

(defn inner-text
  ([node] (inner-text 0 node))
  ([n node] (some-> (:content node) (nth n) trim)))
