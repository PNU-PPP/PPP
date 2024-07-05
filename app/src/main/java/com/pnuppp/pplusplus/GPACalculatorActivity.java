package com.pnuppp.pplusplus;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import kotlinx.coroutines.selects.SelectUnbiasedKt;

public class GPACalculatorActivity extends AppCompatActivity {

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
    }


    private float semesterGPA(List<SubjectInfo> subjectInfo) {
        float res = 0.0f;
        int total_credit = 0;
        for (SubjectInfo s : subjectInfo) {
            total_credit += s.credit;
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
                res += (s.credit * s.grade) / total_majorCredit;
            }
        }
        return res;
    }
}