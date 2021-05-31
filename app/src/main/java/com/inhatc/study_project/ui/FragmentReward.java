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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.inhatc.study_project.R;
import com.inhatc.study_project.adapter.RecyclerViewAdapter;
import com.inhatc.study_project.adapter.RewardListAdapter;
import com.inhatc.study_project.data.AppDatabase;
import com.inhatc.study_project.data.Goal;
import com.inhatc.study_project.data.Reward;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class FragmentReward extends Fragment implements View.OnClickListener {
    private AppDatabase db;
    private TextView rewardCount;
    private Button btnshowDialog, btnAdd, btnCancel;
    private RecyclerView rewardList;
    private RewardListAdapter mAdapter;
    private Spinner badgeSpinner;
    private EditText rewardName;
    private Dialog addRewardDialog;
    private int nowReward, selCount;
    private String selSpinner;
    private Boolean isAdd;
    RewardHandler rHandler = new RewardHandler();
    ToastHandler tHandler = new ToastHandler();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reward, container, false);

        rewardCount = (TextView) view.findViewById(R.id.tv_usersReward);
        rewardList = view.findViewById(R.id.rewardList);
        btnshowDialog = (Button) view.findViewById(R.id.btn_showAddDialog);

        btnshowDialog.setOnClickListener(this);

        db = AppDatabase.getAppDatabase(getContext());

        // recyclerView 연결
        rewardList.setHasFixedSize(true);
        rewardList.setLayoutManager(new LinearLayoutManager(this.getContext()));
        mAdapter = new RewardListAdapter();
        rewardList.setAdapter(mAdapter);

        // 오늘 날짜 가져와서 초기화하는 날인지 확인
        Date dToday = new Date();
        if(dToday.getMonth()+1 <= 3) {
            Date start = new Date(dToday.getYear(), 0, 1, 0,0,0);
            getRewardData(start, dToday);
        } else if(dToday.getMonth()+1 <= 6) {
            Date start = new Date(dToday.getYear(), 3, 1, 0,0,0);
            getRewardData(start, dToday);
        } else if(dToday.getMonth()+1 <= 9) {
            Date start = new Date(dToday.getYear(), 6, 1, 0,0,0);
            getRewardData(start, dToday);
        } else {
            Date start = new Date(dToday.getYear(), 9, 1, 0,0,0);
            getRewardData(start, dToday);
        }

        db.rewardDao().getRewardList().observe(getActivity(), new Observer<List<Reward>>() {
            @Override
            public void onChanged(List<Reward> rewards) {
                mAdapter.setItem(rewards, rewardCount.getText().toString());
            }
        });
        return view;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_showAddDialog :
                showAddRewardDialog();
                break;
            case R.id.btn_cancel :
                addRewardDialog.dismiss();
                break;
            case R.id.btn_add :
                // 보상명 입력했는지 확인
                if(rewardName.getText().toString().matches("")) {
                    Toast.makeText(getContext(), "갖고 싶은 보상을 입력해주세요", Toast.LENGTH_SHORT).show();
                    break;
                }
                // 선택한 개수 데이터가 있는지 확인
                new Thread(() -> {
                    selCount = db.rewardDao().getDataCount(selSpinner);
                    if(selCount > 0) {
                        isAdd = false;
                        Message msg = tHandler.obtainMessage();
                        tHandler.sendMessage(msg);
                    } else {
                        isAdd = true;
                        Reward reward = new Reward();
                        reward.setBadge(selSpinner);
                        reward.setRewardName(rewardName.getText().toString());
                        db.rewardDao().insert(reward);
                        Message msg = tHandler.obtainMessage();
                        tHandler.sendMessage(msg);
                    }
                }).start();
                break;
        }
    }

    public void showAddRewardDialog() {
        addRewardDialog = new Dialog(getContext());
        addRewardDialog.setContentView(R.layout.dialog_addreward);
        addRewardDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        addRewardDialog.show();

        btnAdd = addRewardDialog.findViewById(R.id.btn_add);
        btnCancel = addRewardDialog.findViewById(R.id.btn_cancel);
        badgeSpinner = (Spinner)addRewardDialog.findViewById(R.id.badge_spinner);
        rewardName = addRewardDialog.findViewById(R.id.edt_rewardName);
        btnAdd.setOnClickListener(this);
        btnCancel.setOnClickListener(this);

        // 스피너 설정
        String[] items = getResources().getStringArray(R.array.badge);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, items);
        badgeSpinner.setAdapter(adapter);
        badgeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selSpinner = items[position];
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    public void getRewardData(Date startDate, Date nowDate) {
        new Thread(() -> {
            Date endDate = new Date(nowDate.getYear(), nowDate.getMonth(), nowDate.getDate(), 0, 0, 0);
            nowReward = db.goalDao().getOKGoals(startDate.getTime(), endDate.getTime());
            Message msg = rHandler.obtainMessage();
            rHandler.sendMessage(msg);
        }).start();
    }

    class RewardHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            rewardCount.setText(nowReward + "개");
        }
    }

    class ToastHandler extends Handler {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            if(isAdd) {
                Toast.makeText(getContext(), "완료되었습니다.", Toast.LENGTH_SHORT).show();
                addRewardDialog.dismiss();
            } else {
                Toast.makeText(getContext(), selSpinner+"는 이미 추가되어 있습니다. 다시 선택해주세요.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}