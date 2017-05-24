package com.whenhi.hi.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.whenhi.hi.App;
import com.whenhi.hi.R;
import com.whenhi.hi.adapter.HaibiIndexListAdapter;
import com.whenhi.hi.model.UserScoreModel;
import com.whenhi.hi.network.HttpAPI;

/**
 * A simple {@link Fragment} subclass.
 */
public class HaibiIndexListFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener {
    private static final String TAG = HaibiIndexListFragment.class.getSimpleName();


    private SwipeToLoadLayout mSwipeToLoadLayout;

    private RecyclerView mRecyclerView;

    private HaibiIndexListAdapter mAdapter;

    private int mPageNo = 1;
    private String mExtras = "";

    public static HaibiIndexListFragment newInstance() {
        HaibiIndexListFragment fragment = new HaibiIndexListFragment();
        return fragment;
    }

    public HaibiIndexListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new HaibiIndexListAdapter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.list_base, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mSwipeToLoadLayout = (SwipeToLoadLayout) view.findViewById(R.id.swipeToLoadLayout);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.swipe_target);

        final RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setAutoMeasureEnabled(true);


        mRecyclerView.setNestedScrollingEnabled(false);

        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //mRecyclerView.getRecycledViewPool().setMaxRecycledViews(mAdapter.getItemViewType(0),3);


        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);


        mRecyclerView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                if (newState == RecyclerView.SCROLL_STATE_IDLE ){
                    if (!ViewCompat.canScrollVertically(recyclerView, 1)){
                        mSwipeToLoadLayout.setLoadingMore(true);
                    }
                }
            }
        });
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mSwipeToLoadLayout.post(new Runnable() {
            @Override
            public void run() {
                mSwipeToLoadLayout.setRefreshing(true);
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mSwipeToLoadLayout.isRefreshing()) {
            mSwipeToLoadLayout.setRefreshing(false);
        }
        if (mSwipeToLoadLayout.isLoadingMore()) {
            mSwipeToLoadLayout.setLoadingMore(false);
        }
    }

    @Override
    public void onLoadMore() {
        mSwipeToLoadLayout.setLoadingMore(false);
        /* mPageNo++;
        HttpAPI.userIndexList(mPageNo,mExtras,new HttpAPI.Callback<UserScoreModel>() {
            @Override
            public void onSuccess(UserScoreModel userScoreModel) {
                if(userScoreModel.getState() == 0){
                    mExtras = userScoreModel.getData().getExtras();
                    mAdapter.append(userScoreModel.getData().getList());
                    mSwipeToLoadLayout.setLoadingMore(false);
                }else{
                    //// TODO: 2017/1/10 提示用户服务器何种问题
                }

            }

            @Override
            public void onFailure(Exception e) {
                mSwipeToLoadLayout.setLoadingMore(false);
            }
        });*/
    }
    @Override
    public void onRefresh() {
        mPageNo = 1;
        HttpAPI.userIndexList(mPageNo,mExtras,new HttpAPI.Callback<UserScoreModel>() {
            @Override
            public void onSuccess(UserScoreModel userScoreModel) {
                mSwipeToLoadLayout.setRefreshing(false);
                if(userScoreModel.getState() == 0){
                    mExtras = userScoreModel.getData().getExtras();
                    mAdapter.setList(userScoreModel.getData().getList());

                }else{
                    Toast.makeText(App.getContext(), userScoreModel.getMsgText(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(App.getContext(), "服务器异常", Toast.LENGTH_SHORT).show();
                mSwipeToLoadLayout.setRefreshing(false);
            }
        });


    }

    @Override
    public void destroy() {
       /* if(viewCreate){
            mRecyclerView.removeAllViews();
            mRecyclerView.setAdapter(null);
            mRecyclerView.setAdapter(mAdapter);
           // mAdapter.notifyDataSetChanged();
        }*/
    }

    @Override
    public void onvisible() {
        /*if(viewCreate) {
            mSwipeToLoadLayout.setRefreshing(true);
        }
        onRefresh();*/
    }

}
