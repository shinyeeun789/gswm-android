package com.inhatc.study_project.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inhatc.study_project.R;
import com.inhatc.study_project.data.Goal;
import com.inhatc.study_project.ui.Stopwatch;

import java.util.ArrayList;
import java.util.List;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    // 어댑터 내부 동작 : getItemCount가 호출되어 총 item의 갯수를 판단하고, onCreateViewHolder에서 viewType에 해당하는 ViewHolder(각 item의 다양한 view 정보를 가지고 있음)를 생성하여 return
    // onBindViewHolder가 호출되어 viewHolder의 객체와 리스트에서 해당 viewHolder의 위치를 인자로 전달, 어댑터는 인자로 받은 위치에 맞는 데이터를 찾은 후 그것을 viewHolder의 view에 결합
    private List<RecyclerViewItem> mData = new ArrayList<>();
    private Context mContext;

    public List<RecyclerViewItem> getItems() {return mData;}

    // onCreateViewHolder : 아이템 뷰를 위한 뷰홀더 객체를 생성하여 리턴
    // 뷰 홀더를 생성하고 뷰를 붙여주는 부분
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.recycler_item, parent, false);
        return new ViewHolder(holder);
    }

    // onBindViewHolder : position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    // 재활용 되는 View가 호출, Adapter가 해당 position에 해당하는 데이터 결합
    @Override
    public void onBindViewHolder(@NonNull RecyclerViewAdapter.ViewHolder holder, int position) {
        holder.onBind(mData.get(position), position);
    }

    // getItemCount : 전체 아이템 갯수 return
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name;
        private TextView time;
        private TextView mark;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조
            name = itemView.findViewById(R.id.recycler_name);
            time = itemView.findViewById(R.id.recycler_time);
            mark = itemView.findViewById(R.id.recycler_mark);

            itemView.setClickable(true);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int pos = getAdapterPosition();                 // 사용자가 클릭한 아이템의 position을 줌
                    if (pos != RecyclerView.NO_POSITION) {          // 포지션이 recyclerView의 item을 클릭한 것인지 item이 아닌 다른 클릭인지 여부 확인
                        // 새로운 액티비티에 현재 item 항목의 name을 전달하기 위해 사용
                        Intent intent = new Intent(mContext, Stopwatch.class).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.putExtra("GoalName", mData.get(pos).getName());
                        intent.putExtra("GoalTime", mData.get(pos).getTime());
                        intent.putExtra("GoalRangeValue", mData.get(pos).getRangeValue());
                        mContext.startActivity(intent);
                    }
                }
            });
        }

        public void onBind(RecyclerViewItem recyclerItem, int position) {
            name.setText(recyclerItem.getName());
            time.setText(recyclerItem.getTime());
            mark.setText(recyclerItem.getMark());
        }
    }
    public void setItem(List<Goal> data) {
        mData.clear();
        if (data.size() != 0) {
            for(int i=0; i<data.size(); i++) {
                RecyclerViewItem item = new RecyclerViewItem(data.get(i).getGoalName(), data.get(i).getGoalStudyTime(), data.get(i).getState(), data.get(i).getRangeValue());
                mData.add(item);
            }
        } else {
            mData.add(new RecyclerViewItem("공부시간 측정", "00:00:00", "-", ""));
        }
        notifyDataSetChanged();
    }
}
