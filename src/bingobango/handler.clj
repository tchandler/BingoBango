(ns bingobango.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :refer [resource-response response]]
            [ring.middleware.json :as middleware]))

(defroutes app-routes
  (GET "/" [] (resource-response "index.html" {:root "public"}))
  (GET "/widgets" [] (response [{:id "1" :name "TOMMY"} {:id "2" :name "RAYAN"}]))
  (route/resources "/")
  (route/not-found "Not Found"))

(def app
  (-> (handler/site app-routes)
  	  (middleware/wrap-json-body)
  	  (middleware/wrap-json-response)))