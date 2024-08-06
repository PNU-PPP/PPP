package com.pnuppp.pplusplus;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Pair;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import android.view.View;
import android.content.SharedPreferences;

public class RSSDepartmentNoticeActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private NoticeAdapter noticeAdapter;
    private List<RSSItem> rssItems = new ArrayList<>();
    private Map<String, Pair<String[], String[]>> departmentRSSMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_notice);

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        noticeAdapter = new NoticeAdapter(rssItems, this);
        recyclerView.setAdapter(noticeAdapter);

        departmentRSSMap = new HashMap<>();
        setupRSSMapping();

        // SharedPreferences에서 학과 정보 가져오기
        SharedPreferences sharedPref = getSharedPreferences("com.pnuppp.pplusplus.user_info", MODE_PRIVATE);
        String tempSelectedDepartment = getIntent().getStringExtra("major");
        if (tempSelectedDepartment == null) {
            tempSelectedDepartment = sharedPref.getString("major", null);
        }
        final String selectedDepartment = tempSelectedDepartment; // 람다 표현식에서 사용할 final 변수
        Log.d("RSSDepartmentNotice", "Selected Department: " + selectedDepartment);

        RadioGroup noticeTypeGroup = findViewById(R.id.noticeTypeGroup);
        noticeTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            Pair<String[], String[]> urls = departmentRSSMap.get(selectedDepartment);
            if (urls != null) {
                if (checkedId == R.id.undergraduateButton) {
                    if (urls.first != null) {
                        fetchRSSFeeds(urls.first);
                    }
                } else if (checkedId == R.id.graduateButton) {
                    if (urls.second != null) {
                        fetchRSSFeeds(urls.second); // 다중 URL의 경우
                    }
                }
            } else {
                Log.e("RSSDepartmentNotice", "Invalid department selected or no RSS URL available.");
            }
        });

        // 학과 선택에 따른 초기 버튼 상태 설정
        updateButtonState(selectedDepartment);

        // 기본적으로 학부 공지사항을 표시
        noticeTypeGroup.check(R.id.undergraduateButton);

        // 학부 공지를 바로 로드
        Pair<String[], String[]> initialUrls = departmentRSSMap.get(selectedDepartment);
        if (initialUrls != null && initialUrls.first != null) {
            fetchRSSFeeds(initialUrls.first);
        }
    }

    // 선택된 학과에 따라 버튼 상태를 업데이트하는 메소드
    private void updateButtonState(String department) {
        Pair<String[], String[]> urls = departmentRSSMap.get(department);
        boolean hasUndergraduateNotice = (urls != null && urls.first != null && urls.first.length > 0);
        boolean hasGraduateNotice = (urls != null && urls.second != null && urls.second.length > 0);
        View undergraduateButton = findViewById(R.id.undergraduateButton);
        View graduateButton = findViewById(R.id.graduateButton);

        // 학부 공지사항이 없는 경우, 버튼을 숨김
        if (!hasUndergraduateNotice) {
            undergraduateButton.setVisibility(View.GONE);
        }

        // 대학원 공지사항이 없는 경우, 버튼을 숨김
        if (!hasGraduateNotice) {
            graduateButton.setVisibility(View.GONE);
        }
    }

    //학부, 대학원 순서
    private void setupRSSMapping() {
        departmentRSSMap.put("국어국문학과", new Pair<>(new String[]{"https://bkorea.pusan.ac.kr/bbs/bkorea/3375/rssList.do?row=50"}, new String[]{"https://bkorea.pusan.ac.kr/bbs/bkorea/3376/rssList.do?row=50"}));
        departmentRSSMap.put("일어일문학과", new Pair<>(new String[]{"https://japan.pusan.ac.kr/bbs/japan/3422/rssList.do?row=50"}, new String[]{"https://japan.pusan.ac.kr/bbs/japan/3423/rssList.do?row=50"}));
        departmentRSSMap.put("불어불문학과", new Pair<>(new String[]{"https://french.pusan.ac.kr/bbs/french/4295/rssList.do?row=50"}, null));
        departmentRSSMap.put("노어노문학과", new Pair<>(new String[]{"https://russia.pusan.ac.kr/bbs/russia/16061/rssList.do?row=50"}, new String[]{"https://russia.pusan.ac.kr/bbs/russia/16062/rssList.do?row=50"}));
        departmentRSSMap.put("중어중문학과", new Pair<>(new String[]{"https://china.pusan.ac.kr/bbs/china/5363/rssList.do?row=50"}, new String[]{"https://china.pusan.ac.kr/bbs/china/5364/rssList.do?row=50"}));
        departmentRSSMap.put("영어영문학과", new Pair<>(new String[]{"https://pnuenglish.pusan.ac.kr/bbs/pnuenglish/3108/rssList.do?row=50"}, new String[]{"https://pnuenglish.pusan.ac.kr/bbs/pnuenglish/3107/rssList.do?row=50"}));
        departmentRSSMap.put("독어독문학과", new Pair<>(new String[]{"https://german.pusan.ac.kr/bbs/german/5032/rssList.do?row=50"}, new String[]{"https://german.pusan.ac.kr/bbs/german/5033/rssList.do?row=50"}));
        departmentRSSMap.put("언어정보학과", new Pair<>(new String[]{"https://linguistics.pusan.ac.kr/bbs/linguistics/4133/rssList.do?row=50"}, null));
        departmentRSSMap.put("사학과", new Pair<>(new String[]{"https://history.pusan.ac.kr/bbs/history/3466/rssList.do?row=50"}, new String[]{"https://history.pusan.ac.kr/bbs/history/11701/rssList.do?row=50"}));
        // 사회과학대학
        departmentRSSMap.put("행정학과", new Pair<>(new String[]{"https://pub-adm.pusan.ac.kr/bbs/pub-adm/2744/rssList.do?row=50"}, new String[]{"https://pub-adm.pusan.ac.kr/bbs/pub-adm/2743/rssList.do?row=50"}));
        departmentRSSMap.put("정치외교학과", new Pair<>(new String[]{"https://polsci.pusan.ac.kr/bbs/polsci/2837/rssList.do?row=50"}, null));
        departmentRSSMap.put("사회학과", new Pair<>(new String[]{"https://soc.pusan.ac.kr/bbs/soc/7527/rssList.do?row=50"}, new String[]{"https://soc.pusan.ac.kr/bbs/soc/9673/rssList.do?row=50"}));
        departmentRSSMap.put("심리학과", new Pair<>(new String[]{"https://psy.pusan.ac.kr/bbs/psy/2798/rssList.do?row=50"}, new String[]{"https://psy.pusan.ac.kr/bbs/psy/2797/rssList.do?row=50"}));
        departmentRSSMap.put("문헌정보학과", new Pair<>(new String[]{"https://lais.pusan.ac.kr/bbs/lais/15732/rssList.do?row=50"}, new String[]{"https://lais.pusan.ac.kr/bbs/lais/15733/rssList.do?row=50"}));
        departmentRSSMap.put("미디어커뮤니케이션학과", new Pair<>(null, null)); //rss 상 오류 뜸
        // 자연과학대학
        departmentRSSMap.put("수학과", new Pair<>(new String[]{"https://math.pusan.ac.kr/bbs/math/2818/rssList.do?row=50"}, new String[]{"https://math.pusan.ac.kr/bbs/math/16237/rssList.do?row=50"}));
        departmentRSSMap.put("통계학과", new Pair<>(new String[]{"https://stat.pusan.ac.kr/bbs/stat/2705/rssList.do?row=50"}, null));
        departmentRSSMap.put("화학과", new Pair<>(new String[]{"https://chem.pusan.ac.kr/bbs/chem/2734/rssList.do?row=50"}, new String[]{"https://chem.pusan.ac.kr/bbs/chem/2734/rssList.do?row=50"}));
        departmentRSSMap.put("생명과학과", new Pair<>(new String[]{"https://biology.pusan.ac.kr/bbs/biology/3143/rssList.do?row=50"}, new String[]{"https://biology.pusan.ac.kr/bbs/biology/3144/rssList.do?row=50"}));
        departmentRSSMap.put("미생물학과", new Pair<>(new String[]{"https://microbio.pusan.ac.kr/bbs/microbio/3085/rssList.do?row=50"}, new String[]{"https://microbio.pusan.ac.kr/bbs/microbio/3086/rssList.do?row=50"}));
        departmentRSSMap.put("분자생물학과", new Pair<>(new String[]{"https://molbiology.pusan.ac.kr/bbs/molbiology/3918/rssList.do?row=50"}, null));
        departmentRSSMap.put("지질환경과학과", new Pair<>(new String[]{"https://geology.pusan.ac.kr/bbs/geology/2800/rssList.do?row=50"}, null));
        departmentRSSMap.put("대기환경과학과", new Pair<>(new String[]{"https://atmos.pusan.ac.kr/bbs/atmos/3096/rssList.do?row=50"}, null));
        departmentRSSMap.put("해양학과", new Pair<>(new String[]{"https://ocean.pusan.ac.kr/bbs/ocean/2877/rssList.do?row=50"}, null));
        // 공과대학
        departmentRSSMap.put("고분자공학과", new Pair<>(new String[]{"https://polymer.pusan.ac.kr/bbs/polymer/16257/rssList.do?row=50"}, null));
        departmentRSSMap.put("유기소재시스템공학과", new Pair<>(new String[]{"https://omse.pusan.ac.kr/bbs/omse/3203/rssList.do?row=50"}, new String[]{"https://omse.pusan.ac.kr/bbs/omse/12392/rssList.do?row=50"}));
        departmentRSSMap.put("전기전자공학부 전자공학전공", new Pair<>(new String[]{"https://ee.pusan.ac.kr/bbs/ee/2635/rssList.do?row=50"}, new String[]{"https://ee.pusan.ac.kr/bbs/ee/2642/rssList.do?row=50"})); //학부, 취업 묶음
        departmentRSSMap.put("전기전자공학부 전기공학전공", new Pair<>(new String[]{"https://eec.pusan.ac.kr/bbs/eehome/2650/rssList.do?row=50"}, new String[]{"https://eec.pusan.ac.kr/bbs/eehome/2651/rssList.do?row=50"}));
        departmentRSSMap.put("조선해양공학과", new Pair<>(new String[]{"https://naoe.pusan.ac.kr/bbs/naoe/2754/rssList.do?row=50"}, new String[]{"https://naoe.pusan.ac.kr/bbs/naoe/2756/rssList.do?row=50"}));
        departmentRSSMap.put("재료공학부", new Pair<>(new String[]{"https://mse.pusan.ac.kr/bbs/mse/8972/rssList.do?row=50"}, new String[]{"https://mse.pusan.ac.kr/bbs/mse/12265/rssList.do?row=50", "https://mse.pusan.ac.kr/bbs/mse/12266/rssList.do?row=50", "https://mse.pusan.ac.kr/bbs/mse/12267/rssList.do?row=50", "https://mse.pusan.ac.kr/bbs/mse/12268/rssList.do?row=50", "https://mse.pusan.ac.kr/bbs/mse/12269/rssList.do?row=50"}));
        departmentRSSMap.put("항공우주공학과", new Pair<>(new String[]{"https://aerospace.pusan.ac.kr/bbs/aerospace/3213/rssList.do?row=50"}, null));
        departmentRSSMap.put("건축공학과", new Pair<>(new String[]{"https://archieng.pusan.ac.kr/bbs/_archieng/3964/rssList.do?row=50"}, new String[]{"https://archieng.pusan.ac.kr/bbs/_archieng/14096/rssList.do?row=50"}));
        departmentRSSMap.put("건축학과", new Pair<>(new String[]{"https://archi.pusan.ac.kr/bbs/archi/11920/rssList.do?row=50"}, new String[]{"https://archi.pusan.ac.kr/bbs/archi/11921/rssList.do?row=50"}));
        departmentRSSMap.put("도시공학과", new Pair<>(new String[]{"https://urban.pusan.ac.kr/bbs/urban/3413/rssList.do?row=50"}, null));
        departmentRSSMap.put("사회기반시스템공학과", new Pair<>(new String[]{"https://civil.pusan.ac.kr/bbs/civil/3207/rssList.do?row=50"}, new String[]{"https://civil.pusan.ac.kr/bbs/civil/3206/rssList.do?row=50", "https://civil.pusan.ac.kr/bbs/civil/3208/rssList.do?row=50"}));
        // 사범대학
        departmentRSSMap.put("국어교육과", new Pair<>(new String[]{"https://koredu.pusan.ac.kr/bbs/koredu/5262/rssList.do?row=50"}, new String[]{"https://koredu.pusan.ac.kr/bbs/koredu/5264/rssList.do?row=50", "https://koredu.pusan.ac.kr/bbs/koredu/5265/rssList.do?row=50"}));
        departmentRSSMap.put("영어교육과", new Pair<>(new String[]{"https://englishedu.pusan.ac.kr/bbs/englishedupnu/8789/rssList.do?row=50"}, new String[]{"https://englishedu.pusan.ac.kr/bbs/englishedupnu/8790/rssList.do?row=50"}));
        departmentRSSMap.put("독어교육과", new Pair<>(new String[]{"https://geredu.pusan.ac.kr/bbs/geredu/4381/rssList.do?row=50"}, null));
        departmentRSSMap.put("불어교육과", new Pair<>(new String[]{"https://fredu.pusan.ac.kr/bbs/fredu/4398/rssList.do?row=50"}, null));
        departmentRSSMap.put("교육학과", new Pair<>(new String[]{"https://ed.pusan.ac.kr/bbs/ed/2768/rssList.do?row=50"}, null));
        departmentRSSMap.put("유아교육과", new Pair<>(new String[]{"https://child.pusan.ac.kr/bbs/child/3129/rssList.do?row=50"}, new String[]{"https://child.pusan.ac.kr/bbs/child/3131/rssList.do?row=50"}));
        departmentRSSMap.put("특수교육과", new Pair<>(new String[]{"https://special.pusan.ac.kr/bbs/special/3470/rssList.do?row=50"}, new String[]{"https://special.pusan.ac.kr/bbs/special/3795/rssList.do?row=50", "https://special.pusan.ac.kr/bbs/special/3796/rssList.do?row=50"}));
        departmentRSSMap.put("일반사회교육과", new Pair<>(new String[]{"https://socialedu.pusan.ac.kr/bbs/socialedu/4102/rssList.do?row=50"}, null));
        departmentRSSMap.put("역사교육과", new Pair<>(new String[]{"https://hisedu.pusan.ac.kr/bbs/hisedu/3361/rssList.do?row=50"}, null));
        departmentRSSMap.put("지리교육과", new Pair<>(new String[]{"https://geoedu.pusan.ac.kr/bbs/geoedu/4310/rssList.do?row=50"}, null));
        departmentRSSMap.put("윤리교육과", new Pair<>(new String[]{"https://ethics.pusan.ac.kr/bbs/ethics/4351/rssList.do?row=50"}, null));
        departmentRSSMap.put("수학교육과", new Pair<>(new String[]{"https://mathedu.pusan.ac.kr/bbs/mathedu/2696/rssList.do?row=50"}, new String[]{"https://mathedu.pusan.ac.kr/bbs/mathedu/2698/rssList.do?row=50", "https://mathedu.pusan.ac.kr/bbs/mathedu/2697/rssList.do?row=50"}));
        departmentRSSMap.put("물리교육과", new Pair<>(new String[]{"https://physedu.pusan.ac.kr/bbs/physedu/3403/rssList.do?row=50"}, null));
        departmentRSSMap.put("화학교육과", new Pair<>(new String[]{"https://chemedu.pusan.ac.kr/bbs/chemedu/4094/rssList.do?row=50"}, new String[]{"https://chemedu.pusan.ac.kr/bbs/chemedu/4089/rssList.do?row=50", "https://chemedu.pusan.ac.kr/bbs/chemedu/4057/rssList.do?row=50"}));
        departmentRSSMap.put("생물교육과", new Pair<>(new String[]{"https://edubio.pusan.ac.kr/bbs/edubio/4231/rssList.do?row=50"}, new String[]{"https://edubio.pusan.ac.kr/bbs/edubio/4243/rssList.do?row=50", "https://edubio.pusan.ac.kr/bbs/edubio/4240/rssList.do?row=50"}));
        departmentRSSMap.put("지구과학교육과", new Pair<>(new String[]{""}, new String[]{""}));
        departmentRSSMap.put("체육교육과", new Pair<>(new String[]{""}, new String[]{""}));
        // 경제통상대학
        departmentRSSMap.put("무역학부", new Pair<>(new String[]{""}, new String[]{""}));
        departmentRSSMap.put("경제학부", new Pair<>(new String[]{""}, new String[]{""}));
        departmentRSSMap.put("관광컨벤션학과", new Pair<>(new String[]{""}, new String[]{""}));
        departmentRSSMap.put("공공정책학부", new Pair<>(new String[]{""}, new String[]{""}));
        // 경영대학
        departmentRSSMap.put("경영학과", new Pair<>(new String[]{""}, new String[]{""}));
        // 약학대학
        departmentRSSMap.put("약학전공", new Pair<>(new String[]{""}, new String[]{""}));
        departmentRSSMap.put("제약학전공", new Pair<>(new String[]{""}, new String[]{""}));
        // 생활과학대학
        departmentRSSMap.put("아동가족학과", new Pair<>(new String[]{""}, new String[]{""}));
        departmentRSSMap.put("의류학과", new Pair<>(new String[]{""}, new String[]{""}));
        departmentRSSMap.put("식품영양학과", new Pair<>(new String[]{""}, new String[]{""}));
        // 예술대학
        departmentRSSMap.put("음악학과", new Pair<>(new String[]{""}, new String[]{""}));
        departmentRSSMap.put("한국음악학과", new Pair<>(new String[]{""}, new String[]{""}));
        departmentRSSMap.put("미술학과", new Pair<>(new String[]{""}, new String[]{""}));
        departmentRSSMap.put("조형학과", new Pair<>(new String[]{""}, new String[]{""}));
        departmentRSSMap.put("디자인학과", new Pair<>(new String[]{""}, new String[]{""}));
        departmentRSSMap.put("무용학과", new Pair<>(new String[]{""}, new String[]{""}));
        departmentRSSMap.put("예술문화영상학과", new Pair<>(new String[]{""}, new String[]{""}));
        // 나노과학기술대학
        departmentRSSMap.put("나노메카트로닉스공학과", new Pair<>(new String[]{""}, new String[]{""}));
        departmentRSSMap.put("나노에너지공학과", new Pair<>(new String[]{""}, new String[]{""}));
        departmentRSSMap.put("광메카트로닉스공학과", new Pair<>(new String[]{""}, new String[]{""}));
        // 생명자원과학대학
        departmentRSSMap.put("식물생명과학과", new Pair<>(new String[]{""}, new String[]{""}));
        departmentRSSMap.put("원예생명과학과", new Pair<>(new String[]{""}, new String[]{""}));
        departmentRSSMap.put("동물생명자원과학과", new Pair<>(new String[]{""}, new String[]{""}));
        departmentRSSMap.put("식품공학과", new Pair<>(new String[]{""}, new String[]{""}));
        departmentRSSMap.put("생명환경화학과", new Pair<>(new String[]{""}, new String[]{""}));
        departmentRSSMap.put("바이오소재과학과", new Pair<>(new String[]{""}, new String[]{""}));
        departmentRSSMap.put("바이오산업기계공학과", new Pair<>(new String[]{""}, new String[]{""}));
        departmentRSSMap.put("조경학과", new Pair<>(new String[]{""}, new String[]{""}));
        departmentRSSMap.put("식품자원경제학과", new Pair<>(new String[]{""}, new String[]{""}));
        departmentRSSMap.put("IT응용공학과", new Pair<>(new String[]{""}, new String[]{""}));
        departmentRSSMap.put("바이오환경에너지학과", new Pair<>(new String[]{""}, new String[]{""}));
        // 간호대학
        departmentRSSMap.put("간호학과", new Pair<>(new String[]{""}, new String[]{""}));
        // 의과대학
        departmentRSSMap.put("의예과", new Pair<>(new String[]{""}, new String[]{""}));
        departmentRSSMap.put("의학과", new Pair<>(new String[]{""}, new String[]{""}));
        departmentRSSMap.put("의과학과", new Pair<>(new String[]{""}, new String[]{""}));
        // 정보의생명공학대학
        departmentRSSMap.put("정보컴퓨터공학부 컴퓨터공학전공", new Pair<>(new String[]{""}, new String[]{""}));
        departmentRSSMap.put("정보컴퓨터공학부 인공지능전공", new Pair<>(new String[]{""}, new String[]{""}));
        departmentRSSMap.put("의생명융학공학부", new Pair<>(new String[]{""}, new String[]{""}));
        Log.d("RSSDepartmentNotice", "RSS Mapping Setup Complete");
    }

    // 단일 URL을 처리하는 메소드
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

    // 다중 URL을 처리하는 메소드
    private void fetchRSSFeeds(String[] rssUrls) {
        rssItems.clear();
        new Thread(() -> {
            for (String rssUrl : rssUrls) {
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
            Log.e("RSSParsing", "Error parsing RSS feed", e);
        }
    }
}
