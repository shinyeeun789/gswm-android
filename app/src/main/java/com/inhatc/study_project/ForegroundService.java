package com.inhatc.study_project;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

public class ForegroundService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        // pendingIntent를 이용하면 포그라운드 서비스 상태에서 알림을 누르면 태스크에 있는 액티비티 띄움
        // PendingIntent는 다른 컴포넌트에게 파라미터로 받은 인텐트를 특정한 시점에 실행하게 하는 인텐트
        Intent i = new Intent(getApplicationContext(), notificationBroadcast.class);
        PendingIntent alarmPendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, i, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder clsBuilder;
        // 오레오 윗버전일 때는 아래와 같이 채널을 만들어 Notification과 연결해야 함
        if(Build.VERSION.SDK_INT >= 26) {
            String CHANNEL_ID = "channel_id";
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, "서비스 앱", NotificationManager.IMPORTANCE_DEFAULT);
            ((NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE)).createNotificationChannel(channel);

            clsBuilder = new NotificationCompat.Builder(this, CHANNEL_ID);
        } else {
            clsBuilder = new NotificationCompat.Builder(this);
        }

        clsBuilder.setSmallIcon(R.drawable.icon_study)
                .setContentTitle("#GSWM").setContentText("공부시간 측정 중입니다:)")
                .setContentIntent(alarmPendingIntent);

        // foreground 서비스로 실행
        startForeground(1, clsBuilder.build());

        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
