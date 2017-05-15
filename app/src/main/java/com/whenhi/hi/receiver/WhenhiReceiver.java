package com.whenhi.hi.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.util.Log;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.whenhi.hi.App;
import com.whenhi.hi.R;
import com.whenhi.hi.activity.MainActivity;
import com.whenhi.hi.activity.PicActivity;
import com.whenhi.hi.activity.PushActivity;
import com.whenhi.hi.activity.TextActivity;
import com.whenhi.hi.activity.VideoActivity;
import com.whenhi.hi.activity.WebViewActivity;
import com.whenhi.hi.model.NotificationData;
import com.whenhi.hi.model.NotificationModel;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Iterator;
import java.util.List;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by 王雷 on 2017/1/13.
 */

public class WhenhiReceiver extends BroadcastReceiver {
    private static final String TAG = WhenhiReceiver.class.getSimpleName();
    private int notificationId = 1;


    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        Log.d(TAG, "[WhenhiReceiver] onReceive - " + intent.getAction() + ", extras: " + printBundle(bundle));

        if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
            String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            Log.d(TAG, "[WhenhiReceiver] 接收Registration Id : " + regId);
            //send the Registration Id to your server...

        } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[WhenhiReceiver] 接收到推送下来的自定义消息: " + bundle.getString(JPushInterface.EXTRA_MESSAGE));
            receivingNotification(context,bundle);

        } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
            Log.d(TAG, "[WhenhiReceiver] 接收到推送下来的通知");
            int notifactionId = bundle.getInt(JPushInterface.EXTRA_NOTIFICATION_ID);
            Log.d(TAG, "[WhenhiReceiver] 接收到推送下来的通知的ID: " + notifactionId);


        } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
            Log.d(TAG, "[WhenhiReceiver] 用户点击打开了通知");

            //打开自定义的Activity
            Intent i = new Intent(context, MainActivity.class);
            i.putExtras(bundle);
            //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
            context.startActivity(i);

        } else if (JPushInterface.ACTION_RICHPUSH_CALLBACK.equals(intent.getAction())) {
            Log.d(TAG, "[WhenhiReceiver] 用户收到到RICH PUSH CALLBACK: " + bundle.getString(JPushInterface.EXTRA_EXTRA));
            //在这里根据 JPushInterface.EXTRA_EXTRA 的内容处理代码，比如打开新的Activity， 打开一个网页等..

        } else if(JPushInterface.ACTION_CONNECTION_CHANGE.equals(intent.getAction())) {
            boolean connected = intent.getBooleanExtra(JPushInterface.EXTRA_CONNECTION_CHANGE, false);
            Log.w(TAG, "[WhenhiReceiver]" + intent.getAction() +" connected state change to "+connected);
        } else {
            Log.d(TAG, "[WhenhiReceiver] Unhandled intent - " + intent.getAction());
        }
    }

    // 打印所有的 intent extra 数据
    private static String printBundle(Bundle bundle) {
        StringBuilder sb = new StringBuilder();
        for (String key : bundle.keySet()) {
            if (key.equals(JPushInterface.EXTRA_NOTIFICATION_ID)) {
                sb.append("\nkey:" + key + ", value:" + bundle.getInt(key));
            }else if(key.equals(JPushInterface.EXTRA_CONNECTION_CHANGE)){
                sb.append("\nkey:" + key + ", value:" + bundle.getBoolean(key));
            } else if (key.equals(JPushInterface.EXTRA_EXTRA)) {
                if (TextUtils.isEmpty(bundle.getString(JPushInterface.EXTRA_EXTRA))) {
                    Log.i(TAG, "This message has no Extra data");
                    continue;
                }

                try {
                    JSONObject json = new JSONObject(bundle.getString(JPushInterface.EXTRA_EXTRA));
                    Iterator<String> it =  json.keys();

                    while (it.hasNext()) {
                        String myKey = it.next().toString();
                        sb.append("\nkey:" + key + ", value: [" +
                                myKey + " - " +json.optString(myKey) + "]");
                    }
                } catch (JSONException e) {
                    Log.e(TAG, "Get message extra JSON error!");
                }

            } else {
                sb.append("\nkey:" + key + ", value:" + bundle.getString(key));
            }
        }
        return sb.toString();
    }



    private void receivingNotification(Context context, Bundle bundle) {




        String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);




        // 使用notification

        Gson gson = new Gson();
        NotificationModel notificationModel = gson.fromJson(message, com.whenhi.hi.model.NotificationModel.class);
        int launchApp = notificationModel.getLaunchApp();
        if(launchApp == 1){//客户端做一次校验确定信息是发给该平台的
            int uid = notificationModel.getUid();
            if (uid == 0){//假如uid为0说明是安卓所有客户端接受消息，否则要根据uid进行一次校验确认该消息是发给自己的
                messageNext(notificationModel,context);
            }else if (App.getUserId().equals(""+uid+"")){
                messageNext(notificationModel,context);
            }
        }else if(launchApp == 0){
            int uid = notificationModel.getUid();
            if (uid == 0){//假如uid为0说明是安卓所有客户端接受消息，否则要根据uid进行一次校验确认该消息是发给自己的
                notificationBar(notificationModel,context);
            }else if (App.getUserId().equals(""+uid+"")){
                notificationBar(notificationModel,context);
            }
        }


    }


    private void messageNext(NotificationModel notificationModel,Context context){



        List<NotificationData> notificationDatas = notificationModel.getData();
        for (int i = 0; i < notificationDatas.size(); i++){
            NotificationData notificationData = notificationDatas.get(i);
            String type = notificationData.getType();
            com.whenhi.hi.model.Notification notification = notificationData.getData();
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            boolean isScreenOn = pm.isScreenOn();
            if (type.equals("show")){
                String subtype = notificationData.getSubtype();
                if(subtype.equals("link")) {
                    Intent intent = new Intent(context, PushActivity.class);
                    intent.putExtra("NotificationData", notificationData);
                    //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);
                }else if(subtype.equals("msg")){
                    Intent intent = new Intent(context, PushActivity.class);
                    intent.putExtra("NotificationData", notificationData);
                    //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    context.startActivity(intent);


                }else if(subtype.equals("newmsg")){

                    if(isScreenOn){


                    }else{
                        Intent intent = new Intent(context, PushActivity.class);
                        intent.putExtra("NotificationData", notificationData);
                        //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                        context.startActivity(intent);
                    }

                    Intent intent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
                    intent.putExtra(MainActivity.KEY_MESSAGE, "newmsg");
                    intent.putExtra(MainActivity.KEY_EXTRAS, ""+notificationData.getData().getCount());
                    context.sendBroadcast(intent);
                }



                /*Intent intent = new Intent(context, PushActivity.class);
                intent.putExtra("NotificationData", notificationData);
                //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                context.startActivity(intent);*/

               /* if(isScreenOn){
                    Intent intent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
                    intent.putExtra(MainActivity.KEY_MESSAGE, "1");
                    context.sendBroadcast(intent);

                }else{
                    Intent intent = new Intent(context, PushActivity.class);
                    intent.putExtra("NotificationData", notificationData);
                    //i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    context.startActivity(intent);
                }*/
            }else if(type.equals("cmd")){


            }
        }
    }


    private void notificationBar(NotificationModel notificationModel, final Context context){
        final NotificationManager manager = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        // 使用广播或者通知进行内容的显示
        final NotificationCompat.Builder builder = new NotificationCompat.Builder(
                context);

        Bitmap icon = BitmapFactory.decodeResource(context.getResources(),R.mipmap.logo);


        List<NotificationData> notificationDatas = notificationModel.getData();
        for (int i = 0; i < notificationDatas.size(); i++) {
            NotificationData notificationData = notificationDatas.get(i);
            final com.whenhi.hi.model.Notification notification = notificationData.getData();

            String type = notificationData.getType();
            if (type.equals("show")){
                String subtype = notificationData.getSubtype();
                if(subtype.equals("newmsg")){
                    builder.setSmallIcon(R.mipmap.logo)
                            .setLargeIcon(icon)
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setTicker("您有"+notification.getCount()+"条新消息")
                            .setContentTitle("您有"+notification.getCount()+"条新消息");
                    //.setContentText(notification.getDescription());

                    Intent intent1 = new Intent(context,MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent1, 0);
                    builder.setContentIntent(pendingIntent);
                    manager.notify(notificationId,builder.build());
                    notificationId++;


                    Intent intent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
                    intent.putExtra(MainActivity.KEY_MESSAGE, "newmsg");
                    intent.putExtra(MainActivity.KEY_EXTRAS, ""+notificationData.getData().getCount());
                    context.sendBroadcast(intent);
                }else if(subtype.equals("link")){
                    builder.setSmallIcon(R.mipmap.logo)
                            .setLargeIcon(icon)
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setTicker(notification.getTitle())
                            .setContentTitle(notification.getTitle())
                            .setContentText(notification.getDescription());

                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri content_url = Uri.parse(notification.getLink());
                    intent.setData(content_url);



                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                    builder.setContentIntent(pendingIntent);

                    manager.notify(notificationId,builder.build());
                    notificationId++;
                }else if(subtype.equals("msg")) {
                    builder.setSmallIcon(R.mipmap.logo)
                            .setLargeIcon(icon)
                            .setDefaults(Notification.DEFAULT_SOUND)
                            .setPriority(Notification.PRIORITY_HIGH)
                            .setTicker(notification.getTitle())
                            .setContentTitle(notification.getTitle())
                            .setContentText(notification.getDescription());

                    Intent intent = new Intent(context, MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
                    builder.setContentIntent(pendingIntent);

                    manager.notify(notificationId,builder.build());
                    notificationId++;
                }else if(subtype.equals("feed")) {



                    final RemoteViews remoteViews = new RemoteViews(context.getPackageName(),
                            R.layout.layout_notifycation);

                    remoteViews.setImageViewResource(R.id.item_notice_image,R.mipmap.logo);
                    builder.setSmallIcon(R.mipmap.logo);
                    builder.setTicker(notification.getTitle());
                    builder.setDefaults(Notification.DEFAULT_SOUND);
                    builder.setPriority(Notification.PRIORITY_HIGH);


                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            URL picUrl = null;
                            try {
                                picUrl = new URL(notification.getPicUrl());
                            } catch (MalformedURLException e) {
                                e.printStackTrace();
                            }
                            try {
                                Bitmap pngBM = BitmapFactory.decodeStream(picUrl.openStream());
                                remoteViews.setImageViewBitmap(R.id.item_notice_image,pngBM);
                                remoteViews.setTextViewText(R.id.item_notice_title, notification.getTitle());
                                remoteViews.setTextViewText(R.id.item_notice_des,notification.getDescription());


                                builder.setContent(remoteViews);



                                Intent intent = new Intent(context,MainActivity.class);

                                if(notification.getFeedCategory() == 1){//视频
                                    intent = new Intent(context, VideoActivity.class);

                                }else if(notification.getFeedCategory() == 4){//段子
                                    intent = new Intent(context, TextActivity.class);
                                }else if(notification.getFeedCategory() == 5){//图片
                                    intent = new Intent(context, PicActivity.class);
                                }else if(notification.getFeedCategory() == 6){//广告
                                    intent = new Intent(context, WebViewActivity.class);
                                }else if(notification.getFeedCategory() == 9){//资讯
                                    intent = new Intent(context, WebViewActivity.class);
                                }

                                intent.putExtra("isPush", true);
                                intent.putExtra("feedId",notification.getFeedId());
                                intent.putExtra("feedCategory",notification.getFeedCategory());

                                PendingIntent pIntent = PendingIntent.getActivity(context,1,intent,0);
                                //设置点击大图后跳转
                                builder.setContentIntent(pIntent);
                                if (Build.VERSION.SDK_INT > Build.VERSION_CODES.JELLY_BEAN) {
                                    builder.setCustomBigContentView(remoteViews);
                                }

                                manager.notify(notificationId,builder.build());
                                notificationId++;
                            } catch (IOException e) {
                                e.printStackTrace();
                            }

                        }
                    }).start();




                }
            }else if(type.equals("cmd")){
                String subtype = notificationData.getSubtype();
                if(subtype.equals("newmsg")){
                    Intent intent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
                    intent.putExtra(MainActivity.KEY_MESSAGE, "newmsg");
                    intent.putExtra(MainActivity.KEY_EXTRAS, ""+notificationData.getData().getCount());
                    context.sendBroadcast(intent);
                }else if(subtype.equals("link")){

                }else if(subtype.equals("msg")) {

                }

            }







        }

    }

}
