package com.inhatc.study_project.ui;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.inhatc.study_project.EventDecorator;
import com.inhatc.study_project.MySelectorDecorator;
import com.inhatc.study_project.OneDayDecorator;
import com.inhatc.study_project.R;
import com.inhatc.study_project.adapter.ToDoListAdapter;
import com.inhatc.study_project.data.AppDatabase;
import com.inhatc.study_project.data.Dday;
import com.inhatc.study_project.data.Goal;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FragmentCalendar extends Fragment implements View.OnClickListener {
    private MaterialCalendarView materialCalendarView;
    private RecyclerView todoList;
    private Button btnToday, btnAddChoice, btnGoGoal, btnGoDday;
    private TextView cal_date, todoCount, cal_slidingDate;
    private Dialog choiceDialog;
    private int goalCount, year, month, day;
    private ToDoListAdapter mAdapter;
    private AppDatabase db;
    private List<Goal> saveTodoList;
    private List<Dday> saveDdayList;
    private EventDecorator eventDecorator;
    ValueHandler handler = new ValueHandler();
    AdapterHandler aHandler = new AdapterHandler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_calendar, container, false);

        cal_date = (TextView) view.findViewById(R.id.cal_dateView);
        cal_slidingDate = (TextView) view.findViewById(R.id.cal_slidingDate);
        todoCount = (TextView) view.findViewById(R.id.todoCount);
        materialCalendarView = view.findViewById(R.id.calendarView);
        btnToday = view.findViewById(R.id.btnToday);
        btnAddChoice = view.findViewById(R.id.btnAddChoice);
        todoList = view.findViewById(R.id.todoList);

        saveTodoList = new ArrayList<Goal>();
        saveDdayList = new ArrayList<Dday>();

        ArrayList<CalendarDay> goalDayList = new ArrayList<>();
        ArrayList<CalendarDay> dDayList = new ArrayList<>();

        db = AppDatabase.getAppDatabase(getContext());
        materialCalendarView.addDecorators(new OneDayDecorator(),
                new MySelectorDecorator(getActivity()));
        updateTodayCal();
        db.goalDao().getGoalDate().observe(getActivity(), new Observer<List<Long>>() {
            @Override
            public void onChanged(List<Long> dates) {
                // 날짜 값 초기화
                materialCalendarView.removeDecorator(eventDecorator);
                goalDayList.clear();
                for (int i=0; i<dates.size(); i++) {
                    Date date = new Date(dates.get(i));
                    goalDayList.add(CalendarDay.from(1900+date.getYear(), date.getMonth(), date.getDate()));
                }
                eventDecorator = new EventDecorator(Color.parseColor("#9999FF"), goalDayList);
                materialCalendarView.addDecorator(eventDecorator);
                materialCalendarView.invalidateDecorators();
            }
        });
        db.ddayDao().getDdayDate().observe(getActivity(), new Observer<List<Long>>() {
            @Override
            public void onChanged(List<Long> dDayDates) {
                for (int i=0; i<dDayDates.size(); i++) {
                    Date date = new Date(dDayDates.get(i));
                    dDayList.add(CalendarDay.from(1900+date.getYear(), date.getMonth(), date.getDate()));
                }
                materialCalendarView.addDecorator(new EventDecorator(Color.parseColor("#FFC000"), dDayList));
            }
        });

        // recyclerView 연결
        todoList.setHasFixedSize(true);
        todoList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mAdapter = new ToDoListAdapter(db);
        todoList.setAdapter(mAdapter);

        // 오늘 날짜 가져오기
        Calendar cal = Calendar.getInstance();
        year = cal.get(Calendar.YEAR);
        month = cal.get(Calendar.MONTH) + 1;
        day = cal.get(Calendar.DAY_OF_MONTH);
        // 일정 갯수 출력
        updateCount(year, month, day);

        // 캘린더에서 날짜를 선택했을 때
        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {
                year = materialCalendarView.getSelectedDate().getYear();
                month = materialCalendarView.getSelectedDate().getMonth() + 1;
                day = materialCalendarView.getSelectedDate().getDay();
                cal_date.setText(year + "년 " + month + "월 " + day + "일");
                cal_slidingDate.setText(month + "월 " + day + "일에 할 일");
                updateCount(year, month, day);            // 일정 갯수 출력
                loadListData(year, month, day);
            }
        });
        btnToday.setOnClickListener(this);
        btnAddChoice.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btnToday :        // 오늘 버튼 클릭하면 오늘 날짜 선택하기
                updateTodayCal();
                break;
            case R.id.btnAddChoice :      // 추가 버튼 눌렀을 때
                showChoiceDialog();
                break;
            case R.id.btnGoGoal :
                Intent intentGoal = new Intent(getActivity(), AddGoal.class);
                intentGoal.putExtra("selectYear", materialCalendarView.getSelectedDate().getYear());
                intentGoal.putExtra("selectMonth", materialCalendarView.getSelectedDate().getMonth());
                intentGoal.putExtra("selectDay", materialCalendarView.getSelectedDate().getDay());
                startActivity(intentGoal);
                choiceDialog.dismiss();
                break;
            case R.id.btnGoDday :
                Intent intentDday = new Intent(getActivity(), AddDday.class);
                intentDday.putExtra("selectYear", materialCalendarView.getSelectedDate().getYear());
                intentDday.putExtra("selectMonth", materialCalendarView.getSelectedDate().getMonth());
                intentDday.putExtra("selectDay", materialCalendarView.getSelectedDate().getDay());
                startActivity(intentDday);
                choiceDialog.dismiss();
                break;
        }
    }

    public void showChoiceDialog() {
        choiceDialog = new Dialog(getContext());
        choiceDialog.setContentView(R.layout.dialog_choiceincal);
        choiceDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        choiceDialog.show();

        btnGoGoal = choiceDialog.findViewById(R.id.btnGoGoal);
        btnGoDday = choiceDialog.findViewById(R.id.btnGoDday);
        btnGoGoal.setOnClickListener(this);
        btnGoDday.setOnClickListener(this);
    }

    public void updateTodayCal() {
        materialCalendarView.setSelectedDate(CalendarDay.today());
        cal_date.setText(materialCalendarView.getSelectedDate().getYear() + "년 " + (materialCalendarView.getSelectedDate().getMonth() + 1) + "월 " + materialCalendarView.getSelectedDate().getDay() + "일");
        cal_date.setTextColor(Color.parseColor("#595959"));
        cal_slidingDate.setText(materialCalendarView.getSelectedDate().getMonth() + 1 + "월 " + materialCalendarView.getSelectedDate().getDay() + "일에 할 일");
    }

    public void loadListData(int year, int month, int day) {
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dDate = dateFormat.parse(year+"-"+month+"-"+day);
            new Thread(() -> {
                saveTodoList = db.goalDao().getListViewItemNoLD(dDate.getTime());
                saveDdayList = db.ddayDao().getListViewItem(dDate.getTime());
                Message msg = aHandler.obtainMessage();
                aHandler.sendMessage(msg);
            }).start();
        } catch(ParseException parseException) {
            Log.d("FragmentCalendar", parseException.toString());
        }
    }

    public void updateCount(int inputYear, int inputMonth, int inputDay) {
        new Thread() {
            public void run() {
                try {
                    String selDay = String.format("%04d.%02d.%02d", inputYear, inputMonth, inputDay);
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd");
                    Date dSelDay = dateFormat.parse(selDay);
                    goalCount = db.goalDao().getGoalCount(dSelDay.getTime());
                    Message msg = handler.obtainMessage();
                    handler.sendMessage(msg);
                } catch (ParseException pEx) {
                    Log.d("FragmentCalendar", pEx.toString());
                } catch (Exception e) {
                    Log.d("FragmentCalendar", e.toString());
                }
            }
        }.start();
    }

    class ValueHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            todoCount.setText(String.format("%d개의 할 일", goalCount));
            todoCount.setTextColor(Color.parseColor("#595959"));
        }
    }

    class AdapterHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            mAdapter.setItem(saveTodoList, saveDdayList);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        updateTodayCal();
        updateCount(materialCalendarView.getSelectedDate().getYear(), materialCalendarView.getSelectedDate().getMonth()+1, materialCalendarView.getSelectedDate().getDay());
        loadListData(materialCalendarView.getSelectedDate().getYear(), materialCalendarView.getSelectedDate().getMonth()+1, materialCalendarView.getSelectedDate().getDay());
    }
}
