package com.inhatc.study_project.ui;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.room.Room;

import com.inhatc.study_project.AlarmReceiver;
import com.inhatc.study_project.R;
import com.inhatc.study_project.data.AppDatabase;
import com.inhatc.study_project.data.Goal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class AddGoal extends AppCompatActivity implements View.OnClickListener {
    private Spinner quantitySpinner;
    private Button btnTimePicker, btnDatePicker1, btnDatePicker2, btnOK;
    private Switch switchButton;
    private CheckBox chkMon, chkTue, chkWed, chkThu, chkFri, chkSat, chkSun;
    private EditText edtGoalName, edtQuantity;
    private int selectYear, selectMon, selectDay, bYear, bMonth, bDay;
    private String selSpinner;
    private Date date;
    private ArrayList<Date> dateList;

    private AlarmManager alarmManager;
    private NotificationManager notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addgoal);

        chkMon = (CheckBox) findViewById(R.id.chk_mon);
        chkTue = (CheckBox) findViewById(R.id.chk_tue);
        chkWed = (CheckBox) findViewById(R.id.chk_wed);
        chkThu = (CheckBox) findViewById(R.id.chk_thu);
        chkFri = (CheckBox) findViewById(R.id.chk_fri);
        chkSat = (CheckBox) findViewById(R.id.chk_sat);
        chkSun = (CheckBox) findViewById(R.id.chk_sun);
        btnTimePicker = (Button) findViewById(R.id.btnTimePicker);
        btnDatePicker1 = (Button) findViewById(R.id.btnDatePicker1);
        btnDatePicker2 = (Button) findViewById(R.id.btnDatePicker2);
        btnOK = (Button) findViewById(R.id.btnOK);
        switchButton = (Switch) findViewById(R.id.switchButton);
        edtGoalName = (EditText) findViewById(R.id.edt_goalName);
        edtQuantity = (EditText) findViewById(R.id.edt_quantity);

        // 첫번째 editText에 포커스 주기
        edtGoalName.post(new Runnable() {
            @Override
            public void run() {
                edtGoalName.setFocusableInTouchMode(true);
                edtGoalName.requestFocus();
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.showSoftInput(edtGoalName, 0);
            }
        });

        // 스피너 설정
        String[] items = getResources().getStringArray(R.array.quantities);
        quantitySpinner = (Spinner)findViewById(R.id.quantityLists);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, R.layout.spinner_item, items);
        quantitySpinner.setAdapter(adapter);
        quantitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selSpinner = items[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });

        // TimePicker 띄우기
        btnTimePicker.setOnClickListener(this);

        // 스위치 활성화/비활성화
        switchButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {        // 스위치 버튼이 활성화 되었으면
                    btnDatePicker1.setEnabled(true);
                    btnDatePicker2.setEnabled(true);
                    btnDatePicker1.setTextColor(Color.parseColor("#000000"));
                    btnDatePicker2.setTextColor(Color.parseColor("#000000"));
                    chkMon.setEnabled(true);
                    chkTue.setEnabled(true);
                    chkWed.setEnabled(true);
                    chkThu.setEnabled(true);
                    chkFri.setEnabled(true);
                    chkSat.setEnabled(true);
                    chkSun.setEnabled(true);
                } else {
                    btnDatePicker1.setEnabled(false);
                    btnDatePicker2.setEnabled(false);
                    btnDatePicker1.setTextColor(Color.parseColor("#9F9F9F"));
                    btnDatePicker2.setTextColor(Color.parseColor("#9F9F9F"));
                    chkMon.setEnabled(false);
                    chkTue.setEnabled(false);
                    chkWed.setEnabled(false);
                    chkThu.setEnabled(false);
                    chkFri.setEnabled(false);
                    chkSat.setEnabled(false);
                    chkSun.setEnabled(false);
                }
            }
        });
        btnDatePicker1.setOnClickListener(this);
        btnDatePicker2.setOnClickListener(this);
        btnOK.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        // Calendar에서 보낸 선택한 날짜 받기
        Intent intent = getIntent();
        selectYear = intent.getIntExtra("selectYear", 0);
        selectMon = intent.getIntExtra("selectMonth", 0);
        selectDay = intent.getIntExtra("selectDay", 0);

        // DB 생성
        AppDatabase db = AppDatabase.getAppDatabase(this);
        
        switch (v.getId()) {
            case R.id.btnTimePicker :
                TimePickerDialog timeDialog = new TimePickerDialog(AddGoal.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        btnTimePicker.setText(String.format("%02d : %02d", hourOfDay, minute));
                    }
                }, 0, 0, false);
                timeDialog.show();
                break;
            case R.id.btnDatePicker1 :
                DatePickerDialog datePicker1 = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                btnDatePicker1.setText(String.format("%02d/%02d/%02d", year, month+1, dayOfMonth));
                                bYear = year;
                                bMonth = month;
                                bDay = dayOfMonth;
                            }
                        }, selectYear, selectMon, selectDay);
                datePicker1.show();
                break;
            case R.id.btnDatePicker2 :
                if(btnDatePicker1.getText().toString().equals("시작 날짜 선택")) {
                    Toast.makeText(this, "시작 날짜를 먼저 선택해주세요.", Toast.LENGTH_SHORT).show();
                    break;
                }
                DatePickerDialog datePicker2 = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                btnDatePicker2.setText(String.format("%02d/%02d/%02d", year, month+1, dayOfMonth));
                            }
                        }, bYear, bMonth, bDay+1);
                Calendar minDate = Calendar.getInstance();
                minDate.set(bYear, bMonth, bDay+1);
                datePicker2.getDatePicker().setMinDate(minDate.getTimeInMillis());
                datePicker2.show();
                break;
            case R.id.btnOK:                                // 추가 버튼 눌렀을 때
                // 정상적으로 입력됐는지 확인하기
                try {
                    // EditText 입력 여부 확인
                    if(edtGoalName.getText().toString().matches("") || edtQuantity.getText().toString().matches("")) {
                        Toast.makeText(AddGoal.this, "입력되지 않은 항목이 있습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if(Integer.parseInt(edtQuantity.getText().toString()) <= 0) {
                        Toast.makeText(AddGoal.this, "하루 목표 분량에 0이나 음수는 입력할 수 없습니다.", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    // 하루 목표 분량에 숫자가 입력되었는지 확인
                    Integer.parseInt(edtQuantity.getText().toString());
                    // 날짜 변환
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                    date = dateFormat.parse(selectYear+"-"+(selectMon+1)+"-"+selectDay);
                } catch(NumberFormatException numEx) {
                    Toast.makeText(AddGoal.this, "하루 목표 분량은 숫자만 입력 가능합니다.", Toast.LENGTH_SHORT).show();
                    return;
                } catch(ParseException pEx) {
                    Log.d("AddGoal", pEx.toString());
                } catch(Exception e) {
                    Toast.makeText(AddGoal.this, "오류가 발생했습니다. 잠시 후 시도해주세요.", Toast.LENGTH_SHORT).show();
                    Log.d("AddGoal", e.toString());
                }

                // 입력된 데이터 DB에 저장하기
                if(switchButton.isChecked()) {      // 스위치 버튼이 활성화된 상태인 경우
                    if (btnDatePicker1.getText().equals("시작 날짜 선택") || btnDatePicker2.getText().equals("종료 날짜 선택")) {
                        Toast.makeText(AddGoal.this, "시작 날짜와 종료 날짜를 선택해주세요.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    dateList = new ArrayList<Date>();
                    String[] startDate = btnDatePicker1.getText().toString().split("/");
                    Calendar startCal = Calendar.getInstance(); startCal.set(Integer.parseInt(startDate[0]), Integer.parseInt(startDate[1])-1, Integer.parseInt(startDate[2])-1, 0, 0, 0);
                    String[] endDate = btnDatePicker2.getText().toString().split("/");
                    Calendar endCal = Calendar.getInstance(); endCal.set(Integer.parseInt(endDate[0]), Integer.parseInt(endDate[1])-1, Integer.parseInt(endDate[2])+1, 0, 0, 0);
                    Calendar cal = Calendar.getInstance();
                    cal.set(Integer.parseInt(startDate[0]), Integer.parseInt(startDate[1])-1, Integer.parseInt(startDate[2]), 0, 0, 0);
                    // 추가할 날짜 데이터 구하기
                    while (endCal.compareTo(cal) > 0) {
                        if (chkMon.isChecked()) {
                            cal.getTime();
                            cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
                            addDateList(cal, startCal, endCal);
                        } if (chkTue.isChecked()) {
                            cal.getTime();
                            cal.set(Calendar.DAY_OF_WEEK, Calendar.TUESDAY);
                            addDateList(cal, startCal, endCal);
                        }  if (chkWed.isChecked()) {
                            cal.getTime();
                            cal.set(Calendar.DAY_OF_WEEK, Calendar.WEDNESDAY);
                            addDateList(cal, startCal, endCal);
                        }  if (chkThu.isChecked()) {
                            cal.getTime();
                            cal.set(Calendar.DAY_OF_WEEK, Calendar.THURSDAY);
                            addDateList(cal, startCal, endCal);
                        }  if (chkFri.isChecked()) {
                            cal.getTime();
                            cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
                            addDateList(cal, startCal, endCal);
                        }  if (chkSat.isChecked()) {
                            cal.getTime();
                            cal.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
                            addDateList(cal, startCal, endCal);
                        }  if (chkSun.isChecked()) {
                            cal.getTime();
                            cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                            addDateList(cal, startCal, endCal);
                        }
                        cal.add(cal.DAY_OF_MONTH, 7);
                        cal.getTime();
                        cal.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
                    }
                    if(dateList.size() == 0) {
                        Toast.makeText(AddGoal.this, "추가할 목표가 없습니다.", Toast.LENGTH_SHORT).show();
                        break;
                    }
                    new Thread(() -> {
                        for (int i=0; i<dateList.size(); i++) {
                            Goal goal = convertGoal(dateList.get(i));
                            db.goalDao().insert(goal);
                        }
                    }).start();
                    Toast.makeText(AddGoal.this, "목표가 추가되었습니다 :)", Toast.LENGTH_SHORT).show();
                } else {
                    // 입력된 데이터 DB에 저장
                    new Thread(() -> {
                        Goal goal = convertGoal(date);
                        db.goalDao().insert(goal);
                    }).start();
                    Toast.makeText(AddGoal.this, "목표가 추가되었습니다 :)", Toast.LENGTH_SHORT).show();
                }
                finish();
                break;
        }
    }

    public Goal convertGoal(Date date) {
        Goal goal = new Goal();
        goal.setGoalName(edtGoalName.getText().toString());
        goal.setGoalDate(date);
        goal.setGoalTime(btnTimePicker.getText().toString());
        goal.setQuantity(Integer.parseInt(edtQuantity.getText().toString()));
        goal.setRangeValue(selSpinner);
        return goal;
    }
    
    public void addDateList(Calendar cal, Calendar startCal, Calendar endCal) {
        if(cal.compareTo(startCal) > 0 && endCal.compareTo(cal) > 0) {
            try {
                String calDay = String.format("%04d-%02d-%02d", cal.getTime().getYear() + 1900, cal.getTime().getMonth() + 1, cal.getTime().getDate());
                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
                Date dCalDay = dateFormat.parse(calDay);
                dateList.add(dCalDay);
            } catch (ParseException pEx) {
                Log.d("FragmentCalendar", pEx.toString());
            } catch (Exception e) {
                Log.d("FragmentCalendar", e.toString());
            }
        }
    }

    private void setAlarm() {
        // AlarmReceiver에 값 전달
        Intent receiverIntent = new Intent(getApplicationContext(), AlarmReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(getApplicationContext(), 0, receiverIntent, 0);

        String from = "2021-05-25 11:00:00";        // 임의 날짜
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date dateTime = null;
        try {
            dateTime = sdf.parse(from);
        } catch(ParseException e) {
            e.printStackTrace();
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(dateTime);

        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}