(defproject mqtt-broker "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :main mqtt-broker.core
  :repl-options {
    :init-ns mqtt-broker.core
    :init (println "entrando a " *ns*)
    :prompt (fn [ns] (str ns "-> "))
    :welcome (println "Hols")
  }
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [org.clojure/data.json "0.2.6"]
                 [clojurewerkz/machine_head "1.0.0"]
                 [mongerr "1.0.0-SNAPSHOT"]])
