package com.whenhi.hi.listener;

import android.view.View;

/**
 * Created by 王雷 on 2017/2/21.
 */

public interface LoginListener<C> {
    void login(boolean is);
    void logout(boolean is);
    void refresh();
    void mobile(String mobile);
}
