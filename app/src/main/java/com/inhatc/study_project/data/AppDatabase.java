package com.inhatc.study_project.data;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

@Database(entities = {Goal.class, Dday.class, DayStats.class, Reward.class}, version = 1, exportSchema = false)
@TypeConverters({RoomTypeConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    // 데이터베이스를 매번 생성하는 건 리소스를 많이 사용하므로 싱글톤이 권장됨
    private static AppDatabase INSTANCE;

    // 데이터베이스와 연결되는 DAO
    // DAO는 abstract로 "getter"를 제공
    public abstract GoalDao goalDao();
    public abstract DdayDao ddayDao();
    public abstract DayStatsDao statsDao();
    public abstract RewardDao rewardDao();

    // 디비 객체 생성 가져오기
    public static AppDatabase getAppDatabase(Context context) {
        if(INSTANCE == null) {
            synchronized(AppDatabase.class) {
                if(INSTANCE == null)
                    INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "goal-db").build();
            }
        }
        return INSTANCE;
    }

    // 디비 객체 제거
    public static void destroyInstance() {
        INSTANCE = null;
    }
}
