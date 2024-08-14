package com.pnuppp.pplusplus;

import org.jsoup.nodes.Document;
import java.util.List;

public interface HtmlParser {
    List<HtmlItem> parse(Document document) throws Exception;
}
