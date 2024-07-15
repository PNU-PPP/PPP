package com.pnuppp.pplusplus;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicInteger;

public class GPACalculatorActivity extends AppCompatActivity {
    private SharedPreferences preferences; // 객체를 저장할 변수
    private static final String PREFS_NAME = "GPA_Preferences"; // 파일 이름 저장할 변수
    private static final String GPA_KEY = "GPA_List"; // 저장할 데이터의 키
    private List<SubjectInfo> tempSubjectInfo = new ArrayList<>();

    private List<EditText> editTextSubjects = new ArrayList<>();
    private List<EditText> editTextCredits = new ArrayList<>();
    private List<Spinner> spinnerGrades = new ArrayList<>();
    private List<CheckBox> checkBoxMajors = new ArrayList<>();

    private Spinner spinnerYear;
    private Spinner spinnerSemester;
    private TextView textViewGPA;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState); // super 클래스의 onCreate 매서드 호출 -> 기본 초기화 수행
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_gpa_calculator);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextSubjects.add(findViewById(R.id.editTextSubject1));
        editTextSubjects.add(findViewById(R.id.editTextSubject2));
        editTextSubjects.add(findViewById(R.id.editTextSubject3));
        editTextSubjects.add(findViewById(R.id.editTextSubject4));

        editTextCredits.add(findViewById(R.id.editTextCredit1));
        editTextCredits.add(findViewById(R.id.editTextCredit2));
        editTextCredits.add(findViewById(R.id.editTextCredit3));
        editTextCredits.add(findViewById(R.id.editTextCredit4));

        spinnerGrades.add(findViewById(R.id.grade1));
        spinnerGrades.add(findViewById(R.id.grade2));
        spinnerGrades.add(findViewById(R.id.grade3));
        spinnerGrades.add(findViewById(R.id.grade4));

        checkBoxMajors.add(findViewById(R.id.checkBoxMajor1));
        checkBoxMajors.add(findViewById(R.id.checkBoxMajor2));
        checkBoxMajors.add(findViewById(R.id.checkBoxMajor3));
        checkBoxMajors.add(findViewById(R.id.checkBoxMajor4));

        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE); // sharedPreference 객체 초기화

        // Spinner 및 TextView 초기화
        spinnerYear = findViewById(R.id.spinnerYear);
        spinnerSemester = findViewById(R.id.spinnerSemester);
        textViewGPA = findViewById(R.id.textViewGPA);

        // 공통 이벤트 리스너 설정
        AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("GPACalculatorActivity", "Spinner item selected: position=" + position + ", id=" + id);
                updateGPA();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        };

        // Spinner에 공통 이벤트 리스너 설정
        spinnerYear.setOnItemSelectedListener(spinnerListener);
        spinnerSemester.setOnItemSelectedListener(spinnerListener);


        // 원래 정보 save 하기
        List<SubjectInfo> savedSubjectInfo = loadGPAData();
        if (savedSubjectInfo != null) {
            tempSubjectInfo.addAll(savedSubjectInfo);
            Toast.makeText(this, "Loaded GPA Data", Toast.LENGTH_LONG).show();
        }
        else {
            Toast.makeText(this, "No GPA Data Found", Toast.LENGTH_LONG).show();
        }

        /////////////// 학점 계산 테스트 용 //////////////
        tempSubjectInfo.add(new SubjectInfo(1, 1, "C++", 4, 4.0f, true));
        tempSubjectInfo.add(new SubjectInfo(1, 1, "데과입", 3, 3.5f, true));
        tempSubjectInfo.add(new SubjectInfo(1, 1, "시소", 3, 3.0f, true));
        tempSubjectInfo.add(new SubjectInfo(1, 1, "논회설", 3, 4.0f, true));
        tempSubjectInfo.add(new SubjectInfo(1, 2, "유닉스", 3, 4.0f, true));
        tempSubjectInfo.add(new SubjectInfo(1, 2, "교양", 3, 2.0f, false));

        Toast.makeText(this, "semester: " + semesterGPA(tempSubjectInfo, 1, 1), Toast.LENGTH_LONG).show();
        Toast.makeText(this, "major: " + majorGPA(tempSubjectInfo), Toast.LENGTH_LONG).show();
        Toast.makeText(this, "total GPA: " + calculateTotalGPA(tempSubjectInfo), Toast.LENGTH_LONG).show();
        ////////////////////////////////////////////

        //////////// Everytime 시간표 파싱 테스트 //////////////
        Button buttonEverytime = findViewById(R.id.buttonEverytime);
        buttonEverytime.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("에브리타임 시간표 가져오기");
            builder.setMessage("URL 입력\n(https://everytime.kr/@b30NuC8130Bz1mLBaScr)");

            EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            builder.setView(input);

            builder.setPositiveButton(getString(android.R.string.ok), (dialog, which) -> {
                String inputUrl = input.getText().toString();
                String inputIdentifier = inputUrl.replace("https://everytime.kr/@", "");

                EverytimeTimetableParser.getSemesters(inputIdentifier, new EverytimeTimetableParser.OnSemestersParsedListener() {
                    @Override
                    public void onSuccess(List<EverytimeIdentifier> everytimeIdentifiers) {
                        String[] items = new String[everytimeIdentifiers.size()];
                        for (int i = 0; i < everytimeIdentifiers.size(); i++)
                            items[i] = everytimeIdentifiers.get(i).year + "년 " + everytimeIdentifiers.get(i).semester + "학기";

                        AtomicInteger selectedItem = new AtomicInteger();
                        new AlertDialog.Builder(GPACalculatorActivity.this)
                                .setTitle("가져올 시간표 선택")
                                .setSingleChoiceItems(items, 0, (dialog, which)-> selectedItem.set(which))
                                .setPositiveButton(android.R.string.ok,(dialog, which)->{
                                    EverytimeTimetableParser.getSubjects(everytimeIdentifiers.get(selectedItem.get()).identifier, new EverytimeTimetableParser.OnSubjectsParsedListener() {
                                        @Override
                                        public void onSuccess(List<SubjectInfo> subjectInfos) {
                                            updateTableUi(subjectInfos);
                                            Toast.makeText(GPACalculatorActivity.this, "Loaded Subject Data", Toast.LENGTH_LONG).show();
                                        }

                                        @Override
                                        public void onFailed(int errorCode, String errorMessage) {
                                            Toast.makeText(GPACalculatorActivity.this, "Failed: " + errorMessage, Toast.LENGTH_LONG).show();
                                        }
                                    });
                                }).setNegativeButton(android.R.string.cancel,null)
                                .show();
                        //tempSubjectInfo.addAll(subjectInfos);
                        Toast.makeText(GPACalculatorActivity.this, "Loaded Semester Data", Toast.LENGTH_LONG).show();
                        for (EverytimeIdentifier everytimeIdentifier : everytimeIdentifiers) {
                            Log.i("TAG", everytimeIdentifier.year + " " + everytimeIdentifier.semester + " " + everytimeIdentifier.identifier);
                        }
                    }

                    @Override
                    public void onFailed(int errorCode, String errorMessage) {
                        //TODO: 네트워크 오류 발생 시 처리
                    }
                });
            });
            builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());

            builder.show();
        });

        List<SubjectInfo> subjectInfoList = loadGPAData();
        if (subjectInfoList != null) {
            Toast.makeText(this, "saved GPA Data", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "No saved GPA Data", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        saveGPAData(getEverytimeSubjectInfos());
    }

    private float numberToGrade(int gradeNum){
        if (gradeNum == 0) return 4.5f;
        else if (gradeNum == 1) return 4.0f;
        else if (gradeNum == 2) return 3.5f;
        else if (gradeNum == 3) return 3.0f;
        else if (gradeNum == 4) return 2.5f;
        else if (gradeNum == 5) return 2.0f;
        else if (gradeNum == 6) return 1.5f;
        else if (gradeNum == 7) return 1.0f;
        else if (gradeNum == 8) return 0.5f;
        else if (gradeNum == 9) return 0.0f;
        return 0.0f;
    }

    private int gradeToNumber(float grade){
        if(grade == 4.5f) return 0;
        else if(grade == 4.0f) return 1;
        else if(grade == 3.5f) return 2;
        else if(grade == 3.0f) return 3;
        else if(grade == 2.5f) return 4;
        else if(grade == 2.0f) return 5;
        else if(grade == 1.5f) return 6;
        else if(grade == 1.0f) return 7;
        else if(grade == 0.5f) return 8;
        else return 9;
    }

    private void updateTableUi(List<SubjectInfo> subjectInfos) {
        for (int i = 0; i < subjectInfos.size(); i++) {
            if (i >= editTextCredits.size()) //TODO: 임시
                return;
            editTextSubjects.get(i).setText(subjectInfos.get(i).subjectName);
            editTextCredits.get(i).setText(String.valueOf(subjectInfos.get(i).credit));
            spinnerGrades.get(i).setSelection(gradeToNumber(subjectInfos.get(i).grade));
            checkBoxMajors.get(i).setChecked(subjectInfos.get(i).isMajor);
        }
    }

    private void updateGPA() {
        try {
            int year = spinnerYear.getSelectedItemPosition()+1;
            int semester = spinnerSemester.getSelectedItemPosition()+1;
            Log.d("GPACalculatorActivity", "Year: " + year + ", Semester: " + semester);
            float gpa = semesterGPA(tempSubjectInfo, year, semester);
            textViewGPA.setText("GPA for year " + year + " semester " + semester + " is: " + gpa);
            Log.d("GPACalculatorActivity", "GPA updated: " + gpa);
        }
        catch (NumberFormatException e) {
            Log.e("GPACalculatorActivity", "NumberFormatException: " + e.getMessage());
        }
    }

    private float semesterGPA(List<SubjectInfo> subjectInfo, int year, int semester) {
        float res = 0.0f;
        int total_credit = 0;
        for (SubjectInfo s : subjectInfo) {
            if (s.year == year && s.semester == semester) {
                total_credit += s.credit;
            }
        }
        if (total_credit == 0) return 0.0f; // 학점이 없으면 0 반환
        for (SubjectInfo s : subjectInfo) {
            if (s.year == year && s.semester == semester) {
                res += (s.credit * s.grade);
            }
        }
        return res / total_credit;
    }

    private float majorGPA(List<SubjectInfo> subjectInfo) {
        float res = 0.0f;
        int total_majorCredit = 0;
        for (SubjectInfo s : subjectInfo) {
            if (s.isMajor) {
                total_majorCredit += s.credit;
            }
        }
        if (total_majorCredit == 0) return 0.0f; // 학점이 없으면 0 반환
        for (SubjectInfo s : subjectInfo) {
            if (s.isMajor) {
                res += (s.credit * s.grade);
            }
        }
        return res / total_majorCredit;
    }

    // 전체 학기의 총 학점 계산
    private float calculateTotalGPA(List<SubjectInfo> subjectInfo) {
        float totalGPA = 0.0f;
        int total_credits = 0;

        for (int y = 1; y <= 4; y++) {
            for (int sem = 1; sem <= 2; sem++) {
                float GPA = semesterGPA(subjectInfo, y, sem);
                int credits = 0;

                for (SubjectInfo s : subjectInfo) {
                    if (s.year == y && s.semester == sem) {
                        credits += s.credit;
                    }
                }
                totalGPA += GPA * credits;
                total_credits += credits;
            }
        }
        return total_credits != 0 ? totalGPA / total_credits : 0.0f;
    }

    private List<SubjectInfo> getEverytimeSubjectInfos(){
        return tempSubjectInfo;
    }

    // 주어진 리스트를 JSON으로 변환하여 SharedPreferences에 저장
    private void saveGPAData(List<SubjectInfo> subjectInfos) {
        SharedPreferences.Editor editor = preferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(subjectInfos);
        editor.putString(GPA_KEY, json);
        editor.apply();
    }

    // SharedPreferences에서 저장된 리스트를 로드하여 반환
    private List<SubjectInfo> loadGPAData() {
        Gson gson = new Gson();
        String json = preferences.getString(GPA_KEY, null);
        Type type = new TypeToken<List<SubjectInfo>>() {}.getType();
        return gson.fromJson(json, type);
    }
}