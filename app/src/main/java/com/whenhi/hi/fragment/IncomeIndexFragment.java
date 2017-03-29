package com.whenhi.hi.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.whenhi.hi.R;
import com.whenhi.hi.activity.CraftActivity;
import com.whenhi.hi.activity.IncomeIndexActivity;
import com.whenhi.hi.adapter.IncomeIndexAdapter;
import com.whenhi.hi.listener.OnItemClickListener;
import com.whenhi.hi.listener.OnItemLongClickListener;
import com.whenhi.hi.model.User;

import java.util.List;

/**
 * Created by 王雷 on 2017/2/28.
 */

public class IncomeIndexFragment extends BaseFragment implements OnItemClickListener<User>, OnItemLongClickListener<User> {
    private static final String TAG = LoveIndexListFragment.class.getSimpleName();


    private RecyclerView mRecyclerView;

    private IncomeIndexAdapter mAdapter;
    private static List<User> mUsers;

    private int mPageNo = 1;
    private String mExtras = "";

    public static Fragment newInstance(List<User> users) {
        mUsers = users;
        IncomeIndexFragment fragment = new IncomeIndexFragment();
        return fragment;
    }

    public IncomeIndexFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAdapter = new IncomeIndexAdapter();
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
        mAdapter.setList(mUsers);

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
    public void onItemClick(int position, User user, View view) {
        Intent intent = new Intent(view.getContext(), IncomeIndexActivity.class);
        view.getContext().startActivity(intent);
    }

    @Override
    public boolean onClickItemLongClick(int groupPosition, User user, View view) {



        return true;
    }
}
