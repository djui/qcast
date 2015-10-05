(ns qcast.hooks-app
  (:require [cheshire.core     :as json]
            [qcast.http-client :as http-client]
            [taoensso.timbre   :refer :all]))


;;; Interface

(defn publish [item alertid apikey]
  (let [url (str "https://api.gethooksapp.com/v1/push/" alertid)
        data {"message" (str "InfoQ presentation: " (:title item) " | " (:summary item))
              "url" (:link item)}
        opts {:headers {"Hooks-Authorization" apikey}
              :content-type :json
              :body (json/generate-string data)
              :as :json}
        res (http-client/post url opts)]
    (debug "Published" (:id item) (:status res) (:body res))))
