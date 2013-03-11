(ns ^{:doc "Progress formatter for Midje output"}
  midje.emission.plugins.progress
  (:use midje.emission.util)
  (:require [midje.data.fact :as fact]
            [midje.emission.colorize :as color]
            [midje.emission.state :as state]
            [midje.emission.plugins.util :as util]
            [midje.emission.plugins.silence :as silence]
            [midje.emission.plugins.default :as default]
            [midje.emission.plugins.default-failure-lines :as lines]
            [clojure.string :as str]))

(defn pass []
  (print (color/pass "."))
  (flush))

(defn fail [failure-map]
  (print (color/fail "F"))
  (flush))
  
(defn future-fact [description-list position]
  (print (color/note "P"))
  (flush))

(defn starting-fact-stream []
  (let [failures (state/raw-fact-failures)]
    (when-not (empty? failures)
      (println)
      (println)
      (doseq [failure-map failures] (util/emit-lines (lines/summarize failure-map)))
      (flush))))

(defn finishing-fact-stream [midje-counters clojure-test-map]
  (println)
  (default/finishing-fact-stream midje-counters clojure-test-map))

(defn make-map [& keys]
  (zipmap keys
          (map #(ns-resolve *ns* (symbol (name %))) keys)))

(def emission-map (merge silence/emission-map
                         (make-map :fail
                                   :pass
                                   :future-fact
                                   :starting-fact-stream
                                   :finishing-fact-stream)))

(state/install-emission-map emission-map)