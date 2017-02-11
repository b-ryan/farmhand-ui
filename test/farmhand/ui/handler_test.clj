(ns farmhand.ui.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :refer :all]
            [farmhand.ui.handler :refer :all]))

(deftest test-redirect
  (testing "main route redirects"
    (let [response ((app) (request :get "/"))]
      (is (= 302 (:status response)))
      (is (= "http://localhost/failed" (get-in response [:headers "Location"])))))
  (testing "not-found route"
    (let [response ((app) (request :get "/invalid"))]
      (is (= 404 (:status response))))))
