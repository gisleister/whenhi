package com.whenhi.hi.dialog;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.whenhi.hi.R;
import com.whenhi.hi.util.MarketUtil;

/**
 * Created by 王雷 on 2016/12/29.
 */

public class Dialog {





    /**
     * 升级对话框
     * @param context
     * @param isForce 是否强制升级
     * @param updateText
     * @param url
     */
    public static void showUpdateDialog(final Context context, final boolean isForce, String updateText, final String url) {


    }


    public static void showCommentDialog(final Context context, String updateText) {




    }


    public static void showNotificationDialog(final Context context, String updateText) {

        Holder holder = new ViewHolder(R.layout.layout_dialog_notification);

        final DialogPlus dialog = DialogPlus.newDialog(context)
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
                                MarketUtil.launchAppDetail(context,"com.mamashai.rainbow_android","taobao");
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
        updateContent.setText(updateText);


    }

}
