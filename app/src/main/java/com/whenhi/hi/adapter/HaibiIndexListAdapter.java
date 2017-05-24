package com.whenhi.hi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.whenhi.hi.App;
import com.whenhi.hi.R;
import com.whenhi.hi.model.UserScore;
import com.whenhi.hi.util.ImageUtil;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class HaibiIndexListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG =HaibiIndexListAdapter.class.getSimpleName();

    private final List<UserScore> mDataList;



    public HaibiIndexListAdapter() {
        mDataList = new ArrayList<>();
    }

    public void setList(List<UserScore> users) {
        mDataList.clear();
        append(users);
    }

    public void append(List<UserScore> users) {
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
        View itemView = inflate(viewGroup, R.layout.item_user_income_index);
        final UserScoreHolder holder = new UserScoreHolder(itemView);

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
        UserScore user = mDataList.get(position);

        int index = position+1;
        holder.userScoreIndex.setText(""+index);
        holder.userNickName.setText(user.getUserName());
        holder.userScore.setText(""+user.getScore() + "");

        Context context = App.getContext();
        ImageUtil.avatarImage(context,user.getUserLogo(),holder.userAvatar);
        //if(index < 4){
         //   holder.userScoreIndex.setTextColor(Color.rgb(255,96,0));
        //}
        if(index == 1){
            holder.userScoreIndexImage.setVisibility(View.VISIBLE);
            holder.userScoreIndex.setVisibility(View.GONE);
            holder.userScoreIndexImage.setImageResource(R.mipmap.paihang_icon1);
        }else if(index == 2){
            holder.userScoreIndexImage.setVisibility(View.VISIBLE);
            holder.userScoreIndex.setVisibility(View.GONE);
            holder.userScoreIndexImage.setImageResource(R.mipmap.paihang_icon2);
        }else if(index == 3){
            holder.userScoreIndexImage.setVisibility(View.VISIBLE);
            holder.userScoreIndex.setVisibility(View.GONE);
            holder.userScoreIndexImage.setImageResource(R.mipmap.paihang_icon3);
        }else{
            holder.userScoreIndexImage.setVisibility(View.GONE);
            holder.userScoreIndex.setVisibility(View.VISIBLE);
        }

    }



    int getPosition(int position) {
        return position;
    }


    static class UserScoreHolder extends RecyclerView.ViewHolder {

        ImageView userAvatar;
        TextView userNickName;
        TextView userScore;
        TextView userScoreIndex;
        ImageView userScoreIndexImage;


        public UserScoreHolder(View itemView) {
            super(itemView);
            userAvatar = (ImageView) itemView.findViewById(R.id.item_user_score_avatar);
            userNickName = (TextView) itemView.findViewById(R.id.item_user_score_nickname);
            userScore = (TextView) itemView.findViewById(R.id.item_user_score_text);
            userScoreIndex = (TextView) itemView.findViewById(R.id.item_user_score_index);
            userScoreIndexImage = (ImageView) itemView.findViewById(R.id.item_user_score_index_image);

        }
    }




}
