package com.inhatc.study_project;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.inhatc.study_project.ui.MainActivity;

public class AlarmService extends Service {
    private int REQUEST_CODE = 1;
    public AlarmService() {}

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Intent mMainIntent = new Intent(this, MainActivity.class);
        PendingIntent mPendingIntent = PendingIntent.getActivity(this, REQUEST_CODE, mMainIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this).setSmallIcon(R.drawable.app_icon)
                .setContentTitle("contentTitle")
                .setContentIntent(mPendingIntent)
                .setContentText("contentText")
                .setDefaults(Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE).setAutoCancel(true);

        NotificationManager mNotifyMgr = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        Notification noti = mBuilder.build();
        noti.flags = Notification.FLAG_AUTO_CANCEL;
        mNotifyMgr.notify(Integer.valueOf("20210526110000"), noti);

        return START_NOT_STICKY;
    }
}
