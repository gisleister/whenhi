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



    private static final int TYPE_FIRST = 0;
    private static final int TYPE_SECOND = 1;
    private static final int TYPE_THREE = 2;
    private static final int TYPE_FOURE = 3;


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
            return TYPE_FIRST;
        } else if (position == 1) {
            return TYPE_SECOND;
        } else if (position == 2) {
            return TYPE_THREE;
        } else {
            return TYPE_FOURE;
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
            case TYPE_FIRST:
                itemView = inflate(viewGroup, R.layout.item_first);
                return new FirstHolder(itemView);
            case TYPE_SECOND:
                itemView = inflate(viewGroup, R.layout.item_second);
                return new SecondHolder(itemView);
            case TYPE_THREE:
                itemView = inflate(viewGroup, R.layout.item_three);
                return new ThreeHolder(itemView);
            case TYPE_FOURE:
                itemView = inflate(viewGroup, R.layout.item_comment);
                final FoureHolder holder = new FoureHolder(itemView);
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
            case TYPE_FIRST:
                onBindFirstHolder((FirstHolder) viewHolder);
                break;
            case TYPE_SECOND:
                onBindSecondHolder((SecondHolder) viewHolder);
                break;
            case TYPE_THREE:
                onBindThreeHolder((ThreeHolder) viewHolder);
                break;
            case TYPE_FOURE:
                onBindFoureHolder((FoureHolder) viewHolder, getChildPosition(i));
                break;
        }
    }

    private void onBindFirstHolder(FirstHolder holder) {

        if(mFeed == null)
            return;

        holder.mText.setText(mFeed.getContent());

    }

    private void onBindSecondHolder(final SecondHolder holder) {

        if(mFeed == null)
            return;

        holder.mText.setText("#热门评论");



    }

    private void onBindThreeHolder(final ThreeHolder holder) {
        holder.mText.setText("");
    }

    private void onBindFoureHolder(FoureHolder holder, int childPosition) {
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



    class FirstHolder extends RecyclerView.ViewHolder {
        TextView mText;



        public FirstHolder(View itemView) {
            super(itemView);
            mText = (TextView) itemView.findViewById(R.id.detail_text);

        }
    }

    class SecondHolder extends RecyclerView.ViewHolder {


        TextView mText;



        public SecondHolder(View itemView) {
            super(itemView);
            mText = (TextView) itemView.findViewById(R.id.pinglun_title_text);



        }

    }

    class ThreeHolder extends RecyclerView.ViewHolder {
        TextView mText;



        public ThreeHolder(View itemView) {
            super(itemView);
            mText = (TextView) itemView.findViewById(R.id.detail_text);
        }
    }

    static class FoureHolder extends RecyclerView.ViewHolder {
        ImageView userAvatar;
        TextView userNickName;
        TextView commentText;


        public FoureHolder(View itemView) {
            super(itemView);
            userNickName = (TextView) itemView.findViewById(R.id.user_nickname);
            userAvatar = (ImageView) itemView.findViewById(R.id.user_avatar);
            commentText = (TextView) itemView.findViewById(R.id.comment_text);

        }
    }






    private CommentListener mCommentListener = new CommentListener() {
        @Override
        public void commentSuccess(Comment comment) {
            mComments.add(0,comment);
            notifyItemInserted(3);
        }
    };


}
