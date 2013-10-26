(ns ^{:doc "Payment extenstion: -
            Specification: -"}
  qcast.feed.ext.payment
  (:require [clj-http.client :refer [generate-query-string]]
            [hiccup.util     :refer [escape-html]]))


;;; Internals

(defn- create-url [url query-params]
  (str url "?" (generate-query-string query-params)))


;;; Interface

;; Channel

(defn flattr [user-id url language category title description tags]
  (let [base-url "https://flattr.com/submit/auto"
        query-params {:user_id user-id
                      :url url
                      :language language
                      :category category
                      :title title
                      :description description
                      :tags tags}]
    [:atom:link {:rel "payment"
                 :title "Flattr this!"
                 :href (escape-html (create-url base-url query-params))
                 :type "text/html"}]))


;; Channel or Item


;; Item
