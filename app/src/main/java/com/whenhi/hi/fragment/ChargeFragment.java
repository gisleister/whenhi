package com.whenhi.hi.fragment;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.whenhi.hi.R;
import com.whenhi.hi.activity.ChargeActivity;
import com.whenhi.hi.adapter.ChargeAdapter;
import com.whenhi.hi.listener.OnItemClickListener;
import com.whenhi.hi.listener.OnItemLongClickListener;
import com.whenhi.hi.model.Charge;
import com.whenhi.hi.model.ChargeModel;
import com.whenhi.hi.network.HttpAPI;

/**
 * Created by 王雷 on 2017/2/28.
 */

public class ChargeFragment extends BaseFragment implements OnRefreshListener, OnLoadMoreListener,
        OnItemClickListener<Charge>,
        OnItemLongClickListener<Charge> {

    private static final String TAG = ChargeFragment.class.getSimpleName();

    private SwipeToLoadLayout mSwipeToLoadLayout;

    private RecyclerView mRecyclerView;

    private ChargeAdapter mAdapter;

    private int mPageNo = 1;
    private String mExtras = "";

    public static Fragment newInstance() {
        ChargeFragment fragment = new ChargeFragment();
        return fragment;
    }

    public ChargeFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new ChargeAdapter();
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
        GridLayoutManager layoutManager = null;
        layoutManager = new GridLayoutManager(getContext(),3);
        layoutManager.setAutoMeasureEnabled(true);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.setAdapter(mAdapter);

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
    public void onPause() {
        super.onPause();
        //App.getRequestQueue().cancelAll(TAG + "refresh");
        //App.getRequestQueue().cancelAll(TAG+"loadmore");
        if (mSwipeToLoadLayout.isRefreshing()) {
            mSwipeToLoadLayout.setRefreshing(false);
        }
        if (mSwipeToLoadLayout.isLoadingMore()) {
            mSwipeToLoadLayout.setLoadingMore(false);
        }
    }

    @Override
    public void onRefresh() {
        mPageNo = 1;

       /**/ HttpAPI.getChargeItems(mExtras,mPageNo, new HttpAPI.Callback<ChargeModel>() {
            @Override
            public void onSuccess(ChargeModel phoneModel) {
                if(phoneModel.getState() == 0){
                    mExtras = phoneModel.getData().getExtras();
                    mAdapter.setList(phoneModel.getData().getList());
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
    public void onLoadMore() {
        mPageNo++;
        mSwipeToLoadLayout.setLoadingMore(false);
        /*HttpAPI.getChargeItems(mExtras,mPageNo, new HttpAPI.Callback<ChargeModel>() {
            @Override
            public void onSuccess(ChargeModel phoneModel) {
                if(phoneModel.getState() == 0){
                    mExtras = phoneModel.getData().getExtras();
                   mAdapter.append(phoneModel.getData().getList());
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
    public void onItemClick(int position, Charge charge, View view) {
        Context context = view.getContext();
        if (context instanceof ChargeActivity){
            ChargeActivity activity = (ChargeActivity)context;
            activity.acceptChargClick(charge.getId());
        }
    }

    @Override
    public boolean onClickItemLongClick(int groupPosition, Charge charge, View view) {
        return false;
    }
}
