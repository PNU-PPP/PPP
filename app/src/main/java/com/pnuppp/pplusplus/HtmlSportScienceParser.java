package com.pnuppp.pplusplus;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class HtmlSportScienceParser implements HtmlParser {

    @Override
    public List<HtmlItem> parse(Document doc) {
        List<HtmlItem> items = new ArrayList<>();

        // 공지사항 항목을 포함하는 모든 "tr" 태그를 선택합니다 (고정공지 및 일반공지 포함).
        Elements noticeElements = doc.select("tr");

        for (Element element : noticeElements) {
            // 고정 공지인지 확인 (클래스에 "headline"이 포함되어 있으면 고정 공지임)
            if (element.hasClass("headline")) {
                continue; // 고정 공지는 건너뜁니다.
            }

            // 제목, 작성자, 작성일, URL 추출
            String title = element.select("td._artclTdTitle a").text(); // 제목
            String author = element.select("td._artclTdWriter").text(); // 작성자
            String date = element.select("td._artclTdRdate").text(); // 작성일
            String url = element.select("td._artclTdTitle a").attr("href"); // 상대 경로로 된 URL

            // 빈 제목이거나 URL이 없으면 건너뜀 (빈 카드 문제 해결)
            if (title.isEmpty() || url.isEmpty()) {
                continue;
            }

            // URL이 상대 경로일 경우 절대 경로로 변환
            if (!url.startsWith("http")) {
                url = "https://sportscience.pusan.ac.kr" + url;
            }

            // HtmlItem 객체 생성
            HtmlItem item = new HtmlItem(title, date, author, url);
            items.add(item);
        }

        return items;
    }
}
