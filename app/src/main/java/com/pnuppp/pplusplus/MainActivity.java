package com.pnuppp.pplusplus;

import android.Manifest;
import android.os.Build;
import android.provider.Settings;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;


import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.Calendar;

import androidx.appcompat.app.AlertDialog;

import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;

// import com.kakao.vectormap.KakaoMap;
// import com.kakao.vectormap.KakaoMapReadyCallback;
// import com.kakao.vectormap.MapLifeCycleCallback;
// import com.kakao.vectormap.MapView;

public class MainActivity extends AppCompatActivity {
    private static final int REQUEST_CODE_POST_NOTIFICATIONS = 1;
    private static final int REQUEST_CODE_SCHEDULE_EXACT_ALARM = 2;
    private MaterialCalendarView calendarView;
    private DataBase db;
    private EventDao eventDao;
    private Map<String, String> events = new HashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        calendarView = findViewById(R.id.calendarView);
        db = Room.databaseBuilder(getApplicationContext(), DataBase.class, "event-database")
                .allowMainThreadQueries()
                .fallbackToDestructiveMigration()
                .build();
        eventDao = db.eventDao();

        initializeDatabase();

        // POST_NOTIFICATIONS 권한 확인
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.POST_NOTIFICATIONS}, REQUEST_CODE_POST_NOTIFICATIONS);
        }
        else { // 권한이 이미 부여된 경우
            loadEvents();
        }
        // 정확한 알람 권한 요청 (API 31 이상에서만 가능)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (!alarmManager.canScheduleExactAlarms()) {
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                startActivityForResult(intent, REQUEST_CODE_SCHEDULE_EXACT_ALARM);
            }
        }

        calendarView.setOnDateChangedListener((widget, date, selected) -> {
            String selectedDate = formatDateForMap(date);
            String eventDescription = events.get(selectedDate);
            showEventDialog(selectedDate, eventDescription);
        });

        Button buttonnewnew = findViewById(R.id.button5);
        buttonnewnew.setOnClickListener(v -> {
            Intent newIntent = new Intent(MainActivity.this, newnewActivity.class);
            startActivity(newIntent);
        });

        Button buttonMyGPA = findViewById(R.id.button3);
        buttonMyGPA.setOnClickListener(v -> {
            Intent myIntent = new Intent(MainActivity.this, GPACalculatorActivity.class);
            startActivity(myIntent);
        });

        
        Button noticeButton = findViewById(R.id.noticeButton);
        noticeButton.setOnClickListener(v->{
            Intent intent = new Intent(MainActivity.this, RSSDepartmentNoticeActivity.class);
            startActivity(intent);
        });

        TextView textView8 = findViewById(R.id.textView8);
        TextView textView9 = findViewById(R.id.textView9);

        Intent intent = getIntent();
        String major = intent.getStringExtra("major");
        String studentID = intent.getStringExtra("student_id");
        String name = intent.getStringExtra("name");

        if (major != null && studentID != null && name != null) {
            textView8.setText(major);
            textView9.setText(studentID + " " + name);
        } else {
            SharedPreferences sharedPref = getSharedPreferences("com.pnuppp.pplusplus.user_info", MODE_PRIVATE);
            if (sharedPref.contains("major") && sharedPref.contains("student_id") && sharedPref.contains("name")) {
                String savedMajor = sharedPref.getString("major", "");
                String savedStudentID = sharedPref.getString("student_id", "");
                String savedName = sharedPref.getString("name", "");

                textView8.setText(savedMajor);
                textView9.setText(savedStudentID + " " + savedName);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CODE_POST_NOTIFICATIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadEvents();
            }
            else {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("권한 거부됨")
                        .setMessage("이 앱이 정상적으로 작동하려면 알림 권한이 필요합니다.")
                        .setPositiveButton("OK", (dialog, which) -> dialog.dismiss())
                        .show();
            }
        }
    }

    private void initializeDatabase() {
        String[][] eventData = {
                {"2024-03-04", "1학기 개강\n1학기 1차 수강 정정 시작\n(학부, 타대생, 대학원 08:00~)"},
                {"2024-03-05", "1학기 휴·복학 신청 기간 시작"},
                {"2024-03-07", "1학기 휴·복학 신청 기간 끝"},
                {"2024-03-08", "1학기 1차 수강 정정 마감\n(학부, 타대생 ~17:00)\n(대학원 ~23:50)"},
                {"2024-03-11", "1학기 후기 학위 청구 자격 종합 시험 시작"},
                {"2024-03-15", "1학기 2차 폐강 강좌 공고\n1학기 후기 학위 청구 자격 종합 시험 끝"},
                {"2024-03-18", "1학기 2차 수강 정정 시작\n(학부, 타대생, 대학원 10:00~)"},
                {"2024-03-19", "1학기 2차 수강 정정 마감\n(학부, 타대생, 대학원 ~17:00)\n1학기 휴·복학 신청 시작\n1학기 수료후 연구생 등록금 납부 시작\n1학기 재학생 차등 납부 등록 시작"},
                {"2024-03-20", "1학기 확정 출석부 출력"},
                {"2024-03-21", "1학기 휴·복학 신청 마감\n1학기 수료후 연구생 등록금 납부 마감\n1학기 재학생 차등 납부 등록 마감"},

                {"2024-04-02", "1학기 수강 취소 시작"},
                {"2024-04-04", "1학기 후기 학위 청구 심사용 논문 제출"},
                {"2024-04-08", "1학기 수강 취소 마감\n1학기 수업일수 1/3선"},
                {"2024-04-22", "1학기 중간고사 시작"},
                {"2024-04-26", "1학기 수업일수 1/2선"},
                {"2024-04-27", "1학기 중간고사 끝"},

                {"2024-05-01", "여름 계절 및 도약 수업 복학 신청 시작"},
                {"2024-05-07", "여름 계절 및 도약 수업 복학 신청 마감"},
                {"2024-05-09", "여름 계절 수업 희망 과목 담기 시작"},
                {"2024-05-10", "여름 계절 수업 희망 과목 담기 마감"},
                {"2024-05-11", "여름 계절 수업 자동 신청 결과 확인"},
                {"2024-05-16", "1학기 수업일수 2/3선\n여름 계절 및 도약 수업 복학 신청 시작\n여름 계절 수업 수강 신청 시작(학부, 타대생, 대학원 08:00~)"},
                {"2024-05-20", "여름 계절 및 도약 수업 복학 신청 마감\n여름 계절 수업 수강 신청 마감(학부, 타대생, 대학원 ~17:00)"},
                {"2024-05-24", "여름 계절 수업 1차 폐강 강좌 공고"},
                {"2024-05-27", "2학기 재입학 신청 시작(학부)\n여름 계절 수업 1차 수강 정정 시작(학부, 타대생, 대학원 10:00~)\n여름 계절 및 도약 수업 복학 신청 시작"},
                {"2024-05-28", "여름 계절 수업 1차 수강 정정 마감(학부, 타대생, 대학원 ~17:00)\n여름 계절 및 도약 수업 복학 신청 마감"},

                {"2024-06-03", "여름 계절 수업 2차 폐강 강좌 공고"},
                {"2024-06-04", "여름 계절 수업 2차 수강 정정 시작(학부, 타대생, 대학원 10:00~)\n여름 계절 및 도약 수업 복학 신청 기간"},
                {"2024-06-05", "여름 계절 수업 2차 수강 정정 마감(학부, 타대생, 대학원 ~17:00)\n여름 계절 및 도약 수업 복학 신청 기간"},
                {"2024-06-07", "2학기 재입학 신청 마감(학부)"},
                {"2024-06-13", "여름 계절 수업 등록금 납부 시작"},
                {"2024-06-15", "1학기 기말고사 시작"},
                {"2024-06-17", "여름 계절 수업 등록금 납부 마감"},
                {"2024-06-21", "1학기 종강\n1학기 기말고사 끝"},
                {"2024-06-24", "여름 계절 수업 개강"},

                {"2024-07-01", "1학기 성적 입력 마감"},
                {"2024-07-05", "1학기 후위 학위 논문 심사 결과 보고서 및 최종 논문 제출"},
                {"2024-07-15", "2학기 교수 계획표 입력 시작"},
                {"2024-07-19", "여름 계절 수업 종강"},
                {"2024-07-26", "2학기 교수 계획표 입력 마감\n2학기 휴·복학 신청 기간 시작"},

                {"2024-08-02", "2학기 휴·복학 신청 기간 마감"},
                {"2024-08-06", "2학기 희망 과목 담기 시작"},
                {"2024-08-07", "2학기 희망 과목 담기 마감"},
                {"2024-08-08", "2학기 자동 신청 결과 확인"},
                {"2024-08-12", "2학기 1차 수강 신청 시작\n(학부, 타대생, 대학원 08:00~)"},
                {"2024-08-14", "2학기 1차 수강 신청 마감\n(학부, 타대생, 대학원 ~17:00)"},
                {"2024-08-19", "2학기 2차 수강 신청 시작\n(학부, 타대생, 대학원 10:00~)"},
                {"2024-08-20", "2학기 2차 수강 신청 마감\n(학부, 타대생, 대학원 ~17:00)\n2학기 등록금 납부 시작"},
                {"2024-08-23", "2학기 등록금 납부 마감"},
                {"2024-08-27", "2학기 1차 폐강 강좌 공고"},
                {"2024-08-30", "1학기 후기 학위 수여식"},

                {"2024-09-02", "2학기 개강\n2학기 1차 수강 정정 시작\n(학부, 타대생, 대학원 08:00~)"},
                {"2024-09-06", "2학기 1차 수강 정정 마감\n(학부, 타대생 ~17:00)\n(대학원 ~23:50)"},
                {"2024-09-13", "2학기 2차 폐강 강좌 공고"},
                {"2024-09-19", "2학기 2차 수강 정정 시작\n(학부, 타대생, 대학원 10:00~)"},
                {"2024-09-20", "2학기 2차 수강 정정 마감\n(학부, 타대생, 대학원 ~17:00)"},
                {"2024-09-23", "2학기 확정 출석부 출력\n2학기 전기 학위 청구 자격 종합 시험 시작"},
                {"2024-09-27", "2학기 전기 학위 청구 자격 종합 시험 끝"},

                {"2024-10-04", "2학기 수강 취소 기간 시작"},
                {"2024-10-11", "2학기 수강 취소 기간 끝\n2학기 수업일수 1/3선\n2학기 전기 학위 청구 심사용 논문 제출"},
                {"2024-10-21", "2학기 중간고사 시작"},
                {"2024-10-26", "2학기 중간고사 끝"},
                {"2024-10-31", "2학기 수업일수 1/2선"},

                {"2024-11-11", "겨울 계절 수업 희망 과목 담기 시작"},
                {"2024-11-12", "겨울 계절 수업 희망 과목 담기 마감"},
                {"2024-11-13", "겨울 계절 수업 자동 신청 결과 확인"},
                {"2024-11-15", "겨울 계절 수업 수강 신청 시작\n(학부, 타대생, 대학원)\n2학기 수업일수 2/3선"},
                {"2024-11-19", "겨울 계절 수업 수강 신청 마감\n(학부, 타대생, 대학원)"},
                {"2024-11-25", "겨울 계절 수업 1차 폐강 강좌 공고"},
                {"2024-11-26", "겨울 계절 수업 1차 수강 정정 시작\n(학부, 타대생, 대학원 10:00~)"},
                {"2024-11-27", "겨울 계절 수업 1차 수강 정정 마감\n(학부, 타대생, 대학원 ~17:00)"},

                {"2024-12-03", "겨울 계절 수업 2차 폐강 강좌 공고"},
                {"2024-12-04", "겨울 계절 수업 2차 수강 정정 시작\n(학부, 타대생, 대학원 10:00~)"},
                {"2024-12-05", "겨울 계절 수업 2차 수강 정정 마감\n(학부, 타대생, 대학원 ~17:00)"},
                {"2024-12-12", "겨울 계절 수업 등록금 납부 시작"},
                {"2024-12-14", "2학기 기말고사 시작"},
                {"2024-12-16", "겨울 계절 수업 등록금 납부 마감"},
                {"2024-12-20", "2학기 기말고사 끝\n2학기 종강"},
                {"2024-12-21", "겨울 계절 개강"},

                {"2025-01-17", "겨울 계절 종강"},
                {"2025-01-24", "1학기 휴·복학 신청 시작"},

                {"2025-02-04", "1학기 휴·복학 신청 마감"},
                {"2025-02-06", "1학기 희망 과목 담기 시작"},
                {"2025-02-07", "1학기 희망 과목 담기 마감"},
                {"2025-02-12", "1학기 1차 수강 신청 시작\n(학부, 타대생, 대학원 08:00~)"},
                {"2025-02-14", "1학기 1차 수강 신청 마감\n(학부, 타대생 ~17:00)\n(대학원 ~23:50)"},
                {"2025-02-17", "1학기 1차 수강 신청 시작(신입생)"},
                {"2025-02-18", "1학기 등록금 납부 시작\n1학기 1차 수강 신청 마감(신입생)"},
                {"2025-02-19", "1학기 2차 수강 신청 시작\n(학부, 타대생, 대학원 10:00~)"},
                {"2025-02-20", "1학기 2차 수강 신청 마감\n(학부, 타대생, 대학원 ~17:00)"},
                {"2025-02-21", "1학기 등록금 납부 마감"},
                {"2025-02-27", "1학기 1차 폐강 강좌 공고"},
                {"2025-02-28", "1학기 전기 학위 수여식"}
        };

        for (String[] data : eventData) {
            Event event = new Event();
            event.setDate(data[0]);
            event.setDescription(data[1]);
            eventDao.insert(event);
        }
    }

    private void loadEvents() {
        List<Event> eventList = eventDao.getAllEvents();
        Set<CalendarDay> eventDays = new HashSet<>();
        for (Event event : eventList) {
            String date = event.getDate();
            CalendarDay day = parseDateToCalendarDay(date);
            eventDays.add(day);
            events.put(date, event.getDescription());

            if (event.getDescription() != null && !event.getDescription().isEmpty() && isTodayDate(day)) {
                scheduleDailyNotification(day, event.getDescription());
            }
        }

        calendarView.addDecorator(new EventDecorator(Color.BLUE, eventDays));
        calendarView.addDecorator(new TodayDecorator());
    }

    private boolean isTodayDate(CalendarDay day) {
        Calendar today = Calendar.getInstance();
        Calendar eventDate = Calendar.getInstance();
        eventDate.set(day.getYear(), day.getMonth()-1, day.getDay());
        return !eventDate.before(today);
    }

    private void scheduleDailyNotification(CalendarDay day, String eventDescription) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(day.getYear(), day.getMonth() - 1, day.getDay(), 9, 0, 0); // 알림을 보낼 시간 설정 (예: 오전 9시)

        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("eventDescription", eventDescription);
        intent.putExtra("eventDate", day.getYear() + "-" + String.format("%02d", day.getMonth()) + "-" + String.format("%02d", day.getDay()));

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, day.hashCode(), intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }

    private CalendarDay parseDateToCalendarDay(String date) {
        String[] dateParts = date.split("-");
        int year = Integer.parseInt(dateParts[0]);
        int month = Integer.parseInt(dateParts[1]);
        int day = Integer.parseInt(dateParts[2]);
        return CalendarDay.from(year, month, day);
    }

    private String formatDateForMap(CalendarDay date) {
        return date.getYear() + "-" + String.format("%02d", date.getMonth()) + "-" + String.format("%02d", date.getDay());
    }

    private void showEventDialog(String date, String eventDescription) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(date);
        builder.setMessage(eventDescription != null && !eventDescription.isEmpty() ? eventDescription : "일정이 존재하지 않습니다.");
        builder.setPositiveButton("OK", (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}

