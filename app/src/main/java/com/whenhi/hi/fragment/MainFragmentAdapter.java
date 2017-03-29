package com.whenhi.hi.fragment;

import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;

import com.aspsine.fragmentnavigator.FragmentNavigatorAdapter;
import com.whenhi.hi.Constants;

/**
 * Created by aspsine on 16/4/28.
 */
public class MainFragmentAdapter implements FragmentNavigatorAdapter {

    private AppCompatActivity mActivity;
    public MainFragmentAdapter(AppCompatActivity activity){
        mActivity = activity;
    }



    @Override
    public Fragment onCreateFragment(int position) {
        System.gc();
        switch (position) {
            case Constants.NAV_BOTTOM_BAR_HOME:
                return HomeNavFragment.newInstance();
            case Constants.NAV_BOTTOM_BAR_EXPLORE:
                return ExploreNavFragment.newInstance();
            case Constants.NAV_BOTTOM_BAR_MESSAGE:
                return MessageNavFragment.newInstance();
            case Constants.NAV_BOTTOM_BAR_MORE:
                return MoreNavFragment.newInstance();
        }
        return null;
    }

    @Override
    public String getTag(int position) {
        return String.valueOf(position);
    }

    @Override
    public int getCount() {
        return 4;
    }
}