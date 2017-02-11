(ns farmhand.ui.config
  (:require [farmhand.config :refer [merge*] :as root-config]))

(def redis #'root-config/redis)

(defn http-port
  []
  (merge* ["FARMHAND_UI_HTTP_PORT" :int] [:ui :http-port] 8888))

(defn- truthy?
  [s]
  (and (seq s)
       (not (#{"no" "false" "0"} (.toLowerCase s)))))

(defn use-nrepl?
  []
  (merge* ["FARMHAND_UI_USE_NREPL" truthy?] [:ui :use-nrepl] true))

(defn nrepl-port
  []
  (merge* ["FARMHAND_UI_NREPL_PORT" :int] [:ui :nrepl-port] 7000))

(defn nrepl-bind
  []
  (merge* ["FARMHAND_UI_NREPL_BIND"] [:ui :nrepl-bind] "localhost"))
