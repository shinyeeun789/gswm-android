package com.inhatc.study_project.ui;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.google.android.material.slider.LabelFormatter;
import com.inhatc.study_project.MySelectorDecorator;
import com.inhatc.study_project.OneDayDecorator;
import com.inhatc.study_project.R;
import com.inhatc.study_project.data.AppDatabase;
import com.inhatc.study_project.data.DayStats;
import com.inhatc.study_project.data.Goal;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class FragmentStatDay extends Fragment {
    private TextView totalStudyTime, maxConcenTime, goalsAchieve, dayDate;
    private HorizontalBarChart dayChart;
    private MaterialCalendarView weekCalendar;
    private Date dToday;
    private int selectYear, selectMon, selectDay;
    private double countGoal, countComplete;                    // 목표 달성률 얻기 위한 변수
    private List<String> goalNames;                             // 목표별 달성률 얻기 위한 변수
    private List<Goal> goalDatas;                               // 목표별 달성률 얻기 위한 변수
    private ArrayList<Integer> colorList = new ArrayList<Integer>();
    private AppDatabase db;
    private DayStats dayStats = null;
    DayHandler handler = new DayHandler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_day, container, false);

        colorList.add(Color.parseColor("#E2BFFF"));     colorList.add(Color.parseColor("#BEAEE8"));
        colorList.add(Color.parseColor("#CCCCFF"));     colorList.add(Color.parseColor("#AEBDE8"));
        colorList.add(Color.parseColor("#BFE1FF"));      colorList.add(Color.parseColor("#A2B5E8"));
        colorList.add(Color.parseColor("#A2DAE8"));

        dayDate = (TextView) view.findViewById(R.id.tv_dayDate);
        totalStudyTime = (TextView) view.findViewById(R.id.tv_totalStudyTime);
        maxConcenTime = (TextView) view.findViewById(R.id.tv_maxConcenTime);
        goalsAchieve = (TextView) view.findViewById(R.id.tv_goalsAchieve);
        dayChart = view.findViewById(R.id.ct_dayChart);
        weekCalendar = view.findViewById(R.id.weekCalendar);

        // 변수 초기화
        db = AppDatabase.getAppDatabase(getContext());
        goalNames = new ArrayList<String>();

        // 캘린더 커스텀 및 오늘 날짜 선택
        dToday = new Date();
        weekCalendar.addDecorators(new OneDayDecorator(),
                new MySelectorDecorator(getActivity()));
        weekCalendar.setSelectedDate(dToday);
        dayDate.setText(String.format("%04d년 %02d월 %02d일", dToday.getYear()+1900, dToday.getMonth()+1, dToday.getDate()));

        // DB에서 데이터 가져와 출력하기
        getDayStatDate(dToday.getYear(), dToday.getMonth(), dToday.getDate());

        // 날짜 선택되었을 때
        weekCalendar.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                selectYear = weekCalendar.getSelectedDate().getYear();
                selectMon = weekCalendar.getSelectedDate().getMonth();
                selectDay = weekCalendar.getSelectedDate().getDay();
                dayDate.setText(String.format("%04d년 %02d월 %02d일", selectYear, selectMon+1, selectDay));

                // DB에서 데이터 가져와 출력하기
                getDayStatDate(selectYear-1900, selectMon, selectDay);
            }
        });
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        weekCalendar.setSelectedDate(dToday);

    }

    public void getDayStatDate(int year, int month, int day) {
        goalDatas = new ArrayList<Goal>();
        new Thread(() -> {
            Date date = new Date(year, month, day, 0, 0, 0);
            // 통계
            dayStats = db.statsDao().getDayStatData(date.getTime());
            // 목표 달성률
            countGoal = db.goalDao().countAll(date.getTime(), date.getTime());
            countComplete = db.goalDao().countComplete(date.getTime(), date.getTime());
            // 목표별 달성률
            goalNames = db.goalDao().getGaolNames(date.getTime());
            for(int i=0; i<goalNames.size(); i++) {
                goalDatas.add(db.goalDao().selectWithNameDate(goalNames.get(i), date.getTime()));
            }
            Message msg = handler.obtainMessage();
            handler.sendMessage(msg);
        }).start();
    }

    class DayHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            dayChart.clear();
            if(dayStats != null) {
                totalStudyTime.setText(dayStats.getStudyTime());
                maxConcenTime.setText(dayStats.getConcenTime());
                goalsAchieve.setText(String.format("%d%%", (int)(countComplete / countGoal * 100.0)));

                // Bar Chart 출력
                BarData barData = new BarData();
                for(int i=0; i<goalDatas.size(); i++) {
                    List<BarEntry> entries = new ArrayList<>();
                    double quantity = goalDatas.get(i).getQuantity();
                    double fallQuantity = goalDatas.get(i).getFallQuantity();
                    if (quantity == 0 || goalDatas.get(i).getState().equals("O"))
                        entries.add(new BarEntry(i, 100));
                    else if (goalDatas.get(i).getState().equals("X"))
                        entries.add(new BarEntry(i, 0));
                    else
                        entries.add(new BarEntry(i, (float)((quantity-fallQuantity) / quantity * 100.0)));

                    BarDataSet barDataSet = new BarDataSet(entries, goalDatas.get(i).getGoalName());
                    barDataSet.setColor(colorList.get(i%7));
                    barData.addDataSet(barDataSet);
                    barData.setBarWidth(0.8f);
                    dayChart.setData(barData);
                }

                // 차트 설정
                dayChart.setDoubleTapToZoomEnabled(false);
                dayChart.setDrawGridBackground(false);
                dayChart.getAxisLeft().setAxisMinimum(0f);
                dayChart.getAxisLeft().setAxisMaximum(101f);
                dayChart.getAxisRight().setEnabled(false);
                dayChart.setDrawValueAboveBar(true);
                dayChart.getDescription().setEnabled(false);
                dayChart.setTouchEnabled(false);
                dayChart.getXAxis().setEnabled(false);
                dayChart.animateY(2000, Easing.EaseInCubic);
                dayChart.invalidate();
            } else {
                totalStudyTime.setText("00:00:00");
                maxConcenTime.setText("00:00:00");
                goalsAchieve.setText("0%");
            }
        }
    }
}
