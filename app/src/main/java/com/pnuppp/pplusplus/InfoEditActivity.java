package com.pnuppp.pplusplus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InfoEditActivity extends AppCompatActivity {

    private String[] majors = new String[]{
        //rss인 학과
            //인문대학
            "국어국문학과", "일어일문학과", "불어불문학과", "노어노문학과",
            "중어중문학과", "영어영문학과", "독어독문학과", "언어정보학과", "사학과",
            //사회과학대학
            "행정학과", "정치외교학과", "사회학과", "심리학과", "문헌정보학과", "미디어커뮤니케이션학과",
            //자연과학대학
            "수학과", "통계학과", "화학과", "생명과학과", "미생물학과", "분자생물학과", "지질환경과학과",
            "대기환경과학과", "해양학과",
            //공과대학
            "고분자공학과", "유기소재시스템공학과", "전기전자공학부 전자공학전공", "전기전자공학부 전기공학전공",
            "조선해양공학과", "재료공학부", "항공우주공학과", "건축공학과", "건축학과", "도시공학과", "사회기반시스템공학과",
            //사범대학
            "국어교육과", "영어교육과", "독어교육과", "불어교육과", "교육학과", "유아교육과", "특수교육과", "일반사회교육과",
            "역사교육과", "지리교육과", "윤리교육과", "수학교육과", "물리교육과", "화학교육과", "생물교육과", "지구과학교육과", "체육교육과",
            //경제통상대학
            "무역학부", "경제학부", "관광컨벤션학과", "공공정책학부",
            //경영대학
            "경영학과",
            //약학대학
            "약학전공", "제약학전공",
            //생활과학대학
            "아동가족학과", "의류학과", "식품영양학과",
            //예술대학
            "음악학과", "한국음악학과", "미술학과", "조형학과", "디자인학과", "무용학과", "예술문화영상학과",
            //나노과학기술대학
            "나노메카트로닉스공학과", "나노에너지공학과", "광메카트로닉스공학과",
            //생명자원과학대학
            "식물생명과학과", "원예생명과학과", "동물생명자원과학과", "식품공학과", "생명환경화학과", "바이오소재과학과",
            "바이오산업기계공학과", "조경학과", "식품자원경제학과", "IT응용공학과", "바이오환경에너지학과",
            //간호대학
            "간호학과",
            //의과대학(행정실 공지도 따로 있던데 어떡해야할지모르겠네영)
            "의예과", "의학과", "의과학과",
            //정보의생명공학대학
            "정보컴퓨터공학부 컴퓨터공학전공", "정보컴퓨터공학부 인공지능전공", "의생명융학공학부",


            //rss 아닌 학과
            "한문학과", "철학과", "고고학과", "사회복지학과", "물리학과", "기계공학부", "화공생명환경공학부 화공생명공학전공",
            "화공생명환경공학부 환경공학전공", "전기전자공학부 반도체공학전공", "산업공학과", "국제학부", "실내환경디자인학과",
            "스포츠과학과",

    };




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_edit);

        AutoCompleteTextView editTextMajor = findViewById(R.id.etMajor); // AutoCompleteTextView로 변경
        EditText editTextStudentID = findViewById(R.id.etStudentID);
        EditText editTextName = findViewById(R.id.etName);


        // ArrayAdapter를 생성하고 AutoCompleteTextView에 설정
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, majors);
        editTextMajor.setAdapter(adapter);
        editTextMajor.setThreshold(1); // 1글자 입력 시부터 제안 시작

        SharedPreferences sharedPref = getSharedPreferences("com.pnuppp.pplusplus.user_info", MODE_PRIVATE);
        Button buttonSave = findViewById(R.id.button_save);

        if(sharedPref.contains("major") && sharedPref.contains("student_id") && sharedPref.contains("name")){
            editTextName.setText(sharedPref.getString("name", ""));
            editTextStudentID.setText(sharedPref.getString("student_id", ""));
            editTextMajor.setText(sharedPref.getString("major", ""));
        }

        buttonSave.setOnClickListener(v -> {
            String major = editTextMajor.getText().toString();
            String studentID = editTextStudentID.getText().toString();
            String name = editTextName.getText().toString();

            // 입력 검증: major가 majors 배열에 포함되지 않으면 경고 메시지 표시
            if(!isValidMajor(major)) {
                Toast.makeText(this, "정확한 학부(+전공)/학과명을 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            if(major.isEmpty() || studentID.isEmpty() || name.isEmpty()) {
                Toast.makeText(this, "모든 정보를 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("name", name);
            editor.putString("student_id", studentID);
            editor.putString("major", major);
            editor.apply();

            Toast.makeText(this, "저장되었습니다.", Toast.LENGTH_SHORT).show();

            // MainActivity로 값 전달
            Intent mainIntent = new Intent(InfoEditActivity.this, MainActivity.class);
            mainIntent.putExtra("major", major);
            mainIntent.putExtra("student_id", studentID);
            mainIntent.putExtra("name", name);
            startActivity(mainIntent);



            finish();
        });
    }
    // 학과명이 majors 배열에 존재하는지 확인하는 메서드
    private boolean isValidMajor(String major) {
        for (String m : majors) {
            if (m.equals(major)) {
                return true;
            }
        }
        return false;
    }

}
