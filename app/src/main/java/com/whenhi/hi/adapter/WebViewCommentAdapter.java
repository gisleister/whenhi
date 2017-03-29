package com.whenhi.hi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.whenhi.hi.App;
import com.whenhi.hi.R;
import com.whenhi.hi.listener.OnChildItemClickListener;
import com.whenhi.hi.listener.OnChildItemLongClickListener;
import com.whenhi.hi.listener.OnGroupItemClickListener;
import com.whenhi.hi.listener.OnGroupItemLongClickListener;
import com.whenhi.hi.image.CircleTransform;
import com.whenhi.hi.model.Comment;
import com.whenhi.hi.model.Feed;
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
                itemView = inflate(viewGroup, R.layout.item_webview);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
                itemView.setLayoutParams(lp);
                return new DetailHolder(itemView);
            case TYPE_GROUP:
                itemView = inflate(viewGroup, R.layout.item_detail_group);
                return new GroupHolder(itemView);
            case TYPE_CHILD:
                itemView = inflate(viewGroup, R.layout.item_webview_comment);
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
        Context context = holder.itemView.getContext();
        if (target != null) {
            Glide.with(context)
                    .load(mFeed.getUserLogo())
                    .transform(new CircleTransform(context))
                    .error(R.mipmap.user_default)
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

        final Context context = holder.itemView.getContext();

        holder.loveBtn.setText(""+mFeed.getLikeCount());
        holder.favBtn.setText(""+mFeed.getLikeCount());
        holder.shareBtn.setText(""+mFeed.getCommentCount());
        holder.commentBtn.setText(""+mFeed.getCommentCount());

        ClickUtil.toolbarClick(holder.loveBtn,holder.shareBtn,holder.favBtn,holder.commentBtn,context,holder.itemView,mFeed);
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
            userAvatar = (ImageView) itemView.findViewById(R.id.item_user).findViewById(R.id.user_avatar);
            userNickName = (TextView) itemView.findViewById(R.id.item_user).findViewById(R.id.user_nickname);
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
        Button loveBtn;
        Button favBtn;
        Button shareBtn;
        Button commentBtn;
        public GroupHolder(View itemView) {
            super(itemView);
            headerText = (TextView) itemView.findViewById(R.id.item_comment_header_text);
            loveBtn = (Button) itemView.findViewById(R.id.item_toolbar).findViewById(R.id.toolbar_love);
            shareBtn = (Button) itemView.findViewById(R.id.item_toolbar).findViewById(R.id.toolbar_share);
            favBtn = (Button) itemView.findViewById(R.id.item_toolbar).findViewById(R.id.toolbar_fav);
            commentBtn = (Button) itemView.findViewById(R.id.item_toolbar).findViewById(R.id.toolbar_comment);
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


}
