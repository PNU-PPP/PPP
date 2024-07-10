package com.pnuppp.pplusplus;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.ArrayList;

public class GPACalculatorActivity extends AppCompatActivity {
    private SharedPreferences preferences;
    private static final String PREFS_NAME = "GPA_Preferences";
    private static final String GPA_KEY = "GPA_List";
    private List<SubjectInfo> tempSubjectInfo = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gpa_calculator);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);

        /////////////// 학점 계산 테스트 용 //////////////
        //List<SubjectInfo> tempSubjectInfo = new ArrayList<>();
        tempSubjectInfo.add(new SubjectInfo("C++", 4, 4.0f, true));
        tempSubjectInfo.add(new SubjectInfo("데과입", 3, 3.5f, true));
        tempSubjectInfo.add(new SubjectInfo("시소", 3, 3.0f, true));
        tempSubjectInfo.add(new SubjectInfo("논회설", 3, 4.0f, true));
        tempSubjectInfo.add(new SubjectInfo("유닉스", 3, 4.0f, true));
        tempSubjectInfo.add(new SubjectInfo("교양", 3, 2.0f, false));

        Toast.makeText(this, "semester: " + semesterGPA(tempSubjectInfo), Toast.LENGTH_LONG).show();
        Toast.makeText(this, "major: " + majorGPA(tempSubjectInfo), Toast.LENGTH_LONG).show();
        ////////////////////////////////////////////

        //////////// Everytime 시간표 파싱 테스트 //////////////
        Button buttonEverytime = findViewById(R.id.buttonEverytime);
        buttonEverytime.setOnClickListener(v -> {
            EverytimeTimetableParser.parse("b30NuC8130Bz1mLBaScr");
        });
        ////////////////////////////////////////////////////

        List<SubjectInfo> subjectInfoList = loadGPAData();
        if (subjectInfoList != null) {
            Toast.makeText(this, "Loaded GPA Data", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No GPA Data Found", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveGPAData(getEverytimeSubjectInfos());
    }

    private float semesterGPA(List<SubjectInfo> subjectInfo) {
        float res = 0.0f;
        int total_credit = 0;
        for (SubjectInfo s : subjectInfo) {
            total_credit += s.credit;
        }
        for (SubjectInfo s : subjectInfo) {
            res += (s.credit * s.grade) / total_credit;
        }
        return res;
    }

    private float majorGPA(List<SubjectInfo> subjectInfo) {
        float res = 0.0f;
        int total_majorCredit = 0;
        for (SubjectInfo s : subjectInfo) {
            if (s.isMajor) {
                total_majorCredit += s.credit;
            }
        }
        for (SubjectInfo s : subjectInfo) {
            if (s.isMajor) {
                res += (s.credit * s.grade) / total_majorCredit;
            }
        }
        return res;
    }

    private List<SubjectInfo> getEverytimeSubjectInfos(){
        return tempSubjectInfo;
    }

    private void saveGPAData(List<SubjectInfo> subjectInfos) {
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(subjectInfos);
        editor.putString(GPA_KEY, json);
        editor.apply();
    }

    private List<SubjectInfo> loadGPAData() {
        Gson gson = new Gson();
        String json = preferences.getString(GPA_KEY, null);
        Type type = new TypeToken<List<SubjectInfo>>() {}.getType();
        return gson.fromJson(json, type);
    }
}