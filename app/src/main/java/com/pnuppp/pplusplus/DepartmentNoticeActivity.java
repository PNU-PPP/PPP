package com.pnuppp.pplusplus;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;



import android.util.Log;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;


public class DepartmentNoticeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NoticeAdapter noticeAdapter;
    private List<RSSItem> rssItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_notice);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noticeAdapter = new NoticeAdapter(rssItems, this);
        recyclerView.setAdapter(noticeAdapter);

        fetchRSSFeed();
    }

    private void fetchRSSFeed() {
        new Thread(() -> {
            try {
                URL url = new URL("https://cse.pusan.ac.kr/bbs/cse/2605/rssList.do?row=50");
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream inputStream = connection.getInputStream();
                String content = readStream(inputStream); // 데이터를 String으로 읽어오는 함수
                Log.d("RSSFeed", content); // 데이터를 로그에 출력하여 확인

                parseRSS(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    private String readStream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder out = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            out.append(line);
        }
        return out.toString();
    }



    private void parseRSS(InputStream inputStream) {
        try {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = factory.newPullParser();
            parser.setInput(inputStream, null);

            int eventType = parser.getEventType();
            RSSItem currentItem = null;

            while (eventType != XmlPullParser.END_DOCUMENT) {
                String tagName;
                switch (eventType) {
                    case XmlPullParser.START_TAG:
                        tagName = parser.getName();
                        if (tagName.equals("item")) {
                            currentItem = new RSSItem();
                        } else if (currentItem != null) {
                            switch (tagName) {
                                case "title":
                                    currentItem.setTitle(parser.nextText());
                                    break;
                                case "link":
                                    currentItem.setLink(parser.nextText());
                                    break;
                                case "pubDate":
                                    currentItem.setPubDate(parser.nextText());
                                    break;
                                case "author":
                                    currentItem.setAuthor(parser.nextText());
                                    break;
                                case "category":
                                    currentItem.setCategory(parser.nextText());
                                    break;
                                case "description":
                                    currentItem.setDescription(parser.nextText());
                                    break;
                            }
                        }
                        break;

                    case XmlPullParser.END_TAG:
                        tagName = parser.getName();
                        if (tagName.equalsIgnoreCase("item") && currentItem != null) {
                            rssItems.add(currentItem);
                            currentItem = null;
                        }
                        break;
                }
                eventType = parser.next();
            }

            new Handler(Looper.getMainLooper()).post(() -> noticeAdapter.notifyDataSetChanged());

        } catch (Exception e) {
            Log.e("RSSParsing", "Error parsing RSS feed", e); // 예외 메시지를 로그에 출력
        }
    }


}
