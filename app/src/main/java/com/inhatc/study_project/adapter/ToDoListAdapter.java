package com.inhatc.study_project.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inhatc.study_project.R;
import com.inhatc.study_project.data.AppDatabase;
import com.inhatc.study_project.data.Dday;
import com.inhatc.study_project.data.Goal;
import com.inhatc.study_project.ui.DelayGoal;

import java.util.ArrayList;
import java.util.List;

public class ToDoListAdapter extends RecyclerView.Adapter<ToDoListAdapter.ViewHolder> {
    private List<TodoListItem> mData = new ArrayList<>();
    private Context mContext;
    private Dialog procGoalDialog;
    private AppDatabase db;

    public ToDoListAdapter(AppDatabase db) {
        this.db = db;
    }

    public List<TodoListItem> getItems() {return mData;}

    // onCreateViewHolder : 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    @NonNull
    @Override
    public ToDoListAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.todolist_item, parent, false);
        return new ViewHolder(holder);
    }

    // onBindViewHolder : position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    @Override
    public void onBindViewHolder(@NonNull ToDoListAdapter.ViewHolder holder, int position) {
        holder.onBind(mData.get(position), position);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView range;
        private TextView fallRange;
        private TextView mark;
        private int index;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조
            name = itemView.findViewById(R.id.todoItem_name);
            range = itemView.findViewById(R.id.todoItem_range);
            fallRange = itemView.findViewById(R.id.todoItem_fallRange);
            mark = itemView.findViewById(R.id.todoItem_mark);

            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();                 // 사용자가 클릭한 아이템의 position을 줌
                    if (pos != RecyclerView.NO_POSITION) {          // 포지션이 recyclerView의 item을 클릭한 것인지 item이 아닌 다른 클릭인지 여부 확인
                        if (!mData.get(pos).getMark().toString().equals("D-day !!"))
                            showProcGoalDialog(mData.get(pos), pos);
                    }
                }
            });
        }

        public void onBind(TodoListItem todoListItem, int position) {
            index = position;
            name.setText(todoListItem.getName());
            range.setText(todoListItem.getRange());
            mark.setText(todoListItem.getMark());
            fallRange.setText(todoListItem.getFallRange());
        }
    }

    public void setItem(List<Goal> goalData, List<Dday> dDayData) {
        mData.clear();
        for(int i=0; i<goalData.size(); i++) {
            TodoListItem item;
            if (goalData.get(i).getFallQuantity() == 0) {
                item = new TodoListItem(goalData.get(i).getGoalID(), goalData.get(i).getGoalName(), "", String.format("(%s%s)", goalData.get(i).getQuantity(), goalData.get(i).getRangeValue()),
                        goalData.get(i).getState(), goalData.get(i).getQuantity());
            } else {
                item = new TodoListItem(goalData.get(i).getGoalID(), goalData.get(i).getGoalName(), String.format("(%s%s)", goalData.get(i).getFallQuantity(), goalData.get(i).getRangeValue()),
                        String.format("(%s%s)", goalData.get(i).getQuantity(), goalData.get(i).getRangeValue()), goalData.get(i).getState(), goalData.get(i).getQuantity());
            }
            System.out.println(item);
            mData.add(item);
        }
        for(int i=0; i<dDayData.size(); i++) {
            TodoListItem item;
            item = new TodoListItem(0, dDayData.get(i).getDdayName(),"", "","D-day !!", 0);
            mData.add(item);
        }
        notifyDataSetChanged();
    }

    public void showProcGoalDialog(TodoListItem todoListItem, int pos) {
        procGoalDialog = new Dialog(mContext);
        procGoalDialog.setContentView(R.layout.dialog_procgoal);
        procGoalDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        procGoalDialog.show();

        TextView tvGoalDialogName = (TextView) procGoalDialog.findViewById(R.id.tvGoalDialogName);
        Button btnNoAchGoal = (Button) procGoalDialog.findViewById(R.id.btnNoAchGoal);
        Button btnDelayGoal = (Button) procGoalDialog.findViewById(R.id.btnDelayGoal);
        Button btnAchGoal = (Button) procGoalDialog.findViewById(R.id.btnAchGoal);

        tvGoalDialogName.setText(todoListItem.getName());
        // 미달성 버튼 눌렀을 때
        btnNoAchGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(() -> {
                    db.goalDao().deleteDelayGoalID(mData.get(pos).getName(), mData.get(pos).getId());           // 미룬 목표가 있을 경우 삭제하기
                    db.goalDao().updateState("X", 0, todoListItem.getId());
                }).start();
                mData.set(pos, new TodoListItem(todoListItem.getId(), todoListItem.getName(), "", todoListItem.getRange(), "X", 0));
                notifyDataSetChanged();
                procGoalDialog.dismiss();
            }
        });
        // 미룸 버튼 눌렀을 때
        btnDelayGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!todoListItem.getMark().equals("→")) {
                    Intent intentDelayGoal = new Intent(mContext, DelayGoal.class);
                    intentDelayGoal.putExtra("goalID", todoListItem.getId());
                    mContext.startActivity(intentDelayGoal);
                } else {
                    Toast.makeText(mContext, "이미 미룬 목표입니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
        // 달성 버튼 눌렀을 때
        btnAchGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Thread(() -> {
                    db.goalDao().deleteDelayGoalID(mData.get(pos).getName(), mData.get(pos).getId());           // 미룬 목표가 있을 경우 삭제하기
                    db.goalDao().updateState("O", 0, todoListItem.getId());
                }).start();
                mData.set(pos, new TodoListItem(todoListItem.getId(), todoListItem.getName(), "", todoListItem.getRange(), "O", todoListItem.getBasicRange()));
                notifyDataSetChanged();
                procGoalDialog.dismiss();
            }
        });
    }
}
