;; Copyright © 2020, JUXT LTD.

(ns juxt.rest
  (:require
   [clojure.edn :as edn]
   [clojure.java.shell :as sh]
   [clojure.string :as str]))

(defn make-target-dir []
  (apply sh/sh ["mkdir" "-p" "target"]))

(defn build-cmd [{:keys [dialect includedir]}]
  (assert dialect)
  (assert includedir)
  (->
   ["asciidoctor"]
   (conj "-a" "allow-uri-read")
   (conj "-a" (str "includedir=" includedir))
   (conj "--failure-level" "WARN")
   (conj "-o" (format "target/README-%s.html" dialect))
   (conj "README.adoc")))

(defn build [{:keys [dialect includedir]}]
  (make-target-dir)
  (let [{:keys [exit out err]}
        (apply
         sh/sh
         (build-cmd
          {:dialect dialect
           :includedir includedir
           }))]

    (when-not (str/blank? out) (println out))
    (when-not (str/blank? err) (println err))

    exit))

(def dialects (edn/read-string (slurp "dialects.edn")))

(doseq [[dialect {:keys [includedir]}] dialects]
  (println "Building dialect:" dialect)
  (build
   {:dialect dialect
    :includedir includedir}))
