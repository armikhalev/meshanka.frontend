(ns meshanka.pages.conjugator
  (:require
   ;; [meshanka.subs :as subs]
   [reagent.core :as r]
   ;; [re-frame.core :as rf]
   [clojure.string :as str]
   [goog.string :as gstr]))

(def accented-vowels #"[áéíúó]")

(defn replace-accented-vowels
  [past-impf]
  (let [[base _]         (str/split past-impf accented-vowels) ;; get stressed vowel out
        unstressed-vowel (case (re-find accented-vowels past-impf)
                           "á" "a"
                           "é" "e"
                           "í" "i"
                           "ú" "u"
                           "ó" "o"
                           (last past-impf))]
    [base unstressed-vowel]))

(defn past-perfective
  [{:keys [base prefix verb-type]}]
  (let [root (if (= verb-type :va)(.slice base 0 -2)(.slice base 0 -1))
        ending (if (= verb-type :va)(.slice root -1)(.slice base -1))
        stressed-vowel (case ending "a" "á" "e" "é" "u" "ú" "o" "ó" "i" "í" ending)
        stem (.slice root 0 -1)]
    (case  verb-type
      :iti      (str prefix "šed")
      :but      "be"
      :va       (str prefix stem stressed-vowel)
      :ji       (str prefix root "í")
      :je       (str prefix root "é")
      :ja       (str prefix stem "í")
      :ga->že   (str prefix stem "žé")
      :ka->če   (str prefix stem "čé")
      :ha->še   (str prefix stem "šé")
      :ša->si   (str prefix stem "sí")
      :žaji->zi (str prefix stem "zí")
      :nuva     (str prefix (.slice stem 0 -1) "ú")
      (str prefix root stressed-vowel))))

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
      :but  "buva"
      :va   (str (.slice root 0 -1) stressed-vowel "va")
      :nuva (str (.slice root 0 -3) stressed-vowel "va")
      (str root stressed-vowel "va"))))

(defn imperfective-infinitive
  [{:keys [verb-type base] :as props}]
  (let [imperf-base (past-imperfective props)]
    (case  verb-type
      :iti    "iti"
      :but    "but"
      :mogči  "mogči"
      :nuva   (str (.slice imperf-base 0 -3) "at")
      (str base "t"))))

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
  (let [verb (present-tense props)]
    [:div#present-tense.block
     [:h4 "Nastoječi čas / Present tense"]
     [:div.present-tense
      [:table.present-tense-table
       [:thead
        [:tr
         [:th "Jedinistveni lik / Singular "]
         [:th "Množistveni lik / Plural"]]]
       [:tbody
        [:tr.person1
         [:td (:ja verb)]
         [:td (:mi verb)]]
        [:tr.person2
         [:td (:ti verb)]
         [:td (:vi verb)]]
        [:tr.person3
         [:td (:on verb)]
         [:td (:oni verb)]]]]]]))

(defn future-perfective
  [{:keys [verb-type] :as props}]
  (let [past-perf (past-perfective props)
        but-type? (= :but verb-type)
        iti-type? (= :iti verb-type)
        [base base-vowel] (replace-accented-vowels past-perf)] ;; replace stressed vowel with unstressed
    {:ja  (cond but-type? "budem"  iti-type? "ja če priidem"   :default (str "ja če " base base-vowel "m"))
     :mi  (cond but-type? "budemo" iti-type? "mi če priidemo"  :default (str "mi če " base base-vowel "mo"))
     :ti  (cond but-type? "budeš"  iti-type? "ti če priideš"   :default (str "ti če " base base-vowel "š"))
     :vi  (cond but-type? "budete" iti-type? "vi če priidete"  :default (str "vi če " base base-vowel "te"))
     :on  (cond but-type? "bude"   iti-type? "on če priide"    :default (str "on če " base base-vowel))
     :oni (cond but-type? "budejo" iti-type? "oni če priidejo" :default (str "oni če " base base-vowel "jo"))}))

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
  (let [verb (future-imperfective props)]
    [:div.block
     [:table.future-tense-imperfective-table
      [:thead
       [:tr
        [:th "Jedinistveni lik / Singular "]
        [:th "Množistveni lik / Plural"]]]
      [:tbody
       [:tr.person1
        [:td (:ja verb)]
        [:td (:mi verb)]]
       [:tr.person2
        [:td (:ti verb)]
        [:td (:vi verb)]]
       [:tr.person3
        [:td (:on verb)]
        [:td (:oni verb)]]]]]))

(defn future-perfective-view
  [props]
  (let [verb (future-perfective props)]
    [:div.block
     [:table.future-tense-perfective-table
      [:thead
       [:tr
        [:th "Jedinistveni lik / Singular "]
        [:th "Množistveni lik / Plural"]]]
      [:tbody
       [:tr.person1
        [:td (:ja verb)]
        [:td (:mi verb)]]
       [:tr.person2
        [:td (:ti verb)]
        [:td (:vi verb)]]
       [:tr.person3
        [:td (:on verb)]
        [:td (:oni verb)]]]]]))

(defn imperative-imperfective
  [{:keys [verb-type] :as props}]
  (let [past-impf         (past-imperfective props)
        imperative-base  #(.slice past-impf 0 %)
        imper-base-min-3  (imperative-base -3)
        [base base-vowel] (replace-accented-vowels past-impf) ;; replace stressed vowel with unstressed
        base (case verb-type
               :but                   "budi"
               (or :mogči :iti :e :i) (str imper-base-min-3 "í")
               :va                    (str base base-vowel "váj")
                                        ;else
               (str (imperative-base -2) "j"))]

    {:sg base
     :pl (str base "te")}))

(defn imperative-perfective
  [{:keys [verb-type prefix] :as props}]
  (let [past-perf       (past-perfective props)
        imperative-base (.slice past-perf 0 -1)
        base (case verb-type
               :but "budi"
               :iti "priidí"
               (or :a :ova) (str imperative-base "áj")
               :va (str past-perf "j")
               (str imperative-base "í"))]
    {:sg base
     :pl (str base "te")}))

(defn present-active-participle
  [props]
  (str (:base props) "či"))

(defn present-passive-participle
  [props]
  (str (:base props) "mi"))

(defn past-active-participle
  [props]
  (case (:verb-type props)
    :but  {:pf "bevši"
           :impf "buvši"}
    :nuva {:pf (str (past-perfective props) "vši")
           :impf (str (past-imperfective props) "vši")}
    ;; default
    {:pf   (str (past-perfective props) "vši")
     :impf (str (:base props) "vši")}))

(defn past-passive-participle
  [{:keys [verb-type base prefix] :as props}]
  (let [past-perfective-form (past-perfective props)
        ends-on-a-accented?  (gstr/caseInsensitiveEndsWith past-perfective-form "á")
        ends-on-a?           (gstr/caseInsensitiveEndsWith past-perfective-form "a")
        base                 (:base props)
        pf-root              (.slice past-perfective-form 0 -1)
        impf-root            (.slice base 0 -1)]
    (case verb-type
      :but       {:pf "" :impf ""} ;; <-- no such forms
      :ša->si    {:impf  (str base "ni")
                  :pf    (str prefix impf-root "eni")}
      :žaji->zi  {:impf  (str base "ni")
                  :pf    (str prefix impf-root "eni")}
      (:ji :ja ) {:impf  (str base "ni")
                  :pf    (str pf-root "jeni")}
      :i         {:impf  (str impf-root "jeni")
                  :pf    (str pf-root "jeni")}
      :nuva      {:pf (str prefix impf-root "áni") :impf (str (past-imperfective props) "ni")}
      :va        (cond
                   (not (or ends-on-a? ends-on-a-accented?))
                   {:impf  (str base "ni")
                    :pf    (str past-perfective-form "jeni")}

                   :default
                   {:impf  (str base "ni")
                    :pf    (str past-perfective-form "ni")})
      ;; default
      {:pf   (str past-perfective-form "ni")
       :impf (str base "ni")})))

(defn verbal-noun
  [{:keys [verb-type base] :as props}]
  (case verb-type
    :but "butie"
    :i   (str (.slice base 0 -1) "jenie")
    ;; default
    (str (:base props) "nie")))

;; Auxiliary ;;

(def verb-types
  "A set that contains all the verb types. It should be updated when types are added or removed."
  {1 :iti
   2 :but
   3 :nuva
   4 :mogči
   5 :ova
   6 :va
   7 :e
   8 :i
   9 :a
   10 :je
   11 :ji
   12 :ja
   13 :ga->že
   14 :ka->če
   15 :ha->še
   16 :ša->si
   17 :žaji->zi})

(def verb-types-set (set (vals verb-types)))

(defn find-verb-type
  "Conditionally applies Meshanka rules to determine given verb's type."
  [verb]
  (let [found-type (case verb
                     "ide" :iti
                     "bude" :but
                     (condp #(gstr/endsWith %2 %1) verb
                       "nuvaje":nuva
                       "može"  :mogči
                       "ovaje" :ova
                       "vaje"  :va
                       "žaji"  :žaji->zi
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
                       nil))]
    (if (verb-types-set found-type) found-type "...")))

(defn input-field [v]
  [:div#input-conjugator
   [:input {:type "text"
            :on-change #(reset! v (-> % .-target .-value))}]])

(defn prepare-verb-props [v]
  (let [v3person-sg          (gstr/trim v)
        splitted-by-dash     (str/split v3person-sg #"-" 2)
        [prefix verb]        (if (= (count splitted-by-dash) 2) splitted-by-dash [nil (first splitted-by-dash)])
        verb-type            (find-verb-type (gstr/trim verb))
        exception-ending     (case verb-type :je "je" :ji "ji" :nuva "je" :ova "je" :va "je" :žaji->zi "ji" nil)]
    {:base        (if exception-ending (first (str/split verb exception-ending)) verb)
     :prefix      prefix
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
         [:div
          [:div.tile.is-ancestor
           [:div.box
            [:span [:span "All verb types: "]
             (for [[i v] verb-types]
               [:span.tag {:key (str "verb-type--" i v)} (str i ": " (name v))])]]]]
         (when (not (str/blank? @!v))
           [:div
            [:div.tile.is-ancestor
             [:div.box
              [:span [:span "Verb type: "] [:span.tag (-> props :verb-type  name)]]]
             [:div.box
              [:span [:span "Prefix: "] [:span.tag (:prefix  props)]]]]
            [:div.tile.is-ancestor
             [:div.box
              [:h4 "Infinitiv"]
              [:div.block
               [:h6 "Nesoveršeni Vid / Imperfective Aspect"]
               [:div.tag (imperfective-infinitive props)]]
              [:div.block
               [:h6 "Soveršeni Vid / Perfective Aspect"]
               [:div.tag (perfective-infinitive props)]]]
             [:div.box
              [:div
               [:h4 "Past tense"]
               [:div.block
                [:h6 "Nesoveršeni Vid / Imperfective Aspect"]
                [:div.tag (past-imperfective props)]]
               [:div.block
                [:h6 "Soveršeni Vid / Perfective Aspect"]
                [:div.tag (past-perfective props)]]]]
             [:div.box
              [:div
               [:h4 "Conditional"]
               [:div.block
                [:h6 "Nesoveršeni Vid / Imperfective Aspect"]
                [:div.tag (str "Ja bi " (past-imperfective props))]]
               [:div.block
                [:h6 "Soveršeni Vid / Perfective Aspect"]
                [:div.tag (str "Ja bi " (past-perfective props))]]]]
             [:div.box
              [:div
               [:h4 "Imperative"]
               [:div.block
                [:h6 "Nesoveršeni Vid / Imperfective Aspect"]
                [:div.tile
                 [:div.tile
                  [:div "Singular: "]
                  [:div.tag (:sg (imperative-imperfective props))]]
                 [:div.tile
                  [:div "Plural: "]
                  [:div.tag (:pl (imperative-imperfective props))]]]]
               [:div.block
                [:h6 "Soveršeni Vid / Perfective Aspect"]
                [:div.tile
                 [:div.tile
                  [:div "Singular: "]
                  [:div.tag (:sg (imperative-perfective props))]]
                 [:div.tile
                  [:div "Plural: "]
                  [:div.tag (:pl (imperative-perfective props))]]]]]]]
            [:div.tile.is-ancestor
             [:div.box
              [:div
               [:h4 "Present Participle"]
               [:div.block
                [:h6 "Active"]
                [:div.tag (present-active-participle props)]]
               [:div.block
                [:h6 "Passive"]
                [:div.tag (present-passive-participle props)]]]]
             [:div.box
              [:div
               [:h4 "Past Active Participle"]
               [:div.block
                [:h6 "Nesoveršeni Vid / Imperfective Aspect"]
                [:div.tag (-> props past-active-participle :impf)]]
               [:div.block
                [:h6 "Soveršeni Vid / Perfective Aspect"]
                [:div.tag (-> props past-active-participle :pf)]]]]
             [:div.box
              [:div
               [:h4 "Past Passive Participle"]
               [:div.block
                [:h6 "Nesoveršeni Vid / Imperfective Aspect"]
                [:div.tag (-> props past-passive-participle :impf)]]
               [:div.block
                [:h6 "Soveršeni Vid / Perfective Aspect"]
                [:div.tag (-> props past-passive-participle :pf)]]]]
             [:div.box
              [:div
               [:h4 "Verbal Noun"]
               [:div.block
                [:div.tag (verbal-noun props)]]]]]
            [:hr]
            [present-tense-view props]
            [:hr]
            [:div.block
             [:h4 "Future tense"]
             [:h6 "Nesoveršeni Vid / Imperfective Aspect"]
             [future-imperfective-view props]]
            [:div.block
             [:h6 "Soveršeni Vid / Perfective Aspect"]
             [future-perfective-view props]]
            [:hr]])]))))

