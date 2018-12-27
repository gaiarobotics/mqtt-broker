(ns mqtt-broker.core
  (:gen-class)
  (:require [clojure.data.json :as json]
            [clojurewerkz.machine-head.client :as mh]
            [mongerr.core :as db]))

(defn parsed-payload [payload]
  (let [s (String. payload "UTF-8")
        js (try (json/read-str s) (catch Exception e))]
    (if (map? js) js s)))

(defn -main
  [& args]
  (let [conn (mh/connect "tcp://127.0.0.1:1883")]
    (mh/subscribe conn {"#" 0} (fn [^String topic _ ^bytes payload]
                                     (println (String. payload "UTF-8"))
                                     (println topic)
                                     (db/db-insert topic (parsed-payload payload))))))

; (mh/publish conn "topico/a/" "Hells, worldo")

(defn test
  [& args]
  (let [conn (mh/connect "tcp://127.0.0.1:1883")]
    (mh/subscribe conn {"hello" 0} (fn [^String topic _ ^bytes payload]
                                   (println (String. payload "UTF-8"))
                                   (mh/disconnect conn)
                                   (System/exit 0)))
    (mh/publish conn "hello" "Hello, world")))
