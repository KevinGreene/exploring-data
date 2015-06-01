(ns exploring-data.intro)


;; Use def to define a symbol

;; Numbers
(def x 1)

;; Strings

(def s "Example String")

;; Vectors

(def v [1 2 3 4])

;; Sequences

(def q (seq [1 2 3 4]))

;; Sequence manipulation

(first q)

(first (first q))

(ffirst q)

(nth q 1)

(take 2 q)

(rest q)

;; cons adds - note it doesn't change the symbol's value
(cons 5 q)

(def q2 (cons 5 q))

;; Let's make a more interesting sequence
(def numbers (seq [1 2 3 4 5 6 7 8 9 8 7 6 5 4 3 2 1 2 3 1 2 3]))

;; Some more collection manipulation tools

(reverse numbers)

(distinct numbers)

(sort numbers)

(concat numbers [10 11 12])

(shuffle numbers)

;; Traditional functional goodness

(defn mod3 [x]
  (mod x 3))

;; Map applies a given function to a given sequence
(map mod3 numbers)

;; Predicate functions evaluate truth. Conventionally they end in a ?
(defn divisible-by-3? [x]
  (= 0 (mod3 x)))

(filter divisible-by-3? numbers)

(partition 4 numbers)

(partition-by divisible-by-3? numbers)

;; Tons more. Read core.clj!
