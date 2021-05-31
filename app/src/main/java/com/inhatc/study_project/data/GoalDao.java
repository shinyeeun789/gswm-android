package com.inhatc.study_project.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface GoalDao {
    //LiveData => 테이블에 있는 모든 객체를 계속 관찰하고있다가 변경이 일어나면 자동으로 업데이트
    @Query("SELECT * FROM goal WHERE goalDate=:date ORDER BY goalDate, goalID")
    LiveData<List<Goal>> getListViewItem(long date);

    @Query("SELECT * FROM goal WHERE goalDate=:date ORDER BY goalDate, goalID")
    List<Goal> getListViewItemNoLD(long date);

    @Query("SELECT * FROM goal WHERE goalName=:name AND goalDate=:date")
    Goal selectWithNameDate(String name, long date);

    @Query("SELECT DISTINCT goalDate FROM goal")
    LiveData<List<Long>> getGoalDate();

    @Query("SELECT COUNT(*) FROM goal WHERE goalDate=:date")
    int getGoalCount(long date);

    @Query("SELECT * FROM goal WHERE goalID=:id")
    Goal getGoalInDelay(int id);

    @Query("SELECT COUNT(*) FROM goal WHERE state='O' AND goalDate BETWEEN :startDate AND :endDate")
    int countComplete(long startDate, long endDate);

    @Query("SELECT COUNT(*) FROM goal WHERE goalDate BETWEEN :startDate AND :endDate")
    int countAll(long startDate, long endDate);

    @Query("SELECT * FROM goal WHERE goalDate BETWEEN :startDate AND :endDate")
    List<Goal> getGoalsStudyTime(long startDate, long endDate);

    @Query("SELECT goalName FROM goal WHERE goalDate=:date")
    List<String> getGaolNames(long date);

    @Query("SELECT COUNT(*) FROM goal WHERE state = 'O' AND goalDate BETWEEN :startDate AND :endDate")
    int getOKGoals(long startDate, long endDate);

    @Query("DELETE FROM goal WHERE goalID IN (SELECT goalID FROM goal WHERE delay=1 AND goalName=:name AND goalDate>(SELECT goalDate FROM goal WHERE goalID=:id))")
    int deleteDelayGoalID(String name, int id);

    @Insert
    void insert(Goal goal);

    @Query("UPDATE goal SET goalStudyTime=:time WHERE goalName=:name AND goalDate=:date")
    void updateStudyTime(String time, String name, long date);

    @Query("UPDATE goal SET state=:state, fallQuantity=:fallRange WHERE goalID=:id")
    void updateState(String state, int fallRange, int id);

    @Query("UPDATE goal SET fallQuantity=:fallRange, state='→' WHERE goalID=:id")
    void updateFallQ(int fallRange, int id);
}