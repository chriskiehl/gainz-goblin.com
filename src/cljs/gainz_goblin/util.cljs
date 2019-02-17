(ns gainz-goblin.util)


(defn short-uuid
  "The first 8 chars of a UUID"
  []
  (subs (str (random-uuid)) 0 8))


(defn map-vals
  "Apply f to every value in the supplied map"
  [f m]
  (reduce-kv (fn [m k v] (update m k f)) m m))


(defn index-by
  "group-by, but 1:1 to create addressable maps"
  [f m]
  (reduce (fn [acc val]
            (assoc acc (f val) val))
          {}
          m))
