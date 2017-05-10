package com.whenhi.hi.adapter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Toast;

import com.whenhi.hi.R;
import com.whenhi.hi.activity.VideoActivity;
import com.whenhi.hi.activity.VideoFullActivity;
import com.whenhi.hi.model.BaseModel;
import com.whenhi.hi.model.Feed;
import com.whenhi.hi.network.HttpAPI;

import org.lynxz.zzplayerlibrary.controller.IPlayerImpl;
import org.lynxz.zzplayerlibrary.util.OrientationUtil;
import org.lynxz.zzplayerlibrary.widget.VideoPlayer;

/**
 * Created by 王雷 on 2017/2/21.
 */

public class VideoAdapter{

    private VideoActivity mActivity;
    private Feed mFeed;

    private VideoPlayer mVideoPlayer;
    private String mVideoUrl;
    private int mCount = 0;

    public VideoAdapter(Activity activity, Feed feed, VideoPlayer videoPlayer) {
        mActivity = (VideoActivity)activity;
        mFeed = feed;
        mVideoPlayer = videoPlayer;
        mActivity.setVideoPlayer(videoPlayer);
        initData();
        initView();
        initListener();
    }


    private void initListener() {
        mVideoPlayer.setPlayerController(playerImpl);
    }

    private void initData() {
        mVideoUrl = mFeed.getPlayUrl();
    }

    private void initView() {
        mVideoPlayer.setTitle("视频名称");
        if(TextUtils.isEmpty(mVideoUrl)){
            Toast.makeText(mActivity, "视频地址为空，不能播放", Toast.LENGTH_SHORT).show();
            return;
        }


        mVideoPlayer.loadAndStartVideo(mActivity, mVideoUrl);
        //设置控制栏播放/暂停/全屏/退出全屏按钮图标
        mVideoPlayer.setIconPlay(R.mipmap.play);
        mVideoPlayer.setIconPause(R.mipmap.pause);
        mVideoPlayer.setIconExpand(R.mipmap.expand);
        mVideoPlayer.setIconShrink(R.mipmap.shrink);
        //隐藏/显示控制栏时间值信息
        // mVp.hideTimes();
        mVideoPlayer.showTimes();
        // 自定义加载框图标
        mVideoPlayer.setIconLoading(R.drawable.shape_video_loading);

        // 设置进度条样式
        mVideoPlayer.setProgressThumbDrawable(R.mipmap.progress_thumb);
        mVideoPlayer.setProgressLayerDrawables(R.drawable.shape_video_progressbar);//自定义的layer-list

        mVideoPlayer.setIOrientationImpl(iOrientationImpl);

    }

    public void goOnPlay(int lastTime){
        mVideoPlayer.loadAndStartVideo(mActivity, mVideoUrl);
        mVideoPlayer.setLastUpdateTime(lastTime);
        mVideoPlayer.setLastPlayingPos(lastTime);
        //mVideoPlayer.startOrRestartPlay();
        mVideoPlayer.getVideoView().seekTo(lastTime);
        mVideoPlayer.goOnPlay();
    }

    public int getLastTime(){
        return mVideoPlayer.getCurrentTime();
    }


    private VideoPlayer.IOrientationImpl iOrientationImpl = new VideoPlayer.IOrientationImpl() {
        @Override
        public void onOrientationChange(boolean isPortrait) {

            mVideoPlayer.pausePlay();
            Intent intent = new Intent(mActivity, VideoFullActivity.class);
            Bundle bundle=new Bundle();
            //传递name参数为tinyphp
            bundle.putString("VideoUrl", mVideoUrl);
            bundle.putString("QiniuUrl", mFeed.getQiniuPlayUrl());
            bundle.putInt("LastUpdateTime",mVideoPlayer.getCurrentTime());
            bundle.putBoolean("isPortrait",isPortrait);
            String title = mFeed.getContent();
            if(TextUtils.isEmpty(title)){
                title = "很嗨视频";
            }
            bundle.putString("title", title);
            intent.putExtras(bundle);
            mActivity.startActivity(intent);

        }
    };


    public IPlayerImpl playerImpl = new IPlayerImpl() {

        @Override
        public void onNetWorkError() {
            Toast.makeText(mActivity, "网络出问题了", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onBack() {
            // 全屏播放时,单击左上角返回箭头,先回到竖屏状态,再关闭
            // 这里功能最好跟onBackPressed()操作一致
            int orientation = OrientationUtil.getOrientation(mActivity);
            if (orientation == OrientationUtil.HORIZONTAL) {
                OrientationUtil.forceOrientation(mActivity, OrientationUtil.VERTICAL);
            } else {
                mActivity.finish();
            }

        }

        @Override
        public void onError() {

            if(mCount > 2)
                return;
            mCount++;
            Toast.makeText(mActivity, "视频有点小问题，正自我纠正中，稍等一秒", Toast.LENGTH_SHORT).show();
            mVideoPlayer.stopPlay();
            mVideoUrl = mFeed.getQiniuPlayUrl();
            if(TextUtils.isEmpty(mVideoUrl)){
                Toast.makeText(mActivity, "视频地址为空，不能播放", Toast.LENGTH_SHORT).show();
                return;
            }
            mVideoPlayer.loadAndStartVideo(mActivity, mVideoUrl);
            HttpAPI.noresReport(mFeed.getId(),mFeed.getFeedCategory(),new HttpAPI.Callback<BaseModel>() {
                @Override
                public void onSuccess(BaseModel baseModel) {
                    if(baseModel.getState() == 0){

                    }else{

                    }

                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        }
    };

}
