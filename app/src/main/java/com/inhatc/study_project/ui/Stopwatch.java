package com.inhatc.study_project.ui;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Chronometer;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.inhatc.study_project.ForegroundService;
import com.inhatc.study_project.R;
import com.inhatc.study_project.data.AppDatabase;
import com.inhatc.study_project.data.DayStats;
import com.inhatc.study_project.data.Goal;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Timer;

public class Stopwatch extends AppCompatActivity implements View.OnClickListener {
    private Chronometer cm_concenTime;
    private ImageButton stopButton;
    private TextView tv_mTime, tv_goalName, tv_goalTime;
    private String today, goalName, goalRangeValue, dbConcenTime;
    private String[] goalTimeList, totalTimeList;
    private Date dToday, dYesterday, dBasicDay;
    private Intent serviceIntent;
    private AppDatabase db;
    private SimpleDateFormat timeFormat, dateFormat;
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private TimerHandler tHandler = new TimerHandler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stopwatch);

        // 서비스 인텐트 생성 후 포그라운드 서비스 실행
        serviceIntent = new Intent(Stopwatch.this, ForegroundService.class);
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O)      // android 8.0 이상은 방법이 다름
            startForegroundService(serviceIntent);
        else startService(serviceIntent);

        tv_goalName = (TextView) findViewById(R.id.goalName);
        tv_goalTime = (TextView) findViewById(R.id.goalTime);
        tv_mTime = (TextView) findViewById(R.id.timeView);
        cm_concenTime = (Chronometer) findViewById(R.id.concenTime);
        stopButton = (ImageButton) findViewById(R.id.stopButton);

        // DB 생성
        db = AppDatabase.getAppDatabase(this);

        // 오늘 공부 시간과 최대 집중시간 ROOM에서 가져오기
        Calendar cal = Calendar.getInstance();
        dBasicDay = new Date(cal.getTime().getYear(), cal.getTime().getMonth(), cal.getTime().getDate(), 0,0,0);
        new Thread(()-> {
            DayStats stats = db.statsDao().getDayStatData(dBasicDay.getTime());
            if(stats == null) {
                totalTimeList = "00:00:00".split(":");
                dbConcenTime = "00:00:00";
            } else {
                totalTimeList = stats.getStudyTime().split(":");
                dbConcenTime = stats.getConcenTime();
            }
            Message msg = tHandler.obtainMessage();
            tHandler.sendMessage(msg);
        }).start();

        // 홈에서 보낸 데이터 받고 오늘 공부시간 불러오기
        Intent i = getIntent();
        goalName = i.getStringExtra("GoalName");
        tv_goalName.setText(goalName);
        goalTimeList = i.getStringExtra("GoalTime").split(":");
        goalRangeValue = i.getStringExtra("GoalRangeValue");

        // 중지 버튼 클릭했을 때
        stopButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        Calendar cal = new GregorianCalendar();
        today = String.format("%04d.%02d.%02d", cal.get(Calendar.YEAR), (cal.get(Calendar.MONTH) + 1), cal.get(Calendar.DAY_OF_MONTH));     // 오늘 날짜
        timeFormat = new SimpleDateFormat("HH:mm:ss");
        dateFormat = new SimpleDateFormat("yyyy.MM.dd");
        try {
            Date time = timeFormat.parse(cal.get(Calendar.HOUR_OF_DAY) + ":" + cal.get(Calendar.MINUTE) + ":" + cal.get(Calendar.SECOND));     // 지금 시간
            dToday = dateFormat.parse(today);                                                                                                          // Date 타입의 오늘 날짜
            Date nowConTime = timeFormat.parse(cm_concenTime.getText().toString());
            Date maxConTime = timeFormat.parse(dbConcenTime);

            // 시간을 재는 도중 12시가 넘었을 때
            if (!today.equals(dateFormat.format(dBasicDay))) {
                dYesterday = dateFormat.parse(cal.get(Calendar.YEAR) + "." + (cal.get(Calendar.MONTH) + 1) + "." + (cal.get(Calendar.DAY_OF_MONTH) - 1));   // 어제 날짜
                String yesterStudyTime = yesterTime(timeFormat.parse(tv_mTime.getText().toString()), time);
                if(!goalName.equals("공부시간 측정")) {
                    String yesterGoalTime = yesterTime(timeFormat.parse(tv_goalTime.getText().toString()), time);
                    new Thread(() -> {
                        // Goal DB 처리
                        Goal checkGoal = db.goalDao().selectWithNameDate(goalName, dToday.getTime());                // 다음 날에 목표가 추가되어 있는지 확인
                        if (checkGoal == null) {
                            // 다음날에 목표가 없으면 추가
                            Goal goal = new Goal();
                            goal.setGoalName(goalName);
                            goal.setGoalDate(dToday);
                            goal.setGoalTime("00 : 00");
                            goal.setQuantity(0);
                            goal.setState("O");
                            goal.setRangeValue(goalRangeValue);
                            db.goalDao().insert(goal);
                        }
                        db.goalDao().updateStudyTime(yesterGoalTime, goalName, dYesterday.getTime());          // 어제 공부 시간 업데이트
                        db.goalDao().updateStudyTime(String.format("%02d:%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND)), goalName, dToday.getTime()); // 오늘 공부 시간 업데이트
                    }).start();
                }

                // 어제의 통계 데이터베이스에 저장
                if (nowConTime.compareTo(maxConTime) > 0) {      // 현재 집중시간이 데이터베이스의 최대 집중시간보다 크면
                    insertORupdateStats(dYesterday, yesterStudyTime, cm_concenTime.getText().toString());
                } else {
                    insertORupdateStats(dYesterday, yesterStudyTime, maxConTime.toString());
                }
                // 오늘의 통계 데이터베이스에 저장
                insertStats(dToday, String.format("%02d:%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND)),
                        String.format("%02d:%02d:%02d", cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), cal.get(Calendar.SECOND)));
            } else {
                if (nowConTime.compareTo(maxConTime) > 0) {      // 현재 집중시간이 데이터베이스의 최대 집중시간보다 크면
                    insertORupdateStats(dToday, tv_mTime.getText().toString(), cm_concenTime.getText().toString());
                } else {
                    insertORupdateStats(dToday, tv_mTime.getText().toString(), String.format("%02d:%02d:%02d", maxConTime.getHours(), maxConTime.getMinutes(), maxConTime.getSeconds()));
                }
                if (!goalName.equals("공부시간 측정")) {
                    new Thread(() -> {
                        db.goalDao().updateStudyTime(tv_goalTime.getText().toString(), goalName, dBasicDay.getTime());
                    }).start();
                }
            }
        } catch (ParseException pEx) {
            Log.d("Stopwatch", pEx.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 시간 측정 중단 및 foreground 서비스 중단
        cm_concenTime.stop();
        stopService(serviceIntent);

        finish();                   // Stopwatch 종료
    }

    public Integer parseHour(long time) { return (int)(time / 3600000); }
    public Integer parseMinute(long time, int hour) { return (int)(time - hour * 3600000) / 60000; }
    public Integer parseSecond(long time, int hour, int min) { return (int)(time - hour * 3600000 - min * 60000) / 1000; }
    public Integer parseMS(String[] timeList) { return Integer.parseInt(timeList[0]) * 3600000 + Integer.parseInt(timeList[1]) * 60000 + Integer.parseInt(timeList[2]) * 1000; }
    
    // 측정 도중 자정이 지나면 어제 공부한 시간을 구해주는 메소드
    public String yesterTime(Date time, Date thisTime) {
        long diff = time.getTime() - thisTime.getTime();
        int diffHours = (int) diff / 3600000;
        int diffMinutes = (int) (diff - diffHours * 3600000) / 60000;
        int diffSeconds = (int) (diff - diffHours * 3600000 - diffMinutes * 60000) / 1000;
        return String.format("%02d:%02d:%02d", diffHours, diffMinutes, diffSeconds);
    }

    public void insertStats(Date date, String totalTime, String concenTime) {
        DayStats dayStats = new DayStats();
        dayStats.setStatsDate(date);
        dayStats.setStudyTime(totalTime);
        dayStats.setConcenTime(concenTime);
        new Thread(() -> {
            db.statsDao().insert(dayStats);
        }).start();
    }

    // 그 날의 통계 데이터가 있는 경우 update, 없는 경우 insert 하는 메소드
    public void insertORupdateStats(Date date, String totalTime, String concenTime) {
        DayStats dayStats = new DayStats();
        dayStats.setStatsDate(date);
        dayStats.setStudyTime(totalTime);
        dayStats.setConcenTime(concenTime);
        new Thread(() -> {
            int count = db.statsDao().countDayStat(date.getTime());
            if(count > 0) {
                db.statsDao().updateTimes(totalTime, concenTime, date.getTime());
            } else {
                db.statsDao().insert(dayStats);
            }
        }).start();
    }

    class TimerHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            // 시간 계산 & 출력
            cm_concenTime.setOnChronometerTickListener(new Chronometer.OnChronometerTickListener() {
                // Chronometer가 변경될 때 호출되는 callback
                @Override
                public void onChronometerTick(Chronometer cArg) {
                    // elapsedRealtime(): 부팅 이후의 시간을 리턴, Sleep으로 소모된 시간 포함
                    // cArg.getBase(): Chronometer에 설정된 base time 반환
                    long time = SystemClock.elapsedRealtime() - cArg.getBase();
                    // 현재 집중 시간
                    int conHour = parseHour(time);
                    int conMin = parseMinute(time, conHour);
                    int conSec = parseSecond(time, conHour, conMin);
                    cArg.setText(String.format("%02d:%02d:%02d", conHour, conMin, conSec));
                    // 총 공부 시간
                    int totTime = (int)(time + parseMS(totalTimeList));
                    int totHour = parseHour(totTime);
                    int totMin = parseMinute(totTime, totHour);
                    int totSec = parseSecond(totTime, totHour, totMin);
                    tv_mTime.setText(String.format("%02d:%02d:%02d", totHour, totMin, totSec));
                    // 목표별 공부 시간
                    int goalTime = (int)(time + parseMS(goalTimeList));
                    int goalHour = parseHour(goalTime);
                    int goalMin = parseMinute(goalTime, goalHour);
                    int goalSec = parseSecond(goalTime, goalHour, goalMin);
                    tv_goalTime.setText(String.format("%02d:%02d:%02d", goalHour, goalMin, goalSec));
                }
            });
            cm_concenTime.setBase(SystemClock.elapsedRealtime());       // 카운트 업 되는 timer 설정
            cm_concenTime.start();                                      // 카운팅 시작
        }
    }

    @Override
    public void onBackPressed() {
        // 뒤로가기 버튼 막기
        Toast.makeText(getApplicationContext(), "측정을 그만하고 싶다면 중지 버튼을 눌러주세요.", Toast.LENGTH_SHORT).show();
    }
}