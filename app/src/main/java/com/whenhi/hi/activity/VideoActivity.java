package com.whenhi.hi.activity;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaMetadataRetriever;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
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
import com.whenhi.hi.model.BaseModel;
import com.whenhi.hi.model.Comment;
import com.whenhi.hi.model.Feed;
import com.whenhi.hi.model.BaseFeedModel;
import com.whenhi.hi.network.HttpAPI;
import com.whenhi.hi.receiver.NoticeTransfer;
import com.whenhi.hi.util.ClickUtil;

import org.lynxz.zzplayerlibrary.controller.IPlayerImpl;
import org.lynxz.zzplayerlibrary.widget.VideoPlayer;

import java.util.HashMap;


public class VideoActivity extends BaseActivity{

    private static final String TAG =VideoActivity.class.getSimpleName();

    private FragmentNavigator mFragmentNavigator;
    private Feed mFeed;
    private int targetType = 1;
    private int targetId = 0;
    private String content;
    private EditText  commentEditText;
    private Button commentBtn;
    private DialogPlus dialog;

    private VideoPlayer mVideoPlayer;

    private  Toolbar mToolbar;
    private TextView mTextView;
    private ActionBar mActionBar;
    private DetailFragmentAdapter mDetailFragmentAdapter;

    private boolean isPush = false;
    private int feedId = 0;
    private int feedCategory = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);
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


        commentBtn = (Button) findViewById(R.id.toolbar_comment_image);
        commentBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {


                commentInput();
            }
        });



    }

    public void setVideoPlayer(VideoPlayer videoPlayer){
        mVideoPlayer = videoPlayer;
    }


    private void showToolbarContent(ImageView zan, ImageView fav,ImageView share){

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


        ClickUtil.toolbarClickDetailNew(fav,zan,share,getWindow().getDecorView(),mFeed);

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
            Intent intent = new Intent(VideoActivity.this, VideoFullActivity.class);
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
            Toast.makeText(VideoActivity.this, "视频有点小问题，正自我纠正中，稍等一秒", Toast.LENGTH_SHORT).show();
            mVideoPlayer.loadAndStartVideo(VideoActivity.this, mFeed.getQiniuPlayUrl());
        }
    };



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
        mFragmentNavigator = new FragmentNavigator(getSupportFragmentManager(), mDetailFragmentAdapter, R.id.fragment_detail_video_container);
        mFragmentNavigator.onCreate(savedInstanceState);
        setDefaultFrag();

        ImageView zan = (ImageView)findViewById(R.id.toolbar_love_image);
        ImageView fav = (ImageView)findViewById(R.id.toolbar_fav_image);
        ImageView share = (ImageView)findViewById(R.id.toolbar_share_image);
        showToolbarContent(zan,fav,share);

        ImageView rec = (ImageView)findViewById(R.id.toolbar_rec_image);

        rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HttpAPI.feedRecAdd(mFeed.getId(),mFeed.getFeedCategory(),new HttpAPI.Callback<BaseModel>() {
                    @Override
                    public void onSuccess(BaseModel baseModel) {
                        if(baseModel.getState() == 0){
                            Toast.makeText(App.getContext(), "推荐成功", Toast.LENGTH_SHORT).show();

                        }else{
                            Toast.makeText(App.getContext(), baseModel.getMsgText(), Toast.LENGTH_SHORT).show();
                        }

                    }

                    @Override
                    public void onFailure(Exception e) {
                        Toast.makeText(App.getContext(), "服务器貌似出问题了", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });



        mVideoPlayer = (VideoPlayer) findViewById(R.id.detail_video);
        mVideoPlayer.setTitle("");
        String videoUrl = mFeed.getPlayUrl();
        String vUrl = videoUrl.replaceFirst("https","http");
        mFeed.setPlayUrl(vUrl);
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
        mVideoPlayer.setIconLoading(R.drawable.shape_video_loading_rotate);

        // 设置进度条样式
        mVideoPlayer.setProgressThumbDrawable(R.mipmap.progress_thumb);
        mVideoPlayer.setProgressLayerDrawables(R.drawable.shape_video_progressbar);//自定义的layer-list

        mVideoPlayer.setIOrientationImpl(iOrientationImpl);
        initListener();
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
        mToolbar = null;
        mTextView = null;
        mActionBar = null;
        //mDetailFragmentAdapter.destroy();

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
                        }

                    }
                })
                .setExpanded(false)//设置扩展模式可控制dialog的高度

                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(final DialogPlus dialog, View view) {
                        switch (view.getId()) {
                            case R.id.comment_send:
                                updateComment();
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
        commentEditText.setHint(commentBtn.getText().toString());

        InputMethodManager imm = (InputMethodManager) commentEditText.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(0, InputMethodManager.SHOW_FORCED);

    }

    private void updateComment(){
        boolean isLogin = App.isLogin();

        if(isLogin){
            content = commentEditText.getText().toString();

            if(targetType == 1){
                targetId = mFeed.getId();
            }

            if(content.equals("")){
                Toast.makeText(App.getContext(), "多少要写点东西哦", Toast.LENGTH_SHORT).show();
                return;
            }
            HttpAPI.addComment(mFeed.getId(),targetId,targetType,content,new HttpAPI.Callback<BaseModel>() {
                @Override
                public void onSuccess(BaseModel baseModel) {
                    if(baseModel.getState() == 0){
                        Toast.makeText(App.getContext(), "评论成功", Toast.LENGTH_SHORT).show();
                        Comment comment = new Comment();
                        comment.setUserId(Integer.parseInt(App.getUserId()));
                        comment.setUserLogo(App.getUserLogo());
                        comment.setUserName(App.getNickname());
                        comment.setContent(content);
                        comment.setFeedId(mFeed.getId());
                        NoticeTransfer.commentSuccess(comment);
                        commentEditText.setText("继续写点东西");
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (imm != null) {
                            View v = getCurrentFocus();
                            imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                        }
                        dialog.dismiss();

                    }else{
                        Toast.makeText(App.getContext(), baseModel.getMsgText(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(App.getContext(), "服务器貌似出问题了", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Intent intent = new Intent(VideoActivity.this, LoginActivity.class);
            startActivity(intent);

            overridePendingTransition(R.anim.activity_open,0);
        }
    }


    public void acceptCommentClick(Comment comment){
        targetType = 2;
        targetId = comment.getId();
        commentBtn.setText("@"+comment.getUserName());
    }

}




