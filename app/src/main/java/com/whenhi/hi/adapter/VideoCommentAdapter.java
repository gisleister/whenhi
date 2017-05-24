package com.whenhi.hi.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.whenhi.hi.App;
import com.whenhi.hi.R;
import com.whenhi.hi.image.CircleTransform;
import com.whenhi.hi.image.RoundTransform;
import com.whenhi.hi.listener.CommentListener;
import com.whenhi.hi.listener.OnChildItemClickListener;
import com.whenhi.hi.listener.OnChildItemLongClickListener;
import com.whenhi.hi.listener.OnGroupItemClickListener;
import com.whenhi.hi.listener.OnGroupItemLongClickListener;
import com.whenhi.hi.model.Comment;
import com.whenhi.hi.model.Feed;
import com.whenhi.hi.receiver.NoticeTransfer;
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

    private int lastTime;

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

        NoticeTransfer noticeTransfer = new NoticeTransfer();
        noticeTransfer.setCommentListener(mCommentListener);

    }

    public void setList(List<Comment> comments) {
        mComments.clear();

        if(comments.size() == 0){
            Comment comment = new Comment();
            comment.setContent("暂时没有任何评论，赶紧坐沙发吧，再晚就没了。嘻嘻！");
            comment.setId(-1);
            comments.add(comment);
        }
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
                itemView = inflate(viewGroup, R.layout.item_comment);
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

        if(mFeed == null)
            return;
        holder.detailText.setText(mFeed.getContent());

        if(TextUtils.isEmpty(mFeed.getContent())){
            holder.detailText.setVisibility(View.GONE);
        }else{
            holder.detailText.setVisibility(View.VISIBLE);
        }

        holder.userNickName.setText(mFeed.getUserName());
        final WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(holder.userAvatar);
        ImageView target = imageViewWeakReference.get();
        Context context = App.getContext();
        if (target != null) {
            Glide.with(context)
                    .load(mFeed.getUserLogo())
                    .transform(new CircleTransform(context))
                    .error(R.mipmap.logo)
                    .into(target);
        }

    }

    private void onBindDetailHolder(final DetailHolder holder) {

        if(mFeed == null)
            return;
        final WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(holder.imageContent);
        ImageView target = imageViewWeakReference.get();
        Context context = App.getContext();
        if (target != null) {
            Glide.with(context)
                    .load(mFeed.getImageUrl())
                    .transform(new RoundTransform(context,10))
                    .error(R.mipmap.bg_image)
                    .into(target);
        }






        holder.imageContent.setOnClickListener(new Button.OnClickListener(){//创建监听
            public void onClick(View v) {

                Log.d("video click start url:",mFeed.getPlayUrl());

                String videoUrl = mFeed.getPlayUrl();

                String vUrl = videoUrl.replaceFirst("https","http");

                Log.d("video click doing url:",vUrl);

                mFeed.setPlayUrl(vUrl);

                mVideoAdapter = new VideoAdapter(mActivity,mFeed,holder.videoPlayer);
                holder.videoPlayer.setVisibility(View.VISIBLE);
                holder.imageContent.setVisibility(View.GONE);
                holder.imagePlay.setVisibility(View.GONE);
                Log.d("video click end ","ok");
            }
        });




    }

    private void onBindGroupHolder(final GroupHolder holder) {
        holder.headerText.setText("热门评论");
        showToolbarContent(holder,mFeed);
    }

    private void onBindChildHolder(ChildHolder holder, int childPosition) {
        //Character character = mSections.get(parentPosition).getCharacters().get(childPosition);
        Comment comment = mComments.get(childPosition);
        holder.commentText.setText(comment.getContent());

        if(TextUtils.isEmpty(comment.getUserName())){
            holder.userNickName.setVisibility(View.GONE);
            holder.userAvatar.setVisibility(View.GONE);
        }

        holder.userNickName.setText(comment.getUserName());
        final WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(holder.userAvatar);
        ImageView target = imageViewWeakReference.get();
        Context context = App.getContext();
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
            userAvatar = (ImageView) itemView.findViewById(R.id.user_avatar);
            userNickName = (TextView) itemView.findViewById(R.id.user_nickname);

            detailText = (TextView) itemView.findViewById(R.id.detail_text);

        }
    }

    class DetailHolder extends RecyclerView.ViewHolder {


        VideoPlayer videoPlayer;

        ImageView imageContent;
        ImageView imagePlay;



        public DetailHolder(View itemView) {
            super(itemView);
            //superVideoPlayer = (SuperVideoPlayer) itemView.findViewById(R.id.detail_video);
            //playerContainer = (FrameLayout) itemView.findViewById(R.id.detail_video);

            videoPlayer = (VideoPlayer) itemView.findViewById(R.id.detail_video);
            imageContent = (ImageView) itemView.findViewById(R.id.item_detail_content_image);
            imagePlay = (ImageView) itemView.findViewById(R.id.item_detail_content_play);





        }

    }

    class GroupHolder extends RecyclerView.ViewHolder {
        TextView headerText;

        ImageView loveImage;
        ImageView favImage;
        ImageView shareImage;
        ImageView commentImage;


        TextView loveText;
        TextView favText;
        TextView shareText;
        TextView commentText;

        LinearLayout loveBtn;
        LinearLayout favBtn;
        LinearLayout shareBtn;
        LinearLayout commentBtn;


        public GroupHolder(View itemView) {
            super(itemView);
            headerText = (TextView) itemView.findViewById(R.id.item_comment_header_text);
            loveImage = (ImageView) itemView.findViewById(R.id.toolbar_love_image);
            shareImage = (ImageView) itemView.findViewById(R.id.toolbar_share_image);
            favImage = (ImageView) itemView.findViewById(R.id.toolbar_fav_image);
            commentImage = (ImageView) itemView.findViewById(R.id.toolbar_comment_image);


            loveText = (TextView) itemView.findViewById(R.id.toolbar_love_text);
            shareText = (TextView) itemView.findViewById(R.id.toolbar_share_text);
            favText = (TextView) itemView.findViewById(R.id.toolbar_fav_text);
            commentText = (TextView) itemView.findViewById(R.id.toolbar_comment_text);

            loveBtn = (LinearLayout) itemView.findViewById(R.id.toolbar_love_btn);
            favBtn = (LinearLayout) itemView.findViewById(R.id.toolbar_fav_btn);
            shareBtn = (LinearLayout) itemView.findViewById(R.id.toolbar_share_btn);
            commentBtn = (LinearLayout) itemView.findViewById(R.id.toolbar_comment_btn);
        }
    }

    static class ChildHolder extends RecyclerView.ViewHolder {
        ImageView userAvatar;
        TextView userNickName;
        TextView commentText;


        public ChildHolder(View itemView) {
            super(itemView);
            userNickName = (TextView) itemView.findViewById(R.id.user_nickname);
            userAvatar = (ImageView) itemView.findViewById(R.id.user_avatar);
            commentText = (TextView) itemView.findViewById(R.id.comment_text);

        }
    }



    private void showToolbarContent(GroupHolder holder, Feed feed){

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
       // ClickUtil.toolbarClick(holder.loveImage, holder.favImage,holder.loveText,holder.favText,holder.favBtn,holder.loveBtn,holder.shareBtn,holder.commentBtn,holder.itemView,feed);

    }



    private CommentListener mCommentListener = new CommentListener() {
        @Override
        public void commentSuccess(Comment comment) {
            mComments.add(0,comment);
            notifyItemInserted(3);
        }
    };


}
