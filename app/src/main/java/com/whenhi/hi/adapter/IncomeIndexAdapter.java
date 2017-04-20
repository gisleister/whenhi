package com.whenhi.hi.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.whenhi.hi.App;
import com.whenhi.hi.R;
import com.whenhi.hi.activity.LoginActivity;
import com.whenhi.hi.activity.PicActivity;
import com.whenhi.hi.activity.ShareActivity;
import com.whenhi.hi.activity.VideoActivity;
import com.whenhi.hi.activity.WebViewActivity;
import com.whenhi.hi.image.CircleTransform;
import com.whenhi.hi.listener.GlideListener;
import com.whenhi.hi.listener.OnItemClickListener;
import com.whenhi.hi.listener.OnItemLongClickListener;
import com.whenhi.hi.model.BaseModel;
import com.whenhi.hi.model.Feed;
import com.whenhi.hi.model.User;
import com.whenhi.hi.network.HttpAPI;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class IncomeIndexAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG =IncomeIndexAdapter.class.getSimpleName();

    private final List<User> mDataList;
    private GlideListener mGlideListener;

    protected OnItemClickListener mOnItemClickListener;

    protected OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }


    public IncomeIndexAdapter() {
        mDataList = new ArrayList<>();
        mGlideListener = new GlideListener();
    }

    public void setList(List<User> users) {
        mDataList.clear();
        append(users);
    }

    public void append(List<User> users) {
        mDataList.addAll(users);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = inflate(viewGroup, R.layout.item_ecplore_income);
        final FeedHolder holder = new FeedHolder(itemView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                User user = mDataList.get(position);
                mOnItemClickListener.onItemClick(position, user, view);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int position = holder.getAdapterPosition();
                User user = mDataList.get(position);
                mOnItemLongClickListener.onClickItemLongClick(position, user, view);
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
        onBindFeedHolder((FeedHolder) viewHolder,i);
    }


    private void onBindFeedHolder(final FeedHolder holder, int position) {
        User user = mDataList.get(position);
        Context context = App.getContext();


        holder.nicknameUser.setText(user.getUserName());
        holder.textContent.setText(""+user.getScore());

        if(position == 0){
            holder.incomeIndex.setText("No.2");
            holder.incomeImage.setImageResource(R.mipmap.no2);
        }else if(position == 1){
            holder.incomeIndex.setText("No.1");
            holder.incomeImage.setImageResource(R.mipmap.no1);
        }else if(position == 2){
            holder.incomeIndex.setText("No.3");
            holder.incomeImage.setImageResource(R.mipmap.no3);
        }



        Glide.with(context)
                .load(user.getUserLogo())
                .transform(new CircleTransform(context))
                .error(R.mipmap.user_default)
                .into(holder.avatarUser);




    }



    static class FeedHolder extends RecyclerView.ViewHolder {

        ImageView avatarUser;
        TextView nicknameUser;
        TextView textContent;
        TextView incomeIndex;
        ImageView incomeImage;


        public FeedHolder(View itemView) {
            super(itemView);
            avatarUser = (ImageView) itemView.findViewById(R.id.user_avatar);
            nicknameUser = (TextView) itemView.findViewById(R.id.user_nickname);

            textContent = (TextView) itemView.findViewById(R.id.user_income);

            incomeIndex = (TextView) itemView.findViewById(R.id.user_income_index);
            incomeImage = (ImageView) itemView.findViewById(R.id.user_income_image);


        }
    }




}
