package com.inhatc.study_project.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.inhatc.study_project.R;
import com.inhatc.study_project.data.AppDatabase;
import com.inhatc.study_project.data.Goal;

import java.util.Calendar;
import java.util.Date;

public class DelayGoal extends AppCompatActivity implements View.OnClickListener {
    private TextView tvFallRange;
    private EditText edtFallRange;
    private Button btnTomorrow, btnNextWeek, btnChoose;
    private int goalID;
    private Goal goal;
    private Date date;
    private Calendar cal;
    private AppDatabase db;
    private DelayHandler handler = new DelayHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delaygoal);

        tvFallRange = (TextView) findViewById(R.id.tv_fallRange);
        edtFallRange = (EditText) findViewById(R.id.edt_fallRange);
        btnTomorrow = (Button) findViewById(R.id.btnDelayT);
        btnNextWeek = (Button) findViewById(R.id.btnDelayNW);
        btnChoose = (Button) findViewById(R.id.btnDelayC);

        Intent intent = getIntent();
        goalID = intent.getIntExtra("goalID", 0);
        db = AppDatabase.getAppDatabase(this);
        new Thread(() -> {
            goal = db.goalDao().getGoalInDelay(goalID);
            date = goal.getGoalDate();
            cal = Calendar.getInstance();
            cal.setTime(date);
            Message msg = handler.obtainMessage();
            handler.sendMessage(msg);
        }).start();

        btnTomorrow.setOnClickListener(this);
        btnNextWeek.setOnClickListener(this);
        btnChoose.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // 입력에 대한 예외 처리
        try {
            if(edtFallRange.getText().toString().matches("")) {
                Toast.makeText(this, "분량을 입력해주세요.", Toast.LENGTH_SHORT).show();
                return;
            } else if(Integer.parseInt(edtFallRange.getText().toString()) > goal.getQuantity()) {
                Toast.makeText(this, String.format("기존 목표 분량은 %d였어요 :)", goal.getQuantity()), Toast.LENGTH_SHORT).show();
                return;
            }
            Integer.parseInt(edtFallRange.getText().toString());
        } catch(NumberFormatException numE) {
            Toast.makeText(this, "분량은 숫자만 입력할 수 있습니다.", Toast.LENGTH_SHORT).show();
            return;
        } catch(Exception e) {
            e.printStackTrace();
        }

        switch (v.getId()) {
            case R.id.btnDelayT :                       // 다음 날에 일정 추가
                cal.add(Calendar.DATE,1);       // 1일 후
                Goal TGoal = convertGoal();
                new Thread(() -> {
                    db.goalDao().insert(TGoal);         // 다음 날에 일정 추가
                    db.goalDao().updateFallQ(Integer.parseInt(edtFallRange.getText().toString()), goalID);      // 오늘 일정에 FallQuantity 데이터 업데이트
                }).start();
                Toast.makeText(this, "다음 날 일정에 추가했습니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.btnDelayNW :                      // 다음 주에 일정 추가
                cal.add(Calendar.DATE, 7);      // 7일 후
                Goal NWGoal = convertGoal();
                new Thread(() -> {
                    db.goalDao().insert(NWGoal);
                    db.goalDao().updateFallQ(Integer.parseInt(edtFallRange.getText().toString()), goalID);
                }).start();
                Toast.makeText(this, "다음 주 일정에 추가했습니다.", Toast.LENGTH_SHORT).show();
                finish();
                break;
            case R.id.btnDelayC :                       // 날짜 선택해 일정 추가
                DatePickerDialog datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        new Thread(() -> {
                            cal.set(year, month, dayOfMonth);
                            db.goalDao().insert(convertGoal());
                            db.goalDao().updateFallQ(Integer.parseInt(edtFallRange.getText().toString()), goalID);
                            finish();
                        }).start();
                        Toast.makeText(DelayGoal.this, "선택한 날짜 일정에 추가했습니다.", Toast.LENGTH_SHORT).show();
                    }
                }, date.getYear()+1900, date.getMonth(), date.getDate()+1);
                Calendar minDate = Calendar.getInstance();
                minDate.set(date.getYear()+1900, date.getMonth(), date.getDate()+1);
                datePicker.getDatePicker().setMinDate(minDate.getTimeInMillis());
                datePicker.show();
                break;
        }
    }

    public Goal convertGoal() {
        Goal newGoal = new Goal();
        newGoal.setGoalName(goal.getGoalName());
        newGoal.setGoalDate(cal.getTime());
        newGoal.setGoalTime(goal.getGoalTime());
        newGoal.setQuantity(Integer.parseInt(edtFallRange.getText().toString()));
        newGoal.setRangeValue(goal.getRangeValue());
        newGoal.setDelay(true);
        return newGoal;
    }

    class DelayHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            tvFallRange.setText(String.format("%s의 목표 분량은 %d%s였어요.", goal.getGoalName(), goal.getQuantity(), goal.getRangeValue()));
        }
    }
}