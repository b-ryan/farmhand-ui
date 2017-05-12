(ns farmhand.ui.config
  (:require [farmhand.config :refer [merge*]]))

(defn http-port
  []
  (merge* ["FARMHAND_UI_HTTP_PORT" :int] [:ui :http-port] 8888))
