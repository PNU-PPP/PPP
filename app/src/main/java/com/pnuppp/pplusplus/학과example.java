//package com.pnuppp.pplusplus;
//
//import android.util.Log;
//import android.util.Pair;
//
//public class test {
//    private void setupRSSMapping() {
//        // 인문대학
//        departmentRSSMap.put("국어국문학과", new Pair<>(new String[]{"https://bkorea.pusan.ac.kr/bbs/bkorea/3375/rssList.do?row=50"}, new String[]{"https://bkorea.pusan.ac.kr/bbs/bkorea/3376/rssList.do?row=50"}));
//        departmentRSSMap.put("일어일문학과", new Pair<>(new String[]{"https://japan.pusan.ac.kr/bbs/japan/3422/rssList.do?row=50"}, new String[]{"https://japan.pusan.ac.kr/bbs/japan/3423/rssList.do?row=50"}));
//        departmentRSSMap.put("불어불문학과", new Pair<>(new String[]{"https://french.pusan.ac.kr/bbs/french/4295/rssList.do?row=50"}, null));
//        departmentRSSMap.put("노어노문학과", new Pair<>(new String[]{"https://russia.pusan.ac.kr/bbs/russia/16061/rssList.do?row=50"}, new String[]{"https://russia.pusan.ac.kr/bbs/russia/16062/rssList.do?row=50"}));
//        departmentRSSMap.put("중어중문학과", new Pair<>(new String[]{"https://china.pusan.ac.kr/bbs/china/5363/rssList.do?row=50"}, new String[]{"https://china.pusan.ac.kr/bbs/china/5364/rssList.do?row=50"}));
//        departmentRSSMap.put("영어영문학과", new Pair<>(new String[]{"https://pnuenglish.pusan.ac.kr/bbs/pnuenglish/3108/rssList.do?row=50"}, new String[]{"https://pnuenglish.pusan.ac.kr/bbs/pnuenglish/3107/rssList.do?row=50"}));
//        departmentRSSMap.put("독어독문학과", new Pair<>(new String[]{"https://german.pusan.ac.kr/bbs/german/5032/rssList.do?row=50"}, new String[]{"https://german.pusan.ac.kr/bbs/german/5033/rssList.do?row=50"}));
//        departmentRSSMap.put("언어정보학과", new Pair<>(new String[]{"https://linguistics.pusan.ac.kr/bbs/linguistics/4133/rssList.do?row=50"}, null));
//        departmentRSSMap.put("사학과", new Pair<>(new String[]{"https://history.pusan.ac.kr/bbs/history/3466/rssList.do?row=50"}, new String[]{"https://history.pusan.ac.kr/bbs/history/11701/rssList.do?row=50"}));
//        // 사회과학대학
//        departmentRSSMap.put("행정학과", new Pair<>(new String[]{"https://pub-adm.pusan.ac.kr/bbs/pub-adm/2744/rssList.do?row=50"}, new String[]{"https://pub-adm.pusan.ac.kr/bbs/pub-adm/2743/rssList.do?row=50"}));
//        departmentRSSMap.put("정치외교학과", new Pair<>(new String[]{"https://polsci.pusan.ac.kr/bbs/polsci/2837/rssList.do?row=50"}, null));
//        departmentRSSMap.put("사회학과", new Pair<>(new String[]{"https://soc.pusan.ac.kr/bbs/soc/7527/rssList.do?row=50"}, new String[]{"https://soc.pusan.ac.kr/bbs/soc/9673/rssList.do?row=50"}));
//        departmentRSSMap.put("심리학과", new Pair<>(new String[]{"https://psy.pusan.ac.kr/bbs/psy/2798/rssList.do?row=50"}, new String[]{"https://psy.pusan.ac.kr/bbs/psy/2797/rssList.do?row=50"}));
//        departmentRSSMap.put("문헌정보학과", new Pair<>(new String[]{"https://lais.pusan.ac.kr/bbs/lais/15732/rssList.do?row=50"}, new String[]{"https://lais.pusan.ac.kr/bbs/lais/15733/rssList.do?row=50"}));
//        departmentRSSMap.put("미디어커뮤니케이션학과", new Pair<>(null, null)); //rss 상 오류 뜸
//        // 자연과학대학
//        departmentRSSMap.put("수학과", new Pair<>(new String[]{"https://math.pusan.ac.kr/bbs/math/2818/rssList.do?row=50"}, new String[]{"https://math.pusan.ac.kr/bbs/math/16237/rssList.do?row=50"}));
//        departmentRSSMap.put("통계학과", new Pair<>(new String[]{"https://stat.pusan.ac.kr/bbs/stat/2705/rssList.do?row=50"}, null));
//        departmentRSSMap.put("화학과", new Pair<>(new String[]{"https://chem.pusan.ac.kr/bbs/chem/2734/rssList.do?row=50"}, new String[]{"https://chem.pusan.ac.kr/bbs/chem/2734/rssList.do?row=50"}));
//        departmentRSSMap.put("생명과학과", new Pair<>(new String[]{"https://biology.pusan.ac.kr/bbs/biology/3143/rssList.do?row=50"}, new String[]{"https://biology.pusan.ac.kr/bbs/biology/3144/rssList.do?row=50"}));
//        departmentRSSMap.put("미생물학과", new Pair<>(new String[]{"https://microbio.pusan.ac.kr/bbs/microbio/3085/rssList.do?row=50"}, new String[]{"https://microbio.pusan.ac.kr/bbs/microbio/3086/rssList.do?row=50"}));
//        departmentRSSMap.put("분자생물학과", new Pair<>(new String[]{"https://molbiology.pusan.ac.kr/bbs/molbiology/3918/rssList.do?row=50"}, null));
//        departmentRSSMap.put("지질환경과학과", new Pair<>(new String[]{"https://geology.pusan.ac.kr/bbs/geology/2800/rssList.do?row=50"}, null));
//        departmentRSSMap.put("대기환경과학과", new Pair<>(new String[]{"https://atmos.pusan.ac.kr/bbs/atmos/3096/rssList.do?row=50"}, null));
//        departmentRSSMap.put("해양학과", new Pair<>(new String[]{"https://ocean.pusan.ac.kr/bbs/ocean/2877/rssList.do?row=50"}, null));
//        // 공과대학
//        departmentRSSMap.put("고분자공학과", new Pair<>(new String[]{"https://polymer.pusan.ac.kr/bbs/polymer/16257/rssList.do?row=50"}, null));
//        departmentRSSMap.put("유기소재시스템공학과", new Pair<>(new String[]{"https://omse.pusan.ac.kr/bbs/omse/3203/rssList.do?row=50"}, new String[]{"https://omse.pusan.ac.kr/bbs/omse/12392/rssList.do?row=50"}));
//        departmentRSSMap.put("전기전자공학부 전자공학전공", new Pair<>(new String[]{"https://ee.pusan.ac.kr/bbs/ee/2635/rssList.do?row=50"}, new String[]{"https://ee.pusan.ac.kr/bbs/ee/2642/rssList.do?row=50"})); //학부, 취업 묶음
//        departmentRSSMap.put("전기전자공학부 전기공학전공", new Pair<>(new String[]{"https://eec.pusan.ac.kr/bbs/eehome/2650/rssList.do?row=50"}, new String[]{"https://eec.pusan.ac.kr/bbs/eehome/2651/rssList.do?row=50"}));
//        departmentRSSMap.put("조선해양공학과", new Pair<>(new String[]{"https://naoe.pusan.ac.kr/bbs/naoe/2754/rssList.do?row=50"}, new String[]{"https://naoe.pusan.ac.kr/bbs/naoe/2756/rssList.do?row=50"}));
//        departmentRSSMap.put("재료공학부", new Pair<>(new String[]{"https://mse.pusan.ac.kr/bbs/mse/8972/rssList.do?row=50"}, new String[]{"https://mse.pusan.ac.kr/bbs/mse/12265/rssList.do?row=50", "https://mse.pusan.ac.kr/bbs/mse/12266/rssList.do?row=50", "https://mse.pusan.ac.kr/bbs/mse/12267/rssList.do?row=50", "https://mse.pusan.ac.kr/bbs/mse/12268/rssList.do?row=50", "https://mse.pusan.ac.kr/bbs/mse/12269/rssList.do?row=50"}));
//        departmentRSSMap.put("항공우주공학과", new Pair<>(new String[]{"https://aerospace.pusan.ac.kr/bbs/aerospace/3213/rssList.do?row=50"}, null));
//        departmentRSSMap.put("건축공학과", new Pair<>(new String[]{"https://archieng.pusan.ac.kr/bbs/_archieng/3964/rssList.do?row=50"}, new String[]{"https://archieng.pusan.ac.kr/bbs/_archieng/14096/rssList.do?row=50"}));
//        departmentRSSMap.put("건축학과", new Pair<>(new String[]{"https://archi.pusan.ac.kr/bbs/archi/11920/rssList.do?row=50"}, new String[]{"https://archi.pusan.ac.kr/bbs/archi/11921/rssList.do?row=50"}));
//        departmentRSSMap.put("도시공학과", new Pair<>(new String[]{"https://urban.pusan.ac.kr/bbs/urban/3413/rssList.do?row=50"}, null));
//        departmentRSSMap.put("사회기반시스템공학과", new Pair<>(new String[]{"https://civil.pusan.ac.kr/bbs/civil/3207/rssList.do?row=50"}, new String[]{"https://civil.pusan.ac.kr/bbs/civil/3206/rssList.do?row=50", "https://civil.pusan.ac.kr/bbs/civil/3208/rssList.do?row=50"}));
//        // 사범대학 (일반대학원, 교육대학원이 나누어져 있는 경우, 일단 같이 통합되어 보이게끔 해둠.)
//        departmentRSSMap.put("국어교육과", new Pair<>(new String[]{"https://koredu.pusan.ac.kr/bbs/koredu/5262/rssList.do?row=50"}, new String[]{"https://koredu.pusan.ac.kr/bbs/koredu/5264/rssList.do?row=50", "https://koredu.pusan.ac.kr/bbs/koredu/5265/rssList.do?row=50"}));
//        departmentRSSMap.put("영어교육과", new Pair<>(new String[]{"https://englishedu.pusan.ac.kr/bbs/englishedupnu/8789/rssList.do?row=50"}, new String[]{"https://englishedu.pusan.ac.kr/bbs/englishedupnu/8790/rssList.do?row=50"}));
//        departmentRSSMap.put("독어교육과", new Pair<>(new String[]{"https://geredu.pusan.ac.kr/bbs/geredu/4381/rssList.do?row=50"}, null));
//        departmentRSSMap.put("불어교육과", new Pair<>(new String[]{"https://fredu.pusan.ac.kr/bbs/fredu/4398/rssList.do?row=50"}, null));
//        departmentRSSMap.put("교육학과", new Pair<>(new String[]{"https://ed.pusan.ac.kr/bbs/ed/2768/rssList.do?row=50"}, null));
//        departmentRSSMap.put("유아교육과", new Pair<>(new String[]{"https://child.pusan.ac.kr/bbs/child/3129/rssList.do?row=50"}, new String[]{"https://child.pusan.ac.kr/bbs/child/3131/rssList.do?row=50"}));
//        departmentRSSMap.put("특수교육과", new Pair<>(new String[]{"https://special.pusan.ac.kr/bbs/special/3470/rssList.do?row=50"}, new String[]{"https://special.pusan.ac.kr/bbs/special/3795/rssList.do?row=50", "https://special.pusan.ac.kr/bbs/special/3796/rssList.do?row=50"}));
//        departmentRSSMap.put("일반사회교육과", new Pair<>(new String[]{"https://socialedu.pusan.ac.kr/bbs/socialedu/4102/rssList.do?row=50"}, null));
//        departmentRSSMap.put("역사교육과", new Pair<>(new String[]{"https://hisedu.pusan.ac.kr/bbs/hisedu/3361/rssList.do?row=50"}, null));
//        departmentRSSMap.put("지리교육과", new Pair<>(new String[]{"https://geoedu.pusan.ac.kr/bbs/geoedu/4310/rssList.do?row=50"}, null));
//        departmentRSSMap.put("윤리교육과", new Pair<>(new String[]{"https://ethics.pusan.ac.kr/bbs/ethics/4351/rssList.do?row=50"}, null));
//        departmentRSSMap.put("수학교육과", new Pair<>(new String[]{"https://mathedu.pusan.ac.kr/bbs/mathedu/2696/rssList.do?row=50"}, new String[]{"https://mathedu.pusan.ac.kr/bbs/mathedu/2698/rssList.do?row=50", "https://mathedu.pusan.ac.kr/bbs/mathedu/2697/rssList.do?row=50"}));
//        departmentRSSMap.put("물리교육과", new Pair<>(new String[]{"https://physedu.pusan.ac.kr/bbs/physedu/3403/rssList.do?row=50"}, null));
//        departmentRSSMap.put("화학교육과", new Pair<>(new String[]{"https://chemedu.pusan.ac.kr/bbs/chemedu/4094/rssList.do?row=50"}, new String[]{"https://chemedu.pusan.ac.kr/bbs/chemedu/4089/rssList.do?row=50", "https://chemedu.pusan.ac.kr/bbs/chemedu/4057/rssList.do?row=50"}));
//        departmentRSSMap.put("생물교육과", new Pair<>(new String[]{"https://edubio.pusan.ac.kr/bbs/edubio/4231/rssList.do?row=50"}, new String[]{"https://edubio.pusan.ac.kr/bbs/edubio/4243/rssList.do?row=50", "https://edubio.pusan.ac.kr/bbs/edubio/4240/rssList.do?row=50"}));
//        departmentRSSMap.put("지구과학교육과", new Pair<>(new String[]{"https://earth.pusan.ac.kr/bbs/earth/4654/rssList.do?row=50"}, new String[]{"https://earth.pusan.ac.kr/bbs/earth/4707/rssList.do?row=50"}));
//        departmentRSSMap.put("체육교육과", new Pair<>(new String[]{"https://physicaledu.pusan.ac.kr/bbs/physicaledu/4358/rssList.do?row=50"}, new String[]{"https://physicaledu.pusan.ac.kr/bbs/physicaledu/4357/rssList.do?row=50", "https://physicaledu.pusan.ac.kr/bbs/physicaledu/4369/rssList.do?row=50"}));
//        // 경제통상대학
//        departmentRSSMap.put("무역학부", new Pair<>(new String[]{"https://pnutrade.pusan.ac.kr/bbs/pnutrade/3390/rssList.do?row=50"}, new String[]{"https://pnutrade.pusan.ac.kr/bbs/pnutrade/3391/rssList.do?row=50"}));
//        departmentRSSMap.put("경제학부", new Pair<>(new String[]{"https://pnuecon.pusan.ac.kr/bbs/pnuecon/3210/rssList.do?row=50"}, new String[]{"https://pnuecon.pusan.ac.kr/bbs/pnuecon/17205/rssList.do?row=50"}));
//        departmentRSSMap.put("관광컨벤션학과", new Pair<>(new String[]{"https://convention.pusan.ac.kr/bbs/convention/3346/rssList.do?row=50"}, new String[]{"https://convention.pusan.ac.kr/bbs/convention/9230/rssList.do?row=50"}));
//        departmentRSSMap.put("공공정책학부", new Pair<>(new String[]{"https://ppm.pusan.ac.kr/bbs/ppm/3498/rssList.do?row=50"}, null));
//        // 경영대학
//        departmentRSSMap.put("경영학과", new Pair<>(new String[]{"https://biz.pusan.ac.kr/bbs/biz/2557/rssList.do?row=50"}, new String[]{"https://biz.pusan.ac.kr/bbs/biz/2556/rssList.do?row=50"}));
//        // 약학대학
//        departmentRSSMap.put("약학대학 약학전공", new Pair<>(new String[]{"https://pharmacy.pusan.ac.kr/bbs/pharmacy/2420/rssList.do?row=50"}, new String[]{"https://pharmacy.pusan.ac.kr/bbs/pharmacy/11649/rssList.do?row=50"}));
//        departmentRSSMap.put("약학대학 제약학전공", new Pair<>(new String[]{"https://pharmacy.pusan.ac.kr/bbs/pharmacy/2420/rssList.do?row=50"}, new String[]{"https://pharmacy.pusan.ac.kr/bbs/pharmacy/11649/rssList.do?row=50"}));
//        // 생활과학대학
//        departmentRSSMap.put("아동가족학과", new Pair<>(new String[]{"https://cdfs.pusan.ac.kr/bbs/cdfs/3449/rssList.do?row=50"}, null));
//        departmentRSSMap.put("의류학과", new Pair<>(new String[]{"https://fashion.pusan.ac.kr/bbs/fashion/3442/rssList.do?row=50"}, new String[]{"https://fashion.pusan.ac.kr/bbs/fashion/11724/rssList.do?row=50"}));
//        departmentRSSMap.put("식품영양학과", new Pair<>(new String[]{"https://fsn.pusan.ac.kr/bbs/fsn/2783/rssList.do?row=50"}, null));
//        // 예술대학
//        departmentRSSMap.put("음악학과", new Pair<>(new String[]{"https://music.pusan.ac.kr/bbs/music/3192/rssList.do?row=50"}, null));
//        departmentRSSMap.put("한국음악학과", new Pair<>(new String[]{"https://gukak.pusan.ac.kr/bbs/gukak/3978/rssList.do?row=50"}, null));
//        departmentRSSMap.put("미술학과", new Pair<>(new String[]{"https://pnuart.pusan.ac.kr/bbs/pnuart/3941/rssList.do?row=50"}, null));
//        departmentRSSMap.put("조형학과", new Pair<>(new String[]{"https://plarts.pusan.ac.kr/bbs/plarts/4434/rssList.do?row=50"}, null));
//        departmentRSSMap.put("디자인학과", new Pair<>(new String[]{"https://design.pusan.ac.kr/bbs/design/3353/rssList.do?row=50"}, null));
//        departmentRSSMap.put("무용학과", new Pair<>(new String[]{"https://dance.pusan.ac.kr/bbs/dance/4164/rssList.do?row=50"}, null));
//        departmentRSSMap.put("예술문화영상학과", new Pair<>(new String[]{"https://artimage.pusan.ac.kr/bbs/artimage/3257/rssList.do?row=50"}, null));
//        // 나노과학기술대학
//        departmentRSSMap.put("나노메카트로닉스공학과", new Pair<>(new String[]{"https://nanomecha.pusan.ac.kr/bbs/nanomecha/3264/rssList.do?row=50"}, new String[]{"https://nanomecha.pusan.ac.kr/bbs/nanomecha/14888/rssList.do?row=50"}));
//        departmentRSSMap.put("나노에너지공학과", new Pair<>(new String[]{"https://energy.pusan.ac.kr/bbs/energy/2829/rssList.do?row=50"}, new String[]{"https://energy.pusan.ac.kr/bbs/energy/7496/rssList.do?row=50"}));
//        departmentRSSMap.put("광메카트로닉스공학과", new Pair<>(new String[]{"https://ome.pusan.ac.kr/bbs/ome/3307/rssList.do?row=50"}, null));
//        // 생명자원과학대학
//        departmentRSSMap.put("식물생명과학과", new Pair<>(new String[]{"https://plant.pusan.ac.kr/bbs/plant/4252/rssList.do?row=50"}, null));
//        departmentRSSMap.put("원예생명과학과", new Pair<>(new String[]{"https://his.pusan.ac.kr/horticulture/21127/subview.do"}, null));
//        departmentRSSMap.put("동물생명자원과학과", new Pair<>(new String[]{"https://animal.pusan.ac.kr/bbs/animal/3293/rssList.do?row=50"}, null));
//        departmentRSSMap.put("식품공학과", new Pair<>(new String[]{"https://fst.pusan.ac.kr/bbs/fst/3950/rssList.do?row=50"}, null));
//        departmentRSSMap.put("생명환경화학과", new Pair<>(new String[]{"https://pnu-lseb.pusan.ac.kr/bbs/pnu-lseb/4324/rssList.do?row=50"}, new String[]{"https://pnu-lseb.pusan.ac.kr/bbs/pnu-lseb/12479/rssList.do?row=50"}));
//        departmentRSSMap.put("바이오소재과학과", new Pair<>(new String[]{"https://bm.pusan.ac.kr/bbs/bm/3234/rssList.do?row=50"}, null));
//        departmentRSSMap.put("바이오산업기계공학과", new Pair<>(new String[]{"https://bime.pusan.ac.kr/bbs/bime/3835/rssList.do?row=50"}, null));
//        departmentRSSMap.put("조경학과", new Pair<>(new String[]{"https://la.pusan.ac.kr/bbs/la/4041/rssList.do?row=50"}, null));
//        departmentRSSMap.put("식품자원경제학과", new Pair<>(new String[]{"https://agecon.pusan.ac.kr/bbs/agecon/4116/rssList.do?row=50", "https://agecon.pusan.ac.kr/bbs/agecon/4115/rssList.do?row=50"}, new String[]{""})); //공지사항, 학과알림 두개 있고 둘다 최신게 있길래 학과 공지사항 두개로 묶음
//        departmentRSSMap.put("IT응용공학과", new Pair<>(new String[]{"https://ite.pusan.ac.kr/bbs/ite/3139/rssList.do?row=50"}, null));
//        departmentRSSMap.put("바이오환경에너지학과", new Pair<>(new String[]{"https://bee.pusan.ac.kr/bbs/bee/3905/rssList.do?row=50"}, null));
//        // 간호대학
//        departmentRSSMap.put("간호학과", new Pair<>(new String[]{"https://nursing.pusan.ac.kr/bbs/nursing/2584/rssList.do?row=50"}, null));
//        // 의과대학
//        departmentRSSMap.put("의예과", new Pair<>(new String[]{"https://medicine.pusan.ac.kr/bbs/medicine/2270/rssList.do?row=50", "https://medicine.pusan.ac.kr/bbs/medicine/2257/rssList.do?row=50"}, new String[]{"https://medicine.pusan.ac.kr/bbs/medicine/2267/rssList.do?row=50"}));
//        departmentRSSMap.put("의학과", new Pair<>(new String[]{"https://medicine.pusan.ac.kr/bbs/medicine/2270/rssList.do?row=50", "https://medicine.pusan.ac.kr/bbs/medicine/2260/rssList.do?row=50"}, new String[]{"https://medicine.pusan.ac.kr/bbs/medicine/2267/rssList.do?row=50"}));
//        departmentRSSMap.put("의과학과", new Pair<>(new String[]{"https://medicine.pusan.ac.kr/bbs/medicine/2270/rssList.do?row=50", "https://medicine.pusan.ac.kr/bbs/medicine/2265/rssList.do?row=50"}, new String[]{"https://medicine.pusan.ac.kr/bbs/medicine/2267/rssList.do?row=50"}));
//        // 정보의생명공학대학
//        departmentRSSMap.put("정보컴퓨터공학부 컴퓨터공학전공", new Pair<>(new String[]{"https://cse.pusan.ac.kr/bbs/cse/2605/rssList.do?row=50"}, new String[]{"https://cse.pusan.ac.kr/bbs/cse/2611/rssList.do?row=50"}));
//        departmentRSSMap.put("정보컴퓨터공학부 인공지능전공", new Pair<>(new String[]{"https://cse.pusan.ac.kr/bbs/cse/2605/rssList.do?row=50"}, new String[]{"https://cse.pusan.ac.kr/bbs/cse/2611/rssList.do?row=50"}));
//        departmentRSSMap.put("의생명융학공학부", new Pair<>(new String[]{"https://bce.pusan.ac.kr/bbs/bce/12005/rssList.do?row=50"}, null)); //대학원 공지사항도 있는데 rss디자인인데 보튼이 안떠서 일단 보류
//        //첨단융합학부(신설이라 정보의생명공학자율전공은 홈페이지 아직 없음)
//        departmentRSSMap.put("첨단융합학부 나노자율전공", new Pair<>(new String[]{"https://u-nano.pusan.ac.kr/bbs/u-nano/18379/rssList.do?row=50"}, null));
//        Log.d("RSSDepartmentNotice", "RSS Mapping Setup Complete");
//    }
//
//}
