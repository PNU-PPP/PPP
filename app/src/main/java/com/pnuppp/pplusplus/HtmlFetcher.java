package com.pnuppp.pplusplus;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class HtmlFetcher {

    public interface Callback {
        void onSuccess(List<HtmlItem> items);
        void onError(Exception e);
    }

    private Map<String, HtmlParser> parserMap;
    private ExecutorService executorService;

    public HtmlFetcher() {
        parserMap = new HashMap<>();
        executorService = Executors.newSingleThreadExecutor();

        // HtmlDefaultParser를 사용할 학과들
        HtmlDefaultParser defaultParser = new HtmlDefaultParser();
        parserMap.put("한문학과", defaultParser);
        parserMap.put("철학과", defaultParser);
        parserMap.put("고고학과", defaultParser);
        parserMap.put("사회복지학과", defaultParser);
        parserMap.put("물리학과", defaultParser);
        parserMap.put("화공생명환경공학부 화공생명공학전공", defaultParser);
        parserMap.put("화공생명환경공학부 환경공학전공", defaultParser);
        parserMap.put("전기전자공학부 반도체공학전공", defaultParser);
        parserMap.put("산업공학과", defaultParser);
        parserMap.put("실내환경디자인학과", defaultParser);

        // 디자인 다른 학과(기계공학부, 국제학부, 스포츠과학과)

    }

    public void fetch(String departmentName, String url, Callback callback) {
        HtmlParser parser = parserMap.get(departmentName);

        if (parser == null) {
            callback.onError(new RuntimeException("파서를 찾을 수 없습니다: " + departmentName));
            return;
        }

        executorService.submit(() -> {
            try {
                Document doc = Jsoup.connect(url).get();
                List<HtmlItem> items = null;
                try {
                    items = parser.parse(doc); // Exception을 처리하기 위해 try-catch 추가
                } catch (Exception e) {
                    callback.onError(e);
                    return;
                }
                callback.onSuccess(items);
            } catch (IOException e) {
                callback.onError(e);
            }
        });
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
