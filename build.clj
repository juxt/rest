;; Copyright © 2020, JUXT LTD.

(ns juxt.rest
  (:require
   [clojure.edn :as edn]
   [clojure.java.shell :as sh]
   [clojure.string :as str]))

(defn add-common-arguments [m]
  (-> m
      (conj "-a" "allow-uri-read")
      (conj "-a" "nofooter")
      (conj "-a" "docinfo=shared-footer")
      (conj "--failure-level" "WARN")))

(defn make-target-dir []
  (apply sh/sh ["mkdir" "-p" "target"]))

(defn build-dialect [{:keys [dialect includedir]}]
  (assert dialect)
  (assert includedir)
  (->
   ["asciidoctor"]
   (add-common-arguments)
   (conj "-a" (str "includedir=" includedir))
   (conj "-o" (format "target/README-%s.html" dialect))
   (conj "README.adoc")))

(defn build [{:keys [dialect includedir]}]
  (make-target-dir)
  (let [{:keys [exit out err]}
        (apply
         sh/sh
         (build-dialect
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


;; Build index.html

(println "Building index")

(defn build-index []
  (->
   ["asciidoctor"]
   (add-common-arguments)
   (conj "-o" "target/index.html")
   (conj "index.adoc")))

(let [{:keys [out err]}
        (apply
         sh/sh
         (build-index))]

    (when-not (str/blank? out) (println out))
    (when-not (str/blank? err) (println err)))
