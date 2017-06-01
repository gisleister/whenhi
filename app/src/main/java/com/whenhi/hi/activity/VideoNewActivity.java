package com.whenhi.hi.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.whenhi.hi.App;
import com.whenhi.hi.R;
import com.whenhi.hi.model.Feed;
import com.whenhi.hi.util.ClickUtil;

import org.lynxz.zzplayerlibrary.controller.IPlayerImpl;
import org.lynxz.zzplayerlibrary.util.OrientationUtil;
import org.lynxz.zzplayerlibrary.widget.VideoPlayer;

import java.util.HashMap;

/**
 * Created by 王雷 on 2016/12/25.
 */

public class VideoNewActivity extends BaseActivity {

    private VideoPlayer mVideoPlayer;
    private Feed mFeed;

    public static final int VERTICAL = 0x00;
    public static final int HORIZONTAL = 0x01;
    private  Toolbar mToolbar;
    private TextView mTextView;


    private Button commentBtn;
    private DialogPlus dialog;
    private EditText commentEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //requestWindowFeature(Window.FEATURE_NO_TITLE);
        //this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
        //       WindowManager.LayoutParams.FLAG_FULLSCREEN);//去掉信息栏
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        mFeed = (Feed) intent.getSerializableExtra("Feed");

        setContentView(R.layout.activity_video_new);
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
        mVideoPlayer.setTitle("");
        mVideoPlayer.loadAndStartVideo(this, mFeed.getPlayUrl());
        //设置控制栏播放/暂停/全屏/退出全屏按钮图标
        mVideoPlayer.setIconPlay(R.mipmap.play);
        mVideoPlayer.setIconPause(R.mipmap.pause);
        mVideoPlayer.setIconExpand(R.mipmap.expand);
        mVideoPlayer.setIconShrink(R.mipmap.shrink);
        mVideoPlayer.setIconBack(R.mipmap.qita_icon_fanhui);
        //隐藏/显示控制栏时间值信息
        // mVp.hideTimes();
        mVideoPlayer.showTimes();
        // 自定义加载框图标
        mVideoPlayer.setIconLoading(R.mipmap.loading_red_rotate);

        // 设置进度条样式
        mVideoPlayer.setProgressThumbDrawable(R.mipmap.progress_thumb);
        mVideoPlayer.setProgressLayerDrawables(R.drawable.shape_video_progressbar);//自定义的layer-list

        mVideoPlayer.setIOrientationImpl(iOrientationImpl);
        //mVideoPlayer.showOrHideBars(true,true);

        ImageView zan = (ImageView)findViewById(R.id.toolbar_love_image);
        ImageView fav = (ImageView)findViewById(R.id.toolbar_fav_image);
        ImageView share = (ImageView)findViewById(R.id.toolbar_share_image);
        showToolbarContent(zan,fav,share);
        ImageView comment = (ImageView)findViewById(R.id.comment_list_image);
        comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(App.getContext(), CommentActivity.class);
                intent.putExtra("Feed", mFeed);
                startActivity(intent);
            }
        });

        commentBtn = (Button) findViewById(R.id.comment_button_image);
        commentBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {


                commentInput();
            }
        });

    }

    private VideoPlayer.IOrientationImpl iOrientationImpl = new VideoPlayer.IOrientationImpl() {
        @Override
        public void onOrientationChange() {
            boolean isPortrait = false;

            MediaMetadataRetriever mmr = new MediaMetadataRetriever();
            if (Build.VERSION.SDK_INT >= 14)
                mmr.setDataSource(mFeed.getPlayUrl(), new HashMap<String, String>());
            else
                mmr.setDataSource(mFeed.getPlayUrl());
            int width = Integer.parseInt(mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH));
            int height = Integer.parseInt(mmr.extractMetadata(android.media.MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT));
            if(height > width){
                isPortrait = true;
            }

            mVideoPlayer.pausePlay();
            Intent intent = new Intent(VideoNewActivity.this, VideoFullActivity.class);
            Bundle bundle=new Bundle();
            //传递name参数为tinyphp
            bundle.putString("VideoUrl", mFeed.getPlayUrl());
            bundle.putString("QiniuUrl", mFeed.getQiniuPlayUrl());
            bundle.putInt("LastUpdateTime",mVideoPlayer.getCurrentTime());
            bundle.putBoolean("isPortrait",isPortrait);
            String title = mFeed.getContent();
            if(TextUtils.isEmpty(title)){
                title = "很嗨视频";
            }
            bundle.putString("title", title);
            intent.putExtras(bundle);
            startActivity(intent);


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
            Toast.makeText(VideoNewActivity.this, "视频有点小问题，正自我纠正中，稍等一秒", Toast.LENGTH_SHORT).show();
            mVideoPlayer.loadAndStartVideo(VideoNewActivity.this, mFeed.getQiniuPlayUrl());
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

    private void commentInput(){
        Holder holder = new ViewHolder(R.layout.layout_dialog_comment);

        dialog = DialogPlus.newDialog(this)
                .setContentHolder(holder)
                .setCancelable(true)
                .setGravity(Gravity.BOTTOM)
                .setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogPlus dialog) {
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            View v = getCurrentFocus();
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                            //imm.hideSoftInputFromWindow(commentEditText.getWindowToken(), 0);
                        }

                    }
                })
                .setExpanded(false)//设置扩展模式可控制dialog的高度
                //.setMargin(0, 0, 0, 200)

                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final DialogPlus dialog, View view) {
                        switch (view.getId()) {
                            case R.id.comment_send:
                                break;
                        }

                    }
                })
                .create();
        dialog.show();

        commentEditText = (EditText)findViewById(R.id.comment_edit_text);
        commentEditText.setFocusable(true);
        commentEditText.setFocusableInTouchMode(true);
        commentEditText.requestFocus();

        //InputMethodManager imm = (InputMethodManager) commentEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        //imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

    }

    private void showToolbarContent(ImageView zan, ImageView fav, ImageView share){

        if(mFeed.getLikeState() == 1){
            zan.setImageResource(R.mipmap.xiangqing_icon_zan_click);
        }else{
            zan.setImageResource(R.mipmap.xiangqing_icon_zan);
        }

        if(mFeed.getFavoriteState() == 1){
            fav.setImageResource(R.mipmap.xiangqing_icon_shoucang_click);

        }else{
            fav.setImageResource(R.mipmap.xiangqing_icon_shoucang);
        }


        ClickUtil.toolbarClickDetail(fav,zan,share,getWindow().getDecorView(),mFeed);

    }



}