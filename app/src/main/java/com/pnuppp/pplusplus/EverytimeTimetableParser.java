package com.pnuppp.pplusplus;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EverytimeTimetableParser {
    public static void parse(String identifier){
        ExecutorService executor = Executors.newSingleThreadExecutor();
        Handler handler = new Handler(Looper.getMainLooper());
        executor.execute(() -> {
            try {
                HttpURLConnection httpURLConnection = (HttpURLConnection) new URL("https://api.everytime.kr/find/timetable/table/friend").openConnection();
                httpURLConnection.setRequestMethod("POST");
                httpURLConnection.setDoOutput(true);
                httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=utf-8");
                httpURLConnection.setRequestProperty("User-Agent", System.getProperty("http.agent"));
                OutputStream outputStream = httpURLConnection.getOutputStream();
                outputStream.write(("identifier=" + identifier + "&friendInfo=true").getBytes(StandardCharsets.UTF_8));
                outputStream.flush();
                outputStream.close();

                InputStream inputStream = httpURLConnection.getInputStream();

                List<SubjectInfo> subjectInfoList = new ArrayList<>();
                XmlPullParser xmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
                xmlPullParser.setInput(new InputStreamReader(inputStream));
                int eventType = xmlPullParser.getEventType();

                boolean subjectStarted = false;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if(xmlPullParser.getName().equals("subject")) {
                            subjectInfoList.add(new SubjectInfo());
                            subjectStarted = true;
                        }else if(subjectStarted){
                            if(xmlPullParser.getName().equals("name"))
                                subjectInfoList.get(subjectInfoList.size() - 1).subjectName = xmlPullParser.getAttributeValue(null, "value");
                            else if(xmlPullParser.getName().equals("credit"))
                                subjectInfoList.get(subjectInfoList.size() - 1).credit = Integer.parseInt(xmlPullParser.getAttributeValue(null, "value"));
                        }
                   }else if(eventType == XmlPullParser.END_TAG && xmlPullParser.getName().equals("subject")) {
                        subjectStarted = false;
                    }
                    eventType = xmlPullParser.next();
                }
                inputStream.close();

                for (SubjectInfo subjectInfo : subjectInfoList) {
                   Log.i("ETP", subjectInfo.subjectName + " " + subjectInfo.credit);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        });
    }

    public interface OnParsedListener {
        void onSuccess(String timetable);
        void onFailed(int errorCode, String errorMessage);
    }
}
