(ns exploring-data.data
  (:require [net.cgrand.enlive-html :as html]
            [clojure.string :as str]
            [clojure.xml :as xml]
            [clj-http.client :as client]
            [cheshire.core :as json]
            [clojure.data.csv :as csv]
            [clojure.java.io :as io]
            ))

;; Let's define our random functions
(defn make-rectangle []
  {:height (inc (rand-int 10))
   :width (inc (rand-int 10))})

(defn str-rectangle [rect]
  (str (:height rect)
       ","
       (:width rect)))

(def rectangles (repeatedly make-rectangle))

(def rectangles-5 (take 5 rectangles))

;; CSVs

(def csv-rectangles (map str-rectangle rectangles))

(def five-rect-csv (str/join "\n" (take 5 csv-rectangles)))

(spit "test_output.csv" five-rect-csv)

(def infinite-rect-csv (interleave csv-rectangles (repeat "\n")))

;; Spit requires everything to fit in memory
;; This causes my machine to spin for over a minute
(spit "slow_big_test_output.csv" (str/join "\n" (take 200000000 csv-rectangles)))

;; Easier to just use writer .newLine
;; Currently still slow, digging into why
(with-open [w (io/writer "big_test_output.csv")]
  (doseq [line (take 200000000 csv-rectangles)]
    (.write w line)
    (.newLine w)))

;; Reading has similar analogies
(def small-csv-results (slurp "test_output.csv"))

(with-open [r (io/reader "big_test_output.csv")]
  (count (line-seq r)))

;; Using clojure.data.csv makes things even simpler

(with-open [r (io/reader "test_output.csv")]
  (doall (csv/read-csv r)))

;; Rest APIs

(def clojure-repos-response
  (client/get "https://api.github.com/orgs/clojure/repos"))

(def clojure-repo-json-string (:body clojure-repos-response))

;; JSON
;; Keywords are left as strings
(def parsed-clojure-repos (json/decode clojure-repo-json-string))

;; Keywords are converted to strings
(def parsed-clojure-repos-keyword (json/decode clojure-repo-json-string true))

;; What are all of the names of the Clojure Repositories?

;; XML

(def bgg-api-root "http://www.boardgamegeek.com/xmlapi/")

(defn parse-xml-str [s]
  (xml/parse (java.io.ByteArrayInputStream. (.getBytes s))))

(defn get-boardgame-xml [boardgame-id]
  (let [url (str bgg-api-root "boardgame/" boardgame-id "?stats=1") ]
    (println (str "Getting game at " url))
    (:body (client/get url))))

(defn get-base-game-xml [boardgame-id]
  (->> (parse-xml-str (get-boardgame-xml boardgame-id))
       :content
       first
       :content))

(defn get-game-attr [boardgame-xml attribute]
  (->> boardgame-xml
       (filter #(= (:tag %) attribute))
       (map :content)
       (map first)))

(defn get-game-rating [boardgame-xml]
  (->> (get-game-attr boardgame-xml :statistics)
       first
       :content
       (filter #(= (:tag %) :average))
       first
       :content
       first))

(defn get-bgg-game [boardgame-id]
  (let [game-xml (get-base-game-xml boardgame-id)]
    (if
        (empty?
         (filter #(= (:tag %) :error) game-xml))
      {:bgg-id boardgame-id
       :name (first (get-game-attr game-xml :name))
       :mechanics (get-game-attr game-xml :boardgamemechanic)
       :themes (get-game-attr game-xml :boardgamecategory)
       :rating (get-game-rating game-xml)
       :description (first (get-game-attr game-xml :description))})))

(def games
  (remove nil?
   (for [i (range 100000)]
     (get-bgg-game i))))

(defn game-to-line [game]
  (let [{:keys [bgg-id name mechanics themes rating]} game]
    (str bgg-id ","
         name ","
         (str/join ";" mechanics) ","
         (str/join ";" themes) ","
         rating)))

(with-open [w (io/writer "bgg.csv")]
  (doseq [line (map game-to-line (take 50 games))]
    (.write w (str line))
    (.newLine w)
    ))
