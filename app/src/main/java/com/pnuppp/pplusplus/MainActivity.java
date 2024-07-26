package com.pnuppp.pplusplus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button buttonInfoEdit = findViewById(R.id.button2);
        buttonInfoEdit.setOnClickListener(v -> {
            Intent newIntent = new Intent(MainActivity.this, InfoEditActivity.class);
            startActivity(newIntent);
        });

        Button buttonnewnew = findViewById(R.id.button5);
        buttonnewnew.setOnClickListener(v -> {
            Intent newIntent = new Intent(MainActivity.this, newnewActivity.class);
            startActivity(newIntent);
        });

        Button buttonMyGPA = findViewById(R.id.button3);
        buttonMyGPA.setOnClickListener(v->{
            Intent myIntent = new Intent(MainActivity.this, GPACalculatorActivity.class);
            startActivity(myIntent);
        });

        Button noticeButton = findViewById(R.id.noticeButton);
        noticeButton.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, DepartmentNoticeActivity.class);
            startActivity(intent);

        });


        // 전달받은 값을 설정할 TextView
        TextView textView8 = findViewById(R.id.textView8);
        TextView textView9 = findViewById(R.id.textView9);

        // Intent로 값 받기ㄴ
        Intent intent = getIntent();
        String major = intent.getStringExtra("major");
        String studentID = intent.getStringExtra("student_id");
        String name = intent.getStringExtra("name");

        if (major != null && studentID != null && name != null) {
            textView8.setText(major);
            textView9.setText(studentID + " " + name);
        } else {
            // SharedPreferences에서 값 가져오기
            SharedPreferences sharedPref = getSharedPreferences("com.pnuppp.pplusplus.user_info", MODE_PRIVATE);
            if(sharedPref.contains("major") && sharedPref.contains("student_id") && sharedPref.contains("name")){
                String savedMajor = sharedPref.getString("major", "");
                String savedStudentID = sharedPref.getString("student_id", "");
                String savedName = sharedPref.getString("name", "");

                textView8.setText(savedMajor);
                textView9.setText(savedStudentID + " " + savedName);
            }
        }
    }
}
