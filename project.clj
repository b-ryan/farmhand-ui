(defproject com.buckryan/farmhand-ui "0.3.1"
  :description "Farmhand web interface"
  :url "https://github.com/b-ryan/farmhand-ui"
  :deploy-repositories [["releases" :clojars]
                        ["snapshots" :clojars]]
  :dependencies [[com.buckryan/farmhand "0.9.1"]
                 [compojure "1.5.2"]
                 [org.clojure/clojure "1.7.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [prone "1.1.4"]
                 [ring/ring-defaults "0.2.3"]
                 [selmer "1.10.6"]]
  :target-path "target/%s"
  :jvm-opts ["-server" "-Duser.timezone=GMT"]
  :profiles {:dev {:dependencies [[http-kit "2.2.0"]
                                  [log4j "1.2.17"]
                                  [ring/ring-mock "0.3.0"]]
                   :source-paths ["dev-src"]}
             :1.5 {:dependencies [[org.clojure/clojure "1.5.1"]]}
             :1.6 {:dependencies [[org.clojure/clojure "1.6.0"]]}
             :1.7 {:dependencies [[org.clojure/clojure "1.7.0"]]}
             :1.8 {:dependencies [[org.clojure/clojure "1.8.0"]]}
             :1.9 {:dependencies [[org.clojure/clojure "1.9.0-alpha14"]]}})
