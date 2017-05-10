package com.whenhi.hi.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whenhi.hi.R;
import com.whenhi.hi.listener.GlideListener;
import com.whenhi.hi.listener.OnItemClickListener;
import com.whenhi.hi.listener.OnItemLongClickListener;
import com.whenhi.hi.model.Image;

import java.util.ArrayList;
import java.util.List;


/**
 *
 */
public class TextAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private static final String TAG =TextAdapter.class.getSimpleName();

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


    public TextAdapter() {
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
        View itemView = inflate(viewGroup, R.layout.item_text);
        final TextHolder holder = new TextHolder(itemView);

        return holder;
    }

    private View inflate(ViewGroup parent, int layoutRes) {
        return LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        onBindTextHolder((TextHolder) viewHolder,i);
    }


    private void onBindTextHolder(TextHolder holder, int position) {



    }



    static class TextHolder extends RecyclerView.ViewHolder {


        public TextHolder(View itemView) {
            super(itemView);

        }
    }




}
