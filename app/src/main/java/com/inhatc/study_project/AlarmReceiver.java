package com.inhatc.study_project;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.widget.Toast;

import androidx.core.app.NotificationCompat;

import com.inhatc.study_project.ui.MainActivity;

public class AlarmReceiver extends BroadcastReceiver {
    public AlarmReceiver() { }

    String TAG = "AlarmReceiver";
    NotificationManager manager;
    NotificationCompat.Builder builder;

    // 오레오 이상은 반드시 채널을 설정해줘야 Notification이 작동
    private static String CHANNEL_ID = "channel1";
    private static String CHANNEL_NAME = "Channel1";

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent mServiceIntent = new Intent(context, AlarmService.class);
        context.startService(mServiceIntent);
    }
}
