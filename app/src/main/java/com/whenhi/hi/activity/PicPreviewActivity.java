package com.whenhi.hi.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import com.aspsine.fragmentnavigator.FragmentNavigator;
import com.whenhi.hi.Constants;
import com.whenhi.hi.R;
import com.whenhi.hi.adapter.PicPreviewAdapter;
import com.whenhi.hi.fragment.OtherFragmentAdapter;
import com.whenhi.hi.model.Feed;
import com.whenhi.hi.model.Image;
import com.whenhi.hi.view.pager.WhenhiViewPager;

import java.util.ArrayList;
import java.util.List;

public class PicPreviewActivity extends BaseActivity{

    private static final String TAG =PicPreviewActivity.class.getSimpleName();

    PicPreviewAdapter adapter;


    WhenhiViewPager pager;
    private Feed mFeed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pic_preview);
        Intent intent = getIntent();
        mFeed = (Feed)intent.getSerializableExtra("Feed");

        pager = (WhenhiViewPager) findViewById(R.id.pager);

        List<Image> list = new ArrayList<>();

        if(mFeed.getResList().size() > 0){
            list = mFeed.getResList();
        }else{
            Image image = new Image();
            image.setContent(mFeed.getPicUrl());
            list.add(image);
        }

        adapter = new PicPreviewAdapter(getSupportFragmentManager(), list);
        pager.setAdapter(adapter);


    }



}




