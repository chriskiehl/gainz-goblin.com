(ns gainz-goblin.db)

(def default-db
  {:name "re-frame"
   :active-panel :home
   :route-params nil
   :page-title "Gainz Goblin"
   :cycles '()
   :sets {}
   :show-new-cycle-modal false
   :cycle-form {:squat    {:name :squat :display "Squat 1RM" :value nil}
                :bench    {:name :bench :display "Bench Press 1RM" :value nil}
                :press    {:name :press :display "Overhead Press 1RM" :value nil}
                :deadlift {:name :deadlift :display "Deadlift 1RM" :value nil}}})




