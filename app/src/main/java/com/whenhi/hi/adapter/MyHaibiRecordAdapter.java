package com.whenhi.hi.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.whenhi.hi.App;
import com.whenhi.hi.R;
import com.whenhi.hi.activity.MoneyActivity;
import com.whenhi.hi.activity.RewardActivity;
import com.whenhi.hi.model.UserIncomeRecord;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王雷 on 2017/2/21.
 */

public class MyHaibiRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private static final int TYPE_HEADER = 0;
    private static final int TYPE_DETAIL = 1;
    private static final int TYPE_GROUP = 2;
    private static final int TYPE_CHILD = 3;


    private final List<UserIncomeRecord> mDataList;



    public MyHaibiRecordAdapter() {

        mDataList = new ArrayList<>();


    }

    public void setList(List<UserIncomeRecord> userIncomeRecords) {
        mDataList.clear();

        append(userIncomeRecords);
    }

    public void append(List<UserIncomeRecord> userIncomeRecords) {
        mDataList.addAll(userIncomeRecords);
        if(userIncomeRecords.size() > 0){
            notifyDataSetChanged();
        }

    }


    @Override
    public int getItemViewType(int position) {
        if (position == 0) {
            return TYPE_HEADER;
        } else if (position == 1) {
            return TYPE_DETAIL;
        } else if (position == 2) {
            return TYPE_GROUP;
        } else {
            return TYPE_CHILD;
        }
    }

    @Override
    public int getItemCount() {
        return mDataList.size() + 3;//因为多了一个header,内容和详情 所以ITEM的 的游标 +1 +1 +1
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        int type = getItemViewType(i);
        View itemView = null;
        switch (type) {
            case TYPE_HEADER:
                itemView = inflate(viewGroup, R.layout.item_my_haibi_header);
                return new HeaderHolder(itemView);
            case TYPE_DETAIL:
                itemView = inflate(viewGroup, R.layout.item_my_haibi_sum);
                return new DetailHolder(itemView);
            case TYPE_GROUP:
                itemView = inflate(viewGroup, R.layout.item_my_haibi_group);
                return new GroupHolder(itemView);
            case TYPE_CHILD:
                itemView = inflate(viewGroup, R.layout.item_my_haibi_record);
                final ChildHolder holder = new ChildHolder(itemView);

                return holder;
        }
        throw new IllegalArgumentException("Wrong type!");
    }

    private View inflate(ViewGroup parent, int layoutRes) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        int type = getItemViewType(i);
        switch (type) {
            case TYPE_HEADER:
                onBindHeaderHolder((HeaderHolder) viewHolder);
                break;
            case TYPE_DETAIL:
                onBindDetailHolder((DetailHolder) viewHolder);
                break;
            case TYPE_GROUP:
                onBindGroupHolder((GroupHolder) viewHolder);
                break;
            case TYPE_CHILD:
                onBindChildHolder((ChildHolder) viewHolder, getChildPosition(i));
                break;
        }
    }

    private void onBindHeaderHolder(HeaderHolder holder) {
        holder.zhengce.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), RewardActivity.class);
                v.getContext().startActivity(intent);
            }
        });
    }

    private void onBindDetailHolder(DetailHolder holder) {

        holder.haibi.setText(""+App.getHaibiNum());
        holder.tixian.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MoneyActivity.class);
                v.getContext().startActivity(intent);
            }
        });


    }

    private void onBindGroupHolder(final GroupHolder holder) {
        //holder.headerText.setText("热门评论");

    }

    private void onBindChildHolder(ChildHolder holder, int childPosition) {
        UserIncomeRecord user = mDataList.get(childPosition);
        holder.userIncomeTime.setText(user.getCreateTimeNice());

        String incomeText = "";
        int income = user.getScore();
        if(income > 0){
            incomeText = "+"+income+"";
        }else {
            incomeText = ""+income+"";
        }
        holder.userIncome.setText(incomeText);
        holder.userIncomeContent.setText(user.getCategoryTitle());

    }



    int getChildPosition(int position) {

        int childPositionInGroup = position - 1 - 1 -1;//评论的位置在header详情和和group的下面所以要 -1 -1 -1
        return childPositionInGroup;
    }



    static class HeaderHolder extends RecyclerView.ViewHolder {


        TextView zhengce;

        public HeaderHolder(View itemView) {
            super(itemView);
            zhengce = (TextView) itemView.findViewById(R.id.jiangli_text);

        }
    }

    static class DetailHolder extends RecyclerView.ViewHolder {
        TextView haibi;
        Button tixian;

        public DetailHolder(View itemView) {
            super(itemView);
            haibi = (TextView) itemView.findViewById(R.id.haibi_num);
            tixian = (Button) itemView.findViewById(R.id.tixian);
        }
    }

    static class GroupHolder extends RecyclerView.ViewHolder {


        public GroupHolder(View itemView) {
            super(itemView);


        }
    }

    static class ChildHolder extends RecyclerView.ViewHolder {
        TextView userIncomeTime;
        TextView userIncome;
        TextView userIncomeContent;


        public ChildHolder(View itemView) {
            super(itemView);
            userIncomeTime = (TextView) itemView.findViewById(R.id.item_user_income_time);
            userIncome = (TextView) itemView.findViewById(R.id.item_user_income_text);
            userIncomeContent = (TextView) itemView.findViewById(R.id.item_user_income_content);


        }
    }




}
