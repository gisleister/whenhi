package com.whenhi.hi.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whenhi.hi.R;
import com.whenhi.hi.adapter.ShareIndexAdapter;
import com.whenhi.hi.listener.OnItemClickListener;
import com.whenhi.hi.listener.OnItemLongClickListener;
import com.whenhi.hi.model.Feed;
import com.whenhi.hi.util.ClickUtil;

import java.util.List;

/**
 * Created by 王雷 on 2017/2/28.
 */

public class ShareIndexFragment extends BaseFragment implements OnItemClickListener<Feed>, OnItemLongClickListener<Feed> {
    private static final String TAG = LoveIndexListFragment.class.getSimpleName();


    private RecyclerView mRecyclerView;

    private ShareIndexAdapter mAdapter;
    private static List<Feed> mFeeds;

    private int mPageNo = 1;
    private String mExtras = "";

    public static Fragment newInstance(List<Feed> feeds) {
        mFeeds = feeds;
        ShareIndexFragment fragment = new ShareIndexFragment();
        return fragment;
    }

    public ShareIndexFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ShareIndexAdapter();
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
        mRecyclerView = (RecyclerView) view.findViewById(R.id.swipe_target);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);


        mAdapter.setOnItemClickListener(this);
        mAdapter.setOnItemLongClickListener(this);
        mAdapter.setList(mFeeds);

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
    public void onItemClick(int position, Feed feed, View view) {
        ClickUtil.click(feed,view.getContext());
    }

    @Override
    public boolean onClickItemLongClick(int groupPosition, Feed feed, View view) {
        ClickUtil.goToShare(view.getContext(),feed);

        return true;
    }
}
