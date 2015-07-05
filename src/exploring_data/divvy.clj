(ns exploring-data.divvy
  [:require
   [clojure.data.csv :as csv]
   [clojure.java.io :as io]]
  [:use [incanter core datasets io]])

(def trip-csv-basic
  (rest 
   (with-open [r (io/reader "resources/divvy/Divvy_Trips_2014_Q1Q2.csv")]
     (doall
      (csv/read-csv r)))))

(def station-csv-basic
  (rest 
   (with-open [r (io/reader "resources/divvy/Divvy_Stations_2014_Q1Q2.csv")]
     (doall
      (csv/read-csv r)))))

;; Incanter can easily load CSVs into a dataset
(def data
  (read-dataset "resources/divvy/Divvy_Trips_2014_Q1Q2.csv" :header true))

;; Display the CSV in a traditional table
(view (data-table data))

;; Display data queried
(view (query-dataset data {:gender "Male"}))
