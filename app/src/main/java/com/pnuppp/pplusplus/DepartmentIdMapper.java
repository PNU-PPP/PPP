package com.pnuppp.pplusplus;

import java.util.HashMap;
import java.util.Map;

public class DepartmentIdMapper {
    private static final Map<String, String> DEPARTMENT_ID_MAP = new HashMap<>();

    static {
        DEPARTMENT_ID_MAP.put("국어국문학과", "DP00");
        DEPARTMENT_ID_MAP.put("일어일문학과", "DP01");
        DEPARTMENT_ID_MAP.put("불어불문학과", "DP02");
        DEPARTMENT_ID_MAP.put("노어노문학과", "DP03");
        DEPARTMENT_ID_MAP.put("중어중문학과", "DP04");
        DEPARTMENT_ID_MAP.put("영어영문학과", "DP05");
        DEPARTMENT_ID_MAP.put("독어독문학과", "DP06");
        DEPARTMENT_ID_MAP.put("언어정보학과", "DP07");
        DEPARTMENT_ID_MAP.put("사학과", "DP08");
        DEPARTMENT_ID_MAP.put("행정학과", "DP09");
        DEPARTMENT_ID_MAP.put("정치외교학과", "DP0A");
        DEPARTMENT_ID_MAP.put("사회학과", "DP0B");
        DEPARTMENT_ID_MAP.put("심리학과", "DP0C");
        DEPARTMENT_ID_MAP.put("문헌정보학과", "DP0D");
        DEPARTMENT_ID_MAP.put("미디어커뮤니케이션학과", "DP0E");
        DEPARTMENT_ID_MAP.put("수학과", "DP0F");
        DEPARTMENT_ID_MAP.put("통계학과", "DP10");
        DEPARTMENT_ID_MAP.put("화학과", "DP11");
        DEPARTMENT_ID_MAP.put("생명과학과", "DP12");
        DEPARTMENT_ID_MAP.put("미생물학과", "DP13");
        DEPARTMENT_ID_MAP.put("분자생물학과", "DP14");
        DEPARTMENT_ID_MAP.put("지질환경과학과", "DP15");
        DEPARTMENT_ID_MAP.put("대기환경과학과", "DP16");
        DEPARTMENT_ID_MAP.put("해양학과", "DP17");
        DEPARTMENT_ID_MAP.put("고분자공학과", "DP18");
        DEPARTMENT_ID_MAP.put("유기소재시스템공학과", "DP19");
        DEPARTMENT_ID_MAP.put("전기전자공학부 전자공학전공", "DP1A");
        DEPARTMENT_ID_MAP.put("전기전자공학부 전기공학전공", "DP1B");
        DEPARTMENT_ID_MAP.put("조선해양공학과", "DP1C");
        DEPARTMENT_ID_MAP.put("재료공학부", "DP1D");
        DEPARTMENT_ID_MAP.put("항공우주공학과", "DP1E");
        DEPARTMENT_ID_MAP.put("건축공학과", "DP1F");
        DEPARTMENT_ID_MAP.put("건축학과", "DP20");
        DEPARTMENT_ID_MAP.put("도시공학과", "DP21");
        DEPARTMENT_ID_MAP.put("사회기반시스템공학과", "DP22");
        DEPARTMENT_ID_MAP.put("국어교육과", "DP23");
        DEPARTMENT_ID_MAP.put("영어교육과", "DP24");
        DEPARTMENT_ID_MAP.put("독어교육과", "DP25");
        DEPARTMENT_ID_MAP.put("불어교육과", "DP26");
        DEPARTMENT_ID_MAP.put("교육학과", "DP27");
        DEPARTMENT_ID_MAP.put("유아교육과", "DP28");
        DEPARTMENT_ID_MAP.put("특수교육과", "DP29");
        DEPARTMENT_ID_MAP.put("일반사회교육과", "DP2A");
        DEPARTMENT_ID_MAP.put("역사교육과", "DP2B");
        DEPARTMENT_ID_MAP.put("지리교육과", "DP2C");
        DEPARTMENT_ID_MAP.put("윤리교육과", "DP2D");
        DEPARTMENT_ID_MAP.put("수학교육과", "DP2E");
        DEPARTMENT_ID_MAP.put("물리교육과", "DP2F");
        DEPARTMENT_ID_MAP.put("화학교육과", "DP30");
        DEPARTMENT_ID_MAP.put("생물교육과", "DP31");
        DEPARTMENT_ID_MAP.put("지구과학교육과", "DP32");
        DEPARTMENT_ID_MAP.put("체육교육과", "DP33");
        DEPARTMENT_ID_MAP.put("무역학부", "DP34");
        DEPARTMENT_ID_MAP.put("경제학부", "DP35");
        DEPARTMENT_ID_MAP.put("관광컨벤션학과", "DP36");
        DEPARTMENT_ID_MAP.put("공공정책학부", "DP37");
        DEPARTMENT_ID_MAP.put("경영학과", "DP38");
        DEPARTMENT_ID_MAP.put("약학대학 약학전공", "DP39");
        DEPARTMENT_ID_MAP.put("약학대학 제약학전공", "DP3A");
        DEPARTMENT_ID_MAP.put("아동가족학과", "DP3B");
        DEPARTMENT_ID_MAP.put("의류학과", "DP3C");
        DEPARTMENT_ID_MAP.put("식품영양학과", "DP3D");
        DEPARTMENT_ID_MAP.put("음악학과", "DP3E");
        DEPARTMENT_ID_MAP.put("한국음악학과", "DP3F");
        DEPARTMENT_ID_MAP.put("미술학과", "DP40");
        DEPARTMENT_ID_MAP.put("조형학과", "DP41");
        DEPARTMENT_ID_MAP.put("디자인학과", "DP42");
        DEPARTMENT_ID_MAP.put("무용학과", "DP43");
        DEPARTMENT_ID_MAP.put("예술문화영상학과", "DP44");
        DEPARTMENT_ID_MAP.put("나노메카트로닉스공학과", "DP45");
        DEPARTMENT_ID_MAP.put("나노에너지공학과", "DP46");
        DEPARTMENT_ID_MAP.put("광메카트로닉스공학과", "DP47");
        DEPARTMENT_ID_MAP.put("식물생명과학과", "DP48");
        DEPARTMENT_ID_MAP.put("원예생명과학과", "DP49");
        DEPARTMENT_ID_MAP.put("동물생명자원과학과", "DP4A");
        DEPARTMENT_ID_MAP.put("식품공학과", "DP4B");
        DEPARTMENT_ID_MAP.put("생명환경화학과", "DP4C");
        DEPARTMENT_ID_MAP.put("바이오소재과학과", "DP4D");
        DEPARTMENT_ID_MAP.put("바이오산업기계공학과", "DP4E");
        DEPARTMENT_ID_MAP.put("조경학과", "DP4F");
        DEPARTMENT_ID_MAP.put("식품자원경제학과", "DP50");
        DEPARTMENT_ID_MAP.put("IT응용공학과", "DP51");
        DEPARTMENT_ID_MAP.put("바이오환경에너지학과", "DP52");
        DEPARTMENT_ID_MAP.put("간호학과", "DP53");
        DEPARTMENT_ID_MAP.put("의예과", "DP54");
        DEPARTMENT_ID_MAP.put("의학과", "DP55");
        DEPARTMENT_ID_MAP.put("의과학과", "DP56");
        DEPARTMENT_ID_MAP.put("정보컴퓨터공학부 컴퓨터공학전공", "DP57");
        DEPARTMENT_ID_MAP.put("정보컴퓨터공학부 인공지능전공", "DP58");
        DEPARTMENT_ID_MAP.put("의생명융학공학부", "DP59");
        DEPARTMENT_ID_MAP.put("첨단융합학부 나노자율전공", "DP5A");
    }

    public static String getDepartmentId(String departmentName) {
        return DEPARTMENT_ID_MAP.get(departmentName);
    }
}
