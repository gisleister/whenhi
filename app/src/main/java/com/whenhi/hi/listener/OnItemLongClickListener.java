package com.whenhi.hi.listener;

import android.view.View;

/**
 * Created by aspsine on 16/8/9.
 */

public interface OnItemLongClickListener<C> {
    boolean onClickItemLongClick(int groupPosition,  C c, View view);
}
