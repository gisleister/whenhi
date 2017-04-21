package com.whenhi.hi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.whenhi.hi.App;
import com.whenhi.hi.R;
import com.whenhi.hi.listener.CommentListener;
import com.whenhi.hi.listener.OnChildItemClickListener;
import com.whenhi.hi.listener.OnChildItemLongClickListener;
import com.whenhi.hi.listener.OnGroupItemClickListener;
import com.whenhi.hi.listener.OnGroupItemLongClickListener;
import com.whenhi.hi.image.CircleTransform;
import com.whenhi.hi.model.Comment;
import com.whenhi.hi.model.Feed;
import com.whenhi.hi.receiver.NoticeTransfer;
import com.whenhi.hi.util.ClickUtil;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
/**
 * Created by 王雷 on 2017/2/21.
 */

public class WebViewCommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_DETAIL = 1;
    private static final int TYPE_GROUP = 2;
    private static final int TYPE_CHILD = 3;


    private final List<Comment> mComments;
    private WebviewAdapter mAdapter;




    private Feed mFeed;

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



    public WebViewCommentAdapter(Feed feed) {
        mFeed = feed;
        mComments = new ArrayList<>();
        NoticeTransfer noticeTransfer = new NoticeTransfer();
        noticeTransfer.setCommentListener(mCommentListener);

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
                itemView = inflate(viewGroup, R.layout.item_webview);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                itemView.setLayoutParams(lp);
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

        holder.detailText.setText(mFeed.getContent());

        if(TextUtils.isEmpty(mFeed.getContent())){
            holder.detailText.setVisibility(View.GONE);
        }else{
            holder.detailText.setVisibility(View.VISIBLE);
        }

        holder.userNickName.setText(mFeed.getUserName());
        final WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(holder.userAvatar);
        ImageView target = imageViewWeakReference.get();
        Context context = holder.itemView.getContext();
        if (target != null) {
            Glide.with(context)
                    .load(mFeed.getUserLogo())
                    .transform(new CircleTransform(context))
                    .error(R.mipmap.logo)
                    .into(target);
        }
    }

    private void onBindDetailHolder(DetailHolder holder) {

        mAdapter = new WebviewAdapter(holder.webView);
        mAdapter.setUrl(mFeed.getLinkUrl());
        //mAdapter.setUrl("http://www.sina.com");
        mAdapter.initWebView();



    }



    public void destroy() {
        mAdapter.destroy();
    }

    private void onBindGroupHolder(GroupHolder holder) {
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



    static class HeaderHolder extends RecyclerView.ViewHolder {
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

    static class DetailHolder extends RecyclerView.ViewHolder {
        WebView webView;

        public DetailHolder(View itemView) {
            super(itemView);

            webView = (WebView) itemView.findViewById(R.id.detail_webview);
        }
    }

    static class GroupHolder extends RecyclerView.ViewHolder {
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



    private void showToolbarContent(GroupHolder holder, Feed feed,Context context){

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
        ClickUtil.toolbarClick(holder.loveImage, holder.favImage,holder.loveText,holder.favText,holder.favBtn,holder.loveBtn,holder.shareBtn,holder.commentBtn,holder.itemView,feed);

    }

    private CommentListener mCommentListener = new CommentListener() {
        @Override
        public void commentSuccess(Comment comment) {
            mComments.add(0,comment);
            notifyItemInserted(3);
        }
    };



}
