package com.whenhi.hi.util;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.whenhi.hi.R;
import com.whenhi.hi.activity.OtherActivity;
import com.whenhi.hi.model.BaseModel;
import com.whenhi.hi.network.HttpAPI;

/**
 * Created by 王雷 on 2017/3/22.
 */

public class PermissionUtil {

    Context mContext;
    View rootView;

    public PermissionUtil(Context context, View rootView) {
        this.mContext = context;
        this.rootView = rootView;
    }

    private void check(String permission) {
        if (ActivityCompat.checkSelfPermission(mContext, /*Manifest.permission.READ_PHONE_STATE*/permission)
                != PackageManager.PERMISSION_GRANTED) {
            requesetContanctsPermissions(permission);
        }
    }

    private void requesetContanctsPermissions(String permission) {
        if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) mContext,permission)) {
            final Holder holder = new ViewHolder(R.layout.layout_dialog_code);

            final DialogPlus dialog = DialogPlus.newDialog(mContext)
                    .setContentHolder(holder)
                    .setCancelable(false)
                    .setGravity(Gravity.CENTER)
                    .setOnDismissListener(new OnDismissListener() {
                        @Override
                        public void onDismiss(DialogPlus dialog) {

                        }
                    })
                    .setExpanded(false)//设置扩展模式可控制dialog的高度

                    .setOnClickListener(new OnClickListener() {
                        @Override
                        public void onClick(final DialogPlus dialog, View view) {
                            switch (view.getId()) {
                                case R.id.code_close:
                                    dialog.dismiss();
                                    break;
                                case R.id.code_ok:
                                   // ActivityCompat.requestPermissions(mContext,,);
                                    break;
                            }

                        }
                    })
                    .create();
            dialog.show();

        } else {
            //无需向用户界面提示 , 直接请求权限
//                    ActivityCompat.requestPermissions(mContext,/*请求权的权限 String[] permissions*/,/*请求码 int requestCode*/);
        }
    }
}