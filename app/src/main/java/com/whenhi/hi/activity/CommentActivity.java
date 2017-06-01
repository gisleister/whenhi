package com.whenhi.hi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.TextView;

import com.aspsine.fragmentnavigator.FragmentNavigator;
import com.whenhi.hi.Constants;
import com.whenhi.hi.R;
import com.whenhi.hi.fragment.DetailFragmentAdapter;
import com.whenhi.hi.fragment.OtherFragmentAdapter;
import com.whenhi.hi.model.Feed;

public class CommentActivity extends BaseActivity{

    private static final String TAG =CommentActivity.class.getSimpleName();

    private FragmentNavigator mFragmentNavigator;
    private  Toolbar mToolbar;
    private TextView mTextView;
    private ActionBar mActionBar;
    private DetailFragmentAdapter mDetailFragmentAdapter;
    private Feed mFeed;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
        Intent intent = getIntent();
        mFeed = (Feed)intent.getSerializableExtra("Feed");
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mTextView = (TextView) findViewById(R.id.toolbar_title);
        mTextView.setText("评论");
        setSupportActionBar(mToolbar);


        initView(savedInstanceState);

    }

    private void initView(Bundle savedInstanceState){
        mDetailFragmentAdapter = new DetailFragmentAdapter(mFeed,this);
        mFragmentNavigator = new FragmentNavigator(getSupportFragmentManager(), mDetailFragmentAdapter, R.id.fragment_other_comment_container);
        mFragmentNavigator.onCreate(savedInstanceState);
        setDefaultFrag();
    }


    /*设置默认Fragment*/
    private void setDefaultFrag() {
        mFragmentNavigator.showFragment(Constants.DETAIL_COMMENT);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFragmentNavigator = null;
        mToolbar = null;
        mTextView = null;
        mActionBar = null;
        mDetailFragmentAdapter.destroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }


}




