(ns farmhand.ui.layout
  (:require [farmhand.utils :refer [parse-long]]
            [ring.util.response :refer [content-type]]
            [selmer.filters :as filters]
            [selmer.parser :as parser]))

(declare ^:dynamic *app-context*)
(parser/set-resource-path! (clojure.java.io/resource "templates"))
(parser/cache-off!)

(defn- missing-value-fn
  [tag context-map]
  (throw (ex-info "tag is missing" {:tag tag})))

(selmer.util/set-missing-value-formatter! missing-value-fn :filter-missing-values true)

(filters/add-filter!
  :subs
  (fn
    ([s start] (subs s (parse-long start)))
    ([s start end] (subs s (parse-long start) (parse-long end)))))

(defn render
  "renders the HTML template located relative to resources/templates"
  [template & [params]]
  {:status 200
   :headers {"Content-Type" "text/html; charset=utf-8"}
   :body (parser/render-file
           template
           (assoc params :page template))})

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
   :body    (parser/render-file "error.html" error-details)})
