(ns farmhand.ui.core
  (:require [clojure.tools.logging :as log]
            [clojure.tools.nrepl.server :as nrepl]
            [farmhand.ui.config :as config]
            [farmhand.ui.handler :as handler]
            [org.httpkit.server :refer [run-server]])
  (:gen-class))

(defn -main
  [& args]
  (when (config/use-nrepl?)
    (let [bind (config/nrepl-bind)
          port (config/nrepl-port)]
      (nrepl/start-server :bind bind :port port)
      (log/infof "Started nrepl - nrepl://%s:%d" bind port)))
  (let [port (config/http-port)]
    (run-server (handler/app) {:port port})
    (log/infof "Server started on port %d" port)))
