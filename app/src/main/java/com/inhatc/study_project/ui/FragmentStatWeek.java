package com.inhatc.study_project.ui;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.inhatc.study_project.MySelectorDecorator;
import com.inhatc.study_project.OneDayDecorator;
import com.inhatc.study_project.R;
import com.inhatc.study_project.data.AppDatabase;
import com.inhatc.study_project.data.DayStats;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FragmentStatWeek extends Fragment {
    private TextView weekDate, weekStudyTime, avgStudyTime, weekGoalsAch;
    private BarChart dayStudyChart;
    private MaterialCalendarView monthCalendar;
    private Calendar cToday = Calendar.getInstance(), startDay = Calendar.getInstance(), endDay = Calendar.getInstance();
    private long totalTime, avgTime;
    private double countGoal, countComplete;                    // 목표 달성률 얻기 위한 변수
    private List<DayStats> statsList;
    private ArrayList<Integer> colorList = new ArrayList<Integer>();
    private AppDatabase db;
    WeekHandler handler = new WeekHandler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week, container, false);

        colorList.add(Color.parseColor("#E2BFFF"));       colorList.add(Color.parseColor("#BEAEE8"));        colorList.add(Color.parseColor("#CCCCFF"));
        colorList.add(Color.parseColor("#AEBDE8"));       colorList.add(Color.parseColor("#BFE1FF"));        colorList.add(Color.parseColor("#A2B5E8"));
        colorList.add(Color.parseColor("#A2DAE8"));

        weekDate = (TextView) view.findViewById(R.id.tv_weekDate);
        weekStudyTime = (TextView) view.findViewById(R.id.tv_weekStudyTime);
        avgStudyTime = (TextView) view.findViewById(R.id.tv_weekAvgStudyTime);
        weekGoalsAch = (TextView) view.findViewById(R.id.tv_weekGoalsAchieve);
        dayStudyChart = view.findViewById(R.id.ct_weekDSChart);
        monthCalendar = view.findViewById(R.id.monthCalendar);

        // 변수 초기화
        db = AppDatabase.getAppDatabase(getContext());

        // 캘린더 커스텀 및 오늘 날짜 선택
        monthCalendar.addDecorators(new OneDayDecorator(),
                new MySelectorDecorator(getActivity()));
        monthCalendar.setSelectedDate(cToday);
        startDay.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        endDay.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        weekDate.setText(String.format("%02d월 %02d일 ~ %02d월 %02d일", startDay.getTime().getMonth()+1, startDay.getTime().getDate(), endDay.getTime().getMonth()+1, endDay.getTime().getDate()));

        // DB에서 데이터 가져와 출력하기
        getDayStatDate(startDay.getTime().getYear(), startDay.getTime().getMonth(), startDay.getTime().getDate(),
                endDay.getTime().getYear(), endDay.getTime().getMonth(), endDay.getTime().getDate());

        // 날짜 선택되었을 때
        monthCalendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                startDay.set(monthCalendar.getSelectedDate().getYear(), monthCalendar.getSelectedDate().getMonth(), monthCalendar.getSelectedDate().getDay());      startDay.getTime();
                endDay.set(monthCalendar.getSelectedDate().getYear(), monthCalendar.getSelectedDate().getMonth(), monthCalendar.getSelectedDate().getDay());        endDay.getTime();
                startDay.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                endDay.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                weekDate.setText(String.format("%02d월 %02d일 ~ %02d월 %02d일", startDay.getTime().getMonth()+1, startDay.getTime().getDate(), endDay.getTime().getMonth()+1, endDay.getTime().getDate()));

                // DB에서 데이터 가져와 출력하기
                 getDayStatDate(startDay.getTime().getYear(), startDay.getTime().getMonth(), startDay.getTime().getDate(),
                         endDay.getTime().getYear(), endDay.getTime().getMonth(), endDay.getTime().getDate());
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        monthCalendar.setSelectedDate(cToday);
    }

    public void getDayStatDate(int sYear, int sMonth, int sDay, int eYear, int eMonth, int eDay) {
        new Thread(() -> {
            Date startDate = new Date(sYear, sMonth, sDay, 0, 0, 0);
            Date endDate = new Date(eYear, eMonth, eDay,0, 0, 0);
            // 일주일 간의 통계 데이터 가져오기 (= 요일별 공부시간)
            statsList = db.statsDao().getStudyTimes(startDate.getTime(), endDate.getTime());
            // 총 공부시간 & 평균 공부시간
            try {
                totalTime = 0L;
                SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
                for(int i=0; i<statsList.size(); i++) {
                    totalTime += timeFormat.parse(statsList.get(i).getStudyTime()).getTime();
                }
                avgTime = (statsList.size() > 0) ? (totalTime / statsList.size()) : 0;
            } catch(ParseException pEx) {
                pEx.printStackTrace();
            }
            // 목표 달성률
            countGoal = db.goalDao().countAll(startDate.getTime(), endDate.getTime());
            countComplete = db.goalDao().countComplete(startDate.getTime(), endDate.getTime());

            Message msg = handler.obtainMessage();
            handler.sendMessage(msg);
        }).start();
    }

    class WeekHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            String[] days = {"일","월","화","수","목","금","토"};
            if(statsList.size() > 0) {
                Date dTotal = new Date(totalTime);
                Date dAvg = new Date(avgTime);
                weekStudyTime.setText(String.format("%02d:%02d:%02d", dTotal.getHours(), dTotal.getMinutes(), dTotal.getSeconds()));
                avgStudyTime.setText(String.format("%02d:%02d:%02d", dAvg.getHours(), dAvg.getMinutes(), dAvg.getSeconds()));
                weekGoalsAch.setText(String.format("%d%%", (int)(countComplete / countGoal * 100.0)));

                // Bar Chart 출력
                BarData barData = new BarData();
                for(int i=0; i<statsList.size(); i++) {
                    List<BarEntry> entries = new ArrayList<>();
                    String[] splitTime = statsList.get(i).getStudyTime().split(":");
                    String chartData = String.format("%s.%d", splitTime[0], (int)(Integer.parseInt(splitTime[1])*1.5));      // 시간.(분 * 1.5)
                    
                    entries.add(new BarEntry(i, Float.parseFloat(chartData)));
                    Calendar cDates = Calendar.getInstance();                                   // 요일 얻기 위한 변수
                    cDates.set(statsList.get(i).getStatsDate().getYear()+1900, statsList.get(i).getStatsDate().getMonth(), statsList.get(i).getStatsDate().getDate());
                    BarDataSet barDataSet = new BarDataSet(entries, days[cDates.get(Calendar.DAY_OF_WEEK)-1]+"요일");
                    barDataSet.setColor(colorList.get(i%7));
                    barData.addDataSet(barDataSet);
                    barData.setBarWidth(0.8f);
                    dayStudyChart.setData(barData);
                }

                // 차트 설정
                dayStudyChart.setDoubleTapToZoomEnabled(false);
                dayStudyChart.setDrawGridBackground(false);
                dayStudyChart.getAxisLeft().setAxisMinimum(0f);
                dayStudyChart.getAxisRight().setEnabled(false);
                dayStudyChart.setDrawValueAboveBar(true);
                dayStudyChart.getDescription().setEnabled(false);
                dayStudyChart.setTouchEnabled(false);
                dayStudyChart.getXAxis().setEnabled(false);
                dayStudyChart.animateY(2000, Easing.EaseInCubic);
                dayStudyChart.invalidate();
            } else {
                weekStudyTime.setText("00:00:00");
                avgStudyTime.setText("00:00:00");
                weekGoalsAch.setText("0%");
                dayStudyChart.clear();
            }
        }
    }
}