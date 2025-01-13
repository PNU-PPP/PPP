package com.pnuppp.ppp;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HtmlDepartmentNoticeActivity extends AppCompatActivity {

    // 학과별로 여러 카테고리의 공지사항 URL을 관리하는 Map
    private Map<String, Map<String, String>> departmentHtmlMap;
    private Spinner noticeTypeSpinner;  // Spinner 선언
    private RecyclerView recyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_department_notice);

        // Map과 RecyclerView 초기화
        departmentHtmlMap = new HashMap<>();
        setupHtmlMapping();

        // Spinner와 RecyclerView 연결
        noticeTypeSpinner = findViewById(R.id.categorySpinner);
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        String selectedDepartment = getIntent().getStringExtra("major");

        // 학과 존재 여부 확인 및 카테고리 설정
        if (departmentHtmlMap.containsKey(selectedDepartment)) {
            Map<String, String> noticeCategories = departmentHtmlMap.get(selectedDepartment);
            List<String> categories = new ArrayList<>(noticeCategories.keySet());

            // 카테고리를 알파벳 또는 한글 순서로 정렬
            Collections.sort(categories); // 이 줄이 정확히 어댑터 설정 전에 있어야 함

            // 정렬된 카테고리 목록을 로그로 출력하여 확인
            Log.d("CategorySort", "Sorted Categories: " + categories.toString());

            // 어댑터에 정렬된 카테고리 목록 설정
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, categories);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            noticeTypeSpinner.setAdapter(adapter);

            // Spinner 선택 이벤트 처리
            noticeTypeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    String selectedCategory = categories.get(position);
                    fetchNoticesForDepartment(selectedDepartment, selectedCategory);
                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    // 아무것도 선택되지 않았을 때의 처리
                }
            });

            // 기본적으로 첫 번째 공지사항을 로드
            if (!categories.isEmpty()) {
                String firstCategory = categories.get(0);
                fetchNoticesForDepartment(selectedDepartment, firstCategory);
            }
        } else {
            Log.e("HtmlDepartmentNoticeActivity", "해당 학과에 대한 공지사항을 찾을 수 없습니다.");
        }
    }


    private void setupHtmlMapping() {
        departmentHtmlMap = new HashMap<>();

        // 한문학과
        Map<String, String> hanmunCategories = new HashMap<>();
        hanmunCategories.put("학부 공지", "https://hanmun.pusan.ac.kr/hanmun/20474/subview.do");
        hanmunCategories.put("대학원 공지", "https://hanmun.pusan.ac.kr/hanmun/20475/subview.do");
        departmentHtmlMap.put("한문학과", hanmunCategories);

        // 철학과
        Map<String, String> philosophyCategories = new HashMap<>();
        philosophyCategories.put("학부 공지", "https://philosophy.pusan.ac.kr/philosophy/17294/subview.do");
        philosophyCategories.put("대학원 공지", "https://philosophy.pusan.ac.kr/philosophy/17297/subview.do");
        departmentHtmlMap.put("철학과", philosophyCategories);

        // 고고학과
        Map<String, String> archaeologyCategories = new HashMap<>();
        archaeologyCategories.put("학부 공지", "https://archaeology.pusan.ac.kr/archaeology/21071/subview.do");
        archaeologyCategories.put("대학원 공지", "https://archaeology.pusan.ac.kr/archaeology/65644/subview.do");
        departmentHtmlMap.put("고고학과", archaeologyCategories);

        // 사회복지학과
        Map<String, String> socialWelfareCategories = new HashMap<>();
        socialWelfareCategories.put("학과공지", "https://swf.pusan.ac.kr/swf/16699/subview.do");
        socialWelfareCategories.put("홍보게시판", "https://swf.pusan.ac.kr/swf/16703/subview.do");  // 대학원 공지 없음
        departmentHtmlMap.put("사회복지학과", socialWelfareCategories);

        // 물리학과
        Map<String, String> physicsCategories = new HashMap<>();
        physicsCategories.put("학부공지", "https://phys.pusan.ac.kr/phys/14948/subview.do");
        physicsCategories.put("대학원공지", "https://phys.pusan.ac.kr/phys/14949/subview.do");
        physicsCategories.put("채용공고", "https://phys.pusan.ac.kr/phys/14950/subview.do");
        departmentHtmlMap.put("물리학과", physicsCategories);

        // 화공생명환경공학부 화공생명공학전공
        Map<String, String> chemicalEngCategories = new HashMap<>();
        chemicalEngCategories.put("학사", "https://chemeng.pusan.ac.kr/chemeng/15735/subview.do");
        chemicalEngCategories.put("취업 및 장학", "https://chemeng.pusan.ac.kr/chemeng/15737/subview.do");
        chemicalEngCategories.put("CBE 세미나", "https://chemeng.pusan.ac.kr/chemeng/15736/subview.do");
        departmentHtmlMap.put("화공생명환경공학부 화공생명공학전공", chemicalEngCategories);

        // 화공생명환경공학부 환경공학전공
        Map<String, String> environmentalEngCategories = new HashMap<>();
        environmentalEngCategories.put("공지사항", "https://pnuenv.pusan.ac.kr/pnuenv/15177/subview.do");
        environmentalEngCategories.put("취업정보", "https://pnuenv.pusan.ac.kr/pnuenv/15180/subview.do");
        environmentalEngCategories.put("환경대학원공지", "https://pnuenv.pusan.ac.kr/pnuenv/15181/subview.do");
        departmentHtmlMap.put("화공생명환경공학부 환경공학전공", environmentalEngCategories);

        // 전기전자공학부 반도체공학전공
        Map<String, String> semiconductorEngCategories = new HashMap<>();
        semiconductorEngCategories.put("공지사항", "https://semi.pusan.ac.kr/semi/71943/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGc2VtaSUyRjE3OTQ4JTJGYXJ0Y2xMaXN0LmRvJTNGc3JjaFdyZCUzRCUyNnNyY2hDb2x1bW4lM0RzaiUyNmJic09wZW5XcmRTZXElM0QlMjZpc1ZpZXdNaW5lJTNEZmFsc2UlMjZiYnNDbFNlcSUzRCUyNg%3D%3D");
        departmentHtmlMap.put("전기전자공학부 반도체공학전공", semiconductorEngCategories);

        // 산업공학과
        Map<String, String> industrialEngCategories = new HashMap<>();
        industrialEngCategories.put("공지사항", "https://ie.pusan.ac.kr/ie/73110/subview.do");
        industrialEngCategories.put("학과세미나", "https://ie.pusan.ac.kr/ie/73111/subview.do");
        industrialEngCategories.put("취업정보", "https://ie.pusan.ac.kr/ie/19428/subview.do");
        departmentHtmlMap.put("산업공학과", industrialEngCategories);

        // 실내환경디자인학과
        Map<String, String> interiorDesignCategories = new HashMap<>();
        interiorDesignCategories.put("학부 공지", "https://hid.pusan.ac.kr/hid/17458/subview.do");

        // 첨단융합학부 공학자율전공
        Map<String, String> advancedEngCategories = new HashMap<>();
        advancedEngCategories.put("학부 공지", "https://u-eng.pusan.ac.kr/u-eng/73849/subview.do");
        departmentHtmlMap.put("첨단융합학부 공학자율전공", advancedEngCategories);

        // 아래는 페이지 디자인 다른 학과들
        // 국제학부
        Map<String, String> internationalStudiesCategories = new HashMap<>();
        internationalStudiesCategories.put("공지사항", "https://pnudgs.com:44954/board/bbs/board.php?bo_table=Notice");
        internationalStudiesCategories.put("News", "https://pnudgs.com:44954/board/bbs/board.php?bo_table=News");
        internationalStudiesCategories.put("News", "https://pnudgs.com:44954/board/bbs/board.php?bo_table=Writing_Lab");
        departmentHtmlMap.put("국제학부", internationalStudiesCategories);

        // 스포츠과학과
        Map<String, String> sportsScienceCategories = new HashMap<>();
        sportsScienceCategories.put("학부 공지", "https://sportscience.pusan.ac.kr/sportscience/14558/subview.do");
        departmentHtmlMap.put("스포츠과학과", sportsScienceCategories);

        // 기계공학부
        Map<String, String> mechanicalEngCategories = new HashMap<>();
        mechanicalEngCategories.put("공지(학부)", "https://me.pusan.ac.kr/new/sub05/sub01_01.asp");
        mechanicalEngCategories.put("공지(대학원)", "https://me.pusan.ac.kr/new/sub05/sub01_02.asp");
        mechanicalEngCategories.put("공지(장학)", "https://me.pusan.ac.kr/new/sub05/sub01_05.asp");
        mechanicalEngCategories.put("공지(홍보)", "https://me.pusan.ac.kr/new/sub05/sub01_03.asp");
        mechanicalEngCategories.put("공지(기자재)", "https://me.pusan.ac.kr/new/sub05/sub01_04.asp");
        mechanicalEngCategories.put("학부 소식", "https://me.pusan.ac.kr/new/sub05/sub02.asp");
        mechanicalEngCategories.put("취업 정보", "https://me.pusan.ac.kr/new/sub05/sub05.asp");
        departmentHtmlMap.put("기계공학부", mechanicalEngCategories);

    }

    private void fetchNoticesForDepartment(String departmentName, String category) {
        if (departmentHtmlMap.containsKey(departmentName)) {
            Map<String, String> noticeCategories = departmentHtmlMap.get(departmentName);
            String url = noticeCategories.get(category);

            if (url != null && !url.isEmpty()) {
                fetchHtmlNotices(departmentName, url);
            } else {
                Log.e("HtmlDepartmentNoticeActivity", "URL이 null이거나 비어 있습니다: " + departmentName);
            }
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

        if (items == null || items.isEmpty()) {
            Log.e("HtmlDepartmentNoticeActivity", "공지사항 데이터가 비어 있습니다.");
            items = new ArrayList<>();  // 빈 리스트로 초기화
        }

        HtmlNoticeAdapter noticeAdapter = new HtmlNoticeAdapter(items, this);
        recyclerView.setAdapter(noticeAdapter);

        Log.d("HtmlDepartmentNoticeActivity", "Number of items: " + items.size());
    }
}
