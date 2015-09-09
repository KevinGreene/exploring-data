(ns exploring-data.incanter-datasets
  [:use [incanter core io charts stats datasets]])

;; Iris dataset
;; Sepal and Petal information
;; Which species have the largest?
(def iris-data (get-dataset :iris))

;; Speed of cars / distance taken
;; 
(def car-data (get-dataset :cars))

;; Suvery Data from Scott Lynch
;; What is hte correlation between empathy
;; Tolerance, and selflessness?
(def survey-data (get-dataset :survey))

;; US Array Data for 1973
;; Make a pie chart of crime type
(def us-arrest-data (get-dataset :us-arrests))

;; Flow Meter data used in Bland Altman Lancet paper
(def flow-meter-data (get-dataset :flow-meter))

;; Cold tolerance of grass species
(def co2-data (get-dataset :co2))

;; Effect of diet on early growth of chickens
(def chick-weight-data (get-dataset :chick-weight))

;; Results for an experiment to compare crop yields
(def plant-growth-data (get-dataset :plant-growth))

;; Calibration of load cells
(def pontius-data (get-dataset :pontius))

;; NIST data set for linear regression certification
(def filip-data (get-dataset :filip))

;; Labor statistics
(def longely-data (get-dataset :longely))

;; Test data for non-linear least squares
(def thurstone-data (get-dataset :thurstone))

;; Quarterly Time Series of Australian Resident
(def austres-data (get-dataset :austres))

;; Monthly Airline Passengers from 1949 - 1960
(def airline-passenger-data (get-dataset :airline-passengers))

;; Hair and eye color of sample students
(def hair-eye-color-data (get-dataset :hair-eye-color))

;; Pass / Fail results for high school math / college programming
(def pass-fail-results-data (get-dataset :math-prog))

;; Vote counts for 30 provinces from the 2009 Iranian election
(def iran-election-data (get-dataset :iran-election))
