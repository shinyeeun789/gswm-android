package com.inhatc.study_project.ui;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.Toolbar;
import androidx.appcompat.app.ActionBar;

import com.inhatc.study_project.adapter.RecyclerViewAdapter;
import com.inhatc.study_project.R;
import com.inhatc.study_project.data.AppDatabase;
import com.inhatc.study_project.data.DayStats;
import com.inhatc.study_project.data.Dday;
import com.inhatc.study_project.data.Goal;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FragmentHome extends Fragment implements View.OnClickListener {
    private TextView tv_Date, tv_totStudyTime, tv_Dday;
    private Button btnBreakStore;
    private EditText edtBreakTime;
    private Toolbar toolbar;
    private ImageButton btnBreakTime;
    private Dialog breakTimeDialog;
    private String date;
    private int breakTime;
    private DayStats dayStats;
    StudyHandler sHandler = new StudyHandler();
    private AppDatabase db;
    SharedPreferences pref;
    SharedPreferences.Editor editor;

    // RecyclerView
    private RecyclerView mRecyclerView = null;
    private RecyclerViewAdapter mAdapter = null;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // inflater는 xml로 정의된 view와 layout을 실제 객체화시킴
        // inflate(객체화하고픈 xml 파일, 객체화한 뷰를 넣을 부모 레이아웃/컨테이너, true(바로 붙이기))
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        tv_Date = (TextView) view.findViewById(R.id.textDate);
        tv_totStudyTime = (TextView) view.findViewById(R.id.textStudyTime);
        tv_Dday = (TextView) view.findViewById(R.id.tvDday);
        mRecyclerView = view.findViewById(R.id.recyclerView);
        toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        btnBreakTime = (ImageButton) view.findViewById(R.id.btnBreakTime);
        
        // toolbar 설정
        ((MainActivity)getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((MainActivity)getActivity()).getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        db = AppDatabase.getAppDatabase(getContext());
        pref = this.getActivity().getSharedPreferences("pref", Context.MODE_PRIVATE);
        editor = pref.edit();

        // 홈 화면에 오늘 날짜, 오늘 공부시간 출력
        Calendar cal = Calendar.getInstance();
        Date dToday = new Date(cal.getTime().getYear(), cal.getTime().getMonth(), cal.getTime().getDate(), 0, 0, 0);
        date = String.format("%04d.%02d.%02d", dToday.getYear()+1900, dToday.getMonth()+1, dToday.getDate());
        tv_Date.setText(date);
        db.statsDao().getHomeData(dToday.getTime()).observe(getActivity(), new Observer<DayStats>() {
            @Override
            public void onChanged(DayStats dbData) {
                dayStats = dbData;
                Message msg = sHandler.obtainMessage();
                sHandler.sendMessage(msg);
            }
        });

        // recyclerView 연결
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mAdapter = new RecyclerViewAdapter();
        mRecyclerView.setAdapter(mAdapter);

        // UI 갱신, 해당 DB 값에 변화가 생기면 실행
        db.goalDao().getListViewItem(dToday.getTime()).observe(getActivity(), new Observer<List<Goal>>() {       // 오늘 날짜의 To-do List 가져오기
            @Override
            public void onChanged(List<Goal> recyclerViewItems) {
                mAdapter.setItem(recyclerViewItems);
            }
        });

        // 휴식 시간 설정
        btnBreakTime.setOnClickListener(this);
        // 디데이 설정 (가장 가까운 디데이 출력)
        Date dBasicDay = new Date();
        db.ddayDao().getHomeDdays(dBasicDay.getTime()).observe(getActivity(), new Observer<Dday>() {
            @Override
            public void onChanged(Dday homeDday) {
                // DB에서 가져온 D-day 날짜
                if(homeDday == null) {
                    tv_Dday.setText("캘린더에서 D-DAY 설정 가능 :)");
                } else {
                    Date dDayDate = homeDday.getDdayDate();
                    double diffDay = (dDayDate.getTime() - dBasicDay.getTime()) / (24.0*60.0*60.0*1000.0);
                    int dDay = (int)Math.ceil(diffDay);
                    tv_Dday.setText(String.format("%s  D-%d", homeDday.getDdayName(), dDay));
                }
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBreakTime :
                showBreakTimeDialog();
                break;
            case R.id.btnBreakStore :
                try {
                    int inputBreak = Integer.parseInt(edtBreakTime.getText().toString());
                    if (inputBreak <= 0) {
                        Toast.makeText(getContext(), "음수와 0은 입력할 수 없습니다.", Toast.LENGTH_SHORT).show();
                    } else {
                        editor.putInt("breakTime", inputBreak);
                        editor.commit();
                        breakTimeDialog.dismiss();
                    }
                } catch (NumberFormatException nEx) {
                    Toast.makeText(getContext(), "숫자로 입력해주세요.", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    public void showBreakTimeDialog() {
        breakTimeDialog = new Dialog(getContext());
        breakTimeDialog.setContentView(R.layout.dialog_breaktime);
        breakTimeDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        breakTimeDialog.show();

        breakTime = pref.getInt("breakTime", 0);
        btnBreakStore = breakTimeDialog.findViewById(R.id.btnBreakStore);
        edtBreakTime = (EditText) breakTimeDialog.findViewById(R.id.edtBreakTime);
        edtBreakTime.setText(breakTime+"");
        btnBreakStore.setOnClickListener(this);
    }

    class StudyHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(dayStats == null) {
                tv_totStudyTime.setText("00:00:00");
            } else {
                tv_totStudyTime.setText(dayStats.getStudyTime());
            }
        }
    }
}
