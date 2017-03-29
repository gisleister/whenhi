package com.whenhi.hi.adapter.base;

import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

/**
 * Created by 王雷 on 2017/2/21.
 */

public abstract class BaseArrayAdapter<T> extends BaseAdapter {
    private List<T> mList;
    private int lastPosition = -1;   //lastPosition 记录上一次选中的图片位置，-1表示未选中
    public Vector<Boolean> vector = new Vector<Boolean>();// 定义一个向量作为选中与否容器


    public BaseArrayAdapter() {
        this.mList = new ArrayList<>();
    }

    public void setList(List<T> list) {
        this.mList.clear();
        append(list);
    }

    public void append(List<T> list) {
        this.mList.addAll(list);
        for (int i = 0; i < mList.size(); i++) {
            vector.add(false);
        }
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public T getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void changeState(int position){
        if(lastPosition != -1)
            vector.setElementAt(false, lastPosition);                   //取消上一次的选中状态
        vector.setElementAt(!vector.elementAt(position), position);     //直接取反即可
        lastPosition = position;                                        //记录本次选中的位置
        notifyDataSetChanged();                                         //通知适配器进行更新
    }
}
