package com.inhatc.study_project.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.inhatc.study_project.R;
import com.inhatc.study_project.data.Reward;

import java.util.ArrayList;
import java.util.List;

public class RewardListAdapter extends RecyclerView.Adapter<RewardListAdapter.ViewHolder> {
    private List<RewardListItem> mData = new ArrayList<>();
    private Context mContext;

    public List<RewardListItem> getItems() {return mData;}

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        mContext = parent.getContext();
        View holder = LayoutInflater.from(parent.getContext()).inflate(R.layout.rewardlist_item, parent, false);
        return new ViewHolder(holder);
    }

    // onBindViewHolder : position에 해당하는 데이터를 뷰홀더의 아이템뷰에 표시
    // 재활용 되는 View가 호출, Adapter가 해당 position에 해당하는 데이터 결합
    @Override
    public void onBindViewHolder(@NonNull RewardListAdapter.ViewHolder holder, int position) {
        holder.onBind(mData.get(position), position);
    }

    // getItemCount : 전체 아이템 갯수 return
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // 아이템 뷰를 저장하는 뷰홀더 클래스
    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView count;
        private TextView name;

        ViewHolder(@NonNull View itemView) {
            super(itemView);

            // 뷰 객체에 대한 참조
            count = itemView.findViewById(R.id.reward_count);
            name = itemView.findViewById(R.id.reward_name);
        }

        public void onBind(RewardListItem rewardListItem, int position) {
            count.setText(rewardListItem.getRewardCount());
            name.setText(rewardListItem.getRewardName());
        }
    }
    public void setItem(List<Reward> data, String rewardCount) {
        mData.clear();
        int nowReward = Integer.parseInt(rewardCount.replace("개", ""));
        for(int i=0; i<data.size(); i++) {
            int rewardNum = Integer.parseInt(data.get(i).getBadge().replace("개", ""));
            RewardListItem item;
            if (nowReward >= rewardNum) {
                item = new RewardListItem(data.get(i).getBadge()+"  \uD83C\uDF81", data.get(i).getRewardName());
            } else {
                item = new RewardListItem(data.get(i).getBadge(), data.get(i).getRewardName());
            }
            mData.add(item);
        }
        notifyDataSetChanged();
    }
}
