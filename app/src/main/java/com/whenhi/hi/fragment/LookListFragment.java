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
import com.whenhi.hi.adapter.FeedListAdapter;
import com.whenhi.hi.listener.OnItemClickListener;
import com.whenhi.hi.listener.OnItemLongClickListener;
import com.whenhi.hi.model.Feed;
import com.whenhi.hi.model.FeedModel;
import com.whenhi.hi.network.HttpAPI;
import com.whenhi.hi.util.ClickUtil;

/**
 * A simple {@link Fragment} subclass.
 */
public class LookListFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener,
        OnItemClickListener<Feed>,
        OnItemLongClickListener<Feed> {
    private static final String TAG = LookListFragment.class.getSimpleName();


    private SwipeToLoadLayout mSwipeToLoadLayout;

    private RecyclerView mRecyclerView;

    private FeedListAdapter mAdapter;

    private int mPageNo = 1;
    private String mExtras = "";

    private boolean viewCreate;

    public boolean getViewCreate() {
        return viewCreate;
    }

    public void setViewCreate(boolean viewCreate) {
        this.viewCreate = viewCreate;
    }

    public static LookListFragment newInstance() {
        LookListFragment fragment = new LookListFragment();
        return fragment;
    }

    public LookListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new FeedListAdapter();
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
        RecyclerView.LayoutManager layoutManager = null;
        layoutManager = new LinearLayoutManager(getContext());
        layoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

        //mRecyclerView.getRecycledViewPool().setMaxRecycledViews(mAdapter.getItemViewType(0),3);

        setViewCreate(true);

        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);

        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);

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
        mPageNo++;
        HttpAPI.lookList(mExtras,mPageNo, new HttpAPI.Callback<FeedModel>() {
            @Override
            public void onSuccess(FeedModel feedModel) {
                mSwipeToLoadLayout.setLoadingMore(false);
                if(feedModel.getState() == 0){
                    mExtras = feedModel.getData().getExtras();
                    mAdapter.append(feedModel.getData().getList());

                }else{
                    Toast.makeText(App.getContext(), feedModel.getMsgText(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(App.getContext(), "服务器异常", Toast.LENGTH_SHORT).show();
                mSwipeToLoadLayout.setLoadingMore(false);
            }
        });
    }
    @Override
    public void onRefresh() {
        mPageNo = 1;
        HttpAPI.lookList(mExtras,mPageNo, new HttpAPI.Callback<FeedModel>() {
            @Override
            public void onSuccess(FeedModel feedModel) {
                mSwipeToLoadLayout.setRefreshing(false);
                if(feedModel.getState() == 0){
                    mExtras = feedModel.getData().getExtras();
                    mAdapter.setList(feedModel.getData().getList());

                }else{
                    Toast.makeText(App.getContext(), feedModel.getMsgText(), Toast.LENGTH_SHORT).show();
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
    public void onItemClick(int position, Feed feed, View view) {
        ClickUtil.click(feed,view.getContext());
    }

    @Override
    public boolean onClickItemLongClick(int groupPosition, Feed feed, View view) {
        ClickUtil.goToShare(view.getContext(),feed);
        return true;
    }

    @Override
    public void destroy() {
        if(viewCreate){
            mRecyclerView.removeAllViews();
            mRecyclerView.setAdapter(null);
            mRecyclerView.setAdapter(mAdapter);
            //mAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onvisible() {
        if(viewCreate) {
            mSwipeToLoadLayout.setRefreshing(true);
        }
        onRefresh();
    }
}
