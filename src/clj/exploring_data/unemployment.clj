(ns exploring-data.unemployment
  [:use [incanter core io charts stats]])

(def virginia-beach-data (read-dataset "resources/unemployment/unemployment-virginia-beach-1990-01-2014-10.csv" :header true))

;; We want something that produces "months since starting the data collection"
(defn get-months-since-start [year period]
  (+ (* 12 (- year 1990))
     (Integer/parseInt (apply str (rest period)))))

;; Adding a derived column allows us to reference it directly
(def virginia-beach-labeled-data
  (add-derived-column :month [:year :period] get-months-since-start virginia-beach-data))

;; We can create a straightforward line from the data
(view (xy-plot :month :value :data virginia-beach-labeled-data))
