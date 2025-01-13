package com.pnuppp.ppp;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import android.util.Log;

public class HtmlMechanicalEngineeringParser implements HtmlParser {

    private static final String TAG = "HtmlMechEngParser";

    @Override
    public List<HtmlItem> parse(Document doc) {
        List<HtmlItem> items = new ArrayList<>();

        // 모든 "tr" 태그를 선택하여 파싱합니다.
        Elements noticeElements = doc.select("tr");

        for (Element element : noticeElements) {
            // 제목을 포함한 링크 요소 추출
            Element titleElement = element.selectFirst("td.title.left a");
            if (titleElement == null) continue; // 제목 요소가 없으면 다음으로 넘어감

            // 고정 공지나 불필요한 요소를 제거하기 위해 특정 클래스 또는 속성 확인
            if (element.hasClass("notice")) {
                continue; // 고정 공지 건너뜀
            }

            // 텍스트 부분에서 불필요한 정보를 제외하고 순수한 제목만 추출
            String title = titleElement.ownText().trim();

            // 작성자 추출
            String author = element.select("td.writer").text().trim();

            // 작성일 추출
            String date = element.select("td.date").text().trim();

            // URL 추출
            String url = titleElement.attr("href").trim();

            // JavaScript 함수를 호출하는 href 처리
            if (url.startsWith("javascript:goDetail")) {
                // goDetail 함수에서 ID를 추출하기 위한 정규식
                Pattern pattern = Pattern.compile("goDetail\\((\\d+)\\)");
                Matcher matcher = pattern.matcher(url);
                if (matcher.find()) {
                    String id = matcher.group(1);
                    String baseUrl;

                    // 현재 페이지의 URL에 따라 학부 또는 대학원 공지를 처리
                    if (doc.location().contains("sub01_01")) {
                        // 학부 공지
                        baseUrl = "https://me.pusan.ac.kr/new/sub05/sub01_01.asp";
                        url = baseUrl + "?seq=" + id + "&db=hakbunotice&page=1&perPage=20&SearchPart=BD_SUBJECT&SearchStr=&page_mode=view";
                    } else if (doc.location().contains("sub01_02")) {
                        // 대학원 공지
                        baseUrl = "https://me.pusan.ac.kr/new/sub05/sub01_02.asp";
                        url = baseUrl + "?seq=" + id + "&db=gradnotice&page=1&perPage=20&SearchPart=BD_SUBJECT&SearchStr=&page_mode=view";
                    } else {
                        Log.e(TAG, "Unknown page type: " + doc.location());
                        continue; // 페이지 타입을 알 수 없는 경우 건너뜀
                    }

                    Log.d(TAG, "Constructed URL: " + url); // URL이 제대로 생성되었는지 확인
                } else {
                    Log.e(TAG, "Failed to extract ID from JavaScript: " + url);
                    continue; // ID를 찾지 못한 경우 건너뜁니다.
                }
            }

            // 빈 제목이거나 URL이 없으면 건너뜀
            if (title.isEmpty() || url.isEmpty()) {
                Log.e(TAG, "Skipping item due to empty title or URL");
                continue;
            }

            // HtmlItem 객체 생성
            HtmlItem item = new HtmlItem(title, date, author, url);
            items.add(item);

            Log.d(TAG, "Parsed item: " + title + " | " + url); // 파싱된 아이템 정보 로그
        }

        return items;
    }
}
