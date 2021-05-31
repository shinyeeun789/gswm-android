package com.inhatc.study_project.adapter;

public class TodoListItem {
    private int id;
    private String name;
    private String range;
    private String fallRange;
    private String mark;
    private int basicRange;

    public TodoListItem(int id, String name, String fallRange, String range, String mark, int basicRange) {
        this.id = id;
        this.name = name;
        this.fallRange = fallRange;
        this.range = range;
        this.mark = mark;
        this.basicRange = basicRange;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getRange() {
        return range;
    }
    public void setRange(String range) {
        this.range = range;
    }
    public String getMark() {
        return mark;
    }
    public void setMark(String mark) {
        this.mark = mark;
    }
    public String getFallRange() { return fallRange; }
    public void setFallRange(String fallRange) { this.fallRange = fallRange; }
    public int getBasicRange() { return basicRange; }
    public void setBasicRange(int basicRange) { this.basicRange = basicRange; }
}
