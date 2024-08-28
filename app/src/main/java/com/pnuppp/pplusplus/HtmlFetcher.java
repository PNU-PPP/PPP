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

        // 학과별로 기본 파서와 특수 파서를 설정
        parserMap.put("한문학과", new HtmlDefaultParser("https://hanmun.pusan.ac.kr"));
        parserMap.put("철학과", new HtmlDefaultParser("https://philosophy.pusan.ac.kr"));
        parserMap.put("고고학과", new HtmlDefaultParser("https://archaeology.pusan.ac.kr"));
        parserMap.put("사회복지학과", new HtmlDefaultParser("https://swf.pusan.ac.kr"));
        parserMap.put("물리학과", new HtmlDefaultParser("https://phys.pusan.ac.kr"));
        parserMap.put("화공생명환경공학부 화공생명공학전공", new HtmlDefaultParser("https://chemeng.pusan.ac.kr"));
        parserMap.put("화공생명환경공학부 환경공학전공", new HtmlDefaultParser("https://pnuenv.pusan.ac.kr"));
        parserMap.put("전기전자공학부 반도체공학전공", new HtmlDefaultParser("https://semi.pusan.ac.kr"));
        parserMap.put("산업공학과", new HtmlDefaultParser("https://ie.pusan.ac.kr"));
        parserMap.put("실내환경디자인학과", new HtmlDefaultParser("https://hid.pusan.ac.kr"));
        parserMap.put("첨단융합학부 공학자율전공", new HtmlDefaultParser("https://u-eng.pusan.ac.kr"));

        // 다른 학과도 추가 가능
        parserMap.put("국제학부", new HtmlInternationalParser());
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
                List<HtmlItem> items = parser.parse(doc);
                callback.onSuccess(items);
            } catch (IOException e) {
                callback.onError(new RuntimeException("네트워크 오류 발생: " + e.getMessage(), e));
            } catch (Exception e) {  // 일반적인 Exception 처리 추가
                callback.onError(new RuntimeException("파싱 오류 발생: " + e.getMessage(), e));
            }
        });
    }

    public void shutdown() {
        executorService.shutdown();
    }
}
