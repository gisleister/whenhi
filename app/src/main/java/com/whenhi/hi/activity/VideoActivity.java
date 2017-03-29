package com.whenhi.hi.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.fragmentnavigator.FragmentNavigator;
import com.whenhi.hi.App;
import com.whenhi.hi.Constants;
import com.whenhi.hi.R;
import com.whenhi.hi.fragment.DetailFragmentAdapter;
import com.whenhi.hi.listener.CommentListener;
import com.whenhi.hi.model.BaseModel;
import com.whenhi.hi.model.Comment;
import com.whenhi.hi.model.Feed;
import com.whenhi.hi.network.HttpAPI;
import com.whenhi.hi.receiver.NoticeTransfer;
import com.whenhi.hi.util.ClickUtil;

import org.lynxz.zzplayerlibrary.widget.VideoPlayer;


public class VideoActivity extends BaseActivity{

    private static final String TAG =VideoActivity.class.getSimpleName();

    private FragmentNavigator mFragmentNavigator;
    private Feed mFeed;
    private EditText mCommentEditText;
    private TextView mCommentSend;
    private int targetType = 1;
    private int targetId = 0;
    private String content;

    private VideoPlayer mVideoPlayer;

    private  Toolbar mToolbar;
    private TextView mTextView;
    private ActionBar mActionBar;
    private DetailFragmentAdapter mDetailFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
        Intent intent = getIntent();
        mFeed = (Feed)intent.getSerializableExtra("Feed");
        initView(savedInstanceState);

        mToolbar = (Toolbar) findViewById(R.id.toolbar).findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mTextView = (TextView) findViewById(R.id.toolbar).findViewById(R.id.toolbar_title);
        mTextView.setText("很嗨-视频");

        mToolbar.setNavigationIcon(R.mipmap.fanhui);
        mActionBar = getSupportActionBar();
        if (mActionBar != null){
            mActionBar.setDisplayHomeAsUpEnabled(false);
            mActionBar.setDisplayShowTitleEnabled(false);
        }

        mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_share:
                        if(Constants.DEBUG){
                            if(App.isLogin()){
                                HttpAPI.updateNoOk(mFeed,new HttpAPI.Callback<BaseModel>() {
                                    @Override
                                    public void onSuccess(BaseModel baseModel) {
                                        if(baseModel.getState() == 0){
                                            Toast.makeText(App.getContext(), "非优质内容上报成功", Toast.LENGTH_SHORT).show();
                                        }else{
                                            Toast.makeText(App.getContext(), baseModel.getMsgText(), Toast.LENGTH_SHORT).show();
                                        }

                                    }

                                    @Override
                                    public void onFailure(Exception e) {
                                        Toast.makeText(App.getContext(), "服务器出问题了", Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }else{
                                ClickUtil.goToLogin(getWindow().getDecorView());
                            }

                        }else{
                            Intent intent = new Intent(VideoActivity.this, ShareActivity.class);
                            intent.putExtra("Feed", mFeed);
                            startActivity(intent);
                            overridePendingTransition(R.anim.activity_open,0);
                        }

                        break;
                    default:
                        break;
                }
                return true;
            }
        });


        NoticeTransfer noticeTransfer = new NoticeTransfer();
        noticeTransfer.setCommentListener(mCommentListener);


        mCommentEditText = (EditText)findViewById(R.id.comment_edit_text);
        mCommentSend = (TextView)findViewById(R.id.comment_send);
        mCommentSend.setOnClickListener(new Button.OnClickListener(){//创建监听
            public void onClick(View v) {

                if(targetType == 1){
                    targetId = mFeed.getId();
                }

                content = mCommentEditText.getText().toString();
                ClickUtil.addComment(mFeed,targetId,targetType,content,VideoActivity.this);
                mCommentEditText.setText("");
            }

        });

    }


    private CommentListener mCommentListener = new CommentListener() {
        @Override
        public void commentSuccess() {
            Comment comment = new Comment();
            comment.setUserId(Integer.parseInt(App.getUserId()));
            comment.setUserLogo(App.getUserLogo());
            comment.setUserName(App.getNickname());
            comment.setContent(content);
            comment.setFeedId(mFeed.getId());
            mDetailFragmentAdapter.refresh(Constants.DETAIL_VIDEO,comment);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    private void initView(Bundle savedInstanceState){
        mDetailFragmentAdapter = new DetailFragmentAdapter(mFeed,this);
        mFragmentNavigator = new FragmentNavigator(getSupportFragmentManager(), mDetailFragmentAdapter, R.id.fragment_detail_video_container);
        mFragmentNavigator.onCreate(savedInstanceState);
        setDefaultFrag();
    }

    public void setVideoPlayer(VideoPlayer videoPlayer){
        mVideoPlayer = videoPlayer;
    }


    /*设置默认Fragment*/
    private void setDefaultFrag() {
        mFragmentNavigator.showFragment(Constants.DETAIL_VIDEO);

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }


    public void acceptCommentClick(Comment comment){
        targetType = 2;
        targetId = comment.getId();
        mCommentEditText.setHint("@"+comment.getUserName());
    }


    @Override
    protected void onResume() {
        super.onResume();
        if(mVideoPlayer != null){
            mVideoPlayer.onHostResume();
        }

    }

    @Override
    protected void onPause() {
        super.onPause();
        if(mVideoPlayer != null) {
            mVideoPlayer.onHostPause();
        }
    }

    @Override
    public void onBackPressed() {
        if(mVideoPlayer != null){
            mVideoPlayer.onHostDestroy();
        }

        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(mVideoPlayer != null){
            mVideoPlayer.stopPlay();
            mVideoPlayer.onHostDestroy();
        }

        mFragmentNavigator = null;
        mFeed = null;
        mCommentEditText = null;
        mCommentSend = null;
        mToolbar = null;
        mTextView = null;
        mActionBar = null;
        //mDetailFragmentAdapter.destroy();

    }

}




