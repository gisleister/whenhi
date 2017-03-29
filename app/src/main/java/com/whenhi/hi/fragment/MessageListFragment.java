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
import com.whenhi.hi.adapter.MessageAdapter;
import com.whenhi.hi.decorator.InsetWhenhi;
import com.whenhi.hi.listener.NoticeListener;
import com.whenhi.hi.listener.OnItemClickListener;
import com.whenhi.hi.listener.OnItemLongClickListener;
import com.whenhi.hi.model.Message;
import com.whenhi.hi.model.MessageModel;
import com.whenhi.hi.network.HttpAPI;
import com.whenhi.hi.receiver.NoticeTransfer;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 王雷 on 2016/12/25.
 */

public class MessageListFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener,
        OnItemClickListener<Message>,
        OnItemLongClickListener<Message> {
    private static final String TAG = MessageListFragment.class.getSimpleName();

    public static final int TYPE_LINEAR = 0;

    private SwipeToLoadLayout mSwipeToLoadLayout;

    private RecyclerView mRecyclerView;

    private MessageAdapter mAdapter;

    private int mPageNo = 1;
    private String mExtras = "";
    String mUserId;
    String mToken;

    public static Fragment newInstance() {
        MessageListFragment fragment = new MessageListFragment();
        return fragment;
    }

    public MessageListFragment() {
        NoticeTransfer noticeTransfer = new NoticeTransfer();
        noticeTransfer.setNoticeListener(mNoticeListener);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new MessageAdapter();
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
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

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
    public void onItemClick(int position, Message content, View view) {

    }

    @Override
    public boolean onClickItemLongClick(int groupPosition, Message character, View view) {
        return false;
    }

    @Override
    public void onLoadMore() {
        mPageNo++;

        HttpAPI.getMessage(mPageNo,mExtras, new HttpAPI.Callback<MessageModel>() {
            @Override
            public void onSuccess(MessageModel messageModel) {
                mSwipeToLoadLayout.setLoadingMore(false);
                if(messageModel.getState() == 0){
                    mExtras = messageModel.getData().getExtras();
                    mAdapter.append(messageModel.getData().getList());

                }else{
                    Toast.makeText(App.getContext(), messageModel.getMsgText(), Toast.LENGTH_SHORT).show();
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
        HttpAPI.getMessage(mPageNo,mExtras, new HttpAPI.Callback<MessageModel>() {
            @Override
            public void onSuccess(MessageModel messageModel) {
                mSwipeToLoadLayout.setRefreshing(false);
                if(messageModel.getState() == 0){
                    mExtras = messageModel.getData().getExtras();
                    mAdapter.setList(messageModel.getData().getList());
                }else{
                    Toast.makeText(App.getContext(), messageModel.getMsgText(), Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(App.getContext(), "服务器异常", Toast.LENGTH_SHORT).show();
                mSwipeToLoadLayout.setLoadingMore(false);
            }
        });


    }

    private NoticeListener mNoticeListener = new NoticeListener() {
        @Override
        public void refreshMeesage() {
            onRefresh();
        }
    };


}
