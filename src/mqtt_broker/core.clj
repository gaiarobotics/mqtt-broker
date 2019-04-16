(ns mqtt-broker.core
  (:gen-class)
  (:require [clojure.data.json :as json]
            [clojurewerkz.machine-head.client :as mh]
            [mongerr.core :as db]))

(def url "tcp://127.0.0.1:1883")
(def conn (mh/connect url))

(defn parsed-payload [payload]
  (let [s (String. payload "UTF-8")
        js (try (json/read-str s) (catch Exception e))]
    (if (map? js) js s)))

(defn -main
  [& args]
  (mh/subscribe conn {"#" 0} (fn [^String topic _ ^bytes payload]
                               (try
                                 (println "topic: " topic)
                                 (println "payload: " (parsed-payload payload))
                                 (db/db-insert topic (parsed-payload payload))
                               (catch Exception e (println e))))))

(defn db-get []
  (mh/subscribe (mh/connect url) ; conn
    {"db-get" 0} (fn [^String topic _ ^bytes payload]
                   (try
		     (let [payload    (parsed-payload payload)
                           collection (:collection payload)
                           query      (dissoc payload :collection)
                           result     (db/db collection query)]
                       (mh/publish conn "db-get" {:collection collection
                                                  :query      query
                                                  :result     result}))
		     (catch Exception e (println "Exception e: e"))))))

(defn test
  [& args]
  (mh/subscribe conn {"hello" 0} (fn [^String topic _ ^bytes payload]
                                   (println (String. payload "UTF-8"))
                                   (mh/disconnect conn)
                                   (System/exit 0)))
  (mh/publish conn "hello" "Hello, world"))
