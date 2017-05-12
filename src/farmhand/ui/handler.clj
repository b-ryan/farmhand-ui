(ns farmhand.ui.handler
  (:require [clojure.tools.logging :as log]
            [farmhand.core :as farmhand]
            [farmhand.ui.layout :refer [error-page]]
            [farmhand.ui.routes :refer [routes]]
            [farmhand.utils :refer [fatal?]]
            [prone.middleware :refer [wrap-exceptions]]
            [ring.middleware.defaults :refer [site-defaults wrap-defaults]]
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

(defn- wrap-farmhand-context
  [handler context]
  (fn [request]
    (handler (assoc request :farmhand.ui/context context))))

(defn- wrap-dev
  [handler]
  (-> handler
      wrap-error-page
      wrap-exceptions))

(defn- wrap-base
  [handler context]
  (-> handler
      (wrap-defaults site-defaults)
      (wrap-dev)
      (wrap-resource "farmhand-ui/public")
      (wrap-internal-error)
      (wrap-farmhand-context context)))

(defn app
  "Creates an app that can serve farmhand UI routes.

  It accepts one optional argument: a Farmhand context. If one is not given, it
  will first try to use any existing context in the farmhand.core/context* atom
  and if there is none, it will create one with all the default options."
  ([] (app (or @farmhand/context* (farmhand/create-context))))
  ([context]
   (wrap-base #'routes context)))
