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

(def failure-messages (atom []))

(defn pass []
  (print (color/pass "."))
  (flush))

(defn fail [failure-map]
  (swap! failure-messages conj (lines/summarize failure-map))
  (print (color/fail "F"))
  (flush))
  
(defn future-fact [description-list position]
  (print (color/note "P"))
  (flush))

(defn finishing-fact-stream [midje-counters clojure-test-map]
  (println)
  (doseq [line @failure-messages] (emit-one-line line))
  (default/finishing-fact-stream midje-counters clojure-test-map))

(defn make-map [& keys]
  (zipmap keys
          (map #(ns-resolve *ns* (symbol (name %))) keys)))

(def emission-map (merge silence/emission-map
                         (make-map :fail
                                   :pass
                                   :future-fact
                                   :finishing-fact-stream)))

(state/install-emission-map emission-map)