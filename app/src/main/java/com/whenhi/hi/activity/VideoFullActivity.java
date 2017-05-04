package com.whenhi.hi.activity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.PixelFormat;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.whenhi.hi.R;
import com.whenhi.hi.model.Feed;

import org.lynxz.zzplayerlibrary.constant.PlayState;
import org.lynxz.zzplayerlibrary.controller.IPlayerImpl;
import org.lynxz.zzplayerlibrary.util.OrientationUtil;
import org.lynxz.zzplayerlibrary.widget.VideoPlayer;

/**
 * Created by 王雷 on 2016/12/25.
 */

public class VideoFullActivity extends BaseActivity {

    private VideoPlayer mVideoPlayer;
    private String mVideoUrl;
    private int mLastUpdateTime;
    private String qiniuUrl;
    private String title;
    private boolean isPortrait;

    public static final int VERTICAL = 0x00;
    public static final int HORIZONTAL = 0x01;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        Bundle bundle = this.getIntent().getExtras();
        mVideoUrl = bundle.getString("VideoUrl");
        qiniuUrl = bundle.getString("QiniuUrl");
        mLastUpdateTime = bundle.getInt("LastUpdateTime");
        title = bundle.getString("title");
        isPortrait = bundle.getBoolean("isPortrait");


        setContentView(R.layout.activity_video_full);




        initView();
        initListener();

    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (mVideoPlayer != null) {
            mVideoPlayer.updateActivityOrientation();
        }
    }

    private void initListener() {
        mVideoPlayer.setPlayerController(playerImpl);
    }


    private void initView() {

        mVideoPlayer = (VideoPlayer) findViewById(R.id.video_full);
        mVideoPlayer.setTitle(title);
        mVideoPlayer.loadAndStartVideo(this, mVideoUrl);
        mVideoPlayer.setLastUpdateTime(mLastUpdateTime);
        mVideoPlayer.setLastPlayingPos(mLastUpdateTime);
        //mVideoPlayer.startOrRestartPlay();
        mVideoPlayer.getVideoView().seekTo(mLastUpdateTime);
        //设置控制栏播放/暂停/全屏/退出全屏按钮图标
        mVideoPlayer.setIconPlay(R.mipmap.play);
        mVideoPlayer.setIconPause(R.mipmap.pause);
        mVideoPlayer.setIconExpand(R.mipmap.shrink);
        mVideoPlayer.setIconShrink(R.mipmap.shrink);
        //隐藏/显示控制栏时间值信息
        // mVp.hideTimes();
        mVideoPlayer.showTimes();
        // 自定义加载框图标
        mVideoPlayer.setIconLoading(R.mipmap.loading_red_rotate);

        // 设置进度条样式
        mVideoPlayer.setProgressThumbDrawable(R.mipmap.progress_thumb);
        mVideoPlayer.setProgressLayerDrawables(R.drawable.shape_video_progressbar);//自定义的layer-list

        mVideoPlayer.setIOrientationImpl(iOrientationImpl);

        if(isPortrait){
            OrientationUtil.forceOrientation(this,VERTICAL);
        }else {
            OrientationUtil.forceOrientation(this,HORIZONTAL);
        }


    }

    private VideoPlayer.IOrientationImpl iOrientationImpl = new VideoPlayer.IOrientationImpl() {
        @Override
        public void onOrientationChange(boolean isPortrait) {
            finish();
            // OrientationUtil.changeOrientation(VideoFullActivity.this);
            /*int orientation = OrientationUtil.getOrientation(VideoFullActivity.this);
            if (orientation == OrientationUtil.HORIZONTAL) {
                OrientationUtil.forceOrientation(VideoFullActivity.this, OrientationUtil.VERTICAL);
            }else{
                OrientationUtil.forceOrientation(VideoFullActivity.this, OrientationUtil.HORIZONTAL);
            }*/
        }
    };


    public IPlayerImpl playerImpl = new IPlayerImpl() {

        @Override
        public void onNetWorkError() {

        }

        @Override
        public void onBack() {
            // 全屏播放时,单击左上角返回箭头,先回到竖屏状态,再关闭
            // 这里功能最好跟onBackPressed()操作一致
            finish();

        }

        @Override
        public void onError() {
            Toast.makeText(VideoFullActivity.this, "视频有点小问题，正自我纠正中，稍等一秒", Toast.LENGTH_SHORT).show();
            mVideoPlayer.loadAndStartVideo(VideoFullActivity.this, qiniuUrl);
        }
    };

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


    }



}