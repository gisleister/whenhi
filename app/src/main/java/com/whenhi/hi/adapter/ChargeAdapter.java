package com.whenhi.hi.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.whenhi.hi.R;
import com.whenhi.hi.image.CircleTransform;
import com.whenhi.hi.listener.GlideListener;
import com.whenhi.hi.listener.OnItemClickListener;
import com.whenhi.hi.listener.OnItemLongClickListener;
import com.whenhi.hi.model.Charge;
import com.whenhi.hi.model.Feed;
import com.whenhi.hi.model.Image;
import com.whenhi.hi.util.ClickUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;


/**
 *
 */
public class ChargeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG =ChargeAdapter.class.getSimpleName();

    private final List<Charge> mDataList;
    private int lastPosition = -1;   //lastPosition 记录上一次选中的图片位置，-1表示未选中
    public Vector<Boolean> vector = new Vector<Boolean>();// 定义一个向量作为选中与否容器

    protected OnItemClickListener mOnItemClickListener;

    protected OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }


    public ChargeAdapter() {
        mDataList = new ArrayList<>();
    }

    public void setList(List<Charge> charges) {
        mDataList.clear();
        append(charges);
    }

    public void append(List<Charge> charges) {
        mDataList.addAll(charges);
        for (int i = 0; i < mDataList.size(); i++) {
            vector.add(false);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    public void changeState(int position){
        if(lastPosition != -1)
            vector.setElementAt(false, lastPosition);                   //取消上一次的选中状态
        vector.setElementAt(!vector.elementAt(position), position);     //直接取反即可
        lastPosition = position;                                        //记录本次选中的位置
        notifyDataSetChanged();                                         //通知适配器进行更新
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = inflate(viewGroup, R.layout.item_charge);
        final ChargeHolder holder = new ChargeHolder(itemView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Charge charge = mDataList.get(position);
                changeState(position);
                mOnItemClickListener.onItemClick(position, charge, view);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int position = holder.getAdapterPosition();
                Charge charge = mDataList.get(position);
                mOnItemLongClickListener.onClickItemLongClick(position, charge, view);
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
        onBindFeedHolder((ChargeHolder) viewHolder,i);
    }


    private void onBindFeedHolder(final ChargeHolder holder, int position) {
       Charge charge = mDataList.get(position);
        holder.moneyText.setText(""+charge.getValue()+"元");
        holder.hibiText.setText(""+charge.getScore()+"嗨币");
        if(vector.elementAt(position) == true){
            //noinspection deprecation
            holder.moneyText.setBackgroundResource(R.color.app_sys_color);
            holder.hibiText.setBackgroundResource(R.color.app_sys_color);
        }else{
            //noinspection deprecation
            holder.moneyText.setBackgroundResource(R.color.bg_sys);
            holder.hibiText.setBackgroundResource(R.color.bg_sys);
        }



    }



    class ChargeHolder extends RecyclerView.ViewHolder {

        TextView moneyText;
        TextView hibiText;


        public ChargeHolder(View itemView) {
            super(itemView);
            moneyText = (TextView) itemView.findViewById(R.id.money_text);

            hibiText = (TextView) itemView.findViewById(R.id.hibi_text);




        }
    }

}
