package com.inhatc.study_project.adapter;

public class RecyclerViewItem {
    private String name;
    private String time;
    private String mark;
    private String rangeValue;

    public RecyclerViewItem(String name, String time, String mark, String rangeValue) {
        this.name = name;
        this.time = time;
        this.mark = mark;
        this.rangeValue = rangeValue;
    }

    public String getName() {
        return this.name;
    }
    public String getTime() {
        return time;
    }
    public String getMark() {
        return mark;
    }
    public String getRangeValue() {
        return rangeValue;
    }
    public void setName(String name) {
        this.name = name;
    }
    public void setTime(String time) {
        this.time = time;
    }
    public void setMark(String mark) {
        this.mark = mark;
    }
    public void setRangeValue(String rangeValue) {
        this.rangeValue = rangeValue;
    }
}
