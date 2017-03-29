package com.whenhi.hi.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.whenhi.hi.App;
import com.whenhi.hi.R;
import com.whenhi.hi.activity.LoginActivity;
import com.whenhi.hi.activity.OtherActivity;
import com.whenhi.hi.activity.PicActivity;
import com.whenhi.hi.activity.ShareActivity;
import com.whenhi.hi.activity.TextActivity;
import com.whenhi.hi.activity.VideoActivity;
import com.whenhi.hi.activity.WebViewActivity;
import com.whenhi.hi.adapter.FeedListAdapter;
import com.whenhi.hi.model.BaseModel;
import com.whenhi.hi.model.Feed;
import com.whenhi.hi.model.LoginModel;
import com.whenhi.hi.network.HttpAPI;
import com.whenhi.hi.receiver.NoticeTransfer;

/**
 * Created by 王雷 on 2017/2/28.
 */

public class ClickUtil {

    public static void  click(Feed feed, View view){
        if(feed == null)
            return;
        boolean isLogin = App.isLogin();
        if(isLogin){
            reportClickInfo(feed);

        }

        if(feed.getFeedCategory() == 1){//视频
            goToVideo(view,feed);

        }else if(feed.getFeedCategory() == 2){//动图


        }else if(feed.getFeedCategory() == 3){//漫画
            goToPic(view,feed);

        }else if(feed.getFeedCategory() == 4){//段子
            goToText(view,feed);

        }else if(feed.getFeedCategory() == 5){//图片
            goToPic(view,feed);

        }else if(feed.getFeedCategory() == 6){//广告
            goToWeb(view,feed);

        }else if(feed.getFeedCategory() == 7){//分割线


        }else if(feed.getFeedCategory() == 8){//彩蛋

            goToOther(view,feed);
        }else if(feed.getFeedCategory() == 9){//资讯
            goToWeb(view,feed);

        }







    }

    public static void goToOther(View view, Feed feed){
        Intent intent = new Intent(view.getContext(), OtherActivity.class);

        Bundle bundle=new Bundle();
        //传递name参数为tinyphp
        bundle.putString("imageUrl", feed.getImageUrl());
        bundle.putString("contentText", feed.getContent());
        bundle.putInt("type",3);
        intent.putExtras(bundle);
        view.getContext().startActivity(intent);
        Context context = view.getContext();
        if (context instanceof Activity){
            Activity activity = (Activity)context;
            activity.overridePendingTransition(R.anim.activity_open,0);
        }
    }

    public static void reportClickInfo(final Feed feed){
        HttpAPI.updateClickInfo(feed, new HttpAPI.Callback<BaseModel>() {
            @Override
            public void onSuccess(BaseModel baseModel) {
                if(baseModel.getState() == 0){
                    String data = baseModel.getData();
                    if(!TextUtils.isEmpty(data)){
                        NoticeTransfer.refresh();
                        Toast.makeText(App.getContext(), data, Toast.LENGTH_SHORT).show();
                    }

                }else{

                }

            }

            @Override
            public void onFailure(Exception e) {

            }
        });
    }

    private static void goToVideo(View view, Feed feed){
        Intent intent = new Intent(view.getContext(), VideoActivity.class);
        intent.putExtra("Feed", feed);
        view.getContext().startActivity(intent);
    }

    private static void goToPic(View view, Feed feed){
        Intent intent = new Intent(view.getContext(), PicActivity.class);
        intent.putExtra("Feed", feed);
        view.getContext().startActivity(intent);
    }

    private static void goToWeb(View view, Feed feed){
        Intent intent = new Intent(view.getContext(), WebViewActivity.class);
        intent.putExtra("Feed", feed);
        view.getContext().startActivity(intent);
    }
    public static void goToLogin(View view){
        Intent intent = new Intent(view.getContext(), LoginActivity.class);
        view.getContext().startActivity(intent);

        Context context = view.getContext();
        if (context instanceof Activity){
            Activity activity = (Activity)context;
            activity.overridePendingTransition(R.anim.activity_open,0);
        }
    }

    private static void goToText(View view, Feed feed){
        Intent intent = new Intent(view.getContext(), TextActivity.class);
        intent.putExtra("Feed", feed);
        view.getContext().startActivity(intent);
    }

    public static void goToShare(View view, Feed feed){
        Intent intent = new Intent(view.getContext(), ShareActivity.class);
        intent.putExtra("Feed", feed);
        view.getContext().startActivity(intent);

        Context context = view.getContext();
        if (context instanceof Activity){
            Activity activity = (Activity)context;
            activity.overridePendingTransition(R.anim.activity_open,0);
        }
    }


    public static void addComment(Feed feed, int targetId, int targetType, String content, final Activity activity){

        boolean isLogin = App.isLogin();

        if(isLogin){
            if(content.equals("")){
                Toast.makeText(activity, "多少要写点东西哦", Toast.LENGTH_SHORT).show();
                return;
            }
            HttpAPI.addComment(feed.getId(),targetId,targetType,content,new HttpAPI.Callback<BaseModel>() {
                @Override
                public void onSuccess(BaseModel baseModel) {
                    if(baseModel.getState() == 0){
                        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
                        Toast.makeText(activity, "评论成功", Toast.LENGTH_SHORT).show();
                        NoticeTransfer.commentSuccess();
                    }else{
                        Toast.makeText(activity, baseModel.getMsgText(), Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onFailure(Exception e) {
                    Toast.makeText(activity, "服务器貌似出问题了", Toast.LENGTH_SHORT).show();
                }
            });
        }else{
            Intent intent = new Intent(activity, LoginActivity.class);
            activity.startActivity(intent);

            activity.overridePendingTransition(R.anim.activity_open,0);
        }


    }



    public static void toolbarClick(final Button loveBtn, final Button shareBtn, final Button favBtn, final Button commentBtn, final Context context, final View view, final Feed feed){

        if(feed == null)
            return;
        loveBtn.setOnClickListener(new Button.OnClickListener(){//创建监听
            public void onClick(View v) {

                if(App.isLogin()){
                    if(feed.getLikeState() == 1){
                        HttpAPI.disLoveFeed(feed.getId(),new HttpAPI.Callback<BaseModel>() {
                            @Override
                            public void onSuccess(BaseModel baseModel) {
                                if(baseModel.getState() == 0){
                                    Drawable drawable = context.getResources().getDrawable(R.mipmap.zan);
                                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                    loveBtn.setCompoundDrawables(drawable, null, null, null);
                                    feed.setLikeCount(feed.getLikeCount()-1);
                                    feed.setLikeState(0);
                                    loveBtn.setText(""+(feed.getLikeCount()));
                                }else {
                                    Toast.makeText(view.getContext(), baseModel.getMsgText(), Toast.LENGTH_SHORT).show();
                                }


                            }

                            @Override
                            public void onFailure(Exception e) {

                            }
                        });
                    }else{
                        HttpAPI.loveFeed(feed.getId(),new HttpAPI.Callback<BaseModel>() {
                            @Override
                            public void onSuccess(BaseModel baseModel) {
                                if(baseModel.getState() == 0){
                                    Drawable drawable = context.getResources().getDrawable(R.mipmap.zan_click);
                                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                    loveBtn.setCompoundDrawables(drawable, null, null, null);
                                    feed.setLikeCount(feed.getLikeCount()+1);
                                    feed.setLikeState(1);
                                    loveBtn.setText(""+(feed.getLikeCount()));
                                }else {
                                    Toast.makeText(view.getContext(), baseModel.getMsgText(), Toast.LENGTH_SHORT).show();
                                }


                            }

                            @Override
                            public void onFailure(Exception e) {

                            }
                        });
                    }


                }else{
                    goToLogin(view);
                }

            }

        });

        shareBtn.setOnClickListener(new Button.OnClickListener(){//创建监听
            public void onClick(View v) {
                goToShare(view,feed);
            }

        });

        favBtn.setOnClickListener(new Button.OnClickListener(){//创建监听
            public void onClick(View v) {
                if(App.isLogin()){
                    if(feed.getFavoriteState() == 1){
                        HttpAPI.disFavFeed(feed.getId(),feed.getFeedCategory(),new HttpAPI.Callback<BaseModel>() {
                            @Override
                            public void onSuccess(BaseModel baseModel) {

                                if(baseModel.getState() == 0){
                                    Drawable drawable = context.getResources().getDrawable(R.mipmap.shoucang);
                                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                    favBtn.setCompoundDrawables(drawable, null, null, null);
                                    feed.setFavoriteCount(feed.getFavoriteCount()-1);
                                    feed.setFavoriteState(0);
                                    favBtn.setText(""+(feed.getFavoriteCount()));
                                }else {
                                    Toast.makeText(view.getContext(), baseModel.getMsgText(), Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Exception e) {

                            }
                        });
                    }else{
                        HttpAPI.favFeed(feed.getId(),feed.getFeedCategory(),new HttpAPI.Callback<BaseModel>() {
                            @Override
                            public void onSuccess(BaseModel baseModel) {
                                if(baseModel.getState() == 0){
                                    Drawable drawable = context.getResources().getDrawable(R.mipmap.shoucang_click);
                                    drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight());
                                    favBtn.setCompoundDrawables(drawable, null, null, null);
                                    feed.setFavoriteCount(feed.getFavoriteCount()+1);
                                    feed.setFavoriteState(1);
                                    favBtn.setText(""+(feed.getFavoriteCount()));
                                }else {
                                    Toast.makeText(view.getContext(), baseModel.getMsgText(), Toast.LENGTH_SHORT).show();
                                }

                            }

                            @Override
                            public void onFailure(Exception e) {

                            }
                        });
                    }

                }else{
                    goToLogin(view);
                }

            }

        });

        commentBtn.setOnClickListener(new Button.OnClickListener(){//创建监听
            public void onClick(View v) {
                click(feed,view);
            }

        });


    }

}
