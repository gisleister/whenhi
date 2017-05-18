package com.whenhi.hi.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.whenhi.hi.R;
import com.whenhi.hi.listener.OnItemClickListener;
import com.whenhi.hi.listener.OnItemLongClickListener;
import com.whenhi.hi.model.ChargeRecord;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class ChargeRecordAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG =ChargeRecordAdapter.class.getSimpleName();

    private final List<ChargeRecord> mDataList;


    protected OnItemClickListener mOnItemClickListener;

    protected OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }


    public ChargeRecordAdapter() {
        mDataList = new ArrayList<>();
    }

    public void setList(List<ChargeRecord> chargeRecords) {
        mDataList.clear();
        append(chargeRecords);
    }

    public void append(List<ChargeRecord> chargeRecords) {
        mDataList.addAll(chargeRecords);
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
        View itemView = inflate(viewGroup, R.layout.item_charge_record);
        final Holder holder = new Holder(itemView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                ChargeRecord feed = mDataList.get(position);
                mOnItemClickListener.onItemClick(position, feed, view);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int position = holder.getAdapterPosition();
                ChargeRecord feed = mDataList.get(position);
                mOnItemLongClickListener.onClickItemLongClick(position, feed, view);
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
        onBindUserScoreHolder((Holder) viewHolder,i);
    }


    private void onBindUserScoreHolder(Holder holder, int position) {
        ChargeRecord chargeRecord = mDataList.get(position);
        holder.chargeRecordTime.setText(chargeRecord.getCreateTimeNice());

        if(chargeRecord.getStatus() == 1){
            holder.chargeRecordStatus.setText("提现成功");
        }else if(chargeRecord.getStatus() == 2){
            holder.chargeRecordStatus.setText("正在处理");
        }else if(chargeRecord.getStatus() == 3){
            holder.chargeRecordStatus.setText("提现失败");
        }

        holder.chargeRecordMoney.setText("提现 " + chargeRecord.getCardValue() + " 元");



    }



    int getPosition(int position) {
        return position;
    }


    static class Holder extends RecyclerView.ViewHolder {

        TextView chargeRecordTime;
        TextView chargeRecordMoney;
        TextView chargeRecordStatus;


        public Holder(View itemView) {
            super(itemView);
            chargeRecordTime = (TextView) itemView.findViewById(R.id.item_charge_record_time);
            chargeRecordMoney = (TextView) itemView.findViewById(R.id.item_charge_record_money);
            chargeRecordStatus = (TextView) itemView.findViewById(R.id.item_charge_record_status);

        }
    }




}
