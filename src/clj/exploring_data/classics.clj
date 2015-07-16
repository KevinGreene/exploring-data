(ns exploring-data.classics
  (:require [opennlp.nlp :as nlp]
            [opennlp.treebank :as treebank]))

(defn get-text [name]
  (-> (str "resources/writings/" name ".txt")
      (slurp)
      (clojure.string/replace #"\r\n" " ")))

;; Complete Fairy Tales
;; by the Brothers Grimm
(def grimm-text (get-text "grimm"))

;; Complete Sherlock Holmes
;; by Arthur Conan Doyle
(def sherlock-text (get-text "sherlock"))

;; Les Miserables
;; by Victor Hugo
;; Translated by Isabel F. Hapgood
(def les-mis-text (get-text "les_mis"))

;; Metamorphosis
;; by Franz Kafka
(def metamorphosis-text (get-text "metamorphosis"))

;; The Prince
;; by Nicolo Machiavelli
(def prince-text (get-text "prince"))

;; The Adventures of Tom Sawyer
;; by Mark Twain (Samuel Clemens)
(def tom-sawyer-text (get-text "tom_sawyer"))

;; The Adventures of Huckleberry Finn
;; by Mark Twain (Samuel Clemens)
(def huck-finn-text (get-text "huck_finn"))

;; Consider typing word for word what is said
;; or pulling your favorite script or book off the internet
;; Try Project Gutenberg

;; Some basic functions based on the given training data

;; Parses sentences from a given text
(def get-sentences (nlp/make-sentence-detector "resources/nlp_data/en-sent.bin"))

(comment
  ;; It's a fairly inexpensive function
  ;; But can have some trouble with non-prose
  (take 10 (get-sentences sherlock-text))
  )

;; Parses strings (tokens) from a given text
(def tokenize (nlp/make-tokenizer "resources/nlp_data/en-token.bin"))

(comment
  (tokenize "Here is an example")

  ;; Note that periods are separate
  ;; And this takes more than the amount of time you really need for the first ten
  (take 10 (tokenize sherlock-text))

  ;; We can just take the 100 characters or so of Sherlock
  ;; As it has short tokens
  (->> (take 100 sherlock-text)
       (apply str)
       (tokenize)
       (take 10))
  )

;; Tag a list of tokens with the presumed type
;; Types can be found here:  http://www.clips.ua.ac.be/pages/mbsp-tags
(def pos-tag (nlp/make-pos-tagger "resources/nlp_data/en-pos-maxent.bin"))

(comment
  (pos-tag (tokenize "This is a test sentence to demonstrate part of speech tagging."))
  )

;; Determine what, in a list of tokens, are likely Proper Names
(def name-find (nlp/make-name-finder "resources/nlp_data/en-ner-person.bin"))

;; Chunk the functions into phrases
;; Chunk tags can be found here: http://www.clips.ua.ac.be/pages/mbsp-tags
(def chunker (treebank/make-treebank-chunker "resources/nlp_data/en-chunker.bin"))

(defn get-frequency-wordtypes [text]
  (->> (tokenize text)
       (pos-tag)
       (map second)
       (frequencies)))

(comment
  (def sherlock-frequency (get-frequency-wordtypes sherlock-text))

  ;; Can't always trust pos-tag
  (meta (pos-tag (tokenize "Verbing nouns weirds language")))

  (let [this-thing (chunker (pos-tag (tokenize "Some example speech patterns.")))
        meta-thing (:probabilities (meta this-thing))] (map vector this-thing meta-thing))
  )
