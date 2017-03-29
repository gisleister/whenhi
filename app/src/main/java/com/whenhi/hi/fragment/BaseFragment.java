package com.whenhi.hi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by 王雷 on 2016/12/22.
 */

public class BaseFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        TextView tv = new TextView(getActivity());
        tv.setText("Not developed yet. Support latter...");
        tv.setGravity(Gravity.CENTER);
        return tv;
    }


    /**
     * 当前界面是否呈现给用户的状态标志
     */
    protected boolean isVisible;
    /**
     * 重写Fragment父类生命周期方法，在onCreate之前调用该方法，实现Fragment数据的缓加载.
     * @param isVisibleToUser 当前是否已将界面显示给用户的状态
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if(getUserVisibleHint()) {
            isVisible = true;
            onvisible();
        } else {
            isVisible = false;
            invisible();
        }

    }
    /**
     * 当界面呈现给用户，即设置可见时执行，进行加载数据的方法
     * 在用户可见时加载数据，而不在用户不可见的时候加载数据，是为了防止控件对象出现空指针异常
     */
    protected void onvisible(){

    }
    /**
     * 当界面还没呈现给用户，即设置不可见时执行
     */
    protected void invisible(){
        destroy();
    }

    protected void destroy(){
    }
}

