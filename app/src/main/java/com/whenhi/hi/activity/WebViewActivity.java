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
import com.whenhi.hi.model.BaseModel;
import com.whenhi.hi.model.Comment;
import com.whenhi.hi.model.Feed;
import com.whenhi.hi.model.BaseFeedModel;
import com.whenhi.hi.network.HttpAPI;
import com.whenhi.hi.receiver.NoticeTransfer;
import com.whenhi.hi.util.ClickUtil;

/**
 * Created by 王雷 on 2017/2/7.
 */


public class WebViewActivity extends BaseActivity {


    private FragmentNavigator mFragmentNavigator;
    private Feed mFeed;
    private int targetType = 1;
    private int targetId = 0;
    private String content;
    private EditText  commentEditText;
    private Button commentBtn;
    private DialogPlus dialog;

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
        setContentView(R.layout.activity_webview);
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

        commentBtn = (Button) findViewById(R.id.toolbar_comment_image);
        commentBtn.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {


                commentInput();
            }
        });



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


    private void showToolbarContent(ImageView zan, ImageView fav,ImageView share){

        if(mFeed.getLikeState() == 1){
            zan.setImageResource(R.mipmap.zan_click1);
        }else{
            zan.setImageResource(R.mipmap.zan1);
        }

        if(mFeed.getFavoriteState() == 1){
            fav.setImageResource(R.mipmap.shoucang_click1);

        }else{
            fav.setImageResource(R.mipmap.shoucang1);
        }


        ClickUtil.toolbarClickDetail(fav,zan,share,getWindow().getDecorView(),mFeed);

    }


    private void initView(Bundle savedInstanceState){
        mDetailFragmentAdapter = new DetailFragmentAdapter(mFeed,this);
        mFragmentNavigator = new FragmentNavigator(getSupportFragmentManager(), mDetailFragmentAdapter, R.id.fragment_detail_webview_container);
        mFragmentNavigator.onCreate(savedInstanceState);
        setDefaultFrag();

        ImageView zan = (ImageView)findViewById(R.id.toolbar_love_image);
        ImageView fav = (ImageView)findViewById(R.id.toolbar_fav_image);
        ImageView share = (ImageView)findViewById(R.id.toolbar_share_image);
        showToolbarContent(zan,fav,share);
    }


    /*设置默认Fragment*/
    private void setDefaultFrag() {
        mFragmentNavigator.showFragment(Constants.DETAIL_WEBVIEW);

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
        mToolbar = null;
        mTextView = null;
        mActionBar = null;

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
            Intent intent = new Intent(WebViewActivity.this, LoginActivity.class);
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
