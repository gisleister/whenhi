package com.whenhi.hi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.whenhi.hi.App;
import com.whenhi.hi.R;
import com.whenhi.hi.fragment.BaseFragment;
import com.whenhi.hi.image.CircleTransform;
import com.whenhi.hi.image.RoundTransform;
import com.whenhi.hi.listener.GlideListener;
import com.whenhi.hi.listener.OnItemClickListener;
import com.whenhi.hi.listener.OnItemLongClickListener;
import com.whenhi.hi.model.Feed;
import com.whenhi.hi.model.Image;
import com.whenhi.hi.util.ClickUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class FeedListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG =FeedListAdapter.class.getSimpleName();

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


    public FeedListAdapter() {
        mDataList = new ArrayList<>();
        mGlideListener = new GlideListener();
    }

    public void setList(List<Feed> feeds) {
        mDataList.clear();
        append(feeds);
    }

    public void append(List<Feed> feeds) {
        if(feeds != null){
            mDataList.addAll(feeds);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = inflate(viewGroup, R.layout.item_feed_all);
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
        if(feed == null)
            return;
        Context context = App.getContext();

        mGlideListener.setErroReport(true);
        mGlideListener.setFeedCategory(feed.getFeedCategory());
        mGlideListener.setFeedId(feed.getId());


        holder.nicknameUser.setText(feed.getUserName());
        WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(holder.avatarUser);
        ImageView target = imageViewWeakReference.get();

        Glide.with(context)
                .load(feed.getUserLogo())
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .transform(new CircleTransform(context))
                .error(R.mipmap.logo)
                .into(target);

        showContent(holder,feed,context);//显示内容
        showToolbarContent(holder,feed);//显示工具栏
        other(holder,feed.getFeedCategory());//工具栏是否隐藏


    }




    class FeedHolder extends RecyclerView.ViewHolder {

        ImageView avatarUser;
        TextView nicknameUser;
        ImageView imageContent;
        TextView textContent;
        ImageView imagePlay;






        LinearLayout userLayout;
        LinearLayout toolbarLayout;
        RelativeLayout contentImageLayout;
        LinearLayout contentTextLayout;

        ImageView loveImage;
        ImageView favImage;
        ImageView shareImage;
        ImageView commentImage;


        TextView loveText;
        TextView favText;
        TextView shareText;
        TextView commentText;

        Button imageNum;

        public FeedHolder(View itemView) {
            super(itemView);

            userLayout = (LinearLayout) itemView.findViewById(R.id.item_user_layout);
            contentImageLayout = (RelativeLayout) itemView.findViewById(R.id.item_content_image_layout);
            contentTextLayout = (LinearLayout) itemView.findViewById(R.id.item_content_text_layout);




            toolbarLayout = (LinearLayout) itemView.findViewById(R.id.item_toolbar_layout);

            avatarUser = (ImageView) itemView.findViewById(R.id.user_avatar);
            nicknameUser = (TextView) itemView.findViewById(R.id.user_nickname);

            imageContent = (ImageView) itemView.findViewById(R.id.item_content_image);
            textContent = (TextView) itemView.findViewById(R.id.item_content_text);

            imagePlay = (ImageView) itemView.findViewById(R.id.item_content_play);


            loveImage = (ImageView) itemView.findViewById(R.id.toolbar_love_image);
            shareImage = (ImageView) itemView.findViewById(R.id.toolbar_share_image);
            favImage = (ImageView) itemView.findViewById(R.id.toolbar_fav_image);
            commentImage = (ImageView) itemView.findViewById(R.id.toolbar_comment_image);


            loveText = (TextView) itemView.findViewById(R.id.toolbar_love_text);
            shareText = (TextView) itemView.findViewById(R.id.toolbar_share_text);
            favText = (TextView) itemView.findViewById(R.id.toolbar_fav_text);
            commentText = (TextView) itemView.findViewById(R.id.toolbar_comment_text);
            imageNum = (Button) itemView.findViewById(R.id.item_image_num);



        }
    }

    private void other(FeedHolder holder, int type){
        if( type == 8 || type == 10){//彩蛋和抽奖 没有头像
            holder.userLayout.setVisibility(View.GONE);
            holder.toolbarLayout.setVisibility(View.GONE);

        }else{
            holder.userLayout.setVisibility(View.VISIBLE);
            holder.toolbarLayout.setVisibility(View.VISIBLE);
        }



    }



    private void showContent(FeedHolder holder, final Feed feed, Context context){

        WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(holder.imageContent);
        ImageView target = imageViewWeakReference.get();

        int width = App.deviceWidth();
        int height = 202;
        //String para = "?imageMogr2/gravity/SouthWest/crop/" + width + "x" + height;
        String para = "";

        if(feed.getFeedCategory() == 1){//视频
            holder.textContent.setText(feed.getContent());
            holder.imageNum.setVisibility(View.GONE);
            holder.contentImageLayout.setVisibility(View.VISIBLE);
            holder.imagePlay.setImageResource(R.mipmap.shouye_shipin_bofang);
            if(TextUtils.isEmpty(feed.getContent())){
                holder.contentTextLayout.setVisibility(View.GONE);
            }else{
                holder.contentTextLayout.setVisibility(View.VISIBLE);
            }

            if (target != null) {
                Glide.with(context)
                        .load(feed.getImageUrl()+para)
                        .placeholder(R.mipmap.bg_image)
                        .centerCrop()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .listener(mGlideListener.getListener())
                        .transform(new RoundTransform(context,10))
                        .error(R.mipmap.bg_image)
                        //.override(width, height)
                        .into(target);
            }

            holder.contentImageLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClickUtil.goToVideoNew(v.getContext(),feed);
                    ClickUtil.reportClickInfo(feed);
                }
            });

        }else if(feed.getFeedCategory() == 2){//动图

        }else if(feed.getFeedCategory() == 3){//漫画
            holder.textContent.setText(feed.getContent());
            holder.imagePlay.setImageResource(R.mipmap.shouye_tupian_kan);
            if(TextUtils.isEmpty(feed.getContent())){
                holder.contentTextLayout.setVisibility(View.GONE);
            }else{
                holder.contentTextLayout.setVisibility(View.VISIBLE);
            }
            List<Image> images = feed.getResList();
            if(images != null){
                if(images.size() > 0){
                    Image image = images.get(0);
                    if (target != null) {
                        Glide.with(context)
                                .load(image.getContent()+para)
                                .placeholder(R.mipmap.bg_image)
                                .centerCrop()
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .listener(mGlideListener.getListener())
                                .transform(new RoundTransform(context,10))
                                .error(R.mipmap.bg_image)
                                //.override(600, 200)
                                //.fitCenter()
                                .into(target);
                    }

                }
            }


        }else if(feed.getFeedCategory() == 4){//段子
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            lp.setMargins(0, 15, 0, 0);
            holder.textContent.setLayoutParams(lp);
            holder.textContent.setText(feed.getContent());
            holder.imageNum.setVisibility(View.GONE);
            holder.contentImageLayout.setVisibility(View.GONE);
            feed.setImageUrl(App.getUserLogo());//给分享时候图片赋值为用户头像
        }else if(feed.getFeedCategory() == 5){//图片
            holder.textContent.setText(feed.getContent());
            holder.contentImageLayout.setVisibility(View.VISIBLE);
            holder.imagePlay.setImageResource(R.mipmap.shouye_tupian_kan);
            if(TextUtils.isEmpty(feed.getContent())){
                holder.contentTextLayout.setVisibility(View.GONE);
            }else{
                holder.contentTextLayout.setVisibility(View.VISIBLE);
            }

            List<Image> images = feed.getResList();
            if(images != null){
                if(images.size() > 0){
                    holder.imageNum.setVisibility(View.VISIBLE);
                    holder.imageNum.setText(""+images.size());
                    Image image = images.get(0);
                    if (target != null) {
                        Glide.with(context)
                                .load(image.getContent()+para)
                                .placeholder(R.mipmap.bg_image)
                                .centerCrop()
                                .skipMemoryCache(true)
                                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                                .listener(mGlideListener.getListener())
                                .transform(new RoundTransform(context,10))
                                .error(R.mipmap.bg_image)
                                //.override(width, height)
                                .into(new GlideDrawableImageViewTarget(target, 0));
                    }

                }
            }

            holder.contentImageLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClickUtil.goToPicNew(v.getContext(),feed);
                    ClickUtil.reportClickInfo(feed);
                }
            });

        }else if(feed.getFeedCategory() == 6){//广告

        }else if(feed.getFeedCategory() == 7){//分割线

        }else if(feed.getFeedCategory() == 8){//彩蛋
            holder.imageNum.setVisibility(View.GONE);
            holder.contentImageLayout.setVisibility(View.VISIBLE);
            holder.imagePlay.setImageResource(R.mipmap.shouye_huodong_kai);
            holder.contentTextLayout.setVisibility(View.GONE);
            if (target != null) {
                Glide.with(context)
                        .load(feed.getMaskUrl()+para)
                        .placeholder(R.mipmap.bg_image)
                        .centerCrop()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .listener(mGlideListener.getListener())
                        .transform(new RoundTransform(context,10))
                        .error(R.mipmap.bg_image)
                        //.override(width, height)
                        .into(target);
            }
        }else if(feed.getFeedCategory() == 9){//webview
            holder.imageNum.setVisibility(View.GONE);
            holder.textContent.setText(feed.getSummary());
            holder.contentImageLayout.setVisibility(View.VISIBLE);
            holder.imagePlay.setImageResource(R.mipmap.shouye_tupian_kan);
            if(TextUtils.isEmpty(feed.getSummary())){
                holder.contentTextLayout.setVisibility(View.GONE);
            }else{
                holder.contentTextLayout.setVisibility(View.VISIBLE);
            }
            if (target != null) {
                Glide.with(context)
                        .load(feed.getImageUrl()+para)
                        .placeholder(R.mipmap.bg_image)
                        .centerCrop()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .listener(mGlideListener.getListener())
                        .transform(new RoundTransform(context,10))
                        .error(R.mipmap.bg_image)
                        //.override(width, height)
                        .into(target);
            }

            feed.setContent(feed.getSummary());

        }else if(feed.getFeedCategory() == 10){//抽奖
            //holder.textContent.setText(feed.getMaskContent());
            holder.imageNum.setVisibility(View.GONE);
            holder.contentImageLayout.setVisibility(View.VISIBLE);
            holder.imagePlay.setImageResource(R.mipmap.shouye_huodong_kai);
            holder.contentTextLayout.setVisibility(View.GONE);
            if (target != null) {
                Glide.with(context)
                        .load(feed.getPicUrl()+para)
                        .placeholder(R.mipmap.bg_image)
                        .centerCrop()
                        .skipMemoryCache(true)
                        .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                        .listener(mGlideListener.getListener())
                        .transform(new RoundTransform(context,10))
                        .error(R.mipmap.bg_image)
                        //.override(width, height)
                        .into(target);
            }
        }


    }

    private void showToolbarContent(FeedHolder holder, Feed feed){

        if(feed.getLikeCount() == 0){
            holder.loveText.setText("");
        }else{
            holder.loveText.setText(""+feed.getLikeCount());
        }

        if(feed.getFavoriteCount() == 0){
            holder.favText.setText("");
        }else{
            holder.favText.setText(""+feed.getFavoriteCount() + "");
        }

        if(feed.getShareCount() == 0){
            holder.shareText.setText("");
        }else{
            holder.shareText.setText(""+feed.getShareCount() + "");
        }

        if(feed.getCommentCount() == 0){
            holder.commentText.setText("");
        }else{
            holder.commentText.setText(""+feed.getCommentCount() + "");
        }





        if(feed.getLikeState() == 1){
            holder.loveImage.setImageResource(R.mipmap.shouye_icon_zan_click);
        }else{
            holder.loveImage.setImageResource(R.mipmap.shouye_icon_zan);
        }

        if(feed.getFavoriteState() == 1){
            holder.favImage.setImageResource(R.mipmap.shouye_icon_shoucang_click);

        }else{
            holder.favImage.setImageResource(R.mipmap.shouye_icon_shoucang);
        }
        ClickUtil.toolbarClick(holder.loveImage, holder.favImage,holder.loveText,holder.favText,holder.favImage,holder.loveImage,holder.shareImage,holder.commentImage,holder.itemView,feed);

    }



}
