package com.whenhi.hi.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whenhi.hi.R;
import com.whenhi.hi.listener.OnItemClickListener;
import com.whenhi.hi.listener.OnItemLongClickListener;
import com.whenhi.hi.model.UserIncomeRecord;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class IncomeRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG =IncomeRecordAdapter.class.getSimpleName();

    private final List<UserIncomeRecord> mDataList;


    protected OnItemClickListener mOnItemClickListener;

    protected OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }


    public IncomeRecordAdapter() {
        mDataList = new ArrayList<>();
    }

    public void setList(List<UserIncomeRecord> users) {
        mDataList.clear();
        append(users);
    }

    public void append(List<UserIncomeRecord> users) {
        mDataList.addAll(users);
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = inflate(viewGroup, R.layout.item_user_income_record);
        final UserScoreHolder holder = new UserScoreHolder(itemView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int absolutePosition = holder.getAdapterPosition();
                int position = getPosition(absolutePosition);

            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int absolutePosition = holder.getAdapterPosition();
                int position = getPosition(absolutePosition);

                return true;
            }
        });
        return holder;
    }

    private View inflate(ViewGroup parent, int layoutRes) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        onBindUserScoreHolder((UserScoreHolder) viewHolder,i);
    }


    private void onBindUserScoreHolder(UserScoreHolder holder, int position) {
        UserIncomeRecord user = mDataList.get(position);
        holder.userIncomeTime.setText(user.getCreateTimeNice());

        String incomeText = "";
        int income = user.getScore();
        if(income > 0){
            incomeText = "+"+income+" 嗨币";
        }else {
            incomeText = ""+income+" 嗨币";
        }
        holder.userIncome.setText(incomeText);
        holder.userIncomeContent.setText(user.getCategoryTitle());



    }



    int getPosition(int position) {
        return position;
    }


    static class UserScoreHolder extends RecyclerView.ViewHolder {

        TextView userIncomeTime;
        TextView userIncome;
        TextView userIncomeContent;


        public UserScoreHolder(View itemView) {
            super(itemView);
            userIncomeTime = (TextView) itemView.findViewById(R.id.item_user_income_time);
            userIncome = (TextView) itemView.findViewById(R.id.item_user_income_text);
            userIncomeContent = (TextView) itemView.findViewById(R.id.item_user_income_content);

        }
    }




}
