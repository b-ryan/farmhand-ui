(defproject com.buckryan/farmhand-ui "0.1.3-SNAPSHOT"
  :description "Farmhand web interface"
  :url "https://github.com/b-ryan/farmhand-ui"
  :deploy-repositories [["releases" :clojars]
                        ["snapshots" :clojars]]
  :dependencies [[com.buckryan/farmhand "0.7.0"]
                 [compojure "1.5.2"]
                 [http-kit "2.2.0"]
                 [org.clojure/clojure "1.7.0"]
                 [org.clojure/tools.logging "0.3.1"]
                 [org.clojure/tools.nrepl "0.2.12"]
                 [prone "1.1.4"]
                 [ring/ring-defaults "0.2.3"]
                 [selmer "1.10.6"]]
  :main ^:skip-aot farmhand.ui.core
  :target-path "target/%s"
  :jvm-opts ["-server" "-Duser.timezone=GMT"]
  :profiles {:uberjar {:aot :all}
             :dev {:dependencies [[log4j "1.2.17"]
                                  [ring/ring-mock "0.3.0"]]}
             :1.5 {:dependencies [[org.clojure/clojure "1.5.1"]]}
             :1.6 {:dependencies [[org.clojure/clojure "1.6.0"]]}
             :1.7 {:dependencies [[org.clojure/clojure "1.7.0"]]}
             :1.8 {:dependencies [[org.clojure/clojure "1.8.0"]]}
             :1.9 {:dependencies [[org.clojure/clojure "1.9.0-alpha14"]]}})
