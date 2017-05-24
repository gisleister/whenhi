package com.whenhi.hi.fragment;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.aspsine.fragmentnavigator.FragmentNavigator;
import com.whenhi.hi.App;
import com.whenhi.hi.Constants;
import com.whenhi.hi.R;
import com.whenhi.hi.activity.CraftActivity;
import com.whenhi.hi.activity.IncomeIndexActivity;
import com.whenhi.hi.activity.MaleActivity;
import com.whenhi.hi.model.DiscoveryModel;
import com.whenhi.hi.network.HttpAPI;
import com.whenhi.hi.util.ClickUtil;

/**
 * Created by 王雷 on 2016/12/26.
 */

public class ExploreNavFragment extends BaseFragment {
    private static final String TAG = ExploreNavFragment.class.getSimpleName();




    public static Fragment newInstance() {
        ExploreNavFragment fragment = new ExploreNavFragment();

        return fragment;
    }

    public ExploreNavFragment() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_explore_nav, container, false);
    }

    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = view.getContext();
        if (context instanceof AppCompatActivity){
            final AppCompatActivity activity = (AppCompatActivity)context;

            Toolbar toolbar = (Toolbar) activity.findViewById(R.id.toolbar).findViewById(R.id.toolbar);
            activity.setSupportActionBar(toolbar);
            android.support.v7.app.ActionBar actionBar = activity.getSupportActionBar();
            if (actionBar != null){
                actionBar.setDisplayHomeAsUpEnabled(false);
                actionBar.setDisplayShowTitleEnabled(false);
            }

        }
        OtherFragmentAdapter otherFragmentAdapter = new OtherFragmentAdapter();
        FragmentNavigator fragmentNavigator = new FragmentNavigator(getChildFragmentManager(), otherFragmentAdapter, R.id.fragment_explore_container);
        fragmentNavigator.onCreate(savedInstanceState);
        fragmentNavigator.showFragment(Constants.OTHER_EXPLORE);
    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();

    }





}
