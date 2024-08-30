package com.pnuppp.pplusplus;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Pair;

import java.util.ArrayList;
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

        RadioButton undergraduateButton = findViewById(R.id.undergraduateButton);
        RadioButton graduateButton = findViewById(R.id.graduateButton);

        // 학부 공지 또는 대학원 공지가 없는 경우 해당 라디오 버튼 숨기기
        if (departmentHtmlMap.containsKey(selectedDepartment)) {
            Pair<String[], String[]> urls = departmentHtmlMap.get(selectedDepartment);
            if (urls.first[0] == null || urls.first[0].isEmpty()) {
                undergraduateButton.setVisibility(View.GONE);
            }
            if (urls.second[0] == null || urls.second[0].isEmpty()) {
                graduateButton.setVisibility(View.GONE);
            }
        }

        // 기본적으로 학부 공지를 로드
        fetchNoticesForDepartment(selectedDepartment, true);

        RadioGroup noticeTypeGroup = findViewById(R.id.noticeTypeGroup);
        noticeTypeGroup.setOnCheckedChangeListener((group, checkedId) -> {
            boolean isUndergraduate = (checkedId == R.id.undergraduateButton);
            fetchNoticesForDepartment(selectedDepartment, isUndergraduate);
        });
    }


    private void setupHtmlMapping() {
        departmentHtmlMap.put("한문학과", new Pair<>(new String[]{"https://hanmun.pusan.ac.kr/hanmun/20474/subview.do"}, new String[]{"https://hanmun.pusan.ac.kr/hanmun/20475/subview.do"}));
        departmentHtmlMap.put("철학과", new Pair<>(new String[]{"https://philosophy.pusan.ac.kr/philosophy/17294/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGcGhpbG9zb3BoeSUyRjM0NTMlMkZhcnRjbExpc3QuZG8lM0Y%3D"}, new String[]{"https://philosophy.pusan.ac.kr/philosophy/17297/subview.do"}));
        departmentHtmlMap.put("고고학과", new Pair<>(new String[]{"https://archaeology.pusan.ac.kr/archaeology/21071/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGYXJjaGFlb2xvZ3klMkY0MTY3JTJGYXJ0Y2xMaXN0LmRvJTNG"}, new String[]{"https://archaeology.pusan.ac.kr/archaeology/65644/subview.do"}));
        departmentHtmlMap.put("사회복지학과", new Pair<>(new String[]{"https://swf.pusan.ac.kr/swf/16699/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGc3dmJTJGMzMxNCUyRmFydGNsTGlzdC5kbyUzRg%3D%3D"}, new String[]{null}));
        departmentHtmlMap.put("물리학과", new Pair<>(new String[]{"https://phys.pusan.ac.kr/phys/14948/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGcGh5cyUyRjI2NTglMkZhcnRjbExpc3QuZG8lM0Y%3D"}, new String[]{"https://phys.pusan.ac.kr/phys/14949/subview.do"}));
        departmentHtmlMap.put("화공생명환경공학부 화공생명공학전공", new Pair<>(new String[]{"https://chemeng.pusan.ac.kr/chemeng/15735/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGY2hlbWVuZyUyRjI4NzAlMkZhcnRjbExpc3QuZG8lM0ZzcmNoV3JkJTNEJTI2c3JjaENvbHVtbiUzRHNqJTI2YmJzT3BlbldyZFNlcSUzRCUyNmlzVmlld01pbmUlM0RmYWxzZSUyNmJic0NsU2VxJTNEJTI2"}, null));
        departmentHtmlMap.put("화공생명환경공학부 환경공학전공", new Pair<>(new String[]{"https://pnuenv.pusan.ac.kr/pnuenv/15177/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGcG51ZW52JTJGMjcxNyUyRmFydGNsTGlzdC5kbyUzRg%3D%3D"}, new String[]{"https://pnuenv.pusan.ac.kr/pnuenv/15181/subview.do"}));
        departmentHtmlMap.put("전기전자공학부 반도체공학전공", new Pair<>(new String[]{"https://semi.pusan.ac.kr/semi/71943/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGc2VtaSUyRjE3OTQ4JTJGYXJ0Y2xMaXN0LmRvJTNG"}, new String[]{null}));
        departmentHtmlMap.put("산업공학과", new Pair<>(new String[]{"https://ie.pusan.ac.kr/ie/19424/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGaWUlMkYzODEwJTJGYXJ0Y2xMaXN0LmRvJTNG"}, new String[]{null}));
        departmentHtmlMap.put("실내환경디자인학과", new Pair<>(new String[]{"https://hid.pusan.ac.kr/hid/17458/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGaGlkJTJGMzQ4OSUyRmFydGNsTGlzdC5kbyUzRg%3D%3D"}, new String[]{null}));
        departmentHtmlMap.put("첨단융합학부 공학자율전공", new Pair<>(new String[]{"https://u-eng.pusan.ac.kr/u-eng/73849/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGdS1lbmclMkYxODM5NiUyRmFydGNsTGlzdC5kbyUzRg%3D%3D"}, new String[]{null}));

        //디자인 다른 학과
        departmentHtmlMap.put("국제학부", new Pair<>(new String[]{"https://pnudgs.com:44954/board/bbs/board.php?bo_table=Notice"}, new String[]{null}));
        departmentHtmlMap.put("스포츠과학과", new Pair<>(new String[]{"https://sportscience.pusan.ac.kr/sportscience/14558/subview.do?enc=Zm5jdDF8QEB8JTJGYmJzJTJGc3BvcnRzY2llbmNlJTJGMjU5MSUyRmFydGNsTGlzdC5kbyUzRmJic09wZW5XcmRTZXElM0QlMjZpc1ZpZXdNaW5lJTNEZmFsc2UlMjZzcmNoQ29sdW1uJTNEJTI2cGFnZSUzRDElMjZzcmNoV3JkJTNEJTI2cmdzQmduZGVTdHIlM0QlMjZiYnNDbFNlcSUzRCUyNnBhc3N3b3JkJTNEJTI2cmdzRW5kZGVTdHIlM0QlMjY%3D"}, null));
        departmentHtmlMap.put("기계공학부", new Pair<>(new String[]{"https://me.pusan.ac.kr/new/sub05/sub01_01.asp"}, new String[]{"https://me.pusan.ac.kr/new/sub05/sub01_02.asp"}));
    }


    private void fetchNoticesForDepartment(String departmentName, boolean isUndergraduate) {
        if (departmentHtmlMap.containsKey(departmentName)) {
            Pair<String[], String[]> urls = departmentHtmlMap.get(departmentName);

            if (urls == null || (isUndergraduate && urls.first == null) || (!isUndergraduate && urls.second == null)) {
                Log.e("HtmlDepartmentNoticeActivity", "URL 배열이 null입니다: " + departmentName);
                return;
            }

            String url = null;
            if (isUndergraduate) {
                if (urls.first != null && urls.first.length > 0) {
                    url = urls.first[0];
                }
            } else {
                if (urls.second != null && urls.second.length > 0) {
                    url = urls.second[0];
                }
            }

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
