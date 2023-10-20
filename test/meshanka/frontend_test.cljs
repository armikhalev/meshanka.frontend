(ns meshanka.core-test
    (:require
     [cljs.test :refer-macros [deftest is testing]]
     [meshanka.pages.conjugator :as conjugator]))

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
                  :fut-pf     {:ja "ja če priidem", :mi "mi če priidemo", :ti "ti če priideš", :vi "vi če priidete", :on "on če priide", :oni "oni če priidejo"}
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
                  :fut-pf     {:ja "ja če smožem", :mi "mi če smožemo", :ti "ti če smožeš", :vi "vi če smožete", :on "on če smože", :oni "oni če smožejo"}
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
                  :fut-pf     {:ja "ja če izpolzovam", :mi "mi če izpolzovamo", :ti "ti če izpolzovaš", :vi "vi če izpolzovate", :on "on če izpolzova", :oni "oni če izpolzovajo"}
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
                  :fut-pf     {:ja "ja če ukrim", :mi "mi če ukrimo", :ti "ti če ukriš", :vi "vi če ukrite", :on "on če ukri", :oni "oni če ukrijo"}
                  :fut-impf   {:ja "bum ukrivat", :mi "bumo ukrivat", :ti "buš ukrivat", :vi "buste ukrivat", :on "bude ukrivat", :oni "bujo ukrivat"}
                  :inf-pf     "ukríti"
                  :inf-impf   "ukrivat"
                  :past-pf    "ukrí"
                  :past-impf  "ukríva"

                  :prs-act-prtc   "ukrivači"
                  :prs-pass-prtc  "ukrivami"
                  :past-act-prtc  {:impf "ukrivavši" :pf "ukrívši"}
                  :past-pass-prtc {:impf "ukrivani" :pf "ukríjeni" } ;; `-jeni` for verbs on -i?
                  :verbal-noun    "ukrivanie"}

                 {:verb      "ot-čuvaje"
                  :prepared  {:base "čuva"
                              :prefix "ot"
                              :verb "čuvaje"
                              :verb-type :va}
                  :present    {:ja "čuvam", :mi "čuvamo", :ti "čuvaš", :vi "čuvate", :on "čuvaje", :oni "čuvajo"}
                  :imper-pf   {:sg "otčúj" :pl "otčújte"}
                  :imper-impf {:sg "čuváj" :pl "čuvájte"}
                  :fut-pf     {:ja "ja če otčum", :mi "mi če otčumo", :ti "ti če otčuš", :vi "vi če otčute", :on "on če otču", :oni "oni če otčujo"}
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
                  :fut-pf     {:ja "ja če napisam", :mi "mi če napisamo", :ti "ti če napisaš", :vi "vi če napisate", :on "on če napisa", :oni "oni če napisajo"}
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
                  :fut-pf     {:ja "ja če ukusim", :mi "mi če ukusimo", :ti "ti če ukusiš", :vi "vi če ukusite", :on "on če ukusi", :oni "oni če ukusijo"}
                  :fut-impf   {:ja "bum kusat", :mi "bumo kusat", :ti "buš kusat", :vi "buste kusat", :on "bude kusat", :oni "bujo kusat"}
                  :inf-pf     "ukusíti"
                  :inf-impf   "kusat"
                  :past-pf    "ukusí"
                  :past-impf  "kusáva"

                  :prs-act-prtc   "kusači"
                  :prs-pass-prtc  "kusami"
                  :past-act-prtc  {:impf "kusavši" :pf "ukusívši"}
                  :past-pass-prtc {:impf "kusani" :pf "ukusjeni" } ;; <-- verbs on -i/-ji get ending -jeni
                  :verbal-noun    "kusanie"}

                 {:verb      "stiraje"
                  :prepared  {:base "stira"
                              :prefix nil
                              :verb "stiraje"
                              :verb-type :je}
                  :present    {:ja "stiram", :mi "stiramo", :ti "stiraš", :vi "stirate", :on "stiraje", :oni "stirajo"}
                  :imper-pf   {:sg "stirí" :pl "stiríte"}
                  :imper-impf {:sg "stiráj" :pl "stirájte"}
                  :fut-pf     {:ja "ja če stirem", :mi "mi če stiremo", :ti "ti če stireš", :vi "vi če stirete", :on "on če stire", :oni "oni če stirejo"}
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
                  :fut-pf     {:ja "ja če kupim", :mi "mi če kupimo", :ti "ti če kupiš", :vi "vi če kupite", :on "on če kupi", :oni "oni če kupijo"}
                  :fut-impf   {:ja "bum kupjat", :mi "bumo kupjat", :ti "buš kupjat", :vi "buste kupjat", :on "bude kupjat", :oni "bujo kupjat"}
                  :inf-pf     "kupíti"
                  :inf-impf   "kupjat"
                  :past-pf    "kupí"
                  :past-impf  "kupjáva"

                  :prs-act-prtc   "kupjači"
                  :prs-pass-prtc  "kupjami"
                  :past-act-prtc  {:impf "kupjavši" :pf "kupívši"}
                  :past-pass-prtc {:impf "kupjani" :pf "kupjeni"} ;; <-- verbs on -i/-ji/-ja have ending -jéni
                  :verbal-noun    "kupjanie"}

                 {:verb      "pomoga"
                  :prepared  {:base "pomoga"
                              :prefix nil
                              :verb "pomoga"
                              :verb-type :ga->že}
                  :present    {:ja "pomogam", :mi "pomogamo", :ti "pomogaš", :vi "pomogate", :on "pomoga", :oni "pomogajo"}
                  :imper-pf   {:sg "pomoží" :pl "pomožíte"}
                  :imper-impf {:sg "pomogáj" :pl "pomogájte"}
                  :fut-pf     {:ja "ja če pomožem", :mi "mi če pomožemo", :ti "ti če pomožeš", :vi "vi če pomožete", :on "on če pomože", :oni "oni če pomožejo"}
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
                  :fut-pf     {:ja "ja če poisčem", :mi "mi če poisčemo", :ti "ti če poisčeš", :vi "vi če poisčete", :on "on če poisče", :oni "oni če poisčejo"}
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
                  :fut-pf     {:ja "ja če uslušem", :mi "mi če uslušemo", :ti "ti če uslušeš", :vi "vi če uslušete", :on "on če usluše", :oni "oni če uslušejo"}
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
                  :fut-pf     {:ja "ja če povesim", :mi "mi če povesimo", :ti "ti če povesiš", :vi "vi če povesite", :on "on če povesi", :oni "oni če povesijo"}
                  :fut-impf   {:ja "bum vešat", :mi "bumo vešat", :ti "buš vešat", :vi "buste vešat", :on "bude vešat", :oni "bujo vešat"}
                  :inf-pf     "povesíti"
                  :inf-impf   "vešat"
                  :past-pf    "povesí"
                  :past-impf  "vešáva"

                  :prs-act-prtc   "vešači"
                  :prs-pass-prtc  "vešami"
                  :past-act-prtc  {:impf "vešavši" :pf "povesívši"}
                  :past-pass-prtc {:impf "vešani" :pf "povešeni"}   ;; <-- verbs on -sí in perfect have ending -jéni here
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
                  :fut-pf     {:ja "ja če uvidem", :mi "mi če uvidemo", :ti "ti če uvideš", :vi "vi če uvidete", :on "on če uvide", :oni "oni če uvidejo"}
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
                  :fut-pf     {:ja "ja če poljubim", :mi "mi če poljubimo", :ti "ti če poljubiš", :vi "vi če poljubite", :on "on če poljubi", :oni "oni če poljubijo"}
                  :fut-impf   {:ja "bum ljubit", :mi "bumo ljubit", :ti "buš ljubit", :vi "buste ljubit", :on "bude ljubit", :oni "bujo ljubit"}
                  :inf-pf     "poljubíti"
                  :inf-impf   "ljubit"
                  :past-pf    "poljubí"
                  :past-impf  "ljubíva"

                  :prs-act-prtc   "ljubiči"
                  :prs-pass-prtc  "ljubimi"
                  :past-act-prtc  {:impf "ljubivši" :pf "poljubívši"}
                  :past-pass-prtc {:impf "ljubjeni" :pf "poljubjeni"} ;; <-- verbs on -í have ending -jeni here
                  :verbal-noun    "ljubjenie"} ;; <-- get it from the above form of `past-pass-prtc`

                 {:verb      "za-pameta"
                  :prepared  {:base "pameta"
                              :prefix "za"
                              :verb "pameta"
                              :verb-type :a}
                  :present    {:ja "pametam", :mi "pametamo", :ti "pametaš", :vi "pametate", :on "pameta", :oni "pametajo"}
                  :imper-pf   {:sg "zapametáj" :pl "zapametájte"}
                  :imper-impf {:sg "pametáj" :pl "pametájte"}
                  :fut-pf     {:ja "ja če zapametam", :mi "mi če zapametamo", :ti "ti če zapametaš", :vi "vi če zapametate", :on "on če zapameta", :oni "oni če zapametajo"}
                  :fut-impf   {:ja "bum pametat", :mi "bumo pametat", :ti "buš pametat", :vi "buste pametat", :on "bude pametat", :oni "bujo pametat"}
                  :inf-pf     "zapametáti"
                  :inf-impf   "pametat"
                  :past-pf    "zapametá"
                  :past-impf  "pametáva"

                  :prs-act-prtc   "pametači"
                  :prs-pass-prtc  "pametami"
                  :past-act-prtc  {:impf "pametavši" :pf "zapametávši"}
                  :past-pass-prtc {:impf "pametani" :pf "zapametáni"}
                  :verbal-noun    "pametanie"}

                 {:verb      "izobražaji"
                  :prepared  {:base "izobraža"
                              :prefix nil
                              :verb "izobražaji"
                              :verb-type :žaji->zi}
                  :present    {:ja "izobražam", :mi "izobražamo", :ti "izobražaš", :vi "izobražate", :on "izobražaji", :oni "izobražajo"}
                  :imper-pf   {:sg "izobrazí" :pl "izobrazíte"}
                  :imper-impf {:sg "izobražáj" :pl "izobražájte"}
                  :fut-pf     {:ja "ja če izobrazim", :mi "mi če izobrazimo", :ti "ti če izobraziš", :vi "vi če izobrazite", :on "on če izobrazi", :oni "oni če izobrazijo"}
                  :fut-impf   {:ja "bum izobražat", :mi "bumo izobražat", :ti "buš izobražat", :vi "buste izobražat", :on "bude izobražat", :oni "bujo izobražat"}
                  :inf-pf     "izobrazíti"
                  :inf-impf   "izobražat"
                  :past-pf    "izobrazí"
                  :past-impf  "izobražáva"

                  :prs-act-prtc   "izobražači"
                  :prs-pass-prtc  "izobražami"
                  :past-act-prtc  {:impf "izobražavši" :pf "izobrazívši"}
                  :past-pass-prtc {:impf "izobražani" :pf "izobrazíni"}
                  :verbal-noun    "izobražanie"}])

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

(deftest present-passive-participle-test
  (doseq [v test-verbs]
    (is (= (:prs-pass-prtc v) (-> (:verb v) conjugator/prepare-verb-props conjugator/present-passive-participle)))))

(deftest past-active-participle-test
  (doseq [v test-verbs]
    (is (= (:past-act-prtc v) (-> (:verb v) conjugator/prepare-verb-props conjugator/past-active-participle)))))

(deftest past-passive-participle-test
  (doseq [v test-verbs]
    (let [prepared-verb (-> v :verb conjugator/prepare-verb-props)]
      (is (= (:past-pass-prtc v) (conjugator/past-passive-participle prepared-verb))
          (str ":: Verb type: " (:verb-type prepared-verb))))))

(deftest verbal-noun-test
  (doseq [v test-verbs]
    (let [prepared-verb (-> v :verb conjugator/prepare-verb-props)]
      (is (= (:verbal-noun v) (conjugator/verbal-noun prepared-verb))
          (str ":: Verb type: " (:verb-type prepared-verb))))))
