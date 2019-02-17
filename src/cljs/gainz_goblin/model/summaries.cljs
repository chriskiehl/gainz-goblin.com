(ns gainz-goblin.model.summaries)


(defn completion-stats [sets]
  (let [completed (count (filter :complete sets))
        total (count sets)]
    {:total total
     :completed completed
     :percent-completed (.toFixed (* 100 (/ completed total)) 2)}))


(defn summarize-cycle [state cycle-id]
  (let [sets (filter #(= (:cycle-id %) cycle-id) (vals state))
        stats (completion-stats sets)]
    (merge )))


(defn primary-lift? [set]
  (= (:type set) :primary))


(defn heavy-set? [set]
  (= (:training-type set) :heavy))


(defn summarize-day [sets]
  (let [{:keys [day cycle-id]} (first sets)
        primaries (filter primary-lift? sets)
        main-lifts (filter heavy-set? primaries)
        volume-lifts (filter (complement heavy-set?) primaries)
        heavy-max (apply max (map :weight main-lifts))
        volume-max (apply max (map :weight volume-lifts))
        stats (completion-stats sets)]
    {:day day
     :cycle-id cycle-id
     :percent-completed (:percent-completed stats)
     :heavy-set-name (-> main-lifts first :verbose-name)
     :heavy-set-weight heavy-max
     :volume-set-name (-> volume-lifts first :verbose-name)
     :volume-set-weight volume-max}
    ))



(defn summarize-days [sets]
  (->> sets
       (group-by :day)
       (vals)
       (map summarize-day)
       (sort-by :day)))

