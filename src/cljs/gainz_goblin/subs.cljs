(ns gainz-goblin.subs
  (:require [re-frame.core :as re-frame]
            [gainz-goblin.model.core :as model]
            [gainz-goblin.model.summaries :as summaries]
            [gainz-goblin.routes :as routes]
            [goog.string :as gstring]
            [goog.string.format]))


(re-frame/reg-sub
 :page-title
 (fn [db]
   (:page-title db)))


(re-frame/reg-sub
  :active-panel
  (fn [db]
    (:active-panel db)))


(re-frame/reg-sub
  :previous-page
  (fn [{:keys [active-panel route-params] :as db}]
    (let [{:keys [id day]} route-params]
      (case active-panel
        :home (routes/url-for :home)
        :day-breakdown (routes/url-for :home)
        :exercise-page (routes/url-for :day-breakdown :id id :day day)))))



(re-frame/reg-sub
  :current-cycle
  (fn [db]
    (let [cycle (first (:cycles db))
          sets (model/sets-for-cycle (vals (:sets db)) (:id cycle))]
      (if cycle
        (merge cycle (summaries/completion-stats sets))
        nil))))

(re-frame/reg-sub
  :previous-cycles
  (fn [db]
    (drop 1 (:cycles db))))


(re-frame/reg-sub
  :day-breakdown
  (fn [db]
    (let [cycle-id (-> db :route-params :id)
          sets (model/sets-for-cycle (vals (:sets db)) cycle-id)]
      (summaries/summarize-days sets))))


(re-frame/reg-sub
  :cycle-form
  (fn [db]
    (vals (:cycle-form db))))


(re-frame/reg-sub
  :show-new-cycle-modal
  (fn [db]
    (:show-new-cycle-modal db)))


(re-frame/reg-sub
  :exercises
  (fn [db]
    (let [cycle-id (-> db :route-params :id)
          day (-> db :route-params :day)
          all-sets (vals (:sets db))
          sets (-> all-sets
                   (model/sets-for-cycle cycle-id)
                   (model/sets-for-day day))
          primaries (filter summaries/primary-lift? sets)
          accessories (filter (complement summaries/primary-lift?) sets)
          sorted (fn [s]
                      (->> s
                           (group-by :lift-name)
                           (vals)
                           (sort-by #(:training-type (first %)))
                           (map #(sort-by :order %))))]
      {:primaries (sorted primaries)
       :accessories (sorted accessories)})))



