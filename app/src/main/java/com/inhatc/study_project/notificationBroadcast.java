package com.inhatc.study_project;

import android.app.ActivityManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import java.util.List;

public class notificationBroadcast extends BroadcastReceiver {
    // BroadcastReceiver: 어떤 행위가 왔다는 알림을 받고 방송을 해주는 기능
    @Override
    public void onReceive(Context context, Intent intent) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> tasks = am.getRunningTasks(Integer.MAX_VALUE);
        if(!tasks.isEmpty()) {
            int taskSize = tasks.size();
            for(int i=0; i<taskSize; i++) {
                ActivityManager.RunningTaskInfo taskinfo = tasks.get(i);
                if(taskinfo.topActivity.getPackageName().equals(context.getApplicationContext().getPackageName())) {
                    am.moveTaskToFront(taskinfo.id, 0);
                }
            }
        }
    }
}
