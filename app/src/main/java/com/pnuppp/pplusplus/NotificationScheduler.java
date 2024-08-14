package com.pnuppp.pplusplus;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;

import java.util.Calendar;

import com.prolificinteractive.materialcalendarview.CalendarDay;

public class NotificationScheduler {
    public static void schedulerEventNotification(Context context, CalendarDay date, String eventDescription) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, date.getYear());
        calendar.set(Calendar.MONTH, date.getMonth()); // Calendar.MONTH는 0부터 시작
        calendar.set(Calendar.DAY_OF_MONTH, date.getDay());
        calendar.set(Calendar.HOUR_OF_DAY, 8); // 원하는 시간 설정
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);

        Intent intent = new Intent(context, AlarmReceiver.class);
        intent.putExtra("eventDate", formatDateForMap(date));
        intent.putExtra("eventDescription", eventDescription);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, date.hashCode(), intent, PendingIntent.FLAG_IMMUTABLE);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        if (alarmManager != null) {
            alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        }
    }
    private static String formatDateForMap(CalendarDay date) {
        return date.getYear() + "-" + String.format("%02d", date.getMonth() + 1) + "-" + String.format("%02d", date.getDay());
    }
}
