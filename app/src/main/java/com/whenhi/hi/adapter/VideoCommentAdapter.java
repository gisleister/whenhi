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
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.whenhi.hi.App;
import com.whenhi.hi.R;
import com.whenhi.hi.activity.LoginActivity;
import com.whenhi.hi.activity.PicActivity;
import com.whenhi.hi.activity.ShareActivity;
import com.whenhi.hi.activity.VideoActivity;
import com.whenhi.hi.activity.WebViewActivity;
import com.whenhi.hi.listener.OnChildItemClickListener;
import com.whenhi.hi.listener.OnChildItemLongClickListener;
import com.whenhi.hi.listener.OnGroupItemClickListener;
import com.whenhi.hi.listener.OnGroupItemLongClickListener;
import com.whenhi.hi.image.CircleTransform;
import com.whenhi.hi.model.BaseModel;
import com.whenhi.hi.model.Comment;
import com.whenhi.hi.model.Feed;
import com.whenhi.hi.network.HttpAPI;
import com.whenhi.hi.util.ClickUtil;

import org.lynxz.zzplayerlibrary.widget.VideoPlayer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王雷 on 2017/2/21.
 */

public class VideoCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {



    private static final int TYPE_HEADER = 0;
    private static final int TYPE_DETAIL = 1;
    private static final int TYPE_GROUP = 2;
    private static final int TYPE_CHILD = 3;


    private final List<Comment> mComments;




    private Feed mFeed;
    private Activity mActivity;

    private VideoAdapter mVideoAdapter;

    private boolean updateComment;
    private int lastTime;

    public boolean getUpdateComment() {
        return updateComment;
    }

    public void setUpdateComment(boolean updateComment) {
        this.updateComment = updateComment;
    }

    protected OnGroupItemClickListener mOnGroupItemClickListener;

    protected OnGroupItemLongClickListener mOnGroupItemLongClickListener;

    protected OnChildItemClickListener mOnChildItemClickListener;

    protected OnChildItemLongClickListener mOnChildItemLongClickListener;

    public void setOnChildItemClickListener(OnChildItemClickListener onChildItemClickListener) {
        mOnChildItemClickListener = onChildItemClickListener;
    }

    public void setOnChildItemLongClickListener(OnChildItemLongClickListener onChildItemLongClickListener) {
        mOnChildItemLongClickListener = onChildItemLongClickListener;
    }

    public VideoCommentAdapter(Feed feed, Activity activity) {
        mFeed = feed;
        mComments = new ArrayList<>();
        mActivity = activity;

    }

    public void setList(List<Comment> comments) {
        mComments.clear();

        append(comments);
    }

    public void append(List<Comment> comments) {
        mComments.addAll(comments);
        if(comments.size() > 0){
            notifyDataSetChanged();
        }

    }

    public void refreshComment(Comment comment){
        lastTime = mVideoAdapter.getLastTime();
        mComments.add(0,comment);
        notifyDataSetChanged();
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
        return mComments.size() + 3;//因为多了一个header,内容和详情 所以ITEM的 的游标 +1 +1 +1
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        int type = getItemViewType(i);
        View itemView = null;
        switch (type) {
            case TYPE_HEADER:
                itemView = inflate(viewGroup, R.layout.item_detail_header);
                return new HeaderHolder(itemView);
            case TYPE_DETAIL:
                itemView = inflate(viewGroup, R.layout.item_video);
                return new DetailHolder(itemView);
            case TYPE_GROUP:
                itemView = inflate(viewGroup, R.layout.item_detail_group);
                return new GroupHolder(itemView);
            case TYPE_CHILD:
                itemView = inflate(viewGroup, R.layout.item_video_comment);
                final ChildHolder holder = new ChildHolder(itemView);
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        int position = holder.getAdapterPosition();
                        int childPosition = getChildPosition(position);
                        Comment comment = mComments.get(childPosition);
                        mOnChildItemClickListener.onChildItemClick(childPosition, comment, view);
                    }
                });
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
        holder.detailText.setText(mFeed.getContent());
        holder.userNickName.setText(mFeed.getUserName());
        final WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(holder.userAvatar);
        ImageView target = imageViewWeakReference.get();
        Context context = App.getContext();
        if (target != null) {
            Glide.with(context)
                    .load(mFeed.getUserLogo())
                    .transform(new CircleTransform(context))
                    .error(R.mipmap.user_default)
                    .into(target);
        }
    }

    private void onBindDetailHolder(DetailHolder holder) {

        if(getUpdateComment()){
            mVideoAdapter.goOnPlay(lastTime);
        }else{
            mVideoAdapter = new VideoAdapter(mActivity,mFeed,holder.videoPlayer);
        }


    }

    private void onBindGroupHolder(final GroupHolder holder) {
        holder.headerText.setText("热门评论");

        Context context = holder.itemView.getContext();
        showToolbarContent(holder,mFeed,context);

    }

    private void onBindChildHolder(ChildHolder holder, int childPosition) {
        //Character character = mSections.get(parentPosition).getCharacters().get(childPosition);
        Comment comment = mComments.get(childPosition);
        holder.commentText.setText(comment.getContent());
        holder.userNickName.setText(comment.getUserName());
        final WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(holder.userAvatar);
        ImageView target = imageViewWeakReference.get();
        Context context = holder.itemView.getContext();
        if (target != null) {
            Glide.with(context)
                    .load(comment.getUserLogo())
                    .transform(new CircleTransform(context))
                    .error(R.mipmap.user_default)
                    .into(target);
        }


    }



    int getChildPosition(int position) {

        int childPositionInGroup = position - 1 - 1 -1;//评论的位置在header详情和和group的下面所以要 -1 -1 -1
        return childPositionInGroup;
    }



    class HeaderHolder extends RecyclerView.ViewHolder {
        ImageView userAvatar;
        TextView userNickName;
        TextView detailText;

        public HeaderHolder(View itemView) {
            super(itemView);
            userAvatar = (ImageView) itemView.findViewById(R.id.item_user).findViewById(R.id.user_avatar);
            userNickName = (TextView) itemView.findViewById(R.id.item_user).findViewById(R.id.user_nickname);
            detailText = (TextView) itemView.findViewById(R.id.detail_text);
        }
    }

    class DetailHolder extends RecyclerView.ViewHolder {
        VideoPlayer videoPlayer;

        public DetailHolder(View itemView) {
            super(itemView);
            //superVideoPlayer = (SuperVideoPlayer) itemView.findViewById(R.id.detail_video);
            //playerContainer = (FrameLayout) itemView.findViewById(R.id.detail_video);
            videoPlayer = (VideoPlayer) itemView.findViewById(R.id.detail_video);

        }

    }

    class GroupHolder extends RecyclerView.ViewHolder {
        TextView headerText;
        RelativeLayout toolbar;

        LinearLayout loveBtn;
        LinearLayout favBtn;
        LinearLayout shareBtn;
        LinearLayout commentBtn;

        ImageView loveImage;
        ImageView favImage;
        ImageView shareImage;
        ImageView commentImage;


        TextView loveText;
        TextView favText;
        TextView shareText;
        TextView commentText;

        public GroupHolder(View itemView) {
            super(itemView);
            headerText = (TextView) itemView.findViewById(R.id.item_comment_header_text);
            toolbar = (RelativeLayout) itemView.findViewById(R.id.item_toolbar);

            loveBtn = (LinearLayout) itemView.findViewById(R.id.item_toolbar).findViewById(R.id.toolbar_love_layout);
            shareBtn = (LinearLayout) itemView.findViewById(R.id.item_toolbar).findViewById(R.id.toolbar_share_layout);
            favBtn = (LinearLayout) itemView.findViewById(R.id.item_toolbar).findViewById(R.id.toolbar_fav_layout);
            commentBtn = (LinearLayout) itemView.findViewById(R.id.item_toolbar).findViewById(R.id.toolbar_comment_layout);


            loveImage = (ImageView) itemView.findViewById(R.id.item_toolbar).findViewById(R.id.toolbar_love_image);
            shareImage = (ImageView) itemView.findViewById(R.id.item_toolbar).findViewById(R.id.toolbar_share_image);
            favImage = (ImageView) itemView.findViewById(R.id.item_toolbar).findViewById(R.id.toolbar_fav_image);
            commentImage = (ImageView) itemView.findViewById(R.id.item_toolbar).findViewById(R.id.toolbar_comment_image);


            loveText = (TextView) itemView.findViewById(R.id.item_toolbar).findViewById(R.id.toolbar_love_text);
            shareText = (TextView) itemView.findViewById(R.id.item_toolbar).findViewById(R.id.toolbar_share_text);
            favText = (TextView) itemView.findViewById(R.id.item_toolbar).findViewById(R.id.toolbar_fav_text);
            commentText = (TextView) itemView.findViewById(R.id.item_toolbar).findViewById(R.id.toolbar_comment_text);

        }
    }

    static class ChildHolder extends RecyclerView.ViewHolder {
        ImageView userAvatar;
        TextView userNickName;
        TextView commentText;


        public ChildHolder(View itemView) {
            super(itemView);
            userNickName = (TextView) itemView.findViewById(R.id.item_comment).findViewById(R.id.comment_user_nickname);
            userAvatar = (ImageView) itemView.findViewById(R.id.item_comment).findViewById(R.id.comment_user_avatar);
            commentText = (TextView) itemView.findViewById(R.id.item_comment).findViewById(R.id.comment_text);

        }
    }



    private void showToolbarContent(GroupHolder holder, Feed feed,Context context){

        holder.loveText.setText(""+feed.getLikeCount());
        holder.favText.setText(""+feed.getFavoriteCount());
        holder.shareText.setText(""+feed.getShareCount());
        holder.commentText.setText(""+feed.getCommentCount());

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
        ClickUtil.toolbarClick(holder.loveText,holder.favText,holder.favImage,holder.loveImage,holder.loveBtn,holder.shareBtn,holder.favBtn,holder.commentBtn,context,holder.itemView,feed);

    }



}
