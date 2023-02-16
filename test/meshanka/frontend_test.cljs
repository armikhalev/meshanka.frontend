(ns meshanka.core-test
    (:require
     [cljs.test :refer-macros [deftest is testing]]
     [meshanka.pages.views.conjugator.view :as conjugator]))

(def test-verbs [{:verb "bude"
                  :prepared  {:base      "bude"
                              :verb      "bude"
                              :verb-type :but}
                  :present   {:ja  "jesim, sim, je"
                              :mi  "jesmó, smo, je"
                              :ti  "jesí, si, je"
                              :vi  "jesté, ste, je"
                              :on  "jest, je"
                              :oni "jesó, so, je"}
                  :inf-pf    "but"
                  :inf-impf  "but"
                  :past-pf   "be"
                  :past-impf "buva"}
                 {:verb      "ide"
                  :prepared  {:base      "ide"
                              :verb      "ide"
                              :verb-type :iti}
                  :present   {:ja "idem", :mi "idemo", :ti "ideš", :vi "idete", :on "ide", :oni "idejo"}
                  :inf-pf    "ití"
                  :inf-impf  "iti"
                  :past-pf   "šed"
                  :past-impf "idéva"}
                 {:verb      "može"
                  :prepared  {:base      "može"
                              :verb      "može"
                              :verb-type :mogči}
                  :present   {:ja "možem", :mi "možemo", :ti "možeš", :vi "možete", :on "može", :oni "možejo"}
                  :inf-pf    "mogčí"
                  :inf-impf  "mogči"
                  :past-pf   "možé"
                  :past-impf "možéva"}
                 {:verb      "polzovaje"
                  :prepared  {:base "polzova"
                              :verb "polzovaje"
                              :verb-type :ova}
                  :present   {:ja "polzovam", :mi "polzovamo", :ti "polzovaš", :vi "polzovate", :on "polzovaje", :oni "polzovajo"}
                  :inf-pf    "polzovát"
                  :inf-impf  "polzovat"
                  :past-pf   "polzová"
                  :past-impf "polzováva"}
                 {:verb      "ukrivaje"
                  :prepared  {:base "ukriva"
                              :verb "ukrivaje"
                              :verb-type :va}
                  :present   {:ja "ukrivam", :mi "ukrivamo", :ti "ukrivaš", :vi "ukrivate", :on "ukrivaje", :oni "ukrivajo"}
                  :inf-pf    "ukrít"
                  :inf-impf  "ukrivat"
                  :past-pf   "ukrí"
                  :past-impf "ukríva"}
                 {:verb      "kusaji"
                  :prepared  {:base "kusa"
                              :verb "kusaji"
                              :verb-type :ji}
                  :present   {:ja "kusam", :mi "kusamo", :ti "kusaš", :vi "kusate", :on "kusaji", :oni "kusajo"}
                  :inf-pf    "kusít"
                  :inf-impf  "kusat"
                  :past-pf   "kusí"
                  :past-impf "kusáva"}
                 {:verb      "stiraje"
                  :prepared  {:base "stira"
                              :verb "stiraje"
                              :verb-type :je}
                  :present   {:ja "stiram", :mi "stiramo", :ti "stiraš", :vi "stirate", :on "stiraje", :oni "stirajo"}
                  :inf-pf    "stirét"
                  :inf-impf  "stirat"
                  :past-pf   "stiré"
                  :past-impf "stiráva"}
                 {:verb      "kupja"
                  :prepared  {:base "kupja"
                              :verb "kupja"
                              :verb-type :ja}
                  :present   {:ja "kupjam", :mi "kupjamo", :ti "kupjaš", :vi "kupjate", :on "kupja", :oni "kupjajo"}
                  :inf-pf    "kupít"
                  :inf-impf  "kupjat"
                  :past-pf   "kupí"
                  :past-impf "kupjáva"}
                 {:verb      "pomoga"
                  :prepared  {:base "pomoga"
                              :verb "pomoga"
                              :verb-type :ga->že}
                  :present   {:ja "pomogam", :mi "pomogamo", :ti "pomogaš", :vi "pomogate", :on "pomoga", :oni "pomogajo"}
                  :inf-pf    "pomožét"
                  :inf-impf  "pomogat"
                  :past-pf   "pomožé"
                  :past-impf "pomogáva"}
                 {:verb      "iska"
                  :prepared  {:base "iska"
                              :verb "iska"
                              :verb-type :ka->če}
                  :present   {:ja "iskam", :mi "iskamo", :ti "iskaš", :vi "iskate", :on "iska", :oni "iskajo"}
                  :inf-pf    "isčét"
                  :inf-impf  "iskat"
                  :past-pf   "isčé"
                  :past-impf "iskáva"}
                 {:verb      "sluha"
                  :prepared  {:base "sluha"
                              :verb "sluha"
                              :verb-type :ha->še}
                  :present   {:ja "sluham", :mi "sluhamo", :ti "sluhaš", :vi "sluhate", :on "sluha", :oni "sluhajo"}
                  :inf-pf    "slušét"
                  :inf-impf  "sluhat"
                  :past-pf   "slušé"
                  :past-impf "sluháva"}
                 {:verb      "veša"
                  :prepared  {:base "veša"
                              :verb "veša"
                              :verb-type :ša->si}
                  :present   {:ja "vešam", :mi "vešamo", :ti "vešaš", :vi "vešate", :on "veša", :oni "vešajo"}
                  :inf-pf    "vesít"
                  :inf-impf  "vešat"
                  :past-pf   "vesí"
                  :past-impf "vešáva"}
                 {:verb      "vide"
                  :prepared  {:base "vide"
                              :verb "vide"
                              :verb-type :e}
                  :present   {:ja "videm", :mi "videmo", :ti "videš", :vi "videte", :on "vide", :oni "videjo"}
                  :inf-pf    "vidét"
                  :inf-impf  "videt"
                  :past-pf   "vidé"
                  :past-impf "vidéva"}
                 {:verb      "ljubi"
                  :prepared  {:base "ljubi"
                              :verb "ljubi"
                              :verb-type :i}
                  :present   {:ja "ljubim", :mi "ljubimo", :ti "ljubiš", :vi "ljubite", :on "ljubi", :oni "ljubijo"}
                  :inf-pf    "ljubít"
                  :inf-impf  "ljubit"
                  :past-pf   "ljubí"
                  :past-impf "ljubíva"}
                 {:verb      "pameta"
                  :prepared  {:base "pameta"
                              :verb "pameta"
                              :verb-type :a}
                  :present   {:ja "pametam", :mi "pametamo", :ti "pametaš", :vi "pametate", :on "pameta", :oni "pametajo"} 
                  :inf-pf    "pametát"
                  :inf-impf  "pametat"
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

(deftest perfective-infinitive-test
  (doseq [v test-verbs]
    (is (= (:inf-pf v) (-> (:verb v) conjugator/prepare-verb-props conjugator/perfective-infinitive)))))

(deftest imperfective-infinitive-test
  (doseq [v test-verbs]
    (is (= (:inf-impf v) (-> (:verb v) conjugator/prepare-verb-props conjugator/imperfective-infinitive)))))

(deftest present-tense-test
  (doseq [v test-verbs]
    (is (= (:present v) (-> (:verb v) conjugator/prepare-verb-props conjugator/present-tense)))))

