(ns gainz-goblin.events
  (:require [re-frame.core :as rf]
            [gainz-goblin.db :as db]
            [gainz-goblin.model.core :as model]
            [gainz-goblin.util :as util]
            [cognitect.transit :as t]))


(defn deserialize
  "Deserialize the stored transit data"
  [transit-edn]
  (t/read (t/reader :json) transit-edn))

(defn serialize
  "serialize the sets / cycles subset of the db"
  [db]
  (as-> db $
        (select-keys $ [:sets :cycles])
        (t/write (t/writer :json) $)))



(rf/reg-event-db
  :initialize-db
  (fn [_ _]
    db/default-db))


(rf/reg-event-db
  :load-local-storage
  (fn [db _]
    (let [state (.getItem js/localStorage "state")]
      (if-not (nil? state)
        (merge db (deserialize state))
        db))))


(rf/reg-event-db
  :save-local-storage
  (fn [db _]
    (.setItem js/localStorage "state" (serialize db))
    db))



(rf/reg-event-db
  :set-active-panel
  (fn [db [_ active-panel route-params]]
    (-> db
        (assoc :active-panel active-panel)
        (assoc :route-params route-params))))


(rf/reg-event-fx
  :toggle-complete
  (fn [{:keys [db] :as cofx} [_ set-id]]
    (let [path [:sets set-id :complete]
          complete (get-in db path)]
      {:db (assoc-in db path (not complete))
       :dispatch [:save-local-storage]})))


(rf/reg-event-db
  :form-input
  (fn [db [_ field-name new-value]]
    (assoc-in db [:cycle-form (keyword field-name) :value] (int new-value))))


(rf/reg-event-fx
  :create-cycle
  (fn [{:keys [db] :as cofx} _]
    (let [{:keys [cycles sets cycle-form]} db
          weights {:squat (get-in cycle-form [:squat :value])
                   :bench (get-in cycle-form [:bench :value])
                   :press (get-in cycle-form [:press :value])
                   :deadlift (get-in cycle-form [:deadlift :value])}
          new-cycle (model/new-cycle weights)
          new-sets (util/index-by :id (:sets new-cycle))]
      {:db (-> db
               (update :cycles conj (dissoc new-cycle :sets))
               (update :sets merge new-sets))
       :dispatch-n [[:show-new-cycle-modal false]
                    [:save-local-storage]]})))


(rf/reg-event-fx
  :show-new-cycle-modal
  (fn [{:keys [db] :as cofx} [_ show]]
    {:db (assoc db :show-new-cycle-modal show)
     :dispatch [:seed-cycle-form]}))



(rf/reg-event-db
  :seed-cycle-form
  (fn
    [db _]
    (let [current-cycle (first (:cycles db))
          weights (:1rm current-cycle)
          new-weights (model/bump-weights (or weights {}))]
      (if-not (nil? weights)
        (reduce (fn [db' [lift-name weight]]
                  (assoc-in db' [:cycle-form lift-name :value ] weight))
                db
                new-weights)
        db))))
