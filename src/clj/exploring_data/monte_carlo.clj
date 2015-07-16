(ns exploring-data.monte-carlo
  (:use [incanter core stats charts]))

;; Monte Carlo is essentially repeatedly running a simulation
;; To compute expected probability

(comment 
  (def number-of-coins 100000)

  (def heads-and-tails (sample ["Heads" "Tails"] :size number-of-coins))

  (defn heads? [coin-result]
    (if (= coin-result "Heads") 1 0))

  (def prob-heads (/ (sum (map heads? heads-and-tails)) number-of-coins))

  (view (pie-chart ["Heads" "Tails"] [prob-heads (- 1 prob-heads)]))

  ;; Let's try a more complicated example. For this, we'll need a deck of cards, but just the ranks

  (def card-ranks
    (apply concat (repeat 4 [2 3 4 5 6 7 8 9 10 11 12 13 14])))

  (defn get-sample-cards [number-of-players]
    (sample card-ranks
            :size number-of-players :replacement false))

  (def number-of-games 100)

  (def number-of-players 3)

  (def card-game-results
    (pmap reverse (pmap sort (repeatedly number-of-games #(get-sample-cards number-of-players)))))

  (def decided-card-games (filter (complement  #(= (first %) (second %))) card-game-results))

  (def number-of-ties
    (- number-of-games (count decided-card-games)))

  (view (pie-chart ["Tie" "Winner"] [number-of-ties (count decided-card-games)]))

  )

;; Make a distribution of the highest card winning
