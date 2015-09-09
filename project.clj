(defproject exploring-data "0.1.0-SNAPSHOT"
  :description "A project to explain data exploration and visualization with Clojure"
  :url "https://github.com/KevinGreene/exploring-data"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.7.0"]
                 
                 ;; Accessing Data
                 [enlive "1.1.5"]
                 [clj-http "1.1.2"]
                 [cheshire "5.5.0"]
                 [org.clojure/data.csv "0.1.2"]

                 ;; Core data libraries
                 [net.mikera/core.matrix "0.36.1"]
                 [incanter "1.5.6"]
                 [clojure-opennlp "0.3.3"]
                 [quil "2.2.6"]
                 

                 ;; Random useful tools
                 [stemmers "0.2.2"]
                 [org.clojure/tools.cli "0.2.4"]
                 [criterium "0.4.3"]
                 [clj-time "0.10.0"]]
  :plugins [])
