package com.pnuppp.ppp;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class HtmlInternationalParser implements HtmlParser {

    @Override
    public List<HtmlItem> parse(Document doc) {
        List<HtmlItem> items = new ArrayList<>();

        // "tr" 태그 중 고정 공지를 제외하고 공지사항 항목을 선택
        Elements noticeElements = doc.select("tr.line:not(:has(td.num:contains(Notice)))");

        for (Element element : noticeElements) {
            // 제목과 URL 추출
            Element titleElement = element.selectFirst("td.subject a");
            String title = titleElement.text(); // 제목
            String relativeUrl = titleElement.attr("href"); // 상대 경로 URL

            // 작성 날짜 및 작성자 추출
            String date = element.select("td.datetime").text(); // 작성 날짜
            String author = element.select("td.name").text(); // 작성자명

            // URL이 상대 경로일 경우 절대 경로로 변환
            String url = relativeUrl.startsWith("http") ? relativeUrl : "https://pnudgs.com/board/bbs/" + relativeUrl;

            // HtmlItem 객체 생성
            HtmlItem item = new HtmlItem(title, date, author, url);
            items.add(item);
        }

        return items;
    }
}
