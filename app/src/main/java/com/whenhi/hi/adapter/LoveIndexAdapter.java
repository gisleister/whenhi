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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
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
import com.whenhi.hi.model.Image;
import com.whenhi.hi.network.HttpAPI;
import com.whenhi.hi.util.ClickUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class LoveIndexAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG =LoveIndexAdapter.class.getSimpleName();

    private final List<Feed> mDataList;
    private GlideListener mGlideListener;

    protected OnItemClickListener mOnItemClickListener;

    protected OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }


    public LoveIndexAdapter() {
        mDataList = new ArrayList<>();
        mGlideListener = new GlideListener();
    }

    public void setList(List<Feed> feeds) {
        mDataList.clear();
        append(feeds);
    }

    public void append(List<Feed> feeds) {
        mDataList.addAll(feeds);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = inflate(viewGroup, R.layout.item_ecplore_love);
        final FeedHolder holder = new FeedHolder(itemView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position = holder.getAdapterPosition();
                Feed feed = mDataList.get(position);
                mOnItemClickListener.onItemClick(position, feed, view);
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int position = holder.getAdapterPosition();
                Feed feed = mDataList.get(position);
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
        onBindFeedHolder((FeedHolder) viewHolder,i);
    }


    private void onBindFeedHolder(final FeedHolder holder, int position) {
        Feed feed = mDataList.get(position);
        Context context = App.getContext();

        mGlideListener.setErroReport(true);
        mGlideListener.setFeedCategory(feed.getFeedCategory());
        mGlideListener.setFeedId(feed.getId());

        WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(holder.avatarUser);
        ImageView target = imageViewWeakReference.get();

        Glide.with(context)
                .load(feed.getUserLogo())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .transform(new CircleTransform(context))
                .error(R.mipmap.user_default)
                .into(target);

        showContent(holder,feed,context);//显示内容
        showToolbarContent(holder,feed,context);//显示工具栏

    }



    static class FeedHolder extends RecyclerView.ViewHolder {

        ImageView avatarUser;
        TextView nicknameUser;
        ImageView imageContent;
        TextView textContent;
        ImageView imagePlay;
        Button loveBtn;
        Button favBtn;
        Button shareBtn;
        Button commentBtn;

        public FeedHolder(View itemView) {
            super(itemView);
            avatarUser = (ImageView) itemView.findViewById(R.id.item_user).findViewById(R.id.user_avatar);
            nicknameUser = (TextView) itemView.findViewById(R.id.item_user).findViewById(R.id.user_nickname);

            imageContent = (ImageView) itemView.findViewById(R.id.item_explore_love_content_image);
            textContent = (TextView) itemView.findViewById(R.id.item_explore_love_content_text);

            imagePlay = (ImageView) itemView.findViewById(R.id.item_explore_love_content_play);
            loveBtn = (Button) itemView.findViewById(R.id.item_toolbar).findViewById(R.id.toolbar_love);
            shareBtn = (Button) itemView.findViewById(R.id.item_toolbar).findViewById(R.id.toolbar_share);
            favBtn = (Button) itemView.findViewById(R.id.item_toolbar).findViewById(R.id.toolbar_fav);
            commentBtn = (Button) itemView.findViewById(R.id.item_toolbar).findViewById(R.id.toolbar_comment);
            shareBtn.setVisibility(View.GONE);
            commentBtn.setVisibility(View.GONE);


        }
    }

    private void showContent(FeedHolder holder, Feed feed,Context context){

        WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(holder.imageContent);
        ImageView target = imageViewWeakReference.get();

        int width = App.deviceWidth();
        int height = 202;
        //String para = "?imageMogr2/gravity/SouthWest/crop/" + width + "x" + height;
        String para = "";

        if(feed.getFeedCategory() == 1){//视频
            holder.textContent.setText(feed.getContent());
            holder.imagePlay.setVisibility(View.VISIBLE);
            holder.imageContent.setVisibility(View.VISIBLE);
            holder.textContent.setVisibility(View.VISIBLE);
            if (target != null) {
                Glide.with(context)
                        .load(feed.getImageUrl()+para)
                        .centerCrop()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .listener(mGlideListener.getListener())
                        .error(R.mipmap.bg_image)
                        //.override(width, height)
                        .into(target);
            }

        }else if(feed.getFeedCategory() == 2){//动图

        }else if(feed.getFeedCategory() == 3){//漫画
            holder.textContent.setText(feed.getContent());
            holder.imagePlay.setVisibility(View.GONE);
            holder.imageContent.setVisibility(View.VISIBLE);
            holder.textContent.setVisibility(View.VISIBLE);
            List<Image> images = feed.getResList();
            if(images != null){
                if(images.size() > 0){
                    Image image = images.get(0);
                    if (target != null) {
                        Glide.with(context)
                                .load(image.getContent()+para)
                                .centerCrop()
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .listener(mGlideListener.getListener())
                                .error(R.mipmap.bg_image)
                                //.override(width, height)
                                .into(target);
                    }

                }
            }


        }else if(feed.getFeedCategory() == 4){//段子
            holder.textContent.setText(feed.getContent());
            holder.imageContent.setVisibility(View.GONE);
            holder.imagePlay.setVisibility(View.GONE);
            holder.textContent.setVisibility(View.VISIBLE);
        }else if(feed.getFeedCategory() == 5){//图片
            holder.textContent.setText(feed.getContent());
            holder.imagePlay.setVisibility(View.GONE);
            holder.imageContent.setVisibility(View.VISIBLE);
            holder.textContent.setVisibility(View.VISIBLE);

            List<Image> images = feed.getResList();
            if(images != null){
                if(images.size() > 0){
                    Image image = images.get(0);
                    if (target != null) {
                        Glide.with(context)
                                .load(image.getContent()+para)
                                .centerCrop()
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .listener(mGlideListener.getListener())
                                .error(R.mipmap.bg_image)
                                //.override(width, height)
                                .into(target);
                    }

                }
            }

        }else if(feed.getFeedCategory() == 6){//广告

        }else if(feed.getFeedCategory() == 7){//分割线

        }else if(feed.getFeedCategory() == 8){//彩蛋
            holder.textContent.setText(feed.getMaskContent());
            holder.imagePlay.setVisibility(View.GONE);
            holder.imageContent.setVisibility(View.VISIBLE);
            holder.textContent.setVisibility(View.GONE);
            if (target != null) {
                Glide.with(context)
                        .load(feed.getMaskUrl()+para)
                        .centerCrop()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .listener(mGlideListener.getListener())
                        .error(R.mipmap.bg_image)
                        //.override(width, height)
                        .into(target);
            }
        }


    }

    private void showToolbarContent(FeedHolder holder, Feed feed,Context context){
        holder.nicknameUser.setText(feed.getUserName());
        holder.loveBtn.setText(""+feed.getLikeCount());
        holder.favBtn.setText(""+feed.getFavoriteCount());
        holder.shareBtn.setText(""+feed.getShareCount());
        holder.commentBtn.setText(""+feed.getCommentCount());

        if(feed.getLikeState() == 1){
            Drawable drawable = context.getResources().getDrawable(R.mipmap.zan_click);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.loveBtn.setCompoundDrawables(drawable, null, null, null);
        }else{
            Drawable drawable = context.getResources().getDrawable(R.mipmap.zan);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.loveBtn.setCompoundDrawables(drawable, null, null, null);
        }

        if(feed.getFavoriteState() == 1){
            Drawable drawable = context.getResources().getDrawable(R.mipmap.shoucang_click);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.favBtn.setCompoundDrawables(drawable, null, null, null);
        }else{
            Drawable drawable = context.getResources().getDrawable(R.mipmap.shoucang);
            drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
            holder.favBtn.setCompoundDrawables(drawable, null, null, null);
        }
        ClickUtil.toolbarClick(holder.loveBtn,holder.shareBtn,holder.favBtn,holder.commentBtn,context,holder.itemView,feed);

    }






}
