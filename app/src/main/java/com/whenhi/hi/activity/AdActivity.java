package com.whenhi.hi.activity;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.fragmentnavigator.FragmentNavigator;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.whenhi.hi.App;
import com.whenhi.hi.Constants;
import com.whenhi.hi.R;
import com.whenhi.hi.fragment.DetailFragmentAdapter;
import com.whenhi.hi.model.BaseFeedModel;
import com.whenhi.hi.model.BaseModel;
import com.whenhi.hi.model.Comment;
import com.whenhi.hi.model.Feed;
import com.whenhi.hi.network.HttpAPI;
import com.whenhi.hi.receiver.NoticeTransfer;
import com.whenhi.hi.util.ClickUtil;

/**
 * Created by 王雷 on 2017/2/7.
 */


public class AdActivity extends BaseActivity {


    private FragmentNavigator mFragmentNavigator;
    private Feed mFeed;

    private DetailFragmentAdapter mDetailFragmentAdapter;

    private boolean isPush = false;
    private int feedId = 0;
    private int feedCategory = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
        Intent intent = getIntent();
        mFeed = (Feed) intent.getSerializableExtra("Feed");
        isPush = intent.getBooleanExtra("isPush",false);
        feedId = intent.getIntExtra("feedId",0);
        feedCategory = intent.getIntExtra("feedCategory",0);
        if(isPush){
            initData(savedInstanceState);
        }else{
            initView(savedInstanceState);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView textView = (TextView) findViewById(R.id.toolbar_title);
        textView.setText("详情");
        setSupportActionBar(toolbar);




    }

    private void initData(final Bundle savedInstanceState){
        HttpAPI.requestDetail(feedId,feedCategory, new HttpAPI.Callback<BaseFeedModel>() {
            @Override
            public void onSuccess(BaseFeedModel baseFeedModel) {

                if(baseFeedModel.getState() == 0){

                    mFeed = baseFeedModel.getData();
                    initView(savedInstanceState);
                }else{
                    Toast.makeText(App.getContext(), baseFeedModel.getMsgText(), Toast.LENGTH_SHORT).show();
                }


            }

            @Override
            public void onFailure(Exception e) {

            }
        });

    }



    private void initView(Bundle savedInstanceState){
        mDetailFragmentAdapter = new DetailFragmentAdapter(mFeed,this);
        mFragmentNavigator = new FragmentNavigator(getSupportFragmentManager(), mDetailFragmentAdapter, R.id.fragment_detail_ad_container);
        mFragmentNavigator.onCreate(savedInstanceState);
        setDefaultFrag();

    }


    /*设置默认Fragment*/
    private void setDefaultFrag() {
        mFragmentNavigator.showFragment(Constants.DETAIL_AD);

    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    protected void onDestroy() {

        super.onDestroy();
        mFragmentNavigator = null;
        mFeed = null;


    }


}
