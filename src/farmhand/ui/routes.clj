(ns farmhand.ui.routes
  (:require [compojure.coercions :refer [as-int]]
            [compojure.core :refer [GET POST routes context]]
            [compojure.route :as route]
            [farmhand.jobs :as jobs]
            [farmhand.queue :as queue]
            [farmhand.registry :as registry]
            [farmhand.ui.layout :as layout :refer [error-page]]
            [ring.util.anti-forgery :refer [anti-forgery-field]]
            [clojure.string :refer [replace]]))

(defn- render-registry-page
  [request template registry-name prefix]
  (layout/render-200
    template
    (let [page (as-int (get-in request [:query-params "page"]))]
      (assoc (registry/page (:farmhand.ui/context request)
                            registry-name
                            {:page page})
             :prefix prefix
             :anti-forgery-field (anti-forgery-field)))))

(defn- normalize-prefix [prefix]
  (let [prefix (or prefix "/")]
    (if (= (first prefix) \/)
      prefix
      (str "/" prefix))))

(defn endpoints [prefix]
  (let [prefix (normalize-prefix prefix)
        redirect #(layout/found (replace (str prefix %) #"//" "/"))]

    (routes
      (context prefix []

        (GET "/" []
          (redirect "/queues"))

        (GET "/queues" request
             (layout/render-200
               "queues.html"
               {:queues (queue/describe-queues (:farmhand.ui/context request))
                :prefix prefix
                :anti-forgery-field (anti-forgery-field)}))

        (POST "/queues/:queue-name/purge" [queue-name :as request]
              (queue/purge (:farmhand.ui/context request) queue-name)
              (redirect "/queues"))

        (GET "/in-flight" request
             (render-registry-page request "registries/in_flight.html" queue/in-flight-registry prefix))

        (GET "/scheduled" request
             (render-registry-page request "registries/scheduled.html" queue/scheduled-registry prefix))

        (GET "/completed" request
             (render-registry-page request "registries/completed.html" queue/completed-registry prefix))

        (GET "/failed" request
             (render-registry-page request "registries/failed.html" queue/dead-letter-registry prefix))

        (GET "/jobs/:job-id" [job-id :as request]
             (let [job (jobs/fetch (:farmhand.ui/context request) job-id)]
               (layout/render-200
                 "job_details.html"
                 {:job job
                  :prefix prefix
                  :anti-forgery-field (anti-forgery-field)})))

        (POST "/jobs/:job-id/requeue" [job-id :as request]
              (queue/requeue (:farmhand.ui/context request) job-id)
              (redirect (str "/jobs/" job-id)))

        (route/not-found
          (:body
            (error-page {:status 404
                         :title "page not found"})))))))
