package com.whenhi.hi.activity;


import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ashokvarma.bottomnavigation.BadgeItem;
import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.aspsine.fragmentnavigator.FragmentNavigator;
import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.sina.weibo.sdk.WbSdk;
import com.sina.weibo.sdk.auth.AuthInfo;
import com.whenhi.hi.App;
import com.whenhi.hi.Constants;
import com.whenhi.hi.R;
import com.whenhi.hi.fragment.MainFragmentAdapter;
import com.whenhi.hi.model.BaseModel;
import com.whenhi.hi.model.TicketModel;
import com.whenhi.hi.model.UpdateData;
import com.whenhi.hi.model.UpdateModel;
import com.whenhi.hi.network.HttpAPI;
import com.whenhi.hi.platform.PlatformConfig;
import com.whenhi.hi.receiver.NoticeTransfer;
import com.whenhi.hi.util.BindUtil;
import com.whenhi.hi.util.DateUtil;
import com.whenhi.hi.util.MarketUtil;

import cn.jpush.android.api.JPushInterface;


public class MainActivity extends BaseActivity implements BottomNavigationBar.OnTabSelectedListener {

    private static String TAG = MainActivity.class.getSimpleName();
    private  BottomNavigationBar mBottomNavigationBar;//底部导航栏
    private BadgeItem mBadgeItem;

    private BottomNavigationItem mRcdItem;//推荐
    private BottomNavigationItem mEpeItem;//探索
    private BottomNavigationItem mMseItem;//消息
    private BottomNavigationItem mMreItem;//更多
    private int mMessageCount = 0;


    private FragmentNavigator mFragmentNavigator;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        registerBoradcastReceiver();

        setContentView(R.layout.activity_main);
        initView(savedInstanceState);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);
        //透明状态栏
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        //getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);

        initBottomNavBar();
        initJpush();
        deviceInit();
        todayFirstDo();

        App.setStatusMainActivity(true);

        initWeibo();



    }
    private void initWeibo(){
        PlatformConfig config = PlatformConfig.getInstance();
        AuthInfo weiboAuth = new AuthInfo(MainActivity.this, config.getWeiboKey(), config.getWeiboCallback(),
                config.getWeiboScope());
        WbSdk.install(MainActivity.this,weiboAuth);
    }

    private  void todayFirstDo(){
        if(App.isTodayFirst()){
            checkTicket();

        }

        updateApp();

    }

    private void checkTicket(){
        HttpAPI.checkTicket(new HttpAPI.Callback<TicketModel>() {
            @Override
            public void onSuccess(TicketModel ticketModel) {
                if(ticketModel.getState() == 0){

                    if(ticketModel.getData().getUserId() == -1){
                        App.loginout();
                        NoticeTransfer.logout(true);
                    }
                }else{

                }

            }

            @Override
            public void onFailure(Exception e) {

            }
        });


    }
    // 初始化 JPush。如果已经初始化，但没有登录成功，则执行重新登录。
    private void initJpush(){
        JPushInterface.init(getApplicationContext());
        if(TextUtils.isEmpty(App.getJPushRegisterID())){
            BindUtil.bindJPush();
        }else{
            if(!App.isLogin()){
                if(TextUtils.isEmpty(App.getLastBindJPushTime())){
                    BindUtil.bindJPush();
                }else{
                    String nowTime = DateUtil.getNowTimeString(System.currentTimeMillis());
                    String lastTime = App.getLastBindJPushTime();
                    int count = DateUtil.stringDaysBetween(lastTime,nowTime);

                    Log.d(TAG,"是否大于七天"+count);
                    if(count > 7){
                        BindUtil.bindJPush();
                    }
                }


            }
        }


    }
    private void deviceInit(){
        if(!App.isDeviceInit()){
            HttpAPI.updateDeviceInit(new HttpAPI.Callback<BaseModel>() {
                @Override
                public void onSuccess(BaseModel baseModel) {
                    if(baseModel.getState() == 0){
                        //要将用户变成已登录状态
                        Log.d(TAG,"device init");
                        App.deviceInit();
                    }else{

                    }

                }

                @Override
                public void onFailure(Exception e) {

                }
            });
        }

    }

    private void updateApp(){
        HttpAPI.updateApp(new HttpAPI.Callback<UpdateModel>() {
            @Override
            public void onSuccess(UpdateModel updateModel) {
                if(updateModel.getState() == 0){
                    UpdateData updateData = updateModel.getData();
                    boolean isForce = false;
                    String updateText = updateData.getVersionDesc();
                    String url = updateData.getDownloadUrl();
                    int type = updateData.getUpgradeType();
                    if (type == 2){
                        isForce = true;
                        updateDialog(isForce,updateText,url);
                    }else if(type == 1){
                        updateDialog(isForce,updateText,url);
                    }
                }else{

                }

            }

            @Override
            public void onFailure(Exception e) {

            }
        });


    }



    private void updateDialog(final boolean isForce, String updateText, final String url){

        Holder holder = new ViewHolder(R.layout.layout_dialog_update);

        final DialogPlus dialog = DialogPlus.newDialog(this)
                .setContentHolder(holder)
                .setCancelable(false)
                .setGravity(Gravity.CENTER)
                .setMargin(80, 0, 80, 0)
                .setPadding(0, 0, 0, 0)
                .setExpanded(false)//设置扩展模式可控制dialog的高度
                .setContentBackgroundResource(R.drawable.shape_caidan)

                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        switch (view.getId()) {
                            case R.id.update_ok:
                                Intent intent = new Intent();
                                intent.setAction("android.intent.action.VIEW");
                                Uri content_url = Uri.parse(url);
                                intent.setData(content_url);
                                startActivity(intent);
                                if(!isForce){
                                    dialog.dismiss();
                                }

                                break;
                            case R.id.update_close:
                                dialog.dismiss();
                                break;
                        }

                    }
                })
                .create();
        dialog.show();

        //内容的更新要在dialog显示之后进行
        TextView updateContent =  (TextView) holder.getInflatedView().findViewById(R.id.update_text);
        updateContent.setText(updateText);

        Button button = (Button)holder.getInflatedView().findViewById(R.id.update_close);
        if(isForce){
            button.setVisibility(View.GONE);
        }
    }




    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {

            finish();

            return true;
        }

        return super.onKeyDown(keyCode, event);
    }




    private void initView(Bundle savedInstanceState){
        mFragmentNavigator = new FragmentNavigator(getSupportFragmentManager(), new MainFragmentAdapter(this), R.id.fragment_content_container);
        mFragmentNavigator.onCreate(savedInstanceState);


    }



    /*初始化底部导航栏*/
    private void initBottomNavBar() {
        mBottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        mBottomNavigationBar.setTabSelectedListener(this);
        mBottomNavigationBar.setAutoHideEnabled(false);//自动隐藏

        //BottomNavigationBar.MODE_SHIFTING;
        //BottomNavigationBar.MODE_FIXED;
        //BottomNavigationBar.MODE_DEFAULT;
        mBottomNavigationBar.setMode(BottomNavigationBar.MODE_FIXED);

        // BottomNavigationBar.BACKGROUND_STYLE_DEFAULT;
        // BottomNavigationBar.BACKGROUND_STYLE_RIPPLE
        // BottomNavigationBar.BACKGROUND_STYLE_STATIC
        mBottomNavigationBar.setBackgroundStyle(BottomNavigationBar.BACKGROUND_STYLE_STATIC);


        mBottomNavigationBar.setBarBackgroundColor(R.color.bg_bottom);//背景色
        mBottomNavigationBar.setInActiveColor(R.color.bottom_bar_inactive);//未选中
        mBottomNavigationBar.setActiveColor(R.color.bottom_bar_active);//选中


        mBadgeItem = new BadgeItem().setBackgroundColorResource(R.color.msg_badge).setText("" + mMessageCount).setHideOnSelect(true).hide();//角标

        mRcdItem = new BottomNavigationItem(R.mipmap.home_click, "首页").setInactiveIcon(ContextCompat.getDrawable(this,R.mipmap.home));//非选中的图片;
        mEpeItem = new BottomNavigationItem(R.mipmap.tansuo_click, "探索").setInactiveIcon(ContextCompat.getDrawable(this,R.mipmap.tansuo));
        mMseItem = new BottomNavigationItem(R.mipmap.xiaoxi_click, "消息").setInactiveIcon(ContextCompat.getDrawable(this,R.mipmap.xiaoxi));
        mMseItem.setBadgeItem(mBadgeItem);
        mMreItem = new BottomNavigationItem(R.mipmap.wode_click, "我的").setInactiveIcon(ContextCompat.getDrawable(this,R.mipmap.wode));


        mBottomNavigationBar.addItem(mRcdItem).addItem(mEpeItem).addItem(mMseItem).addItem(mMreItem);
        mBottomNavigationBar.initialise();
        mBottomNavigationBar.setTabSelectedListener(this);
        setDefaultFrag();//显示默认的Frag

    }


    /*设置默认Fragment*/
    private void setDefaultFrag() {

        mFragmentNavigator.showFragment(Constants.NAV_BOTTOM_BAR_HOME);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTabSelected(int position) {
        showFragment(position);
    }

    private void showFragment(int position){
        if(mMessageCount == 0){
            mBadgeItem.hide();
        }

        switch (position) {
            case 0:

                mFragmentNavigator.showFragment(Constants.NAV_BOTTOM_BAR_HOME);
                //getSupportActionBar().setTitle("Home");
                break;
            case 1:
                mFragmentNavigator.showFragment(Constants.NAV_BOTTOM_BAR_EXPLORE);
                //getSupportActionBar().setTitle("Books");
                break;
            case 2:
                mFragmentNavigator.showFragment(Constants.NAV_BOTTOM_BAR_MESSAGE);
                mMessageCount = 0;


                //getSupportActionBar().setTitle("Music");
                break;
            case 3:
                mFragmentNavigator.showFragment(Constants.NAV_BOTTOM_BAR_MORE);

                //getSupportActionBar().setTitle("More");
                break;
            default:
                break;
        }

    }

    @Override
    public void onTabUnselected(int position) {
    }

    @Override
    public void onTabReselected(int position) {
        showFragment(position);
    }

    private void appcomment(){

        Holder holder = new ViewHolder(R.layout.layout_dialog_update);

        final DialogPlus dialog = DialogPlus.newDialog(this)
                .setContentHolder(holder)
                .setHeader(R.layout.layout_dialog_header)
                .setFooter(R.layout.layout_dialog_footer)
                .setCancelable(true)
                .setGravity(Gravity.BOTTOM)

                .setExpanded(false)//设置扩展模式可控制dialog的高度

                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        switch (view.getId()) {
                            case R.id.dialog_confirm_button:
                                MarketUtil.launchAppDetail(MainActivity.this, App.getAppPkg(),App.getChannel("UMENG_CHANNEL"));
                                //MarketUtil.launchAppDetail(App.getAppPkg(),"oppo");
                                dialog.dismiss();
                                break;
                            case R.id.dialog_close_button:
                                dialog.dismiss();
                                break;
                        }

                    }
                })
                .create();
        dialog.show();

        //内容的更新要在dialog显示之后进行
        TextView updateContent =  (TextView) holder.getInflatedView().findViewById(R.id.dialog_update_content);
        updateContent.setText("去商店给我们一个好评吧，亲！");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(mBroadcastReceiver);
        App.setStatusMainActivity(false);
    }


    public static final String MESSAGE_RECEIVED_ACTION = "com.whenhi.hi.activity.MainAvtivity";
    public static final String KEY_MESSAGE = "message";
    public static final String KEY_EXTRAS = "extras";
    private BroadcastReceiver mBroadcastReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context context, Intent intent) {
            if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                String messge = intent.getStringExtra(KEY_MESSAGE);
                String count = intent.getStringExtra(KEY_EXTRAS);
                if(messge.equals("newmsg")){
                    mMessageCount = mMessageCount + Integer.parseInt(count);
                    mBadgeItem.show();
                    mBadgeItem.setText(""+mMessageCount);
                    NoticeTransfer.refreshMeesage();
                }

            }
        }

    };

    public void registerBoradcastReceiver(){
        IntentFilter myIntentFilter = new IntentFilter();
        myIntentFilter.addAction("com.whenhi.hi.activity.MainAvtivity");
        //注册广播
        registerReceiver(mBroadcastReceiver, myIntentFilter);
    }



}
