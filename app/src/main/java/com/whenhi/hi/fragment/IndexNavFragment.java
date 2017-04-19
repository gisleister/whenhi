package com.whenhi.hi.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whenhi.hi.R;

/**
 * A simple {@link Fragment} subclass.
 */
public class IndexNavFragment extends BaseNavPagerFragment {

    public static BaseNavPagerFragment newInstance() {
        BaseNavPagerFragment fragment = new IndexNavFragment();
        return fragment;
    }


    public IndexNavFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // override the method
        // delete app:layout_scrollFlags="scroll|enterAlways"
        // delete reason: ListView don't support coordinate scroll
        // these property lead height measure issue and scroll issue
        // it's not an bug of SwipeToLoadLayout
        return inflater.inflate(R.layout.fragment_home_nav, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    protected String[] getTitles() {
        return new String[]{"人气排行","分享排行"};
    }

    @Override
    protected Fragment getFragment(int position) {
        String title = getTitles()[position];
        Fragment fragment = null;

        if (title.equals("分享排行")) {
            fragment = ShareIndexListFragment.newInstance();
        } else if (title.equals("人气排行")) {
            fragment = LoveIndexListFragment.newInstance();
        }
        return fragment;
    }


}
