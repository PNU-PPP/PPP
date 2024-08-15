package com.pnuppp.pplusplus;

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
                String number = row.select("td._artclTdNum").text();
                String title = row.select("td._artclTdTitle a").text();
                String date = row.select("td._artclTdRdate").text();
                String attachmentCount = row.select("td._artclTdAtchFile").text();
                String views = row.select("td._artclTdAccess").text();
                String url = row.select("td._artclTdTitle a").attr("href");

                // URL이 상대 경로일 경우 절대 경로로 변환
                if (!url.startsWith("http")) {
                    url = baseDomain + url;
                }

                HtmlItem item = new HtmlItem(number, title, date, attachmentCount, views, url);
                items.add(item);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return items;
    }
}
