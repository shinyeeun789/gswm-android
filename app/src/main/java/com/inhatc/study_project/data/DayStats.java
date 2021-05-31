package com.inhatc.study_project.data;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "dayStats")
public class DayStats {
    @NonNull
    @PrimaryKey(autoGenerate = true)
    private int statsID;
    private Date statsDate;
    private String studyTime;
    private String concenTime;

    public int getStatsID() {
        return statsID;
    }
    public Date getStatsDate() {
        return statsDate;
    }
    public String getStudyTime() {
        return studyTime;
    }
    public String getConcenTime() {
        return concenTime;
    }
    public void setStatsID(int statsID) {
        this.statsID = statsID;
    }
    public void setStatsDate(Date statsDate) {
        this.statsDate = statsDate;
    }
    public void setStudyTime(String studyTime) {
        this.studyTime = studyTime;
    }
    public void setConcenTime(String concenTime) {
        this.concenTime = concenTime;
    }

    @Override
    public String toString() {
        return "Stats{" +
                "statsID=" + statsID +
                ", statsDate=" + statsDate +
                ", studyTime='" + studyTime + '\'' +
                ", concenTime='" + concenTime + '\'' +
                '}';
    }
}
