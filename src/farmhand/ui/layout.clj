(ns farmhand.ui.layout
  (:require [farmhand.utils :refer [parse-long]]
            [ring.util.response :refer [content-type]]
            [selmer.filters :as filters]
            [selmer.parser :as parser]))

;; WARNING: do not use parser/set-resource-path!
;; Doing so will cause conflicts & errors when including this repo in another
;; project that also sets its own selmer path
(def custom-resource-path (clojure.java.io/resource "farmhand-ui/templates/"))

(defn- missing-value-fn
  [tag context-map]
  (throw (ex-info "tag is missing" {:tag tag})))

;; FIXME these should not be global settings - figure out how to not conflict
;; with projects that use this repo as a dependency
(parser/cache-off!)
(selmer.util/set-missing-value-formatter! missing-value-fn :filter-missing-values true)

(filters/add-filter!
  :subs ;; FIXME namespace this?
  (fn
    ([s start] (subs s (parse-long start)))
    ([s start end] (subs s (parse-long start) (parse-long end)))))

(defn render
  "A wrapper around Selmer that sets up the custom resource path."
  [template & [params]]
  (parser/render-file
    template
    (assoc params :page template)
    {:custom-resource-path custom-resource-path}))

(defn render-200
  "renders the HTML template and returns a ring 200 response.

  'args' are passed directly to the render function."
  [& args]
  {:status 200
   :headers {"Content-Type" "text/html; charset=utf-8"}
   :body (apply render args)})

(defn error-page
  "error-details should be a map containing the following keys:
   :status - error status
   :title - error title (optional)
   :message - detailed error message (optional)

   returns a response map with the error page as the body
   and the status specified by the status key"
  [error-details]
  {:status  (:status error-details)
   :headers {"Content-Type" "text/html; charset=utf-8"}
   :body    (render "error.html" error-details)})
