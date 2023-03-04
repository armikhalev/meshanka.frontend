(ns meshanka.pages.views.conjugator.view
  (:require
   ;; [meshanka.subs :as subs]
   [reagent.core :as r]
   ;; [re-frame.core :as rf]
   [clojure.string :as str]
   [goog.string :as gstr]))

(defn past-perfective
  [{:keys [base prefix verb-type]}]
  (let [root (if (= verb-type :va)(.slice base 0 -2)(.slice base 0 -1))
        ending (if (= verb-type :va)(.slice root -1)(.slice base -1))
        stressed-vowel (case ending "a" "á" "e" "é" "u" "ú" "o" "ó" "i" "í" ending)
        stem (.slice root 0 -1)]
    (case  verb-type
      :iti    (str prefix "šed")
      :but    "be"
      :va     (str prefix stem stressed-vowel)
      :ji     (str prefix root "í")
      :je     (str prefix root "é")
      :ja     (str prefix stem "í")
      :ga->že (str prefix stem "žé")
      :ka->če (str prefix stem "čé")
      :ha->še (str prefix stem "šé")
      :ša->si (str prefix stem "sí")
      (str prefix root stressed-vowel))))

(defn imperfective-infinitive
  [{:keys [verb-type base]}]
  (case  verb-type
    :iti    "iti"
    :but    "but"
    :mogči  "mogči"
    (str base "t")))

(defn perfective-infinitive
  [{:keys [verb-type prefix base] :as props}]
  (let [past-perf (past-perfective props)]
    (case  verb-type
      :iti   "priití"
      :but   "but"
      :mogči "smogčí"
      (str past-perf "ti"))))

(defn present-tense
  [{:keys [base verb verb-type]}]
  (let [but-type? (= :but verb-type)]
    {:ja  (if but-type? "jesim, sim, je" (str base "m"))
     :mi  (if but-type? "jesmó, smo, je" (str base "mo"))
     :ti  (if but-type? "jesí, si, je" (str base "š"))
     :vi  (if but-type? "jesté, ste, je" (str base "te"))
     :on  (if but-type? "jest, je" verb)
     :oni (if but-type? "jesó, so, je" (str base "jo"))}))

(defn present-tense-view
  [props]
  [:div#present-tense.block
   [:h4 "Nastoječi čas / Present tense"]
   [:div.present-tense
    [:table.present-tense-table
     [:thead
      [:tr
       [:th ""]
       [:th "Jedinistveni lik / Singular "]
       [:th ""]
       [:th "Množistveni lik / Plural"]]]
     [:tbody
      [:tr.person1
       [:td "ja"]
       [:td (-> props present-tense :ja)]
       [:td "mi"]
       [:td (-> props present-tense :mi)]]
      [:tr.person2
       [:td "ti"]
       [:td (-> props present-tense :ti)]
       [:td "vi"]
       [:td (-> props present-tense :vi)]]
      [:tr.person3
       [:td "on"]
       [:td (-> props present-tense :on)]
       [:td "oni"]
       [:td (-> props present-tense :oni)]]]]]])

(defn past-imperfective
  [{:keys [base verb-type]}]
  ;; if -va don't need to add it, otherwise just add -va
  (let [root (if (= verb-type :va)(.slice base 0 -2)(.slice base 0 -1))
        ending (if (= verb-type :va)(.slice root -1)(.slice base -1))
        stressed-vowel (case ending
                         "a" "á"
                         "e" "é"
                         "u" "ú"
                         "o" "ó"
                         "i" "í"
                         ending)]
    (case  verb-type
      :but "buva"
      :va  (str (.slice root 0 -1) stressed-vowel "va")
      (str root stressed-vowel "va"))))

(defn future-perfective
  [{:keys [verb-type] :as props}]
  (let [past-perf (past-perfective props)
        but-type? (= :but verb-type)
        iti-type? (= :iti verb-type)]
    {:ja  (cond but-type? "budem"  iti-type? "ja če priidém"   :default (str "ja če " past-perf "m"))
     :mi  (cond but-type? "budemo" iti-type? "mi če priidémo"  :default (str "mi če " past-perf "mo"))
     :ti  (cond but-type? "budeš"  iti-type? "ti če priidéš"   :default (str "ti če " past-perf "š"))
     :vi  (cond but-type? "budete" iti-type? "vi če priidéte"  :default (str "vi če " past-perf "te"))
     :on  (cond but-type? "bude"   iti-type? "on če priidé"    :default (str "on če " past-perf))
     :oni (cond but-type? "budejo" iti-type? "oni če priidéjo" :default (str "oni če " past-perf "jo"))}))

(defn future-imperfective
  [{:keys [verb-type] :as props}]
  (let [perf-inf  (imperfective-infinitive props)
        but-type? (= :but verb-type)]
    {:ja  (if but-type? "budem" (str "bum " perf-inf))
     :mi  (if but-type? "budemo" (str "bumo " perf-inf))
     :ti  (if but-type? "budeš" (str "buš " perf-inf))
     :vi  (if but-type? "budete" (str "buste " perf-inf))
     :on  (if but-type? "bude" (str "bude " perf-inf))
     :oni (if but-type? "budejo" (str "bujo " perf-inf))}))

(defn future-imperfective-view
  [props]
  [:div.block
   [:table.future-tense-imperfective-table
    [:thead
     [:tr
      [:th ""]
      [:th "Jedinistveni lik / Singular "]
      [:th ""]
      [:th "Množistveni lik / Plural"]]]
    [:tbody
     [:tr.person1
      [:td "ja"]
      [:td (-> props future-imperfective :ja)]
      [:td "mi"]
      [:td (-> props future-imperfective :mi)]]
     [:tr.person2
      [:td "ti"]
      [:td (-> props future-imperfective :ti)]
      [:td "vi"]
      [:td (-> props future-imperfective :vi)]]
     [:tr.person3
      [:td "on"]
      [:td (-> props future-imperfective :on)]
      [:td "oni"]
      [:td (-> props future-imperfective :oni)]]]]])

(defn future-perfective-view
  [props]
  [:div.block
   [:table.future-tense-perfective-table
    [:thead
     [:tr
      [:th ""]
      [:th "Jedinistveni lik / Singular "]
      [:th ""]
      [:th "Množistveni lik / Plural"]]]
    [:tbody
     [:tr.person1
      [:td "ja"]
      [:td (-> props future-perfective :ja)]
      [:td "mi"]
      [:td (-> props future-perfective :mi)]]
     [:tr.person2
      [:td "ti"]
      [:td (-> props future-perfective :ti)]
      [:td "vi"]
      [:td (-> props future-perfective :vi)]]
     [:tr.person3
      [:td "on"]
      [:td (-> props future-perfective :on)]
      [:td "oni"]
      [:td (-> props future-perfective :oni)]]]]])

;; LOGIC ;;

(defn find-verb-type
  "Conditionally applies Meshanka rules to determine given verb's type."
  [verb]
  (case verb
    "ide" :iti
    "bude" :but
    (condp #(gstr/endsWith %2 %1) verb
      "može"  :mogči
      "ovaje" :ova
      "vaje"  :va
      "ji"    :ji
      "je"    :je
      "ja"    :ja
      "ga"    :ga->že
      "ka"    :ka->če
      "ha"    :ha->še
      "ša"    :ša->si
      "e"     :e
      "i"     :i
      "a"     :a
      nil)))

(defn input-field [v]
  [:div#input-conjugator
   [:input {:type "text"
            :on-change #(reset! v (-> % .-target .-value))}]])

(defn prepare-verb-props [v]
  (let [v3person-sg          (gstr/trim v)
        splitted-by-dash     (str/split v3person-sg #"-" 2)
        [prefix verb]   (if (= (count splitted-by-dash) 2) splitted-by-dash [nil (first splitted-by-dash)])
        verb-type            (find-verb-type (gstr/trim verb))
        exception-ending     (case verb-type :je "je" :ji "ji" :ova "je" :va "je" nil)]
    {:base        (if exception-ending (first (str/split verb exception-ending)) verb)
     :prefix prefix
     :verb        verb
     :verb-type   verb-type}))

;; MAIN ;;

(defn page []
  (let [!v (r/atom "na-pisavaje")]
    (fn []
      (let [props (prepare-verb-props @!v)]
        [:section.section>div.container>div.content
         [:h2 "Spregalnik / Conjugator"]
         [input-field !v]
         [:hr]
         (when (not (str/blank? @!v))
           [:div
            [:div.box.has-text-centered
             [:span "Verb type: "] [:span.tag (:verb-type  props)]
             [:span "Prefix: "] [:span.tag (:prefix  props)]]
            [:div
             [:h4 "Infinitiv"]
             [:div.block
              [:h6 "Nesoveršeni Vid / Imperfective Aspect"]
              [:div (imperfective-infinitive props)]]
             [:div.block
              [:h6 "Soveršeni Vid / Perfective Aspect"]
              [:div (perfective-infinitive props)]]]
            [:hr]
            [present-tense-view props]
            [:hr]
            [:div
             [:h4 "Past tense"]
             [:div.block
              [:h6 "Nesoveršeni Vid / Imperfective Aspect"]
              [:div (past-imperfective props)]]
             [:div.block
              [:h6 "Soveršeni Vid / Perfective Aspect"]
              [:div (past-perfective props)]]]
            [:hr]
            [:div
             [:h4 "Future tense"]
             [:h6 "Nesoveršeni Vid / Imperfective Aspect"]
             [future-imperfective-view props]]
            [:div
             [:h6 "Soveršeni Vid / Perfective Aspect"]
             [future-perfective-view props]]])]))))

;; person3sg (case verb-type
;;             :iti   "ide"
;;             :mogči "može"
;;             :ova   
;;             :va
;;             :ji
;;             :je
;;             :but
;;             :ja
;;             :ga->že
;;             :ka->če
;;             :ha->še
;;             :e
;;             :i
;;             :a
;; v)
