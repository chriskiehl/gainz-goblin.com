(ns gainz-goblin.model.template
  (:require [gainz-goblin.util :as util]))

;; Wendler 1.2 program template.
;;
;; The individual exercises are stored in a truncated form to avoid the
;; rampant duplication of the rep schemes. The core tooling in here is to
;; unwind the truncated form into the full expanded program form.
;;
;; Note: the core abstraction here is the `ExerciseSet`. It is the main unit of
;; granularity. All other views are computed from different roll ups of these
;; central values.
;;
;; Some key terms:
;;   * One Rep Max (:1rm) - the maximum weight that can be lifted once
;;   * Training Max (:tm-max) - 90% of the 1rm used for computing working weights
;;   * Primary Lift - These make up the compound lifts we're trying to improve
;;   * Accessory lift - Additional exerciese to round out training. Not tracked.


(def ^:private assistance-group-1
  "daily assistance exercises group 1"
  [{:type :accessory :lift-name :chin-ups :reps (repeat 5 5) :rest 60}
   {:type :accessory :lift-name :side-bends :reps [25 25] :rest 60}
   {:type :accessory :lift-name :push-downs :reps [100] :rest 60}])


(def ^:private assistance-group-2
  "daily assistance exercises group 2"
  [{:type :accessory :lift-name :curls :reps (repeat 5 10) :rest 60}
   {:type :accessory :lift-name :back-raises :reps [10 10 10] :rest 60}
   {:type :accessory :lift-name :face-pulls :reps [100] :rest 60}])


(def ^:private wendler-1-2-template
  "A truncated representation of the rep/set scheme for each lift
  throughout the cycle"
  ;; day 1
  [[{:type :primary :lift-name :squat :reps [3 3 3 js/Infinity] :percentage [0.7 0.8 0.9 1.0] :rest 120 :training-type :heavy}
    {:type :primary :lift-name :bench :reps (repeat 7 5) :percentage (repeat 7 0.75) :rest 60 :training-type :volume}]
   ;; day 2
   [{:type :primary :lift-name :deadlift :reps [3 3 3 js/Infinity] :percentage [0.7 0.8 0.9 1.0] :rest 120 :training-type :heavy}
    {:type :primary :lift-name :press :reps (repeat 7 5) :percentage (repeat 7 0.75) :rest 60 :training-type :volume}]
   ;; day 3
   [{:type :primary :lift-name :squat :reps (repeat 7 5) :percentage (repeat 7 0.75) :rest 120 :training-type :volume}
    {:type :primary :lift-name :bench :reps [3 3 3 js/Infinity] :percentage [0.7 0.8 0.9 1.0] :rest 60 :training-type :heavy}]

   ;; week 2, day 4
   [{:type :primary :lift-name :deadlift :reps (repeat 7 3) :percentage (repeat 7 0.75) :rest 120 :training-type :volume}
    {:type :primary :lift-name :press :reps [3 3 3 js/Infinity] :percentage [0.7 0.8 0.9 1.0] :rest 60 :training-type :heavy}]

   [{:type :primary :lift-name :squat :reps [5 5 5 js/Infinity] :percentage [0.65 0.75 0.85 1.0] :rest 120 :training-type :heavy}
    {:type :primary :lift-name :bench :reps (repeat 7 5) :percentage (repeat 7 0.75) :rest 60 :training-type :volume}]

   [{:type :primary :lift-name :deadlift :reps [3 3 3 js/Infinity] :percentage [0.65 0.75 0.85 1.0] :rest 120 :training-type :heavy}
    {:type :primary :lift-name :press :reps (repeat 7 5) :percentage (repeat 7 0.75) :rest 60 :training-type :volume}]

   ;; week 3
   [{:type :primary :lift-name :squat :reps (repeat 7 5) :percentage (repeat 7 0.75) :rest 120 :training-type :volume}
    {:type :primary :lift-name :bench :reps [5 5 5 js/Infinity] :percentage [0.65 0.75 0.85 1.0] :rest 60 :training-type :heavy}]

   [{:type :primary :lift-name :deadlift :reps (repeat 7 3) :percentage (repeat 7 0.75) :rest 120 :training-type :volume}
    {:type :primary :lift-name :press :reps [5 5 5 js/Infinity] :percentage [0.65 0.75 0.85 1.0] :rest 60 :training-type :heavy}]

   [{:type :primary :lift-name :squat :reps [5 3 1 js/Infinity] :percentage [0.75 0.85 0.95 1.0] :rest 120 :training-type :heavy}
    {:type :primary :lift-name :bench :reps (repeat 7 5) :percentage (repeat 7 0.75) :rest 60 :training-type :volume}]

   ;; week 4
   [{:type :primary :lift-name :deadlift :reps [3 3 1 js/Infinity] :percentage [0.75 0.85 0.95 1.0] :rest 120 :training-type :heavy}
    {:type :primary :lift-name :press :reps (repeat 7 5) :percentage (repeat 7 0.75) :rest 60 :training-type :volume}]

   [{:type :primary :lift-name :squat :reps (repeat 7 5) :percentage (repeat 7 0.75) :rest 120 :training-type :volume}
    {:type :primary :lift-name :bench :reps [5 3 1 js/Infinity] :percentage [0.75 0.85 0.95 1.0] :rest 60 :training-type :heavy}]

   [{:type :primary :lift-name :deadlift :reps (repeat 7 3) :percentage (repeat 7 0.75) :rest 120 :training-type :volume}
    {:type :primary :lift-name :press :reps [5 5 5 js/Infinity] :percentage [0.65 0.75 0.85 1.0] :rest 60 :training-type :heavy}]

   ;; week 5
   [{:type :primary :lift-name :squat :reps (repeat 5 5) :percentage (repeat 5 0.75) :rest 60 :training-type :heavy}
    {:type :primary :lift-name :bench :reps (repeat 5 5) :percentage (repeat 5 0.75) :rest 60 :training-type :volume}]

   [{:type :primary :lift-name :deadlift :reps (repeat 3 5) :percentage (repeat 5 0.75) :rest 60 :training-type :heavy}
    {:type :primary :lift-name :press :reps (repeat 5 5) :percentage (repeat 5 0.75) :rest 60 :training-type :volume}]

   [{:type :primary :lift-name :squat :reps (repeat 5 5) :percentage (repeat 5 0.75) :rest 60 :training-type :heavy}
    {:type :primary :lift-name :bench :reps (repeat 5 5) :percentage (repeat 5 0.75) :rest 60 :training-type :volume}]])



(defn- interleave-assistance
  "Interleave the assistance variations alternating
  between each by day
  (def a [[:a] [:b] [:c]])
  (def b [[1 2] [6 7]])
  (= [[:a 1 2] [:b 6 7] [:c 1 2]] (interleave-assistance a b))
  "
  [primary-lifts assistance-groups]
  (map concat primary-lifts (cycle assistance-groups)))



(defmulti expand-template
          "expands the truncated template format [5 5 5] into a list of
          maps [{} {} {}]"
          :type)


(defmethod expand-template
  :primary
  [template-line]
  (let [{:keys [reps percentage]} template-line]
    (map (fn [rep-count percent order]
           (merge template-line {:reps rep-count :percentage percent :order order}))
         reps
         percentage
         (range (count reps)))))


(defmethod expand-template :accessory
  [template-line]
  (let [{:keys [reps]} template-line]
    (map (fn [rep-count order]
           (merge template-line {:reps rep-count :percentage 1.0}))
         reps
         (range (count reps)))))


(defn calc-tm
  "Calculate the 'Training Max' which is defined as 90% of the 1RM"
  [weight]
  (* 0.9 weight))


(defn nearest-5
  "Lock weights to their nearest 5 pound increments."
  [weight]
  (* 5 (js/Math.round (/ weight 5))))


(defn- stamp-day
  "Apply a day number to each list of exercises"
  [exercise-template]
  (map-indexed (fn [idx items]
                 (map #(assoc %1 :day (inc idx)) items))
               exercise-template))


(defn- stamp-verbose-name
  "Stamp a human friendly name on the set structure."
  [{:keys [lift-name] :as set-info}]
  (let [names {:bench "Bench Press"
               :deadlift "Deadlift"
               :press "Overhead Press"
               :squat "Squat"
               :curls "Bicep Curls"
               :back-raises "Back Raises"
               :chin-ups "Chin up/Pull ups"
               :side-bends "Side Bends"
               :push-downs "Tricep Pushdowns"
               :face-pulls "Face Pulls"}]
    (assoc set-info :verbose-name (get names lift-name))))


(defn concrete-set
  "Generate the concrete weights and training numbers that will
  be used for this set."
  [onerm set-info]
  (let [{:keys [lift-name percentage]} set-info
        lift-1rm (get onerm lift-name)
        tm-max (calc-tm lift-1rm)]
    (merge set-info {:id (util/short-uuid)
                     :weight (* tm-max percentage)
                     :tm-max tm-max
                     :1rm lift-1rm
                     :complete false})))

(defn truncate-decimals [{:keys [weight] :as set}]
  (assoc set :weight (nearest-5 weight)))


(defn stamp-cycle-id
  "Stamp the cycle id onto each set so they can be logically grouped"
  [id set-info]
  (assoc set-info :cycle-id id))


(defn generate-wendler-sets
  "Generate a Wendler 1.2 Program based on the current One Rep Max values"
  [onerm cycle-id]
  (let [tm-max (util/map-vals calc-tm onerm)
        ;cycle-id (util/short-uuid)
        finalize (comp truncate-decimals
                       stamp-verbose-name
                       (partial stamp-cycle-id cycle-id)
                       (partial concrete-set onerm))]
    (as-> wendler-1-2-template $
          (interleave-assistance $ [assistance-group-1 assistance-group-2])
          (stamp-day $)
          (flatten $)
          (map expand-template $)
          (flatten $)
          (map finalize $))))

