(ns farmhand.ui.layout
  (:require [farmhand.utils :refer [parse-long]]
            [ring.util.response :refer [content-type]]
            [selmer.filters :as filters]
            [selmer.parser :as parser]))

;; WARNING: do not use parser/set-resource-path!
;; Doing so will cause conflicts & errors when including this repo in another
;; project that also sets its own selmer path
(def custom-resource-path (clojure.java.io/resource "farmhand-ui/templates/"))

(filters/add-filter!
  :farmhand/subs
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

(defn
  found
  "Returns a 302 redirection to the given URL."
  [url]
  {:status 302
   :headers {"Location" url}
   :body ""})
