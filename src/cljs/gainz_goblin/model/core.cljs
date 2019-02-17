(ns gainz-goblin.model.core
  (:require [gainz-goblin.util :as util]
            [gainz-goblin.model.template :as template]))



(def db {:sets {}
         :cycles {}})


(def std-increments
  "The standard weight increases by lift cycle"
  {:squat 10
   :bench 5
   :press 5
   :deadlift 10})


(defn bump-weights [one-rm-weights]
  (merge-with + one-rm-weights std-increments))


(defn new-cycle
  "Generate a new 5 week Wendler cycle using the supplied 1RM values"
  [next-1rm]
  (let [cycle-id (util/short-uuid)
        sets (template/generate-wendler-sets next-1rm cycle-id)]
    {:id cycle-id
     :start-date (js/Date.now)
     :1rm next-1rm
     :tm-max (util/map-vals #(* 0.9 %) next-1rm)
     :process-controller nil  ;; TODO: for set/rest management
     :sets sets}))


(defn sets-for-cycle [sets cycle-id]
  (filter #(= (:cycle-id %) cycle-id) sets))


(defn sets-for-day [sets day]
  (filter #(= (:day %) (int day)) sets))
