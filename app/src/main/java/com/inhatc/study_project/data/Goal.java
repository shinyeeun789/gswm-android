package com.inhatc.study_project.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "goal")
public class Goal {
    // autoGenerate는 알아서 id를 1씩 증가시켜줌
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int goalID;
    private Date goalDate;
    private String goalTime;
    private String goalName;
    private int quantity;
    private String goalStudyTime = "00:00:00";
    private String state = "X";
    private int fallQuantity = 0;
    private String rangeValue;
    private boolean delay = false;

    public int getGoalID() {
        return goalID;
    }
    public Date getGoalDate() {
        return goalDate;
    }
    public String getGoalTime() {
        return goalTime;
    }
    public String getGoalName() {
        return goalName;
    }
    public int getQuantity() {
        return quantity;
    }
    public String getGoalStudyTime() {
        return goalStudyTime;
    }
    public String getState() {
        return state;
    }
    public int getFallQuantity() {
        return fallQuantity;
    }
    public String getRangeValue() {
        return rangeValue;
    }
    public boolean isDelay() {
        return delay;
    }
    public void setGoalID(int goalID) {
        this.goalID = goalID;
    }
    public void setGoalDate(Date goalDate) {
        this.goalDate = goalDate;
    }
    public void setGoalTime(String goalTime) {
        this.goalTime = goalTime;
    }
    public void setGoalName(String goalName) {
        this.goalName = goalName;
    }
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    public void setGoalStudyTime(String goalStudyTime) {
        this.goalStudyTime = goalStudyTime;
    }
    public void setState(String state) {
        this.state = state;
    }
    public void setFallQuantity(int fallQuantity) {
        this.fallQuantity = fallQuantity;
    }
    public void setRangeValue(String rangeValue) {
        this.rangeValue = rangeValue;
    }
    public void setDelay(boolean delay) {
        this.delay = delay;
    }

    @NonNull
    @Override
    public String toString() {
        return getGoalID() + " " + getGoalName();
    }
}