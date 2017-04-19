package com.whenhi.hi.fragment;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.aspsine.fragmentnavigator.FragmentNavigator;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.whenhi.hi.Constants;
import com.whenhi.hi.R;
import com.whenhi.hi.activity.CraftActivity;
import com.whenhi.hi.activity.IncomeIndexActivity;
import com.whenhi.hi.activity.LoveIndexActivity;
import com.whenhi.hi.activity.ShareIndexActivity;
import com.whenhi.hi.adapter.IncomeIndexAdapter;
import com.whenhi.hi.adapter.LoveIndexAdapter;
import com.whenhi.hi.adapter.ShareIndexAdapter;
import com.whenhi.hi.model.BaseModel;
import com.whenhi.hi.model.DiscoveryModel;
import com.whenhi.hi.model.Feed;
import com.whenhi.hi.model.FeedModel;
import com.whenhi.hi.network.HttpAPI;
import com.whenhi.hi.view.horizontal.HorizontalListView;

import java.util.List;

/**
 * Created by 王雷 on 2016/12/26.
 */

public class ExploreNavFragment extends BaseFragment {
    private static final String TAG = ExploreNavFragment.class.getSimpleName();

    private FragmentNavigator incomeFragmentNavigator;
    private FragmentNavigator loveFragmentNavigator;
    private FragmentNavigator shareFragmentNavigator;

    private FragmentNavigator indexFragmentNavigator;



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

        /*ImageView craft = (ImageView)view.findViewById(R.id.explore_craft);
        craft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), CraftActivity.class);
                view.getContext().startActivity(intent);
            }
        });


        RelativeLayout loveIndex = (RelativeLayout)view.findViewById(R.id.explore_love_layout);
        loveIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), LoveIndexActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        RelativeLayout shareIndex = (RelativeLayout)view.findViewById(R.id.explore_share_layout);
        shareIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), ShareIndexActivity.class);
                view.getContext().startActivity(intent);
            }
        });

        LinearLayout incomeIndex = (LinearLayout) view.findViewById(R.id.explore_income_layout);
        incomeIndex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(view.getContext(), IncomeIndexActivity.class);
                view.getContext().startActivity(intent);
            }
        });*/









    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        initData(savedInstanceState);


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
        incomeFragmentNavigator = null;
        loveFragmentNavigator = null;
        shareFragmentNavigator = null;
        indexFragmentNavigator = null;
    }

    private void initData(final Bundle savedInstanceState){
        HttpAPI.discoveryHome( new HttpAPI.Callback<DiscoveryModel>() {
            @Override
            public void onSuccess(DiscoveryModel discoveryModel) {
                if(discoveryModel.getState() == 0){

                    //initLove(savedInstanceState,discoveryModel);
                    initIncome(savedInstanceState,discoveryModel);
                    initIndex(savedInstanceState);
                    //initShare(savedInstanceState,discoveryModel);
                }else{
                    //// TODO: 2017/1/10 提示用户系统出问题
                }


            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
            }
        });
    }



    /*private void initLove(Bundle savedInstanceState, DiscoveryModel discoveryModel){
        OtherFragmentAdapter otherFragmentAdapter = new OtherFragmentAdapter();
        otherFragmentAdapter.setDiscoveryModel(discoveryModel);
        loveFragmentNavigator = new FragmentNavigator(getChildFragmentManager(), otherFragmentAdapter, R.id.fragment_other_love_sub_container);
        loveFragmentNavigator.onCreate(savedInstanceState);
        loveFragmentNavigator.showFragment(Constants.OTHER_LOVE_INDEX_SUB);
    }*/

    private void initIncome(Bundle savedInstanceState,DiscoveryModel discoveryModel){
        OtherFragmentAdapter otherFragmentAdapter = new OtherFragmentAdapter();
        otherFragmentAdapter.setDiscoveryModel(discoveryModel);
        incomeFragmentNavigator = new FragmentNavigator(getChildFragmentManager(), otherFragmentAdapter, R.id.fragment_other_income_sub_container);
        incomeFragmentNavigator.onCreate(savedInstanceState);
        incomeFragmentNavigator.showFragment(Constants.OTHER_INCOME_INDEX_SUB);
    }

   /* private void initShare(Bundle savedInstanceState,DiscoveryModel discoveryModel){
        OtherFragmentAdapter otherFragmentAdapter = new OtherFragmentAdapter();
        otherFragmentAdapter.setDiscoveryModel(discoveryModel);
        shareFragmentNavigator = new FragmentNavigator(getChildFragmentManager(), otherFragmentAdapter, R.id.fragment_other_share_sub_container);
        shareFragmentNavigator.onCreate(savedInstanceState);
        shareFragmentNavigator.showFragment(Constants.OTHER_SHARE_INDEX_SUB);
    }*/

    private void initIndex(Bundle savedInstanceState){
        OtherFragmentAdapter otherFragmentAdapter = new OtherFragmentAdapter();
        indexFragmentNavigator = new FragmentNavigator(getChildFragmentManager(), otherFragmentAdapter, R.id.fragment_other_index_container);
        indexFragmentNavigator.onCreate(savedInstanceState);
        indexFragmentNavigator.showFragment(Constants.OTHER_INDEX);
    }




}
