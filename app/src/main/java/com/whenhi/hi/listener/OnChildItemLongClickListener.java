package com.whenhi.hi.listener;

import android.view.View;

/**
 * Created by 王雷 on 2017/2/21.
 */

public interface OnChildItemLongClickListener<C> {
    boolean onClickItemLongClick(int groupPosition, int childPosition, C c, View view);
}
