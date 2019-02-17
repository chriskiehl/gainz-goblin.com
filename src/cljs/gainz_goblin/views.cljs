(ns gainz-goblin.views
  (:require
   [re-frame.core :as rf]
   [gainz-goblin.subs :as subs]
   [gainz-goblin.routes :as routes]
   ))



(defn current-cycle [{:keys [id percent-completed start-date] :as cycle}]
  (when-not (nil? cycle)
    [:div.current-cycle-card.mdl-card.mdl-shadow--2dp.card-margin
     [:div.mdl-card__title.current-cycle
      [:div.mdl-card__title-text
       ;[:img {:src "https://s3-us-west-1.amazonaws.com/gainz-goblin-storage/images/gainz-goblin.PNG"}]
       "Current Cycle"]]
     [:div.mdl-card__supporting-text
      [:div "Percent completed: " percent-completed "%"]
      [:div (str "Started on: " (.toLocaleDateString (js/Date. start-date) "en-US"))]
      [:div "Training Maxes:"
       (for [[lift weight] (:1rm cycle)]
         [:div (str lift ": " weight)])]]
     [:div.mdl-card__actions.mdl-card--border
      [:a.mdl-button.mdl-button--colored.mdl-js-button.mdl-js-ripple-effect
       {:href (routes/url-for :day-breakdown :id id)}
       "Let's Go!"]
     [:div.mdl-card__menu
      [:a.mdl-button.mdl-button--icon.mdl-js-button.mdl-js-ripple-effect
       [:i.material-icons "more_vert"]]]]]))



(defn previous-cycles [cycles]
  [:ul.mdl-list
    (for [cycle cycles]
      [:li.mdl-list__item.mdl-list__item--two-line
       [:span.mdl-list__item-primary-content
        [:span
         (.toLocaleDateString (js/Date. (:start-date cycle)) "en-US")]
        [:span.mdl-list__item-sub-title
         (str (:1rm cycle))]]
        [:span.mdl-list__item-secondary-content
         [:span.mdl-list__item-secondary-action
          [:button#id-xxx.mdl-button.mdl-js-button.mdl-button--icon
           [:i.material-icons "more_vert"]]
          ]]])])


(defn previous-cycles-card [cycles]
  (when-not (empty? cycles)
    [:div.current-cycle-card.mdl-card.mdl-shadow--2dp.card-margin
     [:div.mdl-card__title.current-cycle
      [:div.mdl-card__title-text "Previous Cycles"]]
     [:div.mdl-card__supporting-text
      [previous-cycles cycles]
      ]]))


(defn day-summary-card [{:keys [day
                                cycle-id
                                percent-completed
                                heavy-set-name
                                heavy-set-weight
                                volume-set-name
                                volume-set-weight] :as props}]
  [:div.current-cycle-card.mdl-card.mdl-shadow--2dp.card-margin
   [:div.mdl-card__title.current-cycle
    [:div.mdl-card__title-text (str "Day " day)]]
   [:div.mdl-card__supporting-text
    [:div (str heavy-set-name " @ " heavy-set-weight)]
    [:div (str volume-set-name " @ " volume-set-weight)]
    [:div (str "Percent Complete: " percent-completed "%")]
    [:a
     {:href (routes/url-for :exercise-page :id cycle-id :day day) :style {:float "right"}}
     [:button.mdl-button.mdl-button--colored.mdl-js-button.mdl-js-ripple-effect.mdl-button--raised.green-background
      "View Lifts"]]]

   [:div.mdl-card__menu
    [:a.mdl-button.mdl-button--icon.mdl-js-button.mdl-js-ripple-effect
     [:i.material-icons {:style {:color "#7d7d7d"}} "settings"]]]])


(defn day-breakdown [day-summaries]
  [:div
   (map day-summary-card day-summaries)
   [:dialog.mdl-dialog
    [:div.mdl-dialog__content
     "Hello world!"]]])


(defn new-cycle-form [fields cycle]
  [:div
   (if cycle
     [:div (str "Previous 1RM weights: " (:1rm cycle))]
     [:div (str "Set up your 1RM weights for the core lifts")])
   (map (fn [{:keys [name display value] :as field}]
          [:div.mdl-textfield.mdl-js-textfield
           [:div display]
           [:input.mdl-textfield__input
            {:type        "number"
             :name        name
             :value       value
             :placeholder "Weight in Pounds"
             :on-change   #(let [new-val (-> % .-target .-value)]
                             (rf/dispatch [:form-input name new-val]))}]])
        fields)])



(defn modal [{:keys [open title body cancel-fn accept-fn] :as props}]
  (when open
    [:div.modal-backing {:on-click cancel-fn}
      [:div.current-cycle-card.mdl-card.mdl-shadow--2dp.card-margin.modal
       {:on-click (fn [e] (.stopPropagation e))}
       [:div.mdl-card__title.current-cycle
        [:div.mdl-card__title-text title]]
       [:div.mdl-card__supporting-text
        {:style {:flex 1}}
        body]
       [:div.mdl-card__actions.mdl-card--border
        [:button.mdl-button.mdl-button--colored.mdl-js-button.mdl-js-ripple-effect
         {:on-click cancel-fn}
         "cancel"]
        [:button.mdl-button.mdl-button--colored.mdl-js-button.mdl-js-ripple-effect
         {:on-click accept-fn}
         "Accept"]
        ]]]))


(defn empty-state [cycle]
  (when-not cycle
    [:div.empty-state.mdl-shadow--2dp
     [:div.mdl-card__title.current-cycle
      [:h2.empty-state-title "Hai."]]
     [:div.mdl-card__supporting-text
      {:style {:flex 1}}
      [:p "Welcome to Gainz Goblin!"]
      [:p "Click the '+' button below to create your first Wendler 1.2 cycle!"]]]))


(defn home-panel []
  [:div
   [empty-state @(rf/subscribe [:current-cycle])]
   [current-cycle @(rf/subscribe [:current-cycle])]
   [previous-cycles-card @(rf/subscribe [:previous-cycles])]
   [:div.new-cycle-button
    [:button.mdl-button.mdl-js-button.mdl-button--fab.mdl-button--colored
     {:on-click #(rf/dispatch [:show-new-cycle-modal true])}
     [:i.material-icons "add"]]]
   [:div.bottom-image
    [:img {:src "https://s3-us-west-1.amazonaws.com/gainz-goblin-storage/images/gainz-goblin.PNG"
           :style {:max-width "100%"}}]]
   [modal {:open @(rf/subscribe [:show-new-cycle-modal])
           :title "New Cycle"
           :body [new-cycle-form
                    @(rf/subscribe [:cycle-form])
                    @(rf/subscribe [:current-cycle])]
           :cancel-fn #(rf/dispatch [:show-new-cycle-modal false])
           :accept-fn #(rf/dispatch [:create-cycle])
           }]
   ])



(defn primary-lift [sets]
  [:div.primary-lift-card.mdl-shadow--2dp.card-margin
   [:div.primary-lift-card-body
    [:h2.primary-lift-title (:verbose-name (first sets))]
    [:div.primary-lift-rep-scheme
     [:p (str "Current Training Max: " (:tm-max (first sets)))]
     [:ul.mdl-list
      (for [set sets]
        (let [{:keys [complete weight reps percentage id tm-max]} set]
          [:li.mdl-list__item.mdl-list__item--two-line
           {:style {:padding-top "10px" :padding-bottom "10px" :height "auto"}
            :on-click #(rf/dispatch [:toggle-complete id])}
           [:span.mdl-list__item-primary-content
            (str weight " lbs x " reps)
            [:span.mdl-list__item-sub-title
             (str (* 100 percentage) "% of 1RM")]]
           [:span.mdl-list__item-secondary-action
            [:input {:type "checkbox"
                     :checked complete}]]]))]]
    [:div.primary-lift-action-line
     [:button.mdl-button.mdl-button--colored.mdl-js-button.mdl-js-ripple-effect.mdl-button--raised
      "Rest"]]]])


(defn accessory-lift [sets]
  [:div.primary-lift-card.mdl-shadow--2dp.card-margin
   [:div.primary-lift-card-body
    [:h2.primary-lift-title (:verbose-name (first sets))]
    [:div.primary-lift-rep-scheme
     [:ul.mdl-list
      (for [set sets]
        (let [{:keys [complete weight reps percentage id tm-max]} set]
          [:li.mdl-list__item.mdl-list__item--two-line
           {:style {:padding-top "10px" :padding-bottom "10px" :height "auto"}}
           [:span.mdl-list__item-primary-content
            (str "Any Weight x " reps)
            [:span.mdl-list__item-sub-title
             (str "Assistance work")]]
           [:span.mdl-list__item-secondary-action
            [:input {:type "checkbox"
                     :on-click #(rf/dispatch [:toggle-complete id])
                     :checked complete}]]]))]]
    [:div.primary-lift-action-line
     [:button.mdl-button.mdl-button--colored.mdl-js-button.mdl-js-ripple-effect.mdl-button--raised
      "Rest"]]]])


(defn exercise-page [{:keys [primaries accessories] :as exercises}]
  [:div
   (map primary-lift primaries)
   (map accessory-lift accessories)])


(defn- panels [panel-name]
  (case panel-name
    :home  [home-panel]
    :day-breakdown [day-breakdown @(rf/subscribe [:day-breakdown])]
    :exercise-page [exercise-page @(rf/subscribe [:exercises])]
    [:div]))



(defn app-bar [{:keys [active-panel title previous-page] :as props}]
  [:div.app-bar
   [:div.hamburger
    (if (= active-panel :home)
      [:a.mdl-button.mdl-button--icon
        [:i.material-icons "menu" ]]
      [:a.mdl-button.mdl-button--icon {:href previous-page} [:i.material-icons "arrow_back"]])]
   [:span.mdl-layout-title title]])


(defn main-panel []
  (let [active-panel (rf/subscribe [:active-panel])]
    [:div
      [app-bar {:title @(rf/subscribe [:page-title])
                :previous-page @(rf/subscribe [:previous-page])
                :active-panel @active-panel}]
      [:div.container
        [panels @active-panel]]]))
