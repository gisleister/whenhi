package com.whenhi.hi.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.whenhi.hi.App;
import com.whenhi.hi.R;
import com.whenhi.hi.image.RoundTransform;
import com.whenhi.hi.listener.OnItemClickListener;
import com.whenhi.hi.listener.OnItemLongClickListener;
import com.whenhi.hi.listener.GlideListener;
import com.whenhi.hi.model.Image;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class PicAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG =PicAdapter.class.getSimpleName();

    private final List<Image> mDataList;
    private GlideListener mGlideListener;

    protected OnItemClickListener mOnItemClickListener;

    protected OnItemLongClickListener mOnItemLongClickListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener) {
        this.mOnItemLongClickListener = onItemLongClickListener;
    }


    public PicAdapter() {
        mDataList = new ArrayList<>();
        mGlideListener = new GlideListener();
    }

    public void setList(List<Image> images) {
        mDataList.clear();
        append(images);
    }

    public void append(List<Image> images) {
        mDataList.addAll(images);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mDataList.size();
    }



    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = inflate(viewGroup, R.layout.item_pic);
        final PicHolder holder = new PicHolder(itemView);

        return holder;
    }

    private View inflate(ViewGroup parent, int layoutRes) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        onBindPicHolder((PicHolder) viewHolder,i);
    }


    private void onBindPicHolder(PicHolder holder, int position) {
        Image image = mDataList.get(position);
        Context context = App.getContext();

        mGlideListener.setErroReport(true);
        mGlideListener.setFeedCategory(image.getFeedCategory());
        mGlideListener.setFeedId(image.getId());

        final WeakReference<ImageView> imageViewWeakReference = new WeakReference<>(holder.imageContent);
        ImageView target = imageViewWeakReference.get();
        if (target != null) {
            Glide.with(context)
                    .load(image.getContent())
                    .listener(mGlideListener.getListener())
                    .transform(new RoundTransform(context,10))
                    .diskCacheStrategy(DiskCacheStrategy.SOURCE)
                    .error(R.mipmap.bg_image)
                    .into(target);
        }


    }



    static class PicHolder extends RecyclerView.ViewHolder {


        ImageView imageContent;



        public PicHolder(View itemView) {
            super(itemView);
            imageContent = (ImageView) itemView.findViewById(R.id.detail_image);

        }
    }




}
