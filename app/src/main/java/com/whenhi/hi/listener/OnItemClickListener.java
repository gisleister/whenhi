package com.whenhi.hi.listener;

import android.view.View;

/**
 * Created by aspsine on 16/8/9.
 */

public interface OnItemClickListener<C> {
    void onItemClick(int position, C c, View view);
}
