package com.pnuppp.pplusplus;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class InfoEditActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_edit);

        EditText editTextMajor = findViewById(R.id.etMajor);
        EditText editTextStudentID = findViewById(R.id.etStudentID);
        EditText editTextName = findViewById(R.id.etName);

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
}
