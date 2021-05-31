package com.inhatc.study_project.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface DayStatsDao {
    @Query("SELECT * FROM dayStats WHERE statsDate=:date")
    DayStats getDayStatData(long date);

    @Query("SELECT * FROM dayStats WHERE statsDate=:date")
    LiveData<DayStats> getHomeData(long date);

    @Query("SELECT COUNT(*) FROM dayStats WHERE statsDate=:date")
    int countDayStat(long date);

    @Query("SELECT * FROM dayStats WHERE statsDate BETWEEN :startDate AND :endDate")
    List<DayStats> getStudyTimes(long startDate, long endDate);

    @Query("UPDATE dayStats SET studyTime=:studyTime, concenTime=:concenTime WHERE statsDate=:date")
    void updateTimes(String studyTime, String concenTime, long date);

    @Insert
    void insert(DayStats stats);
}
