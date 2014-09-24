(ns bingobango.handler
  (:require [compojure.core :refer :all]
            [compojure.handler :as handler]
            [compojure.route :as route]
            [ring.util.response :refer [resource-response response]]
            [ring.middleware.json :as middleware]))

;; TODO: Figure out how to make this logic conditionally apply to dev
;; mode only
(def request-log (atom '()))
(def max-requests-saved 10)
(defn add-request [req]
  (swap! request-log #(take max-requests-saved (conj % req))))
(defn wrap-request-logging
  [handler]
  (fn [req]
    (add-request req)
    (handler req)))

(defroutes app-routes
  ;;;; Testing out some routes.  These aren't real yet...
  ;; Dump the contents of an html file.  Note that, since this is in
  ;; the public directory, you can also access it via /index.html.  If
  ;; we want to follow this approach but don't want to expose our html
  ;; files like that, we can make a directory in /resources other than
  ;; 'public' and put our more private files there
  (GET "/" [] (resource-response "index.html" {:root "public"}))
  ;; Example of returning dummy JSON data.  The wrap-json-response
  ;; middleware detects certain return types and auto-converts them to JSON
  (GET "/widgets" [] (response [{:id "1" :name "TOMMY"} {:id "2" :name "RAYAN"}]))
  ;; Example of returning a simple string.
  (GET "/werdgetz" [] "STUFF 'n THINGS")
  ;; I believe this automatically exposes any files and sub-dirs
  ;; within the resources folder.  Not yet sure how it knows to use a
  ;; folder named 'resources' specifically
  (route/resources "/")
  ;; Default 404 behavior
  (route/not-found "Not Found"))

(def app
  (->
   (handler/site app-routes)
   (middleware/wrap-json-body)
   (middleware/wrap-json-response)
   (wrap-request-logging)))
