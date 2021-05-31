package com.inhatc.study_project.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "dday")
public class Dday {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int DdayID;
    private Date DdayDate;
    private String DdayName;

    public int getDdayID() { return DdayID; }
    public void setDdayID(int ddayID) { DdayID = ddayID; }
    public Date getDdayDate() { return DdayDate; }
    public void setDdayDate(Date ddayDate) { DdayDate = ddayDate; }
    public String getDdayName() { return DdayName; }
    public void setDdayName(String ddayName) { DdayName = ddayName; }

    @Override
    public String toString() {
        return "Dday{" +
                "DdayID=" + DdayID +
                ", DdayDate=" + DdayDate +
                ", DdayName='" + DdayName + '\'' +
                '}';
    }
}
