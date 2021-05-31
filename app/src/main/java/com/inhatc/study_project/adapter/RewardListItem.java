package com.inhatc.study_project.adapter;

public class RewardListItem {
    private String rewardCount;
    private String rewardName;

    public RewardListItem(String rewardCount, String rewardName) {
        this.rewardCount = rewardCount;
        this.rewardName = rewardName;
    }

    public String getRewardCount() {
        return rewardCount;
    }
    public void setRewardCount(String rewardCount) {
        this.rewardCount = rewardCount;
    }
    public String getRewardName() {
        return rewardName;
    }
    public void setRewardName(String rewardName) {
        this.rewardName = rewardName;
    }
}
