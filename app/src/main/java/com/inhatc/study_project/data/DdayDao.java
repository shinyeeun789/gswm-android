package com.inhatc.study_project.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface DdayDao {
    @Query("SELECT * FROM dday WHERE DdayDate=:date ORDER BY DdayDate, DdayID")
    List<Dday> getListViewItem(long date);

    @Query("SELECT DISTINCT DdayDate FROM dday")
    LiveData<List<Long>> getDdayDate();

    @Query("SELECT * FROM dday WHERE DdayDate>:date ORDER BY DdayDate, DdayID LIMIT 1")
    LiveData<Dday> getHomeDdays(long date);

    @Insert
    void insert(Dday dday);
}
