(ns meshanka.core-test
    (:require
     [cljs.test :refer-macros [deftest is testing]]
     [meshanka.pages.views.conjugator.view :as conjugator]))

(def test-verbs [{:verb "bude"
                  :prepared {:base      "bude"
                             :verb      "bude"
                             :verb-type :but}
                  :past-pf   "be"
                  :past-impf "buva"}
                 {:verb "ide"
                  :prepared {:base      "ide"
                             :verb      "ide"
                             :verb-type :iti}
                  :past-pf   "šed"
                  :past-impf "idéva"}
                 {:verb "može"
                  :prepared {:base      "može"
                             :verb      "može"
                             :verb-type :mogči}
                  :past-pf   "možé"
                  :past-impf "možéva"}
                 {:verb "polzovaje"
                  :prepared {:base "polzova"
                             :verb "polzovaje"
                             :verb-type :ova}
                  :past-pf   "polzová"
                  :past-impf "polzováva"}
                 {:verb "ukrivaje"
                  :prepared {:base "ukriva"
                             :verb "ukrivaje"
                             :verb-type :va}
                  :past-pf   "ukrí"
                  :past-impf "ukríva"}
                 {:verb "kusaji"
                  :prepared {:base "kusa"
                             :verb "kusaji"
                             :verb-type :ji}
                  :past-pf   "kusí"
                  :past-impf "kusáva"}
                 {:verb "stiraje"
                  :prepared {:base "stira"
                             :verb "stiraje"
                             :verb-type :je}
                  :past-pf   "stiré"
                  :past-impf "stiráva"}
                 {:verb "kupja"
                  :prepared {:base "kupja"
                             :verb "kupja"
                             :verb-type :ja}
                  :past-pf   "kupí"
                  :past-impf "kupjáva"}
                 {:verb "pomoga"
                  :prepared {:base "pomoga"
                             :verb "pomoga"
                             :verb-type :ga->že}
                  :past-pf   "pomožé"
                  :past-impf "pomogáva"}
                 {:verb "iska"
                  :prepared {:base "iska"
                             :verb "iska"
                             :verb-type :ka->če}
                  :past-pf   "isčé"
                  :past-impf "iskáva"}
                 {:verb "sluha"
                  :prepared {:base "sluha"
                             :verb "sluha"
                             :verb-type :ha->še}
                  :past-pf   "slušé"
                  :past-impf "sluháva"}
                 {:verb "veša"
                  :prepared {:base "veša"
                             :verb "veša"
                             :verb-type :ša->si}
                  :past-pf   "vesí"
                  :past-impf "vešáva"}
                 {:verb "vide"
                  :prepared {:base "vide"
                             :verb "vide"
                             :verb-type :e}
                  :past-pf   "vidé"
                  :past-impf "vidéva"}
                 {:verb "ljubi"
                  :prepared {:base "ljubi"
                             :verb "ljubi"
                             :verb-type :i}
                  :past-pf   "ljubí"
                  :past-impf "ljubíva"}
                 {:verb "pameta"
                  :prepared {:base "pameta"
                             :verb "pameta"
                             :verb-type :a}
                  :past-pf   "pametá"
                  :past-impf "pametáva"}])

(deftest prepare-verb-props-test
  "should produce a valid map of props used in conjugator fns."
  (doseq [v test-verbs]
   (is (= (:prepared v) (conjugator/prepare-verb-props (:verb v))))))

(deftest past-perfective-test
  (doseq [v test-verbs]
    (is (= (:past-pf v) (-> (:verb v) conjugator/prepare-verb-props conjugator/past-perfective)))))

(deftest past-imperfective-test
  (doseq [v test-verbs]
    (is (= (:past-impf v) (-> (:verb v) conjugator/prepare-verb-props conjugator/past-imperfective)))))
