package com.pnuppp.pplusplus;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.util.ArrayList;
import java.util.List;

public class HtmlInternationalParser implements HtmlParser {

    @Override
    public List<HtmlItem> parse(Document doc) {
        List<HtmlItem> items = new ArrayList<>();

        // "tr" 태그 중 공지사항 항목을 포함하는 요소를 선택
        Elements noticeElements = doc.select("tr.line");

        for (Element element : noticeElements) {
            String title = element.select("td.subject a").text(); // 제목
            String date = element.select("td.datetime").text(); // 작성 날짜
            String author = element.select("td.name").text(); // 작성자명

            // URL이 상대 경로일 경우 절대 경로로 변환
            String url = element.select("td.subject a").attr("href");
            if (!url.startsWith("http")) {
                url = "https://pnudgs.com:44954" + url;
            }

            // HtmlItem 객체 생성
            HtmlItem item = new HtmlItem(title, date, author, url);
            items.add(item);
        }

        return items;
    }
}
