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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.GlideDrawableImageViewTarget;
import com.whenhi.hi.App;
import com.whenhi.hi.R;
import com.whenhi.hi.fragment.BaseFragment;
import com.whenhi.hi.image.RoundTransform;
import com.whenhi.hi.listener.OnItemClickListener;
import com.whenhi.hi.listener.OnItemLongClickListener;
import com.whenhi.hi.image.CircleTransform;
import com.whenhi.hi.listener.GlideListener;
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
    private BaseFragment mBaseFragment;

    protected OnItemClickListener mOnItemClickListener;

    protected OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }


    public FeedListAdapter(BaseFragment fragment) {
        mDataList = new ArrayList<>();
        mGlideListener = new GlideListener();
        mBaseFragment = fragment;
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
                .error(R.mipmap.user_default)
                .into(target);

        showContent(holder,feed,context);//显示内容
        showToolbarContent(holder,feed,context);//显示工具栏
        other(holder,feed.getFeedCategory());//工具栏是否隐藏


    }




    class FeedHolder extends RecyclerView.ViewHolder {

        ImageView avatarUser;
        TextView nicknameUser;
        ImageView imageContent;
        TextView textContent;
        ImageView imagePlay;




        /*LinearLayout loveBtn;
        LinearLayout favBtn;
        LinearLayout shareBtn;
        LinearLayout commentBtn;*/

        ImageView loveImage;
        ImageView favImage;
        ImageView shareImage;
        ImageView commentImage;


        TextView loveText;
        TextView favText;
        TextView shareText;
        TextView commentText;

        public FeedHolder(View itemView) {
            super(itemView);
            avatarUser = (ImageView) itemView.findViewById(R.id.user_avatar);
            nicknameUser = (TextView) itemView.findViewById(R.id.user_nickname);

            imageContent = (ImageView) itemView.findViewById(R.id.item_feed_rec_content_image);
            textContent = (TextView) itemView.findViewById(R.id.item_feed_rec_content_text);

            imagePlay = (ImageView) itemView.findViewById(R.id.item_feed_rec_content_play);


            loveImage = (ImageView) itemView.findViewById(R.id.toolbar_love_image);
            shareImage = (ImageView) itemView.findViewById(R.id.toolbar_share_image);
            favImage = (ImageView) itemView.findViewById(R.id.toolbar_fav_image);
            commentImage = (ImageView) itemView.findViewById(R.id.toolbar_comment_image);


            loveText = (TextView) itemView.findViewById(R.id.toolbar_love_text);
            shareText = (TextView) itemView.findViewById(R.id.toolbar_share_text);
            favText = (TextView) itemView.findViewById(R.id.toolbar_fav_text);
            commentText = (TextView) itemView.findViewById(R.id.toolbar_comment_text);


        }
    }

    private void other(FeedHolder holder, int type){
        if( type == 8){
           // holder.toolbar.setVisibility(View.GONE);

        }else if(type == 4){
            //holder.toolbar.setVisibility(View.VISIBLE);
        }else{
           // holder.toolbar.setVisibility(View.VISIBLE);
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
            //holder.userbar.setVisibility(View.VISIBLE);
            holder.textContent.setVisibility(View.VISIBLE);
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

        }else if(feed.getFeedCategory() == 2){//动图

        }else if(feed.getFeedCategory() == 3){//漫画
            holder.textContent.setText(feed.getContent());
            holder.imagePlay.setVisibility(View.GONE);
            holder.imageContent.setVisibility(View.VISIBLE);
            //holder.userbar.setVisibility(View.VISIBLE);
            holder.textContent.setVisibility(View.VISIBLE);
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
            holder.textContent.setText(feed.getContent());
            holder.imageContent.setVisibility(View.GONE);
            holder.imagePlay.setVisibility(View.GONE);
            //holder.userbar.setVisibility(View.VISIBLE);
            holder.textContent.setVisibility(View.VISIBLE);
            feed.setImageUrl(App.getUserLogo());//给分享时候图片赋值为用户头像
        }else if(feed.getFeedCategory() == 5){//图片
            holder.textContent.setText(feed.getContent());
            holder.imagePlay.setVisibility(View.GONE);
            holder.imageContent.setVisibility(View.VISIBLE);
           // holder.userbar.setVisibility(View.VISIBLE);
            holder.textContent.setVisibility(View.VISIBLE);

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
                                //.override(width, height)
                                .into(new GlideDrawableImageViewTarget(target, 1));
                    }

                }
            }

        }else if(feed.getFeedCategory() == 6){//广告

        }else if(feed.getFeedCategory() == 7){//分割线

        }else if(feed.getFeedCategory() == 8){//彩蛋
            holder.textContent.setText(feed.getMaskContent());
            holder.imagePlay.setVisibility(View.GONE);
            holder.imageContent.setVisibility(View.VISIBLE);
            //holder.userbar.setVisibility(View.GONE);
            holder.textContent.setVisibility(View.GONE);
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
            holder.textContent.setText(feed.getSummary());
            holder.imagePlay.setVisibility(View.GONE);
            holder.imageContent.setVisibility(View.VISIBLE);
           // holder.userbar.setVisibility(View.VISIBLE);
            holder.textContent.setVisibility(View.VISIBLE);
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

        }


    }

    private void showToolbarContent(FeedHolder holder, Feed feed,Context context){

        holder.loveText.setText(""+feed.getLikeCount() + "赞");
        holder.favText.setText(" · "+feed.getFavoriteCount() + "收藏");
        holder.shareText.setText(" · "+feed.getShareCount() + "分享");
        holder.commentText.setText(" · "+feed.getCommentCount() + "评论");

        if(feed.getLikeState() == 1){
            holder.loveImage.setImageResource(R.mipmap.zan_click);
        }else{
            holder.loveImage.setImageResource(R.mipmap.zan);
        }

        if(feed.getFavoriteState() == 1){
            holder.favImage.setImageResource(R.mipmap.shoucang_click);

        }else{
            holder.favImage.setImageResource(R.mipmap.shoucang);
        }
        ClickUtil.toolbarClick(holder.loveText,holder.favText,holder.favImage,holder.loveImage,holder.shareImage,holder.commentImage,holder.itemView,feed);

    }



}
