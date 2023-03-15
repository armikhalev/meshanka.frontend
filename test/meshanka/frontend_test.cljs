(ns meshanka.core-test
    (:require
     [cljs.test :refer-macros [deftest is testing]]
     [meshanka.pages.views.conjugator.view :as conjugator]))

(def test-verbs [{:verb "bude"
                  :prepared  {:base      "bude"
                              :prefix    nil
                              :verb      "bude"
                              :verb-type :but}
                  :present   {:ja  "jesim, sim, je"
                              :mi  "jesmó, smo, je"
                              :ti  "jesí, si, je"
                              :vi  "jesté, ste, je"
                              :on  "jest, je"
                              :oni "jesó, so, je"}
                  :imper-pf   {:sg "budi" :pl "budite"}
                  :imper-impf {:sg "budi" :pl "budite"}
                  :fut-pf     {:ja  "budem" :mi  "budemo" :ti  "budeš" :vi  "budete" :on  "bude" :oni "budejo"}
                  :fut-impf   {:ja  "budem" :mi  "budemo" :ti  "budeš" :vi  "budete" :on  "bude" :oni "budejo"}
                  :inf-pf     "but"
                  :inf-impf   "but"
                  :past-pf    "be"
                  :past-impf  "buva"

                  :prs-act-prtc   "budeči"
                  :prs-pass-prtc  "budemi"
                  :past-act-prtc  {:impf "buvši" :pf "bevši"}
                  :past-pass-prtc {:impf "" :pf ""}
                  :verbal-noun    "butie"}

                 {:verb      "pri-ide"
                  :prepared  {:base      "ide"
                              :prefix    "pri"
                              :verb      "ide"
                              :verb-type :iti}
                  :present    {:ja "idem", :mi "idemo", :ti "ideš", :vi "idete", :on "ide", :oni "idejo"}
                  :imper-pf   {:sg "priidí" :pl "priidíte"}
                  :imper-impf {:sg "idí" :pl "idíte"}
                  :fut-pf     {:ja "ja če priidém", :mi "mi če priidémo", :ti "ti če priidéš", :vi "vi če priidéte", :on "on če priidé", :oni "oni če priidéjo"}
                  :fut-impf   {:ja "bum iti", :mi "bumo iti", :ti "buš iti", :vi "buste iti", :on "bude iti", :oni "bujo iti"}
                  :inf-pf     "priití"
                  :inf-impf   "iti"
                  :past-pf    "prišed"
                  :past-impf  "idéva"

                  :prs-act-prtc   "ideči"
                  :prs-pass-prtc  "idemi"
                  :past-act-prtc  {:impf "idevši" :pf "prišedvši"}
                  :past-pass-prtc {:impf "ideni"  :pf "prišedni"}
                  :verbal-noun    "idenie"}

                 {:verb      "s-može"
                  :prepared  {:base      "može"
                              :prefix    "s"
                              :verb      "može"
                              :verb-type :mogči}
                  :present    {:ja "možem", :mi "možemo", :ti "možeš", :vi "možete", :on "može", :oni "možejo"}
                  :imper-pf   {:sg "smoží" :pl "smožíte"}
                  :imper-impf {:sg "moží" :pl "možíte"}
                  :fut-pf     {:ja "ja če smožém", :mi "mi če smožémo", :ti "ti če smožéš", :vi "vi če smožéte", :on "on če smožé", :oni "oni če smožéjo"}
                  :fut-impf   {:ja "bum mogči", :mi "bumo mogči", :ti "buš mogči", :vi "buste mogči", :on "bude mogči", :oni "bujo mogči"}
                  :inf-pf     "smogčí"
                  :inf-impf   "mogči"
                  :past-pf    "smožé"
                  :past-impf  "možéva"

                  :prs-act-prtc   "možeči"
                  :prs-pass-prtc  "možemi"
                  :past-act-prtc  {:impf "moževši" :pf "smožévši"}
                  :past-pass-prtc {:impf "moženi" :pf "smožéni"}
                  :verbal-noun    "moženie"}

                 {:verb      "iz-polzovaje"
                  :prepared  {:base "polzova"
                              :prefix "iz"
                              :verb "polzovaje"
                              :verb-type :ova}
                  :present    {:ja "polzovam", :mi "polzovamo", :ti "polzovaš", :vi "polzovate", :on "polzovaje", :oni "polzovajo"}
                  :imper-pf   {:sg "izpolzováj" :pl "izpolzovájte"}
                  :imper-impf {:sg "polzováj" :pl "polzovájte"}
                  :fut-pf     {:ja "ja če izpolzovám", :mi "mi če izpolzovámo", :ti "ti če izpolzováš", :vi "vi če izpolzováte", :on "on če izpolzová", :oni "oni če izpolzovájo"}
                  :fut-impf   {:ja "bum polzovat", :mi "bumo polzovat", :ti "buš polzovat", :vi "buste polzovat", :on "bude polzovat", :oni "bujo polzovat"}
                  :inf-pf     "izpolzováti"
                  :inf-impf   "polzovat"
                  :past-pf    "izpolzová"
                  :past-impf  "polzováva"

                  :prs-act-prtc   "polzovači"
                  :prs-pass-prtc  "polzovami"
                  :past-act-prtc  {:impf "polzovavši" :pf "izpolzovávši"}
                  :past-pass-prtc {:impf "polzovani" :pf "izpolzováni" }
                  :verbal-noun    "polzovanie"}

                 {:verb      "ukrivaje"
                  :prepared  {:base "ukriva"
                              :prefix nil
                              :verb "ukrivaje"
                              :verb-type :va}
                  :present    {:ja "ukrivam", :mi "ukrivamo", :ti "ukrivaš", :vi "ukrivate", :on "ukrivaje", :oni "ukrivajo"}
                  :imper-pf   {:sg "ukríj" :pl "ukríjte"}
                  :imper-impf {:sg "ukriváj" :pl "ukrivájte"}
                  :fut-pf     {:ja "ja če ukrím", :mi "mi če ukrímo", :ti "ti če ukríš", :vi "vi če ukríte", :on "on če ukrí", :oni "oni če ukríjo"}
                  :fut-impf   {:ja "bum ukrivat", :mi "bumo ukrivat", :ti "buš ukrivat", :vi "buste ukrivat", :on "bude ukrivat", :oni "bujo ukrivat"}
                  :inf-pf     "ukríti"
                  :inf-impf   "ukrivat"
                  :past-pf    "ukrí"
                  :past-impf  "ukríva"

                  :prs-act-prtc   "ukrivači"
                  :prs-pass-prtc  "ukrivami"
                  :past-act-prtc  {:impf "ukrivavši" :pf "ukrívši"}
                  :past-pass-prtc {:impf "ukrivani" :pf "ukríjeni" } ;; `-jeni` for verbs on -i?
                  :verbal-noun    "polzovanie"}

                 {:verb      "ot-čuvaje"
                  :prepared  {:base "čuva"
                              :prefix "ot"
                              :verb "čuvaje"
                              :verb-type :va}
                  :present    {:ja "čuvam", :mi "čuvamo", :ti "čuvaš", :vi "čuvate", :on "čuvaje", :oni "čuvajo"}
                  :imper-pf   {:sg "otčúj" :pl "otčújte"}
                  :imper-impf {:sg "čuváj" :pl "čuvájte"}
                  :fut-pf     {:ja "ja če otčúm", :mi "mi če otčúmo", :ti "ti če otčúš", :vi "vi če otčúte", :on "on če otčú", :oni "oni če otčújo"}
                  :fut-impf   {:ja "bum čuvat", :mi "bumo čuvat", :ti "buš čuvat", :vi "buste čuvat", :on "bude čuvat", :oni "bujo čuvat"}
                  :inf-pf     "otčúti"
                  :inf-impf   "čuvat"
                  :past-pf    "otčú"
                  :past-impf  "čúva"

                  :prs-act-prtc   "čuvači"
                  :prs-pass-prtc  "čuvami"
                  :past-act-prtc  {:impf "čuvavši" :pf "otčúvši"}
                  :past-pass-prtc {:impf "čuvani" :pf "otčújeni" } ;; <-- verbs that have one syllable root get ending `-jeni`
                  :verbal-noun    "čuvanie"}

                 {:verb      "na-pisavaje"
                  :prepared  {:base "pisava"
                              :prefix "na"
                              :verb "pisavaje"
                              :verb-type :va}
                  :present    {:ja "pisavam", :mi "pisavamo", :ti "pisavaš", :vi "pisavate", :on "pisavaje", :oni "pisavajo"}
                  :imper-pf   {:sg "napisáj" :pl "napisájte"}
                  :imper-impf {:sg "pisaváj" :pl "pisavájte"}
                  :fut-pf     {:ja "ja če napisám", :mi "mi če napisámo", :ti "ti če napisáš", :vi "vi če napisáte", :on "on če napisá", :oni "oni če napisájo"}
                  :fut-impf   {:ja "bum pisavat", :mi "bumo pisavat", :ti "buš pisavat", :vi "buste pisavat", :on "bude pisavat", :oni "bujo pisavat"}
                  :inf-pf     "napisáti"
                  :inf-impf   "pisavat"
                  :past-pf    "napisá"
                  :past-impf  "pisáva"

                  :prs-act-prtc   "pisavači"
                  :prs-pass-prtc  "pisavami"
                  :past-act-prtc  {:impf "pisavavši" :pf "napisávši"}
                  :past-pass-prtc {:impf "pisavani" :pf "napisáni" }
                  :verbal-noun    "pisavanie"}

                 {:verb      "u-kusaji"
                  :prepared  {:base "kusa"
                              :prefix "u"
                              :verb "kusaji"
                              :verb-type :ji}
                  :imper-pf   {:sg "ukusí" :pl "ukusíte"}
                  :imper-impf {:sg "kusáj" :pl "kusájte"}
                  :present    {:ja "kusam", :mi "kusamo", :ti "kusaš", :vi "kusate", :on "kusaji", :oni "kusajo"}
                  :fut-pf     {:ja "ja če ukusím", :mi "mi če ukusímo", :ti "ti če ukusíš", :vi "vi če ukusíte", :on "on če ukusí", :oni "oni če ukusíjo"}
                  :fut-impf   {:ja "bum kusat", :mi "bumo kusat", :ti "buš kusat", :vi "buste kusat", :on "bude kusat", :oni "bujo kusat"}
                  :inf-pf     "ukusíti"
                  :inf-impf   "kusat"
                  :past-pf    "ukusí"
                  :past-impf  "kusáva"

                  :prs-act-prtc   "kusači"
                  :prs-pass-prtc  "kusami"
                  :past-act-prtc  {:impf "kusavši" :pf "ukusívši"}
                  :past-pass-prtc {:impf "kusani" :pf "ukušéni" } ;; <-- verbs on -i/-ji become palatalized s->š
                  :verbal-noun    "kusanie"}

                 {:verb      "stiraje"
                  :prepared  {:base "stira"
                              :prefix nil
                              :verb "stiraje"
                              :verb-type :je}
                  :present    {:ja "stiram", :mi "stiramo", :ti "stiraš", :vi "stirate", :on "stiraje", :oni "stirajo"}
                  :imper-pf   {:sg "stirí" :pl "stiríte"}
                  :imper-impf {:sg "stiráj" :pl "stirájte"}
                  :fut-pf     {:ja "ja če stirém", :mi "mi če stirémo", :ti "ti če stiréš", :vi "vi če stiréte", :on "on če stiré", :oni "oni če stiréjo"}
                  :fut-impf   {:ja "bum stirat", :mi "bumo stirat", :ti "buš stirat", :vi "buste stirat", :on "bude stirat", :oni "bujo stirat"}
                  :inf-pf     "stiréti"
                  :inf-impf   "stirat"
                  :past-pf    "stiré"
                  :past-impf  "stiráva"

                  :prs-act-prtc   "stirači"
                  :prs-pass-prtc  "stirami"
                  :past-act-prtc  {:impf "stiravši" :pf "stirévši"}
                  :past-pass-prtc {:impf "stirani" :pf "stiréni" }
                  :verbal-noun    "stiranie"}

                 {:verb      "kupja"
                  :prepared  {:base "kupja"
                              :prefix nil
                              :verb "kupja"
                              :verb-type :ja}
                  :present    {:ja "kupjam", :mi "kupjamo", :ti "kupjaš", :vi "kupjate", :on "kupja", :oni "kupjajo"}
                  :imper-pf   {:sg "kupí" :pl "kupíte"}
                  :imper-impf {:sg "kupjáj" :pl "kupjájte"}
                  :fut-pf     {:ja "ja če kupím", :mi "mi če kupímo", :ti "ti če kupíš", :vi "vi če kupíte", :on "on če kupí", :oni "oni če kupíjo"}
                  :fut-impf   {:ja "bum kupjat", :mi "bumo kupjat", :ti "buš kupjat", :vi "buste kupjat", :on "bude kupjat", :oni "bujo kupjat"}
                  :inf-pf     "kupíti"
                  :inf-impf   "kupjat"
                  :past-pf    "kupí"
                  :past-impf  "kupjáva"

                  :prs-act-prtc   "kupjači"
                  :prs-pass-prtc  "kupjami"
                  :past-act-prtc  {:impf "kupjavši" :pf "kupívši"}
                  :past-pass-prtc {:impf "kupjani" :pf "kupjéni"} ;; <-- verbs on -i/-ji/-ja have ending -jéni
                  :verbal-noun    "kupjanie"}

                 {:verb      "pomoga"
                  :prepared  {:base "pomoga"
                              :prefix nil
                              :verb "pomoga"
                              :verb-type :ga->že}
                  :present    {:ja "pomogam", :mi "pomogamo", :ti "pomogaš", :vi "pomogate", :on "pomoga", :oni "pomogajo"}
                  :imper-pf   {:sg "pomoží" :pl "pomožíte"}
                  :imper-impf {:sg "pomogáj" :pl "pomogájte"}
                  :fut-pf     {:ja "ja če pomožém", :mi "mi če pomožémo", :ti "ti če pomožéš", :vi "vi če pomožéte", :on "on če pomožé", :oni "oni če pomožéjo"}
                  :fut-impf   {:ja "bum pomogat", :mi "bumo pomogat", :ti "buš pomogat", :vi "buste pomogat", :on "bude pomogat", :oni "bujo pomogat"}
                  :inf-pf     "pomožéti"
                  :inf-impf   "pomogat"
                  :past-pf    "pomožé"
                  :past-impf  "pomogáva"

                  :prs-act-prtc   "pomogači"
                  :prs-pass-prtc  "pomogami"
                  :past-act-prtc  {:impf "pomogavši" :pf "pomožévši"}
                  :past-pass-prtc {:impf "pomogani" :pf "pomožéni"}
                  :verbal-noun    "pomoganie"}

                 {:verb      "po-iska"
                  :prepared  {:base "iska"
                              :prefix "po"
                              :verb "iska"
                              :verb-type :ka->če}
                  :present    {:ja "iskam", :mi "iskamo", :ti "iskaš", :vi "iskate", :on "iska", :oni "iskajo"}
                  :imper-pf   {:sg "poisčí" :pl "poisčíte"}
                  :imper-impf {:sg "iskáj" :pl "iskájte"}
                  :fut-pf     {:ja "ja če poisčém", :mi "mi če poisčémo", :ti "ti če poisčéš", :vi "vi če poisčéte", :on "on če poisčé", :oni "oni če poisčéjo"}
                  :fut-impf   {:ja "bum iskat", :mi "bumo iskat", :ti "buš iskat", :vi "buste iskat", :on "bude iskat", :oni "bujo iskat"}
                  :inf-pf     "poisčéti"
                  :inf-impf   "iskat"
                  :past-pf    "poisčé"
                  :past-impf  "iskáva"

                  :prs-act-prtc   "iskači"
                  :prs-pass-prtc  "iskami"
                  :past-act-prtc  {:impf "iskavši" :pf "poisčévši"}
                  :past-pass-prtc {:impf "iskani" :pf "poisčéni"}
                  :verbal-noun    "iskanie"}

                 {:verb      "u-sluha"
                  :prepared  {:base "sluha"
                              :prefix "u"
                              :verb "sluha"
                              :verb-type :ha->še}
                  :present    {:ja "sluham", :mi "sluhamo", :ti "sluhaš", :vi "sluhate", :on "sluha", :oni "sluhajo"}
                  :imper-pf   {:sg "usluší" :pl "uslušíte"}
                  :imper-impf {:sg "sluháj" :pl "sluhájte"}
                  :fut-pf     {:ja "ja če uslušém", :mi "mi če uslušémo", :ti "ti če uslušéš", :vi "vi če uslušéte", :on "on če uslušé", :oni "oni če uslušéjo"}
                  :fut-impf   {:ja "bum sluhat", :mi "bumo sluhat", :ti "buš sluhat", :vi "buste sluhat", :on "bude sluhat", :oni "bujo sluhat"}
                  :inf-pf     "uslušéti"
                  :inf-impf   "sluhat"
                  :past-pf    "uslušé"
                  :past-impf  "sluháva"

                  :prs-act-prtc   "sluhači"
                  :prs-pass-prtc  "sluhami"
                  :past-act-prtc  {:impf "sluhavši" :pf "uslušévši"}
                  :past-pass-prtc {:impf "sluhani" :pf "uslušéni"}
                  :verbal-noun    "sluhanie"}

                 {:verb      "po-veša"
                  :prepared  {:base "veša"
                              :prefix "po"
                              :verb "veša"
                              :verb-type :ša->si}
                  :present    {:ja "vešam", :mi "vešamo", :ti "vešaš", :vi "vešate", :on "veša", :oni "vešajo"}
                  :imper-pf   {:sg "povesí" :pl "povesíte"}
                  :imper-impf {:sg "vešáj" :pl "vešájte"}
                  :fut-pf     {:ja "ja če povesím", :mi "mi če povesímo", :ti "ti če povesíš", :vi "vi če povesíte", :on "on če povesí", :oni "oni če povesíjo"}
                  :fut-impf   {:ja "bum vešat", :mi "bumo vešat", :ti "buš vešat", :vi "buste vešat", :on "bude vešat", :oni "bujo vešat"}
                  :inf-pf     "povesíti"
                  :inf-impf   "vešat"
                  :past-pf    "povesí"
                  :past-impf  "vešáva"

                  :prs-act-prtc   "vešači"
                  :prs-pass-prtc  "vešami"
                  :past-act-prtc  {:impf "vešavši" :pf "povesívši"}
                  :past-pass-prtc {:impf "vešani" :pf "povešéni"}   ;; <-- verbs on -sí in perfect have ending -jéni here
                                                                    ;; ORR maybe just use impf root where possible to use prefix? And perfective palatalized in other cases?
                  :verbal-noun    "vešanie"}

                 {:verb      "u-vide"
                  :prepared  {:base "vide"
                              :prefix "u"
                              :verb "vide"
                              :verb-type :e}
                  :present    {:ja "videm", :mi "videmo", :ti "videš", :vi "videte", :on "vide", :oni "videjo"}
                  :imper-pf   {:sg "uvidí" :pl "uvidíte"}
                  :imper-impf {:sg "vidí" :pl "vidíte"}
                  :fut-pf     {:ja "ja če uvidém", :mi "mi če uvidémo", :ti "ti če uvidéš", :vi "vi če uvidéte", :on "on če uvidé", :oni "oni če uvidéjo"}
                  :fut-impf   {:ja "bum videt", :mi "bumo videt", :ti "buš videt", :vi "buste videt", :on "bude videt", :oni "bujo videt"}
                  :inf-pf     "uvidéti"
                  :inf-impf   "videt"
                  :past-pf    "uvidé"
                  :past-impf  "vidéva"

                  :prs-act-prtc   "videči"
                  :prs-pass-prtc  "videmi"
                  :past-act-prtc  {:impf "videvši" :pf "uvidévši"}
                  :past-pass-prtc {:impf "videni" :pf "uvidéni"}
                  :verbal-noun    "videnie"}

                 {:verb      "po-ljubi"
                  :prepared  {:base "ljubi"
                              :prefix "po"
                              :verb "ljubi"
                              :verb-type :i}
                  :present    {:ja "ljubim", :mi "ljubimo", :ti "ljubiš", :vi "ljubite", :on "ljubi", :oni "ljubijo"}
                  :imper-pf   {:sg "poljubí" :pl "poljubíte"}
                  :imper-impf {:sg "ljubí" :pl "ljubíte"}
                  :fut-pf     {:ja "ja če poljubím", :mi "mi če poljubímo", :ti "ti če poljubíš", :vi "vi če poljubíte", :on "on če poljubí", :oni "oni če poljubíjo"}
                  :fut-impf   {:ja "bum ljubit", :mi "bumo ljubit", :ti "buš ljubit", :vi "buste ljubit", :on "bude ljubit", :oni "bujo ljubit"}
                  :inf-pf     "poljubíti"
                  :inf-impf   "ljubit"
                  :past-pf    "poljubí"
                  :past-impf  "ljubíva"

                  :prs-act-prtc   "ljubiči"
                  :prs-pass-prtc  "ljubimi"
                  :past-act-prtc  {:impf "ljubivši" :pf "poljubívši"}
                  :past-pass-prtc {:impf "ljubjeni" :pf "poljubjéni"} ;; <-- verbs on -í have ending -jéni here
                  :verbal-noun    "ljubjenie"} ;; <-- get it from the above form of `past-pass-prtc`

                 {:verb      "za-pameta"
                  :prepared  {:base "pameta"
                              :prefix "za"
                              :verb "pameta"
                              :verb-type :a}
                  :present    {:ja "pametam", :mi "pametamo", :ti "pametaš", :vi "pametate", :on "pameta", :oni "pametajo"}
                  :imper-pf   {:sg "zapametáj" :pl "zapametájte"}
                  :imper-impf {:sg "pametáj" :pl "pametájte"}
                  :fut-pf     {:ja "ja če zapametám", :mi "mi če zapametámo", :ti "ti če zapametáš", :vi "vi če zapametáte", :on "on če zapametá", :oni "oni če zapametájo"}
                  :fut-impf   {:ja "bum pametat", :mi "bumo pametat", :ti "buš pametat", :vi "buste pametat", :on "bude pametat", :oni "bujo pametat"}
                  :inf-pf     "zapametáti"
                  :inf-impf   "pametat"
                  :past-pf    "zapametá"
                  :past-impf  "pametáva"

                  :prs-act-prtc   "pametači"
                  :prs-pass-prtc  "pametami"
                  :past-act-prtc  {:impf "pametavši" :pf "zapametávši"}
                  :past-pass-prtc {:impf "pametani" :pf "zapametáni"}
                  :verbal-noun    "pametanie"}])

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

(deftest future-perfective-test
  (doseq [v test-verbs]
    (is (= (:fut-pf v) (-> (:verb v) conjugator/prepare-verb-props conjugator/future-perfective)))))

(deftest future-imperfective-test
  (doseq [v test-verbs]
    (is (= (:fut-impf v) (-> (:verb v) conjugator/prepare-verb-props conjugator/future-imperfective)))))

(deftest imperative-imperfective-test
  (doseq [v test-verbs]
    (is (= (:imper-impf v) (-> (:verb v) conjugator/prepare-verb-props conjugator/imperative-imperfective)))))

(deftest imperative-perfective-test
  (doseq [v test-verbs]
    (is (= (:imper-pf v) (-> (:verb v) conjugator/prepare-verb-props conjugator/imperative-perfective)))))

(deftest present-active-participle-test
  (doseq [v test-verbs]
    (is (= (:prs-act-prtc v) (-> (:verb v) conjugator/prepare-verb-props conjugator/present-active-participle)))))
