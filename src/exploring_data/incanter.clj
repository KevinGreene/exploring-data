(ns exploring-data.incanter
  (:require [clojure.string :as str]
            (incanter [core :refer :all]
                      [charts :refer :all]
                      [stats :refer :all]
                      [datasets :refer :all])))

;; Let's start off by simply displaying a graph of a normal distribution
;; If you run this line and don't see a graph, try changing the size of the window
;; Incanter graphs are often hidden behind the editor

(view (histogram (sample-normal 100)))

(defn parse-bgg-line [line]
  (let [l (str/split line #",")]
    {:bgg-id (nth l 0)
     :name (nth l 1)
     :mechanics (str/split (nth l 2) #";")
     :themes (str/split (nth l 3) #";")
     :rating (nth l 4)
     }))

(def games
  (with-open [r (clojure.java.io/reader "bgg.csv")]
    (doall (map parse-bgg-line (line-seq r)))))

(defn parse-number
  "Reads a number from a string. Returns nil if not a number."
  [s]
  (if (re-find #"^-?\d+\.?\d*$" s)
    (read-string s)))

(view (histogram
       (remove nil?
               (map parse-number
                    (map :rating games)))))

(view (function-plot sin -10 10))


;; From Incanter's README

; function that returns dataset containing weather in London for given month in 2012
(defn weather-for-month [month]
  (-> (format "http://www.wunderground.com/history/airport/EGLL/2012/%d/10/MonthlyHistory.html?format=1" month)
      (read-dataset :header true)))

; get weather data for each month in 2012 and build single dataset
(def data (->> (range 1 13) (map weather-for-month) (apply conj-rows)))

; view dataset in a table and view histogram of mean temperature
(view data)
; wunderground.com formats temperature depending on locale/location/whatever
; so you might need to use "Mean TemperatureF" otherwise you'll get NullPointerException.
(view (histogram "Mean TemperatureC" :nbins 100 :data data))

; function that given month "2012-9-20" extracts month and returns 9
(defn month [date] (Integer/parseInt (second (.split date "-"))))

; dataset that contains 2 columns: month and mean temperature for that month
; don't forget to change to "Mean TemperatureF" if you did so few steps back.
(def grouped-by-month
  (->> (map (fn [date temp] {:month (month date) :temp temp})
            ($ "GMT" data) ($ "Mean TemperatureC" data))
       to-dataset
       ($rollup :mean :temp :month)
       ($order :month :asc)))

; view line chart that shows that August was the warmest month
(view (line-chart :month :temp :data grouped-by-month))
