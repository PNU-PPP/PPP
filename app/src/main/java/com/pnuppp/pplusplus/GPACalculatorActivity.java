package com.pnuppp.pplusplus;

import android.content.SharedPreferences;
import android.graphics.Canvas;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.util.Log;
import android.util.Pair;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.renderer.XAxisRenderer;
import com.github.mikephil.charting.utils.MPPointF;
import com.github.mikephil.charting.utils.Transformer;
import com.github.mikephil.charting.utils.Utils;
import com.github.mikephil.charting.utils.ViewPortHandler;
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
    private List<SubjectInfo> currentSubjectInfos = new ArrayList<>();

    private List<TableRow> tableRows = new ArrayList<>();

    public static final int EDITTEXT_SUBJECT_ID = View.generateViewId();
    public static final int EDITTEXT_CREDIT_ID = View.generateViewId();
    public static final int SPINNER_GRADE_ID = View.generateViewId();
    public static final int CHECKBOX_MAJOR_ID = View.generateViewId();

    private TableLayout mTableLayout;
    private LineChart mLineChart;

    private Spinner spinnerYear;
    private Spinner spinnerSemester;
    private TextView textViewTotalGPA;
    private TextView textViewMajorGPA;
    private TextView textViewSemesterGPA;

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

        preferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE); // sharedPreference 객체 초기화

        // Spinner 및 TextView 초기화
        spinnerYear = findViewById(R.id.spinnerYear);
        spinnerSemester = findViewById(R.id.spinnerSemester);
        textViewSemesterGPA = findViewById(R.id.textViewSemesterGPA);
        textViewMajorGPA = findViewById(R.id.textViewMajorGPA);
        textViewTotalGPA = findViewById(R.id.textViewTotalGPA);

        //과목 추가
        mTableLayout = findViewById(R.id.tableLayout);

        mLineChart = findViewById(R.id.chart);
        mLineChart.setPinchZoom(false);
        mLineChart.setScaleEnabled(false);
        mLineChart.setExtraBottomOffset(20f);

        mLineChart.setDoubleTapToZoomEnabled(false);
        mLineChart.getAxisRight().setEnabled(false);
        mLineChart.getLegend().setEnabled(false);
        mLineChart.getDescription().setEnabled(false);
        mLineChart.getXAxis().setDrawGridLines(false);
        mLineChart.getXAxis().setGranularity(1f);
        mLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        mLineChart.setXAxisRenderer(new CustomXAxisRenderer(mLineChart.getViewPortHandler(), mLineChart.getXAxis(), mLineChart.getTransformer(YAxis.AxisDependency.LEFT)));

        ImageButton buttonAddRow = findViewById(R.id.buttonAddRow);
        buttonAddRow.setOnClickListener(v -> addNewRow());

        // 공통 이벤트 리스너 설정
        AdapterView.OnItemSelectedListener spinnerListener = new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("GPACalculatorActivity", "Spinner item selected: position=" + position + ", id=" + id);
                updateSemesterGPA();
                updateTableUi();
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
            currentSubjectInfos = savedSubjectInfo;
            updateTableUi();
        }

        //////////// Everytime 시간표 파싱 테스트 //////////////
        Button buttonEverytime = findViewById(R.id.buttonEverytime);
        buttonEverytime.setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("에브리타임 시간표 가져오기");
            builder.setMessage("시간표 URL 입력\n[에브라타임->시간표->URL 공유]\n(https://everytime.kr/@_)");

            FrameLayout container = new FrameLayout(GPACalculatorActivity.this);
            FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.leftMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);
            params.rightMargin = getResources().getDimensionPixelSize(R.dimen.dialog_margin);

            EditText input = new EditText(this);
            input.setInputType(InputType.TYPE_CLASS_TEXT);
            input.setLayoutParams(params);
            container.addView(input);
            builder.setView(container);

            builder.setPositiveButton(getString(android.R.string.ok), (dialog, which) -> {
                String inputUrl = input.getText().toString();
                if (!inputUrl.matches("https://everytime.kr/@[a-zA-Z0-9]+")) {
                    Toast.makeText(GPACalculatorActivity.this, "URL이 바르지 않습니다.", Toast.LENGTH_LONG).show();
                    return;
                }
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
                                .setSingleChoiceItems(items, 0, (dialog, which) -> selectedItem.set(which))
                                .setPositiveButton(android.R.string.ok, (dialog, which) ->
                                        EverytimeTimetableParser.getSubjects(everytimeIdentifiers.get(selectedItem.get()).identifier, new EverytimeTimetableParser.OnSubjectsParsedListener() {
                                            @Override
                                            public void onSuccess(List<SubjectInfo> subjectInfos) {
                                                for (SubjectInfo subjectInfo : subjectInfos) {
                                                    subjectInfo.year = spinnerYear.getSelectedItemPosition() + 1;
                                                    subjectInfo.semester = spinnerSemester.getSelectedItemPosition() + 1;
                                                }
                                                replaceSemesterSubjectInfos(subjectInfos);
                                                updateTableUi();
                                            }

                                            @Override
                                            public void onFailed(int errorCode, String errorMessage) {
                                                Toast.makeText(GPACalculatorActivity.this, "네트워크 오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
                                            }
                                        })).setNegativeButton(android.R.string.cancel, null)
                                .show();
                    }

                    @Override
                    public void onFailed(int errorCode, String errorMessage) {
                        Toast.makeText(GPACalculatorActivity.this, "네트워크 오류가 발생하였습니다.", Toast.LENGTH_LONG).show();
                    }
                });
            });
            builder.setNegativeButton(getString(android.R.string.cancel), (dialog, which) -> dialog.cancel());
            builder.show();
        });

        updateTotalGPA();
        updateMajorGPA();
        updateChart();
    }

    private void updateChart() {
        ArrayList<Pair<String, Entry>> list = new ArrayList<>();
        list.add(new Pair<>("1학년\n1학기", new Entry(0, semesterGPA(currentSubjectInfos, 1, 1))));
        list.add(new Pair<>("1학년\n2학기", new Entry(1, semesterGPA(currentSubjectInfos, 1, 2))));
        list.add(new Pair<>("2학년\n1학기", new Entry(2, semesterGPA(currentSubjectInfos, 2, 1))));
        list.add(new Pair<>("2학년\n2학기", new Entry(3, semesterGPA(currentSubjectInfos, 2, 2))));
        list.add(new Pair<>("3학년\n1학기", new Entry(4, semesterGPA(currentSubjectInfos, 3, 1))));
        list.add(new Pair<>("3학년\n2학기", new Entry(5, semesterGPA(currentSubjectInfos, 3, 2))));
        list.add(new Pair<>("4학년\n1학기", new Entry(6, semesterGPA(currentSubjectInfos, 4, 1))));
        list.add(new Pair<>("4학년\n2학기", new Entry(7, semesterGPA(currentSubjectInfos, 4, 2))));
        list.removeIf(pair -> pair.second.getY() == 0);
        for (int i = 0; i < list.size(); i++)
            list.get(i).second.setX(i);

        List<String> xVals = new ArrayList<>();
        for (Pair<String, Entry> pair : list)
            xVals.add(pair.first);

        List<Entry> entries = new ArrayList<>();
        for (Pair<String, Entry> pair : list)
            entries.add(pair.second);

        float max = 0;
        float min = 4.5f;
        for (Entry entry : entries) {
            if (entry.getY() > max) max = entry.getY();
            if (entry.getY() < min) min = entry.getY();
        }
        mLineChart.getAxisLeft().setAxisMinimum(min < 2f ? 0f : 2f);
        mLineChart.getAxisLeft().setAxisMaximum(max > 4f ? 4.5f : 4f);

        LineDataSet lineDataSet = new LineDataSet(entries, "학기별 평균 학점");
        lineDataSet.setColor(getColor(R.color.colorAccent));
        lineDataSet.setCircleColor(getColor(R.color.colorAccent));
        lineDataSet.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return String.format("%.2f", value);
            }
        });
        lineDataSet.setLineWidth(2f);
        lineDataSet.setCircleRadius(5f);

        LineData lineData = new LineData();
        lineData.addDataSet(lineDataSet);
        mLineChart.setData(lineData);

        mLineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xVals));
        mLineChart.invalidate();
    }

    private void addNewRow() {
        TableRow tableRow = new TableRow(this);

        ImageButton imageButton = new ImageButton(this);
        imageButton.setImageResource(R.drawable.baseline_remove_24);
        TypedValue typedValue = new TypedValue();
        getTheme().resolveAttribute(android.R.attr.selectableItemBackground, typedValue, true);
        imageButton.setBackgroundResource(typedValue.resourceId);

        tableRow.addView(imageButton);

        imageButton.setOnClickListener(v -> {
            if (tableRows.size() <= 1) return;
            mTableLayout.removeView(tableRow);
            tableRows.remove(tableRow);
        });

        EditText editText = new EditText(this);
        editText.setId(EDITTEXT_SUBJECT_ID);
        editText.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT, 1.0f));
        editText.setSingleLine(true);
        tableRow.addView(editText);

        EditText editText2 = new EditText(this);
        editText2.setId(EDITTEXT_CREDIT_ID);
        editText2.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.MATCH_PARENT));
        editText2.setEms(2);
        editText2.setGravity(Gravity.CENTER_HORIZONTAL);
        editText2.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
        editText2.setFilters(new InputFilter[]{new InputFilter.LengthFilter(3)});
        editText2.setOnFocusChangeListener((v, hasFocus) -> {
            if (!hasFocus) {
                replaceSemesterSubjectInfos(getCurrentSubjectInfos());
                updateSemesterGPA();
                updateMajorGPA();
                updateTotalGPA();
                updateChart();
            }
        });
        tableRow.addView(editText2);


        ArrayAdapter<CharSequence> spinnerArrayAdapter = ArrayAdapter.createFromResource(this, R.array.gradeSpinnerItems, android.R.layout.simple_spinner_dropdown_item);
        Spinner spinnerGrade = new Spinner(this);
        spinnerGrade.setId(SPINNER_GRADE_ID);
        spinnerGrade.setAdapter(spinnerArrayAdapter);
        spinnerGrade.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                replaceSemesterSubjectInfos(getCurrentSubjectInfos());
                updateSemesterGPA();
                updateMajorGPA();
                updateTotalGPA();
                updateChart();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        tableRow.addView(spinnerGrade);

        CheckBox checkBoxMajor = new CheckBox(this);
        checkBoxMajor.setId(CHECKBOX_MAJOR_ID);
        checkBoxMajor.setChecked(true);
        checkBoxMajor.setOnCheckedChangeListener((buttonView, isChecked) -> {
            replaceSemesterSubjectInfos(getCurrentSubjectInfos());
            updateSemesterGPA();
            updateMajorGPA();
            updateTotalGPA();
            updateChart();
        });
        tableRow.addView(checkBoxMajor);

        tableRows.add(tableRow);
        mTableLayout.addView(tableRow);
    }

    @Override
    protected void onPause() {
        super.onPause();
        replaceSemesterSubjectInfos(getCurrentSubjectInfos());
        saveGPAData(currentSubjectInfos);
    }

    private float numberToGrade(int gradeNum) {
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

    private int gradeToNumber(float grade) {
        if (grade == 4.5f) return 0;
        else if (grade == 4.0f) return 1;
        else if (grade == 3.5f) return 2;
        else if (grade == 3.0f) return 3;
        else if (grade == 2.5f) return 4;
        else if (grade == 2.0f) return 5;
        else if (grade == 1.5f) return 6;
        else if (grade == 1.0f) return 7;
        else if (grade == 0.5f) return 8;
        else return 9;
    }

    private void updateTableUi() {
        for (TableRow tableRow : tableRows)
            mTableLayout.removeView(tableRow);
        tableRows.clear();

        int count = 0;
        for (int i = 0; i < currentSubjectInfos.size(); i++) {
            if (currentSubjectInfos.get(i).year == spinnerYear.getSelectedItemPosition() + 1 && currentSubjectInfos.get(i).semester == spinnerSemester.getSelectedItemPosition() + 1) {
                if (count >= tableRows.size())
                    addNewRow();

                EditText etSubject = tableRows.get(count).findViewById(EDITTEXT_SUBJECT_ID);
                EditText etCredit = tableRows.get(count).findViewById(EDITTEXT_CREDIT_ID);
                Spinner spinnerGrade = tableRows.get(count).findViewById(SPINNER_GRADE_ID);
                CheckBox checkBoxMajor = tableRows.get(count).findViewById(CHECKBOX_MAJOR_ID);

                etSubject.setText(currentSubjectInfos.get(i).subjectName);
                etCredit.setText(String.valueOf(currentSubjectInfos.get(i).credit));
                spinnerGrade.setSelection(gradeToNumber(currentSubjectInfos.get(i).grade));
                checkBoxMajor.setChecked(currentSubjectInfos.get(i).isMajor);
                count++;
                Log.i("TAG", "updateTableUi: ADD!!");
            }
        }

        if (tableRows.size() > count) {
            for (int i = tableRows.size() - 1; i >= count; i--) {
                mTableLayout.removeView(tableRows.get(i));
            }
        }
        if (tableRows.size() < 5) {
            for (int i = count; i < 5; i++) {
                addNewRow();
                Log.i("TAG", "updateTableUi: ADD!");
            }
        }
        Log.i("TAG", "updateTableUi: !");
        updateSemesterGPA();
    }

    private void updateTotalGPA() {
        float gpa = totalGPA(currentSubjectInfos);
        textViewTotalGPA.setText(String.format("%.2f", gpa));
    }

    private void updateMajorGPA() {
        float gpa = majorGPA(currentSubjectInfos);
        textViewMajorGPA.setText(String.format("%.2f", gpa));
    }

    private void updateSemesterGPA() {
        int year = spinnerYear.getSelectedItemPosition() + 1;
        int semester = spinnerSemester.getSelectedItemPosition() + 1;
        float gpa = semesterGPA(currentSubjectInfos, year, semester);
        textViewSemesterGPA.setText(String.format("%.2f", gpa));
    }

    private float semesterGPA(List<SubjectInfo> subjectInfo, int year, int semester) {
        float res = 0.0f;
        float total_credit = 0;
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
        float total_majorCredit = 0;
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
    private float totalGPA(List<SubjectInfo> subjectInfo) {
        float totalGPA = 0.0f;
        float total_credits = 0;

        for (int y = 1; y <= 4; y++) {
            for (int sem = 1; sem <= 2; sem++) {
                float GPA = semesterGPA(subjectInfo, y, sem);
                float credits = 0;

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

    private List<SubjectInfo> getCurrentSubjectInfos() {
        List<SubjectInfo> newSubjectInfos = new ArrayList<>();
        for (int i = 0; i < tableRows.size(); i++) {
            EditText editTextSubject = tableRows.get(i).findViewById(EDITTEXT_SUBJECT_ID);
            String subjectName = editTextSubject.getText().toString();
            if (subjectName.isEmpty()) continue;

            EditText editTextCredit = tableRows.get(i).findViewById(EDITTEXT_CREDIT_ID);
            String strCredit = editTextCredit.getText().toString();
            if (strCredit.isEmpty()) continue;
            float credit = Float.parseFloat(strCredit);

            Spinner spinnerGrade = tableRows.get(i).findViewById(SPINNER_GRADE_ID);
            float grade = numberToGrade(spinnerGrade.getSelectedItemPosition());

            CheckBox checkBoxMajor = tableRows.get(i).findViewById(CHECKBOX_MAJOR_ID);
            boolean isMajor = checkBoxMajor.isChecked();
            newSubjectInfos.add(new SubjectInfo(spinnerYear.getSelectedItemPosition() + 1, spinnerSemester.getSelectedItemPosition() + 1, subjectName, credit, grade, isMajor));
        }
        return newSubjectInfos;
    }

    private void replaceSemesterSubjectInfos(List<SubjectInfo> subjectInfos) {
        for (int i = 0; i < currentSubjectInfos.size(); i++) {
            if (currentSubjectInfos.get(i).year == spinnerYear.getSelectedItemPosition() + 1 &&
                    currentSubjectInfos.get(i).semester == spinnerSemester.getSelectedItemPosition() + 1) {
                currentSubjectInfos.remove(i);
                i--;
            }
        }
        currentSubjectInfos.addAll(subjectInfos);
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
        Type type = new TypeToken<List<SubjectInfo>>() {
        }.getType();
        return gson.fromJson(json, type);
    }
}

class CustomXAxisRenderer extends XAxisRenderer {
    public CustomXAxisRenderer(ViewPortHandler viewPortHandler, XAxis xAxis, Transformer trans) {
        super(viewPortHandler, xAxis, trans);
    }

    @Override
    protected void drawLabel(Canvas c, String formattedLabel, float x, float y, MPPointF anchor, float angleDegrees) {
        String line[] = formattedLabel.split("\n");
        Utils.drawXAxisValue(c, line[0], x, y, mAxisLabelPaint, anchor, angleDegrees);
        if (line.length > 1)
            Utils.drawXAxisValue(c, line[1], x, y + mAxisLabelPaint.getTextSize(), mAxisLabelPaint, anchor, angleDegrees);
    }
}