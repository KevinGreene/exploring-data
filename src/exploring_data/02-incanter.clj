(ns exploring-data.incanter
  (:require (incanter [core :refer :all]
                      [charts :refer :all]
                      [stats :refer :all]
                      [datasets :refer :all])))

;; Let's start off by simply displaying a graph of a normal distribution
;; If you run this line and don't see a graph, try changing the size of the window
;; Incanter graphs are often hidden behind the editor

(view (histogram (sample-normal 1000)))
