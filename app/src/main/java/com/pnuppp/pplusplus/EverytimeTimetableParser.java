package com.pnuppp.pplusplus;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
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

                BufferedReader bufferRead = new BufferedReader(new InputStreamReader(inputStream));
                StringBuffer response = new StringBuffer();
                String nextline = null;
                while ((nextline = bufferRead.readLine()) != null) {
                    response.append(nextline);
                    response.append('\r');
                }
                bufferRead.close();
                String responseStr = response.toString();
                Log.d("ETTP-parse", responseStr);
                inputStream.close();
                /*
                XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = xmlPullParserFactory.newPullParser();
                xmlPullParser.setInput(new InputStreamReader(inputStream));
                int eventType = xmlPullParser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if (xmlPullParser.getName().equals("subject")) {
                        }
                    }
                    eventType = xmlPullParser.next();
                }*/
                }catch(IOException e){
                    e.printStackTrace();
                }
        });
    }

    public interface OnParsedListener {
        void onSuccess(String timetable);
        void onFailed(int errorCode, String errorMessage);
    }
}
