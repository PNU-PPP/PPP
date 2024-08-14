package com.pnuppp.pplusplus;

import android.os.Bundle;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Pair;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HtmlDepartmentNoticeActivity extends AppCompatActivity {

    private Map<String, Pair<String[], String[]>> departmentHtmlMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_notice);

        departmentHtmlMap = new HashMap<>();
        setupHtmlMapping();

        String selectedDepartment = getIntent().getStringExtra("major");
        fetchNoticesForDepartment(selectedDepartment);
    }

    private void setupHtmlMapping() {
        departmentHtmlMap.put("한문학과", new Pair<>(new String[]{"https://hanmun.pusan.ac.kr/hanmun/20474/subview.do"}, new String[]{"https://hanmun.pusan.ac.kr/hanmun/20475/subview.do"}));
        departmentHtmlMap.put("철학과", new Pair<>(new String[]{""}, new String[]{""}));
        departmentHtmlMap.put("고고학과", new Pair<>(new String[]{""}, new String[]{""}));
        departmentHtmlMap.put("사회복지학과", new Pair<>(new String[]{""}, new String[]{""}));
        departmentHtmlMap.put("물리학과", new Pair<>(new String[]{""}, new String[]{""}));
        departmentHtmlMap.put("기계공학부", new Pair<>(new String[]{""}, new String[]{""}));
        departmentHtmlMap.put("화공생명환경공학부 화공생명공학전공", new Pair<>(new String[]{""}, new String[]{""}));
        departmentHtmlMap.put("화공생명환경공학부 환경공학전공", new Pair<>(new String[]{""}, new String[]{""}));
        departmentHtmlMap.put("전기전자공학부 반도체공학전공", new Pair<>(new String[]{""}, new String[]{""}));
        departmentHtmlMap.put("산업공학과", new Pair<>(new String[]{""}, new String[]{""}));
        departmentHtmlMap.put("국제학부", new Pair<>(new String[]{""}, new String[]{""}));
        departmentHtmlMap.put("실내환경디자인학과", new Pair<>(new String[]{""}, new String[]{""}));
        departmentHtmlMap.put("스포츠과학과", new Pair<>(new String[]{""}, new String[]{""}));
        // 필요에 따라 다른 학과들도 추가 가능
    }


    private void fetchNoticesForDepartment(String departmentName) {
        if (departmentHtmlMap.containsKey(departmentName)) {
            Pair<String[], String[]> urls = departmentHtmlMap.get(departmentName);
            fetchHtmlNotices(departmentName, urls.first[0]);  // 학부 공지
            fetchHtmlNotices(departmentName, urls.second[0]); // 대학원 공지
        } else {
            Log.e("HtmlDepartmentNoticeActivity", "해당 학과에 대한 공지사항 URL을 찾을 수 없습니다.");
        }
    }

    private void fetchHtmlNotices(String departmentName, String url) {
        new HtmlFetcher().fetch(departmentName, url, new HtmlFetcher.Callback() {
            @Override
            public void onSuccess(List<HtmlItem> items) {
                runOnUiThread(() -> displayNotices(items));
            }

            @Override
            public void onError(Exception e) {
                Log.e("HtmlDepartmentNoticeActivity", "HTML 공지사항 가져오기 실패", e);
            }
        });
    }

    private void displayNotices(List<HtmlItem> items) {
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        HtmlNoticeAdapter noticeAdapter = new HtmlNoticeAdapter(items, this);
        recyclerView.setAdapter(noticeAdapter);

        // 디버깅 메시지 추가
        Log.d("HtmlDepartmentNoticeActivity", "Number of items: " + items.size());
        recyclerView.getAdapter().notifyDataSetChanged(); // 데이터 갱신
    }
}
