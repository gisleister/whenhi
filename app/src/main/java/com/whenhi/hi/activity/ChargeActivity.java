package com.whenhi.hi.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.aspsine.fragmentnavigator.FragmentNavigator;
import com.whenhi.hi.App;
import com.whenhi.hi.Constants;
import com.whenhi.hi.R;
import com.whenhi.hi.fragment.OtherFragmentAdapter;
import com.whenhi.hi.model.BaseModel;
import com.whenhi.hi.model.Feed;
import com.whenhi.hi.network.HttpAPI;
import com.whenhi.hi.receiver.NoticeTransfer;
import com.whenhi.hi.util.ClickUtil;

public class ChargeActivity extends BaseActivity{

    private static final String TAG =ChargeActivity.class.getSimpleName();
    private FragmentNavigator mFragmentNavigator;
    private String mobile;
    private int itemId = 0;
    private Button mDoCharg;
    private TextView mPhone;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_charge);
        Bundle bundle = this.getIntent().getExtras();
        mobile = bundle.getString("mobile");
        initView(savedInstanceState);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar).findViewById(R.id.toolbar);
        final TextView textView = (TextView) findViewById(R.id.toolbar).findViewById(R.id.toolbar_title);
        textView.setText("免费充值");
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.mipmap.fanhui);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(false);
            actionBar.setDisplayShowTitleEnabled(false);
        }



        mPhone = (TextView) findViewById(R.id.phone_text);
        mPhone.setText(mobile);

        mDoCharg = (Button)findViewById(R.id.phone_docharg);
        mDoCharg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                if(itemId != 0){
                    /*Toast.makeText(ChargeActivity.this, "恭喜您充值成功,马上推荐给朋友", Toast.LENGTH_SHORT).show();
                    NoticeTransfer.refresh();
                    Feed feed = new Feed();
                    feed.setContent("希望你可以打开看看");
                    feed.setShareUrl("http://wwww.baidu.com");
                    feed.setImageUrl("https://ss1.bdstatic.com/70cFvXSh_Q1YnxGkpoWK1HF6hhy/it/u=3844978820,224764709&fm=21&gp=0.jpg");

                    ClickUtil.goToShare(view, feed);
                    finish();*/
                    HttpAPI.doCharge(itemId,mobile,new HttpAPI.Callback<BaseModel>() {
                        @Override
                        public void onSuccess(BaseModel baseModel) {

                            if(baseModel.getState() == 0){
                                //进入分享界面
                                Toast.makeText(ChargeActivity.this, "恭喜您充值成功,马上推荐给朋友", Toast.LENGTH_SHORT).show();
                                NoticeTransfer.refresh();

                                Feed feed = new Feed();//手机充值成功后邀请好友 type=3
                                feed.setType(3);
                                feed.setTitle("每月最多领取150元话费");
                                feed.setContent("很嗨-汇聚全世界最优质最搞笑的内容");
                                String invitePageUrl = App.getInvitePageUrl();
                                String userLogo = App.getUserLogo();

                                feed.setShareUrl(invitePageUrl);
                                feed.setImageUrl(userLogo);
                                ClickUtil.goToShare(view.getContext(), feed);

                            }else{
                                Toast.makeText(ChargeActivity.this, baseModel.getMsgText(), Toast.LENGTH_SHORT).show();
                                /*Feed feed = new Feed();//手机充值成功后邀请好友 feed id为3 类别为0
                                feed.setId(4);
                                feed.setFeedCategory(0);
                                feed.setTitle("赶快加入吧我们一起每月最多领取150元话费");
                                feed.setContent("很嗨-汇聚全世界最优质最搞笑的内容");
                                String invitePageUrl = App.getInvitePageUrl();
                                String userLogo = App.getUserLogo();

                                feed.setShareUrl(invitePageUrl);
                                feed.setImageUrl(userLogo);
                                ClickUtil.goToShare(view, feed);*/

                            }

                        }

                        @Override
                        public void onFailure(Exception e) {
                            Toast.makeText(ChargeActivity.this, "对不起充值系统有点小问题", Toast.LENGTH_SHORT).show();
                        }
                    });
                }else{
                    Toast.makeText(ChargeActivity.this, "请您选择充值面额", Toast.LENGTH_SHORT).show();
                }

            }
        });


    }


    private void initView(Bundle savedInstanceState){
        mFragmentNavigator = new FragmentNavigator(getSupportFragmentManager(), new OtherFragmentAdapter(), R.id.fragment_other_phone_container);
        mFragmentNavigator.onCreate(savedInstanceState);
        setDefaultFrag();
    }


    /*设置默认Fragment*/
    private void setDefaultFrag() {
        mFragmentNavigator.showFragment(Constants.OTHER_PHONE);

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
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public void acceptChargClick(int id){
        itemId = id;
        mDoCharg.setBackgroundResource(R.color.app_sys_color);
    }


}




