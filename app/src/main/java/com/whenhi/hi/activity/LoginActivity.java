package com.whenhi.hi.activity;

import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.BaseAdapter;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.GridHolder;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.OnItemClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.whenhi.hi.App;
import com.whenhi.hi.R;
import com.whenhi.hi.model.BaseModel;
import com.whenhi.hi.model.LoginModel;
import com.whenhi.hi.network.HttpAPI;
import com.whenhi.hi.platform.login.AuthResult;
import com.whenhi.hi.platform.login.ILoginListener;
import com.whenhi.hi.platform.login.qq.QQLoginHandler;
import com.whenhi.hi.platform.login.wechat.WechatLoginHandler;
import com.whenhi.hi.platform.login.weibo.WeiboLoginHandler;
import com.whenhi.hi.platform.model.Login;
import com.whenhi.hi.receiver.NoticeTransfer;
import com.whenhi.hi.util.BindUtil;
import com.whenhi.hi.util.DateUtil;

/**
 * Created by 王雷 on 2017/1/5.
 */

public class LoginActivity extends BaseActivity {

    public static final String TAG = LoginActivity.class.getSimpleName();


    private ILifecycleListener mLifeListener;
    public static boolean dialogcancel = true;




    private static WechatLoginHandler mWechatHandler;

    public static WechatLoginHandler getWechatLoginHandler(){
        return mWechatHandler;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        login();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (mLifeListener != null) {
            mLifeListener.onActivityResult(requestCode, resultCode, data);
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mWechatHandler = null;
    }

    @Override
    public void finish() {
        super.finish();
        //关闭窗体动画显示
        this.overridePendingTransition(R.anim.activity_close,0);
    }


    private void setLifecycleListener(ILifecycleListener lifeListener) {
        mLifeListener = lifeListener;
    }

    private void loginWeibo() {
        final WeiboLoginHandler handler = new WeiboLoginHandler();
        handler.setLogEnable(true);
        handler.setRequestUserInfo(true);
        this.setLifecycleListener(new ILifecycleListener() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                handler.onActivityResult(requestCode, resultCode, data);
                setLifecycleListener(null);
            }
        });
        handler.login(this, new LoginListener());
    }

    private void loginWechat() {

        mWechatHandler = new WechatLoginHandler(this);
        mWechatHandler.setLogEnable(true);
        mWechatHandler.setRequestUserInfo(true);
        mWechatHandler.login(new LoginListener());
    }

    private void loginQQ() {
        final QQLoginHandler handler = new QQLoginHandler(this);
        handler.setLogEnable(true);
        handler.setRequestUserInfo(true);
        this.setLifecycleListener(new ILifecycleListener() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                handler.onActivityResult(requestCode, resultCode, data);
                setLifecycleListener(null);
            }
        });
        handler.login(this, new LoginListener());
    }

    private void onLoginFinish(int code, Object data) {

        Login loginResult = (Login) data;
        switch (code) {
            case ILoginListener.CODE_SUCCESS:
                //用户信息的存储 同时向服务器发送
                HttpAPI.updateUserInfo(loginResult, new HttpAPI.Callback<LoginModel>() {
                    @Override
                    public void onSuccess(LoginModel loginModel) {
                        if (loginModel.getState() == 0) {
                            //要将用户变成已登录状态
                            App.loginSucees(loginModel);
                            NoticeTransfer.login(true);
                        }
                    }

                    @Override
                    public void onFailure(Exception e) {

                    }
                });


                break;

            case ILoginListener.CODE_FAILED:
                break;

            case ILoginListener.CODE_LOGIN_ING:
                break;

            case ILoginListener.CODE_AUTH_SUCCESS:
                int typeAu = loginResult.getType();
                AuthResult result = loginResult.getAuthResult();
                if(typeAu == 1){//qq登录
                    // 用户授权信息
                    Log.d(TAG, "QQ login AuthResult"+result.toString());

                }else if(typeAu == 2){//wx登录
                    // 用户授权信息
                    Log.d(TAG, "WX login AuthResult"+result.toString());

                }else if(typeAu == 3){//wb登录
                    // 用户授权信息
                    Log.d(TAG, "WB login AuthResult"+result.toString());

                }
                break;

            case ILoginListener.CODE_AUTH_EXCEPTION:
                break;

            case ILoginListener.CODE_CANCEL_AUTH:
                break;

            case ILoginListener.CODE_AUTH_FAILED:
                Log.d(TAG, "QQ login data"+data);
                break;
        }


        finish();
    }




    private class LoginListener implements ILoginListener {
        @Override
        public void loginStatus(final int code, final Object data) {
            // running in main thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onLoginFinish(code, data);
                }
            });
        }
    }

    public interface ILifecycleListener {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }


    private void login(){
        Holder holder = new ViewHolder(R.layout.layout_dialog_login);
        final DialogPlus dialog = DialogPlus.newDialog(this)
                .setContentHolder(holder)
                .setCancelable(dialogcancel)
                .setGravity(Gravity.BOTTOM)
                .setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogPlus dialog) {
                        dialogcancel = true;
                        finish();
                    }
                })
                .setExpanded(false)//设置扩展模式可控制dialog的高度
                .setOnClickListener(new OnClickListener() {
                    @Override public void onClick(DialogPlus dialog, View view) {

                        switch (view.getId()) {
                            case R.id.login_image_wechat:
                                loginWechat();
                                break;
                            case R.id.login_image_weibo:
                                loginWeibo();
                                break;
                            case R.id.login_image_qq:
                                loginQQ();
                                break;
                        }
                        //dialog.dismiss();
                    }

                })
                .create();
        dialog.show();
    }
}
