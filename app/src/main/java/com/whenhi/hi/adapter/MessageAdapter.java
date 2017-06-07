package com.whenhi.hi.adapter;

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
import com.whenhi.hi.image.CircleTransform;
import com.whenhi.hi.image.RoundTransform;
import com.whenhi.hi.listener.OnItemClickListener;
import com.whenhi.hi.listener.OnItemLongClickListener;
import com.whenhi.hi.model.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Aspsine on 2015/9/9.
 */
public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_CHILD = 0;

    private final List<Message> mDataList;

    protected OnItemClickListener mOnChildItemClickListener;

    protected OnItemLongClickListener mOnChildItemLongClickListener;

    public MessageAdapter() {
        mDataList = new ArrayList<>();
    }

    public void setList(List<Message> messages) {
        mDataList.clear();
        append(messages);
    }

    public void append(List<Message> messages) {
        mDataList.addAll(messages);
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
        View itemView = inflate(viewGroup, R.layout.item_message);
        final ChildHolder holder = new ChildHolder(itemView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int absolutePosition = holder.getAdapterPosition();
                int childPosition = getChildPosition(absolutePosition);
                Message message = mDataList.get(childPosition);

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
        Message message = mDataList.get(position);

        holder.message.setText(message.getContent());
        holder.time.setText(message.getCreateTimeNice());
        holder.messageUser.setText(message.getUserName());

        /*if(message.getMsgCategory() == 1){
            holder.messageImage.setImageResource(R.mipmap.xitong);
        }else if(message.getMsgCategory() == 2){
            holder.messageImage.setImageResource(R.mipmap.huodong);
        }*/

        Glide.with(App.getContext())
                .load(message.getUserLogo())
                .placeholder(R.mipmap.xitong)
                .centerCrop()
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                .transform(new CircleTransform(App.getContext()))
                .error(R.mipmap.xitong)
                //.override(600, 200)
                //.fitCenter()
                .into(holder.messageImage);

        if(message.getStatus() == 1){
            holder.newMeaasge.setVisibility(View.VISIBLE);
        }else if(message.getStatus() == 2){
            holder.newMeaasge.setVisibility(View.GONE);
        }



    }



    int getChildPosition(int position) {
        return position;
    }


    static class ChildHolder extends RecyclerView.ViewHolder {
        TextView message;
        TextView time;
        ImageView messageImage;
        Button newMeaasge;
        TextView messageUser;

        public ChildHolder(View itemView) {
            super(itemView);
            message = (TextView) itemView.findViewById(R.id.item_message_text);
            time = (TextView) itemView.findViewById(R.id.item_message_time);
            messageImage = (ImageView) itemView.findViewById(R.id.item_message_image);
            newMeaasge = (Button) itemView.findViewById(R.id.item_new);
            messageUser = (TextView) itemView.findViewById(R.id.item_message_user);

        }
    }


}
