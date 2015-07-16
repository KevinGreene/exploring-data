(ns exploring-data.divvy
  [:require
   [clojure.data.csv :as csv]
   [clojure.java.io :as io]
   [clj-time.core :as clj-time]
   [clj-time.coerce :as time-coerce]
   [clj-time.format :as time-format]]
  [:use [incanter core io charts stats latex svg pdf]])

(def divvy-trip-location "resources/divvy/Divvy_Trips_2014_0615_0630.csv")

(def divvy-station-location "resources/divvy/Divvy_Stations_2014_Q1Q2.csv")

(def trip-csv-basic
  (rest 
   (with-open [r (io/reader divvy-trip-location)]
     (doall
      (csv/read-csv r)))))

(def station-csv-basic
  (rest 
   (with-open [r (io/reader divvy-station-location)]
     (doall
      (csv/read-csv r)))))

;; Incanter can easily load CSVs into a dataset
(def data
  (read-dataset divvy-trip-location :header true))

(def station-data
  (read-dataset divvy-station-location :header true))

(def divvy-date-formatter (time-format/formatter "MM/dd/yyyy HH:mm"))

;; Basic views and filters
(comment
  
  ;; Display the CSV in a traditional table
  (view data)

  ;; Get the keys
  (:column-names data)

  ;; Find the number of rows
  (nrow data)

  ;; Get unique options for a field
  (get-categories :gender data)

  ;; What are the types of users?


  ;; Display data queried
  (view (query-dataset data {:gender {:$in #{"Female" "Male"}}}))

  ;; The available query terms include :$gt, :$lt, :$gte, :$lte, :$eq, :$ne, :$in, :$nin, $fn
  ;; Find all data where trip duration is between 45 minutes and an hour

  
  )

;; We'll define a generic set of Valid Data we can change
;; throughout the experience
(def valid-data
  (query-dataset data
                 {:bikeid {:$gt 0}}))

;; Basic Plots and Graphs
(comment
  (view valid-data)
  
  ;; Let's pick a random bike
  (def bike-2006-data (query-dataset valid-data {:bikeid 2006}))

  ;; Do older people ride longer or shorter on this bike?
  (def bike-2006-scatter-plot (scatter-plot :birthyear :tripduration :data bike-2006-data))

  ;; View it
  (view bike-2006-scatter-plot)

  
  ;; Save it as a PDF or SVG
  (save-pdf bike-2006-scatter-plot "bike_2006.pdf")
  (save-svg bike-2006-scatter-plot "bike_2006.svg")
  
  ;; Loading the entire scatter plot is pretty slow - need a way to grab some random hueristics

  ;; First a function to get the data for a bike
  (defn query-bike-data [data bike-id]
    (query-dataset data {:bikeid bike-id}))


  ;; Create a set of all bike-ids
  (def bike-ids (sort (get-categories :bikeid valid-data)))

  ;; Then make a slider for the data
  (sliders [bike-number bike-ids]
           ;; LOTS OF TABLES!
           (view
            (scatter-plot :birthyear :tripduration :data
                          (query-bike-data valid-data bike-number))))

  ;; Need to utilize set-data
  ;; Add birthyear not nil
  ;; Maybe add birthyear > 1930 and tripduration < 3600
  (let [s-plot (scatter-plot [] []
                             :x-label "Birth Year"
                             :y-label "Trip Duration (sec)")]
    (view s-plot)
    (sliders [bike-number bike-ids]
             (set-data s-plot :birthyear :tripduration
                       (query-bike-data valid-data bike-number))))

  ;; Make a slider that allows you to switch between different subscriber types
  ;; For some sort of chart
  
  ;; View Trip Durations
  ;; Trim down valid-data for tripduration
  (view (histogram :tripduration :data valid-data))

  ;; What's the distribution of age?
  (view (histogram :birthyear :data valid-data))

  ;; What about derived data?
  ;; Let's define a helper function to convert the Divvy dates to longs
  (defn date-string-to-long [date-string]
    (time-coerce/to-long (time-format/parse divvy-date-formatter date-string)))

  ;; We can add derived columns to create a more useful plot
  (->> (add-derived-column :start-long [:starttime] date-string-to-long valid-data)
       (scatter-plot :start-long :tripduration :data)
       (view))
  
  ;; How are male trip lengths distributed vs female riders
  (let [male-data (query-dataset valid-data {:gender "Male"})
        female-data (query-dataset valid-data {:gender "Female"})]
    (-> (histogram :tripduration :data male-data :legend true)
        (add-histogram :tripduration :data female-data)
        (view))
    )
  )

;; Interacting with Plots more naturally
(defn update-view! [v plot]
  (swap! v #(do
              (.setContentPane %1 %2)
              (.revalidate %1 )
              (.repaint %1)
              %1) (new org.jfree.chart.ChartPanel plot)))

(def v (atom (view (scatter-plot [] []))))
;; Joining Data and Using Math

(defn rads [degrees]
  (Math/toRadians degrees))

(comment
  ;; SOHCAHTOA

  (sin 45)
  (sin (rads 45))
  (cos (rads 45))
  )


;; Step by step haversine
;; Used to calculated the distance between two points
(defn haversine [lat-deg-1 lon-deg-1 lat-deg-2 lon-deg-2]
  (let [lat1 (rads lat-deg-1)
        lon1 (rads lon-deg-1)
        lat2 (rads lat-deg-2)
        lon2 (rads lon-deg-2)
        radius-of-earth 6371000
        delta-lat (abs (- lat1 lat2))
        delta-lon (abs (- lon1 lon2))
        sin-d-lat (sin (/ delta-lat 2))
        sin2-d-lat (* sin-d-lat sin-d-lat)
        sin-d-lon (sin (/ delta-lon 2))
        sin2-d-lon (* sin-d-lon sin-d-lon)
        a (+ sin2-d-lat (* (cos lat1) (cos lat2) sin2-d-lon))
        c (* 2 (atan2 (sqrt a) (sqrt (- 1 a))))
        distance (* radius-of-earth c)]
    distance))

;; Haversine from Thomas Meier
;; https://github.com/ThomasMeier/haversine/blob/master/src/haversine/core.clj#L15

(defn- alpha
  "Trigonometric calculations for use in haversine."
  [lat1 lat2 delta-lat delta-long]
  (+ (sin2 (/ delta-lat 2))
     (* (sin2 (/ delta-long 2)) (Math/cos lat1) (Math/cos lat2))))

(defn haversine2
  "Distance in km between two points of lat/long. Supply two maps of latitutde and longitude."
  [{lat1 :latitude long1 :longitude}
   {lat2 :latitude long2 :longitude}]
  (let [delta-lat (Math/toRadians (- lat2 lat1))
        delta-long (Math/toRadians (- long2 long1))
        lat1 (Math/toRadians lat1)
        lat2 (Math/toRadians lat2)]
    (* earth-radius 2
       (Math/asin (Math/sqrt (alpha lat1 lat2 delta-lat delta-long))))))

(comment
  (let [from-data (rename-cols {:id :from_station_id
                                :latitude :from-lat
                                :longitude :from-lon}
                               station-data)
        to-data (rename-cols {:id :to_station_id
                              :latitude :to-lat
                              :longitude :to-lon}
                             station-data)]
    (def geo-data
      (->> ($join [:from_station_id :from_station_id] from-data data)
           ($join [:to_station_id :to_station_id]  to-data)
           (add-derived-column :distance [:from-lat :from-lon :to-lat :to-lon] haversine))))

  
  (update-view! v (histogram :distance :data geo-data))

  ;; Some people ride very long distances
  (update-view! v (query-dataset geo-data {:distance {:$gt 15000}}))

  (let [non-looping-data (query-dataset geo-data {:distance {:$gt 10}})]
    (def speed-data (add-derived-column :speed [:distance :tripduration] #(/ %1 %2) non-looping-data)))


  ;; One again we're in a situation with a heavy right tail
  (update-view! v (histogram :speed :data speed-data))

  ;; Let's convert to :mph
  (defn meters-to-miles [meters]
    (* meters 0.000631371))

  (defn mps-to-mph [mps]
    (* (meters-to-miles mps) 60 60))

  ;; Adding most things together
  (let [data (->> (add-derived-column :mph [:speed] mps-to-mph speed-data)
                  (add-derived-column :distance-bucket [:distance] #(* (quot % 100) 100))
                  ($rollup :mean :mph :distance-bucket))
        chart (scatter-plot :distance-bucket :mph :data data)
        x ($ :distance-bucket data)
        y ($ :mph data)
        ;; linear (linear-model y x)
        ;; model (interpolate (map vector x y) :linear)
        ]
    
    (view (add-lines chart x y))
    )


  ;; Find the station that people get away from the fastest
  ;; Find the station that people approach the slowest
  ;; Create a plot that uses sliders for from / to station

  ;; Any other complex relationships?
  
  )
