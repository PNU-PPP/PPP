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
            "정보컴퓨터공학과", "국어국문학과", "일어일문학과", "불어불문학과", "노어노문학과",
            "중어중문학과", "영어영문학과", "독어독문학과", "언어정보학과", "사학과",
            //사회과학대학
            "행정학과", "정치외교학과", "사회학과", "심리학과", "문헌정보학과", "미디어커뮤니케이션학과",
            //자연과학대학
            "수학과",
    };


    //rss 아닌 학과
    /*
    "한문학과", "철학과", "고고학과", "사회복지학과",
     */

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
                Toast.makeText(this, "유효한 학과를 선택해주세요", Toast.LENGTH_SHORT).show();
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
            Intent intent = new Intent(InfoEditActivity.this, MainActivity.class);
            intent.putExtra("major", major);
            intent.putExtra("student_id", studentID);
            intent.putExtra("name", name);
            startActivity(intent);

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
