package com.pnuppp.pplusplus;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class ExtraCurricularActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ExtraCurricularAdapter noticeAdapter;
    private List<RSSItem> rssItems = new ArrayList<>();
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extra_curricular);

        progressBar = findViewById(R.id.progressBar);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noticeAdapter = new ExtraCurricularAdapter(rssItems, this);
        recyclerView.setAdapter(noticeAdapter);

        fetchRSSFeed("http://ppp.jun0.dev:3333/?page=1");
    }

    private void fetchRSSFeed(String rssUrl) {
        if (rssUrl == null || rssUrl.isEmpty()) return;
        rssItems.clear();
        new Thread(() -> {
            try {
                URL url = new URL(rssUrl);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.setRequestMethod("GET");
                InputStream inputStream = connection.getInputStream();
                String content = readStream(inputStream);
                Log.d("RSSFeed", content);

                parseRSS(new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8)));
            } catch (Exception e) {
                Log.e("RSSFetch", "Error fetching RSS feed", e);
            }

            new Handler(Looper.getMainLooper()).post(() -> noticeAdapter.notifyDataSetChanged());
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
                                case "category":
                                    currentItem.setAuthor(parser.nextText());
                                    break;
                                case "description":
                                    currentItem.setCategory(parser.nextText());
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

            new Handler(Looper.getMainLooper()).post(() -> {
                progressBar.setVisibility(ProgressBar.GONE);
                noticeAdapter.notifyDataSetChanged();
            });

        } catch (Exception e) {
            Log.e("RSSParsing", "Error parsing RSS feed", e);
        }
    }
}