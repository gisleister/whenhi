package com.whenhi.hi.fragment;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.whenhi.hi.R;
import com.whenhi.hi.activity.PicActivity;
import com.whenhi.hi.activity.TextActivity;
import com.whenhi.hi.adapter.PicCommentAdapter;
import com.whenhi.hi.adapter.TextCommentAdapter;
import com.whenhi.hi.listener.OnChildItemClickListener;
import com.whenhi.hi.listener.OnItemClickListener;
import com.whenhi.hi.listener.OnItemLongClickListener;
import com.whenhi.hi.model.Comment;
import com.whenhi.hi.model.CommentModel;
import com.whenhi.hi.model.Feed;
import com.whenhi.hi.network.HttpAPI;

/**
 * Created by 王雷 on 2016/12/26.
 */

public class TextFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener,OnChildItemClickListener<Comment> {
    private static final String TAG = PicFragment.class.getSimpleName();


    private SwipeToLoadLayout mSwipeToLoadLayout;

    private RecyclerView mRecyclerView;

    private TextCommentAdapter mAdapter;


    private int mPageNo = 1;
    private String mExtras = "";

    private static Feed mFeed;

    private boolean viewCreate;

    public boolean getViewCreate() {
        return viewCreate;
    }

    public void setViewCreate(boolean viewCreate) {
        this.viewCreate = viewCreate;
    }

    public static TextFragment newInstance(Feed feed) {
        TextFragment fragment = new TextFragment();
        mFeed = feed;
        return fragment;
    }

    public TextFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new TextCommentAdapter(mFeed);
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

        mRecyclerView.getRecycledViewPool().setMaxRecycledViews(mAdapter.getItemViewType(1),3);

        setViewCreate(true);


        mSwipeToLoadLayout.setOnRefreshListener(this);
        mSwipeToLoadLayout.setOnLoadMoreListener(this);

        mAdapter.setOnChildItemClickListener(this);
        // mAdapter.setOnChildItemLongClickListener(this);

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
        HttpAPI.requestComment(mFeed.getId(),mExtras,mPageNo, new HttpAPI.Callback<CommentModel>() {
            @Override
            public void onSuccess(CommentModel commentModel) {
                if(commentModel.getState() == 0){
                    mExtras = commentModel.getData().getExtras();
                    mAdapter.append(commentModel.getData().getList());
                    mSwipeToLoadLayout.setLoadingMore(false);
                }else{
                    //// TODO: 2017/1/10 提示用户系统出问题
                }

            }

            @Override
            public void onFailure(Exception e) {
                mSwipeToLoadLayout.setLoadingMore(false);
            }
        });
    }

    @Override
    public void onRefresh() {

        mPageNo = 1;
        HttpAPI.requestComment(mFeed.getId(),mExtras,mPageNo, new HttpAPI.Callback<CommentModel>() {
            @Override
            public void onSuccess(CommentModel commentModel) {
                if(commentModel.getState() == 0){
                    mExtras = commentModel.getData().getExtras();
                    mAdapter.setList(commentModel.getData().getList());
                    mSwipeToLoadLayout.setRefreshing(false);
                }else{
                    //// TODO: 2017/1/10 提示用户系统出问题
                }

            }

            @Override
            public void onFailure(Exception e) {
                e.printStackTrace();
                mSwipeToLoadLayout.setRefreshing(false);
            }
        });


    }


    @Override
    public void onChildItemClick(int childPosition, Comment comment, View view) {
        Context context = view.getContext();
        if (context instanceof TextActivity){
            TextActivity activity = (TextActivity)context;
            activity.acceptCommentClick(comment);
        }
    }

    @Override
    public void destroy() {
        if(viewCreate){
            /*mRecyclerView.removeAllViews();
            mRecyclerView.setAdapter(null);
            mRecyclerView.setAdapter(mAdapter);*/
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

    public void refreshComment(Comment comment){
        mAdapter.refreshComment(comment);
    }
}
