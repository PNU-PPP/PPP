package com.pnuppp.pplusplus;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.apache.commons.math3.distribution.NormalDistribution;

public class newnew2Activity extends AppCompatActivity {

    private static final String TAG = "newnew2Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newnew2);

        EditText editTextScore = findViewById(R.id.etScore);
        EditText editTextMean = findViewById(R.id.etMean);
        EditText editTextStdDev = findViewById(R.id.etStdDev);  // 표준편차 입력 추가
        EditText editTextNumStudents = findViewById(R.id.etNumStudents);  // 학생 수 입력 추가

        Button buttonCalculate = findViewById(R.id.button_calculate);
        TextView textViewResult = findViewById(R.id.tvResult);

        buttonCalculate.setOnClickListener(v -> {
            String scoreStr = editTextScore.getText().toString();
            String meanStr = editTextMean.getText().toString();
            String stdDevStr = editTextStdDev.getText().toString();
            String numStudentsStr = editTextNumStudents.getText().toString();

            if (scoreStr.isEmpty() || meanStr.isEmpty() || stdDevStr.isEmpty() || numStudentsStr.isEmpty()) {
                Toast.makeText(this, "모든 필드를 입력해주세요", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                // 입력값을 double로 변환
                double score = Double.parseDouble(scoreStr);
                double mean = Double.parseDouble(meanStr);
                double stdDev = Double.parseDouble(stdDevStr);
                int numStudents = Integer.parseInt(numStudentsStr);

                // Z-점수 계산
                double zScore = (score - mean) / stdDev;

                // 정규분포 누적 분포 함수(CDF)를 사용하여 백분위 계산
                NormalDistribution normalDistribution = new NormalDistribution();
                double percentile = normalDistribution.cumulativeProbability(zScore) * 100;

                // 등수 계산
                int rank = (int) Math.ceil((100 - percentile) * numStudents / 100.0);

                // 결과 출력
                String resultText = String.format("나의 등수: %d등\n상위 %.2f%% 입니다.", rank, 100 - percentile);
                textViewResult.setText(resultText);

                // 로그 출력
                Log.d(TAG, "Z-Score: " + zScore + ", Percentile: " + percentile + ", Rank: " + rank);

            } catch (NumberFormatException e) {
                Toast.makeText(this, "올바른 숫자를 입력해 주세요.", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
