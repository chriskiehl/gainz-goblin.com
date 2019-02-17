(ns gainz-goblin.core
  (:require [reagent.core :as reagent]
            [re-frame.core :as re-frame]
            [gainz-goblin.db :as db]
            [gainz-goblin.events :as events]
            [gainz-goblin.subs :as subs]
            [gainz-goblin.views :as views]
            [gainz-goblin.routes :as routes]))



(def debug true)

(defn dev-setup []
  (when debug
    (enable-console-print!)
    (println "dev mode")))



(defn mount-root []
  (re-frame/clear-subscription-cache!)
  (reagent/render [views/main-panel]
                  (.getElementById js/document "app")))



(defn ^:export init []
  (dev-setup)
  (re-frame/dispatch-sync [:initialize-db])
  (re-frame/dispatch-sync [:load-local-storage])
  (routes/app-routes)
  (mount-root)
  )
