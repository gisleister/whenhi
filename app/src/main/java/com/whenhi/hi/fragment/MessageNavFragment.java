package com.whenhi.hi.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.fragmentnavigator.FragmentNavigator;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.whenhi.hi.Constants;
import com.whenhi.hi.R;

/**
 * Created by 王雷 on 2016/12/26.
 */

public class MessageNavFragment extends BaseFragment {
    private static final String TAG = MessageNavFragment.class.getSimpleName();


    private SwipeToLoadLayout mSwipeToLoadLayout;

    private RecyclerView mRecyclerView;


    private int mPageNo = 1;
    private String mExtras = "";


    public static Fragment newInstance() {
        MessageNavFragment fragment = new MessageNavFragment();
        return fragment;
    }

    public MessageNavFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_message_nav, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
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
        FragmentNavigator fragmentNavigator = new FragmentNavigator(getChildFragmentManager(), otherFragmentAdapter, R.id.fragment_message_container);
        fragmentNavigator.onCreate(savedInstanceState);
        fragmentNavigator.showFragment(Constants.OTHER_MESSAGE);


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

}
