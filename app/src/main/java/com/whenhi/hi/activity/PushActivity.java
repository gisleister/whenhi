package com.whenhi.hi.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.net.Uri;
import android.os.PowerManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.whenhi.hi.R;
import com.whenhi.hi.model.Notification;
import com.whenhi.hi.model.NotificationData;

public class PushActivity extends BaseActivity {

    private static final String TAG =PushActivity.class.getSimpleName();

    private NotificationData mNotificationData;
    private PowerManager.WakeLock mWakeLock;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED|WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
        /*KeyguardManager keyguardManager = (KeyguardManager) getSystemService(KEYGUARD_SERVICE);
        KeyguardManager.KeyguardLock keyguardLock = keyguardManager.newKeyguardLock("");
        keyguardLock.disableKeyguard();*/

        setContentView(R.layout.activity_push);
        getWindow().setFormat(PixelFormat.TRANSLUCENT);

       // TextView messgae = (TextView)findViewById(R.id.push_content);
        Intent intent = getIntent();
        mNotificationData = (NotificationData) intent.getSerializableExtra("NotificationData");
        //messgae.setText(mNotification.getDescription());
        showPush(mNotificationData.getData().getDescription());
        acquireWakeLock();
        releaseWakeLock();
    }


    public void acquireWakeLock() {
        if (mWakeLock == null) {
            PowerManager pm = (PowerManager)getSystemService(Context.POWER_SERVICE);
            mWakeLock = pm.newWakeLock(PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "ZHENGYI.WZY");
            if (mWakeLock != null) {
                Log.d(TAG,"屏幕灯光打开");
                mWakeLock.acquire();
            }
        }
    }

    public void releaseWakeLock() {
        if (mWakeLock != null) {
            mWakeLock.release();
            Log.d(TAG,"屏幕灯光关闭");
        }
    }

    private void showPush(String text){
        Holder holder = new ViewHolder(R.layout.layout_dialog_notification);

        final DialogPlus dialog = DialogPlus.newDialog(PushActivity.this)
                .setContentHolder(holder)
                .setHeader(R.layout.layout_dialog_header)
                .setFooter(R.layout.layout_dialog_footer)
                .setCancelable(false)
                .setGravity(Gravity.BOTTOM)
                .setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogPlus dialog) {
                        finish();
                    }
                })

                .setExpanded(false)//设置扩展模式可控制dialog的高度

                .setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(DialogPlus dialog, View view) {
                        switch (view.getId()) {
                            case R.id.dialog_confirm_button:

                                if(mNotificationData.getSubtype().equals("link")){
                                    Intent intent = new Intent();
                                    intent.setAction("android.intent.action.VIEW");
                                    Uri content_url = Uri.parse(mNotificationData.getData().getLink());
                                    intent.setData(content_url);
                                    startActivity(intent);
                                }else if( mNotificationData.getSubtype().equals("msg")){
                                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                                    view.getContext().startActivity(intent);
                                }else if( mNotificationData.getSubtype().equals("newmsg")){
                                    Intent intent = new Intent(view.getContext(), MainActivity.class);
                                    view.getContext().startActivity(intent);
                                }
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
        updateContent.setText(text);

    }


}
