(ns farmhand.ui.handler-test
  (:require [clojure.test :refer :all]
            [ring.mock.request :refer :all]
            [farmhand.ui.handler :refer :all]
            [farmhand.core :as farmhand]))

(deftest test-redirect

  (testing "main route redirects"
    (let [response ((app) (request :get "/"))]
      (is (= 302 (:status response)))
      (is (= "http://localhost/queues" (get-in response [:headers "Location"])))))

  (testing "not-found route"
    (let [response ((app) (request :get "/invalid"))]
      (is (= 404 (:status response))))))

(deftest testing-prefixed-routes

  (testing "works with prefix routes"
    (let [response ((app (farmhand/create-context) "/farmhand") (request :get "/farmhand/"))]
      (is (= 302 (:status response)))
      (is (= "http://localhost/farmhand/queues" (get-in response [:headers "Location"])))))

  (doseq [page ["queues" "scheduled" "in-flight" "completed" "failed"]]
    (testing (str "returns properly the " page " prefixed page")
      (let [response ((app (farmhand/create-context) "farmhand") (request :get (str "/farmhand/" page)))]
        (is (= 200 (:status response))))))

  (testing "works with prefix routes without slash"
   (let [response ((app (farmhand/create-context) "farmhand") (request :get "/farmhand/"))]
     (is (= 302 (:status response)))
     (is (= "http://localhost/farmhand/queues" (get-in response [:headers "Location"])))))

  (testing "returns 404 once prefixed routes not found"
    (let [response ((app (farmhand/create-context) "/farmhand") (request :get "/farmhand/invalid"))]
      (is (= 404 (:status response))))))
