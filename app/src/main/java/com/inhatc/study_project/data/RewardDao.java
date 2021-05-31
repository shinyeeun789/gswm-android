package com.inhatc.study_project.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface RewardDao {
    @Query("SELECT * FROM reward")
    LiveData<List<Reward>> getRewardList();

    @Query("SELECT COUNT(*) FROM reward WHERE badge = :badge")
    int getDataCount(String badge);

    @Insert
    void insert(Reward reward);
}