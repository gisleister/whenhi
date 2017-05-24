package com.whenhi.hi.adapter;

import android.content.Context;
import android.content.Intent;
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
import com.whenhi.hi.activity.HaibiIndexActivity;
import com.whenhi.hi.image.CircleTransform;
import com.whenhi.hi.listener.GlideListener;
import com.whenhi.hi.listener.OnItemClickListener;
import com.whenhi.hi.listener.OnItemLongClickListener;
import com.whenhi.hi.model.User;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class HaibiIndexAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG =HaibiIndexAdapter.class.getSimpleName();

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


    public HaibiIndexAdapter() {
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
        View itemView = inflate(viewGroup, R.layout.item_explore_income);
        final FeedHolder holder = new FeedHolder(itemView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), HaibiIndexActivity.class);
                view.getContext().startActivity(intent);
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

        if(position == 0){
            holder.incomeText.setText(""+user.getScore());
            holder.incomeText.setBackgroundResource(R.drawable.shape_no2);
            holder.incomeImage.setImageResource(R.mipmap.tansuo_paihang2);
        }else if(position == 1){
            holder.incomeText.setText(""+user.getScore());
            holder.incomeImage.setImageResource(R.mipmap.tansuo_paihang1);
            holder.incomeText.setBackgroundResource(R.drawable.shape_no1);
        }else if(position == 2){
            holder.incomeText.setText(""+user.getScore());
            holder.incomeImage.setImageResource(R.mipmap.tansuo_paihang3);
            holder.incomeText.setBackgroundResource(R.drawable.shape_no3);
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
        Button incomeText;
        ImageView incomeImage;


        public FeedHolder(View itemView) {
            super(itemView);
            avatarUser = (ImageView) itemView.findViewById(R.id.user_avatar);
            nicknameUser = (TextView) itemView.findViewById(R.id.user_nickname);

            incomeText = (Button) itemView.findViewById(R.id.user_income_text);
            incomeImage = (ImageView) itemView.findViewById(R.id.user_income_image);


        }
    }




}
