(ns user
  (:require [clojure.tools.logging :as log]
            [farmhand.ui.config :as config]
            [farmhand.ui.handler :as handler]
            [org.httpkit.server :refer [run-server]]))

(defn- missing-value-fn
  [tag context-map]
  (throw (ex-info "tag is missing" {:tag tag})))

(selmer.parser/cache-off!)
(selmer.util/set-missing-value-formatter! missing-value-fn :filter-missing-values true)

(defn run
  []
  (let [port (config/http-port)]
    (run-server (handler/app) {:port port})
    (log/infof "Server started on port %d" port)))
