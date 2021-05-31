package com.inhatc.study_project;

import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;

import java.util.ArrayList;

public class EventDecorator implements DayViewDecorator {
    private final int color;
    private final ArrayList<CalendarDay> dates;

    public EventDecorator(int color, ArrayList<CalendarDay> dates) {
        this.color = color;
        this.dates = new ArrayList<>(dates);
    }

    @Override
    public boolean shouldDecorate(CalendarDay day) {
        return dates.contains(day);
    }

    @Override
    public void decorate(DayViewFacade view) {
        view.addSpan(new DotSpan(6, color));
    }
}