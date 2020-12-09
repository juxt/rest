;; Copyright © 2020, JUXT LTD.

(ns juxt.rest
  (:require
   [clojure.string :as str]
   [clojure.java.shell :as sh]))

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

(defn build []
  (make-target-dir)
  (let [{:keys [exit out err]}
        (apply
         sh/sh
         (build-cmd
          {:includedir "https://raw.githubusercontent.com/juxt/spin/master/doc"
           :dialect "clojure"}))]

    (when-not (str/blank? out) (println out))
    (when-not (str/blank? err) (println err))

    exit))

(build)
