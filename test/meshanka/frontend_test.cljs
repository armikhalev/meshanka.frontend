(ns meshanka.core-test
    (:require
     [cljs.test :refer-macros [deftest is testing]]
     [meshanka.pages.views.conjugator.view :as conjugator]))

(def test-verbs [{:verb "bude"
                  :prepared {:base      "bude"
                             :verb      "bude"
                             :verb-type :but
                             :but-type? true}
                  :past-pf   "be"
                  :past-impf "buva"
                  }
                 ;; "ide"
                 ;; "može"
                 ;; "polzovaje"
                 ;; "zakrivaje"
                 ;; "kusaji"
                 ;; "stiraje"
                 ;; "kupja"
                 ;; "pomoga"
                 ;; "iska"
                 ;; "sluha"
                 ;; "veša"
                 ;; "vide"
                 ;; "ljubi"
                 ;; "pameta"
                 ])

(deftest prepare-verb-props-test
  "should produce a valid map of props used in conjugator fns."
  (doseq [v test-verbs]
   (is (= (:prepared v) (conjugator/prepare-verb-props (:verb v))))))

#_(deftest past-perfective-test
  (doseq [v test-verbs]
    (is (= (:past-pf v) (-> (:verb v) conjugator/prepare-verb-props conjugator/past-perfective)))))

(deftest past-imperfective-test
  (doseq [v test-verbs]
    (is (= (:past-pf v) (-> (:verb v) conjugator/prepare-verb-props conjugator/past-perfective)) "past perfective")
    (is (= (:past-impf v) (-> (:verb v) conjugator/prepare-verb-props conjugator/past-imperfective)) "past imperfective")))
