package com.inhatc.study_project.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "reward")
public class Reward {
    @NonNull
    @PrimaryKey
    private String badge;
    private String rewardName;

    public String getBadge() {
        return badge;
    }
    public void setBadge(String badge) {
        this.badge = badge;
    }
    public String getRewardName() {
        return rewardName;
    }
    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }
}
