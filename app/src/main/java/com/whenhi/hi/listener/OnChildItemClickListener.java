package com.whenhi.hi.listener;

import android.view.View;

/**
 * Created by 王雷 on 2017/2/21.
 */

public interface OnChildItemClickListener<C> {
    void onChildItemClick(int childPosition, C c, View view);
}
