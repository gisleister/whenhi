package com.whenhi.hi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
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
import com.whenhi.hi.image.CircleTransform;
import com.whenhi.hi.image.RoundTransform;
import com.whenhi.hi.listener.OnChildItemClickListener;
import com.whenhi.hi.listener.OnChildItemLongClickListener;
import com.whenhi.hi.listener.OnItemClickListener;
import com.whenhi.hi.listener.OnItemLongClickListener;
import com.whenhi.hi.model.Comment;
import com.whenhi.hi.model.Message;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aspsine on 2015/9/9.
 */
public class CommentAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_CHILD = 0;

    private final List<Comment> mDataList;

    protected OnChildItemClickListener mOnChildItemClickListener;

    protected OnChildItemLongClickListener mOnChildItemLongClickListener;

    public void setOnChildItemClickListener(OnChildItemClickListener onChildItemClickListener) {
        mOnChildItemClickListener = onChildItemClickListener;
    }

    public void setOnChildItemLongClickListener(OnChildItemLongClickListener onChildItemLongClickListener) {
        mOnChildItemLongClickListener = onChildItemLongClickListener;
    }

    public CommentAdapter() {
        mDataList = new ArrayList<>();
    }

    public void setList(List<Comment> comments) {
        mDataList.clear();
        append(comments);
    }

    public void append(List<Comment> comments) {
        mDataList.addAll(comments);
        notifyDataSetChanged();
    }


    @Override
    public int getItemViewType(int position) {
        return TYPE_CHILD;
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }

    @Override
    public void onAttachedToRecyclerView(final RecyclerView recyclerView) {

    }

    @Override
    public void onViewAttachedToWindow(RecyclerView.ViewHolder holder) {
        super.onViewAttachedToWindow(holder);



    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        int type = getItemViewType(i);
        View itemView = inflate(viewGroup, R.layout.item_comment);
        final ChildHolder holder = new ChildHolder(itemView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int absolutePosition = holder.getAdapterPosition();
                int childPosition = getChildPosition(absolutePosition);
                Comment message = mDataList.get(childPosition);

            }
        });
        return holder;
    }

    private View inflate(ViewGroup parent, int layoutRes) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        int type = getItemViewType(i);
        switch (type) {
            case TYPE_CHILD:
                onBindChildHolder((ChildHolder) viewHolder,i);
                break;
        }
    }


    private void onBindChildHolder(ChildHolder holder, int position) {
        Comment comment = mDataList.get(position);
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
        return position;
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


}
