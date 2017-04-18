package com.whenhi.hi.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
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

public class TextActivity extends BaseActivity{

    private static final String TAG =TextActivity.class.getSimpleName();

    private FragmentNavigator mFragmentNavigator;
    private Feed mFeed;
    private EditText mCommentEditText;
    private TextView mCommentSend;
    private int targetType = 1;
    private int targetId = 0;
    private String content;


    private  Toolbar mToolbar;
    private TextView mTextView;
    private ActionBar mActionBar;

    private DetailFragmentAdapter mDetailFragmentAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_text);
        Intent intent = getIntent();
        mFeed = (Feed)intent.getSerializableExtra("Feed");
        initView(savedInstanceState);

        mToolbar = (Toolbar) findViewById(R.id.toolbar).findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);

        mTextView = (TextView) findViewById(R.id.toolbar).findViewById(R.id.toolbar_title);
        mTextView.setText("很嗨-段子");

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
                            Intent intent = new Intent(TextActivity.this, ShareActivity.class);
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


        mCommentEditText = (EditText)findViewById(R.id.comment_edit_text);
        mCommentSend = (TextView)findViewById(R.id.comment_send);
        mCommentSend.setOnClickListener(new Button.OnClickListener(){//创建监听
            public void onClick(View v) {

                if(targetType == 1){
                    targetId = mFeed.getId();
                }

                content = mCommentEditText.getText().toString();

                //ClickUtil.addComment(mFeed,targetId,targetType,content,TextActivity.this);
                mCommentEditText.setText("");
            }

        });

    }



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
        mFragmentNavigator = new FragmentNavigator(getSupportFragmentManager(), mDetailFragmentAdapter, R.id.fragment_detail_text_container);
        mFragmentNavigator.onCreate(savedInstanceState);
        setDefaultFrag();
    }


    /*设置默认Fragment*/
    private void setDefaultFrag() {
        mFragmentNavigator.showFragment(Constants.DETAIL_TEXT);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mFragmentNavigator = null;
        mFeed = null;
        mCommentEditText = null;
        mCommentSend = null;
        mToolbar = null;
        mTextView = null;
        mActionBar = null;
       // mDetailFragmentAdapter.destroy();
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
}




