package com.pnuppp.pplusplus;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;

public class HtmlDefaultParser implements HtmlParser {
    @Override
    public List<HtmlItem> parse(Document document) throws Exception {
        List<HtmlItem> items = new ArrayList<>();

        // 테이블의 각 행을 선택합니다.
        Elements rows = document.select("tbody tr");

        for (Element row : rows) {
            // 번호, 제목, 작성일, 첨부파일 개수, 조회수에 해당하는 데이터를 선택합니다.
            String number = row.select("td._artclTdNum").text();
            String title = row.select("td._artclTdTitle a").text();
            String date = row.select("td._artclTdRdate").text();
            String attachmentCount = row.select("td._artclTdAtchFile").text();
            String views = row.select("td._artclTdAccess").text();

            // HtmlItem 객체를 생성하여 리스트에 추가합니다.
            HtmlItem item = new HtmlItem(number, title, date, attachmentCount, views);
            items.add(item);
        }

        return items;
    }
}
