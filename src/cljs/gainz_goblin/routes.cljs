(ns gainz-goblin.routes
  (:require  [bidi.bidi :as bidi]
             [pushy.core :as pushy]
             [re-frame.core :as re-frame]))



(def routes ["/" {""      :home
                  "about" :about
                  [:id ""] :day-breakdown
                  [:id "/" :day] :exercise-page}])


(defn- parse-url [url]
  (bidi/match-route routes url))


(defn- dispatch-route [{:keys [route-params handler] :as matched-route}]
  (js/console.log "dispatch route:" (clj->js matched-route))
  (js/console.log (clj->js route-params) (clj->js matched-route))
  (let [panel-name (keyword (str (name (:handler matched-route)) "-panel"))]
    (println "dispatching panel name: " panel-name)
    (re-frame/dispatch [:set-active-panel handler route-params])))


(defn app-routes []
  (pushy/start! (pushy/pushy dispatch-route parse-url)))


(def url-for (partial bidi/path-for routes))
