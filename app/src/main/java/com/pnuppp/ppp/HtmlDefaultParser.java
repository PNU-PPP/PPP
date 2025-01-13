package com.pnuppp.ppp;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class HtmlDefaultParser implements HtmlParser {

    private String baseDomain;

    public HtmlDefaultParser(String baseDomain) {
        this.baseDomain = baseDomain;
    }

    @Override
    public List<HtmlItem> parse(Document document) {
        List<HtmlItem> items = new ArrayList<>();

        try {
            Elements rows = document.select("tbody tr");

            for (Element row : rows) {
                String title = row.select("td._artclTdTitle a").text(); // 공지사항 제목
                String date = row.select("td._artclTdRdate").text(); // 작성 날짜
                String author = row.select("td._artclTdWriter").text(); // 작성자명
                String url = row.select("td._artclTdTitle a").attr("href"); // 공지사항 링크

                // URL이 상대 경로일 경우 절대 경로로 변환
                if (!url.startsWith("http")) {
                    url = baseDomain + url;
                }

                HtmlItem item = new HtmlItem(title, date, author, url); // HtmlItem 생성 시 작성자명 추가
                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }
}
