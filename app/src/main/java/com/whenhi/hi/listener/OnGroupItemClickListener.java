package com.whenhi.hi.listener;

import android.view.View;

/**
 * Created by 王雷 on 2017/2/21.
 */

public interface OnGroupItemClickListener<G> {
    void onGroupItemClick(int groupPosition, G g, View view);
}

