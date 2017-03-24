(ns farmhand.ui.routes
  (:require [compojure.coercions :refer [as-int]]
            [compojure.core :refer [GET POST defroutes]]
            [compojure.route :as route]
            [farmhand.dead-letters :as dead-letters]
            [farmhand.jobs :as jobs]
            [farmhand.queue :as queue]
            [farmhand.registry :as registry]
            [farmhand.ui.layout :as layout :refer [error-page]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]))

(def ^:private ^Integer page-size 25)

(defn found
  [url]
  {:status 302
   :headers {"Location" url}
   :body ""})

(defn- render-registry-page
  [request template redis-key]
  (layout/render
    template
    (let [page (as-int (get-in request [:query-params "page"]))]
      (assoc (registry/page redis-key
                            (:farmhand-pool request)
                            {:page page})
             :anti-forgery-field (anti-forgery-field)))))

(defroutes routes
  (GET "/" [] (found "/failed"))

  (GET "/queues" request
       (layout/render
         "queues.html"
         {:queues (queue/describe-queues (:farmhand-pool request))
          :anti-forgery-field (anti-forgery-field)}))

  (POST "/queues/:queue-name/purge" [queue-name :as request]
        (queue/purge (:farmhand-pool request) queue-name)
        (found "/queues"))

  (GET "/in-flight" request
       (render-registry-page request "registries/in_flight.html" (queue/in-flight-key)))
  (GET "/completed" request
       (render-registry-page request "registries/completed.html" (queue/completed-key)))
  (GET "/failed" request
       (render-registry-page request "registries/failed.html" (dead-letters/dead-letter-key)))

  (GET "/jobs/:job-id" [job-id :as request]
       (let [job (jobs/fetch-body job-id (:farmhand-pool request))]
         (layout/render
           "job_details.html"
           {:job job
            :anti-forgery-field (anti-forgery-field)})))

  (POST "/jobs/:job-id/requeue" [job-id :as request]
        (dead-letters/requeue job-id (:farmhand-pool request))
        (found (str "/jobs/" job-id)))

  (route/not-found
    (:body
      (error-page {:status 404
                   :title "page not found"}))))
