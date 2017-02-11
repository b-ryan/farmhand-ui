(ns farmhand.ui.handler
  (:require [clojure.tools.logging :as log]
            [farmhand.redis :as redis]
            [farmhand.ui.config :as config]
            [farmhand.ui.layout :refer [error-page]]
            [farmhand.ui.routes :refer [routes]]
            [farmhand.utils :refer [fatal?]]
            [prone.middleware :refer [wrap-exceptions]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
            [ring.middleware.reload :refer [wrap-reload]]
            [ring.middleware.resource :refer [wrap-resource]]
            [selmer.middleware :refer [wrap-error-page]]))

(defn- wrap-internal-error
  [handler]
  (fn [req]
    (try
      (handler req)
      (catch Throwable t
        (when (fatal? t) (throw t))
        (log/error t)
        (error-page {:status 500
                     :title "An unexpected error occurred."
                     :message "Please see logs for details."})))))

(defn- wrap-pool
  [handler pool]
  (fn [request]
    (handler (assoc request :farmhand-pool pool))))

(defn- wrap-dev
  [handler]
  (-> handler
      wrap-reload
      wrap-error-page
      wrap-exceptions))

(defn- wrap-base
  [handler pool]
  (-> handler
      (wrap-defaults site-defaults)
      (wrap-dev)
      (wrap-resource "public")
      (wrap-internal-error)
      (wrap-pool pool)))

(defn app
  ([]
   (app {}))
  ([{:keys [redis pool]}]
   (let [pool (or pool (redis/create-pool (config/redis redis)))]
     (wrap-base #'routes pool))))
