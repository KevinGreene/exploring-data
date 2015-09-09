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

(-> (distinct numbers)
    (sort)
    (reverse))

(concat numbers [10 11 12])

(shuffle numbers)

;; Traditional functional goodness

(defn mod3 [x]
  (mod x 3))

;; Map applies a given function to a given sequence
(map mod3 numbers)

(reduce (fn [a b] (+ a b)) numbers)

(frequencies numbers)

;; Predicate functions evaluate truth. Conventionally they end in a ?
(defn divisible-by-3? [x]
  (= 0 (mod3 x)))

(filter divisible-by-3? numbers)

;; Add syntax for non-divisible
(partition 4 numbers)

(partition-by divisible-by-3? numbers)

;; Make more interesting sequences

(let
    [pairs (reverse (partition 2 2 (repeat nil) (shuffle  team-members)))]
  (cons
   (filter (complement nil?) (apply concat (take 2 pairs)))
   (drop 2 pairs)))

(def just-x (repeat "x"))

(def one-through-ten (range 1 11))

(def even-numbers (range 0 100 2))

(def probably-1-vec [1 1 1 1 1 1 1 1 1 2])

(defn probably-1 []
  (take 1 (shuffle probably-1-vec)))

(def mostly-1-seq (repeatedly probably-1))


;; Powers of two
(fn [a]
  (* 2 a))

(def powers-of-2 (iterate #(* 2 %) 1))

;; Most data we've worked with is one dimensional
;; To work with real data, we probably need maps

(def presenter {:first "Kevin" :last "Greene"})

(:first presenter)

(keys presenter)

;; Modify key-values with assoc and dissoc.
;; Note - This doesn't change the value of presenter

(assoc presenter :city "Grand Rapids")

(def presenter-with-favorites (assoc presenter :favorites {}))

(assoc-in presenter-with-favorites [:favorites :language] "Clojure")

(defn make-rectangle []
  {:height (inc (rand-int 10))
   :width (inc (rand-int 10))})

(def rectangles (repeatedly make-rectangle))

(def rectangles-100 (take 100 rectangles))

(defn combine-rectangles [rect1 rect2]
  {:height (+ (:height rect1) (:height rect2))
   :width (+ (:width rect1) (:width rect2))})

(def wider-than-tall
  (let [{:keys [height width]}
        (reduce combine-rectangles rectangles-100)]
    (< height width)))

;; Tons more. Read core.clj
