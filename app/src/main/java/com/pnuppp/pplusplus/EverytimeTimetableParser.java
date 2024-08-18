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
    public static void getSemesters(String identifier, OnSemestersParsedListener onSemestersParsedListener){
        parse(identifier, true, null, onSemestersParsedListener);
    }

    public static void getSubjects(String identifier, OnSubjectsParsedListener onSubjectsParsedListener){
        parse(identifier, false, onSubjectsParsedListener, null);
    }

    private static void parse(String identifier, boolean getSemesters,
                       OnSubjectsParsedListener onSubjectsParsedListener, OnSemestersParsedListener onSemestersParsedListener){
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

                List<SubjectInfo> subjectInfos = new ArrayList<>();
                List<EverytimeIdentifier> everytimeIdentifiers = new ArrayList<>();

                XmlPullParser xmlPullParser = XmlPullParserFactory.newInstance().newPullParser();
                xmlPullParser.setInput(new InputStreamReader(inputStream));
                int eventType = xmlPullParser.getEventType();

                boolean subjectStarted = false;
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_TAG) {
                        if(xmlPullParser.getName().equals("subject")) {
                            SubjectInfo subjectInfo = new SubjectInfo();
                            subjectInfo.isMajor = true;
                            subjectInfos.add(subjectInfo);
                            subjectStarted = true;
                        }else if(subjectStarted){
                            if(xmlPullParser.getName().equals("name"))
                                subjectInfos.get(subjectInfos.size() - 1).subjectName = xmlPullParser.getAttributeValue(null, "value");
                            else if(xmlPullParser.getName().equals("credit"))
                                subjectInfos.get(subjectInfos.size() - 1).credit = Float.parseFloat(xmlPullParser.getAttributeValue(null, "value"));
                        }else if(xmlPullParser.getName().equals("primaryTable")){
                            everytimeIdentifiers.add(new EverytimeIdentifier(
                                    xmlPullParser.getAttributeValue(null, "year"),
                                    xmlPullParser.getAttributeValue(null, "semester"),
                                    xmlPullParser.getAttributeValue(null, "identifier")
                            ));
                        }
                   }else if(eventType == XmlPullParser.END_TAG && xmlPullParser.getName().equals("subject")) {
                        subjectStarted = false;
                    }
                    eventType = xmlPullParser.next();
                }
                inputStream.close();

                if(getSemesters) {
                    if(everytimeIdentifiers.isEmpty())
                        handler.post(() -> onSemestersParsedListener.onFailed(0, "No semesters found"));
                    else
                        handler.post(() -> onSemestersParsedListener.onSuccess(everytimeIdentifiers));
                }
                else {
                    if(subjectInfos.isEmpty())
                        handler.post(() -> onSubjectsParsedListener.onFailed(0, "No subjects found"));
                    else
                        handler.post(() -> onSubjectsParsedListener.onSuccess(subjectInfos));
                }

                for (SubjectInfo subjectInfo : subjectInfos) {
                   Log.i("ETP", subjectInfo.subjectName + " " + subjectInfo.credit);
                }
                for (EverytimeIdentifier everytimeIdentifier : everytimeIdentifiers) {
                    Log.i("ETP", everytimeIdentifier.year + " " + everytimeIdentifier.semester + " " + everytimeIdentifier.identifier);
                }
            }catch (Exception e){
                e.printStackTrace();
                if(getSemesters)
                    handler.post(() -> onSemestersParsedListener.onFailed(-1, e.getMessage()));
                else
                    handler.post(() -> onSubjectsParsedListener.onFailed(-1, e.getMessage()));
            }
        });
    }

    public interface OnSubjectsParsedListener {
        void onSuccess(List<SubjectInfo> subjectInfos);
        void onFailed(int errorCode, String errorMessage);
    }

    public interface OnSemestersParsedListener {
        void onSuccess(List<EverytimeIdentifier> everytimeIdentifiers);
        void onFailed(int errorCode, String errorMessage);
    }
}
