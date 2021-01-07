;; Copyright © 2020, JUXT LTD.

(ns juxt.rest
  (:require
   [clojure.java.shell :as sh]
   [clojure.string :as str]))

(defn add-common-arguments [m]
  (-> m
      (conj "-a" "allow-uri-read")
      (conj "-a" "nofooter")
      (conj "-a" "docinfo=shared-footer")
      (conj "-a" "icons=font")
      (conj "-a" "stylesheet=css/juxt.css")
      (conj "--failure-level" "WARN")))

(defn make-target-dir []
  (apply sh/sh ["mkdir" "-p" "target"]))

(defn build-readme []
  (make-target-dir)
  (let [{:keys [exit out err]}
        (apply
         sh/sh
         (->
          ["asciidoctor"]
          (add-common-arguments)
          (conj "-o" "target/README.html")
          (conj "README.adoc")))]

    (when-not (str/blank? out) (println out))
    (when-not (str/blank? err) (println err))

    exit))

(defn build-index []
  (let [{:keys [out err]}
        (apply
         sh/sh
         (->
          ["asciidoctor"]
          (add-common-arguments)
          (conj "-o" "target/index.html")
          (conj "index.adoc")))]

    (when-not (str/blank? out) (println out))
    (when-not (str/blank? err) (println err))))


(println "Building README.html")
(build-readme)
(println "Building index.html")
(build-index)
