package com.whenhi.hi.util;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.whenhi.hi.App;
import com.whenhi.hi.R;
import com.whenhi.hi.activity.LoginActivity;
import com.whenhi.hi.activity.LuckpanActivity;
import com.whenhi.hi.activity.OtherActivity;
import com.whenhi.hi.activity.PicActivity;
import com.whenhi.hi.activity.ShareActivity;
import com.whenhi.hi.activity.TextActivity;
import com.whenhi.hi.activity.VideoActivity;
import com.whenhi.hi.activity.WebViewActivity;
import com.whenhi.hi.model.BaseModel;
import com.whenhi.hi.model.Feed;
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

        }else if(feed.getFeedCategory() == 10){//抽奖大转盘
            goToLuckpan(view,feed);
        }







    }

    public static void goToOther(View view, Feed feed){
        Intent intent = new Intent(view.getContext(), OtherActivity.class);

        Bundle bundle=new Bundle();
        //传递name参数为tinyphp
        bundle.putString("titleText", feed.getContent());
        bundle.putString("score", ""+feed.getScore());
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
                        if(feed.getFeedCategory() != 8){
                            Toast.makeText(App.getContext(), data, Toast.LENGTH_SHORT).show();
                        }

                    }

                }else{
                    Toast.makeText(App.getContext(), "请求失败", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(App.getContext(), "服务器异常", Toast.LENGTH_SHORT).show();
            }
        });
    }


    private static void goToLuckpan(View view, Feed feed){
        Intent intent = new Intent(view.getContext(), LuckpanActivity.class);
        intent.putExtra("Feed", feed);
        view.getContext().startActivity(intent);
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




    public static void toolbarClick(final ImageView loveImage, final ImageView favImage,final TextView loveText, final TextView favText,final LinearLayout favBtn, final LinearLayout loveBtn, final LinearLayout shareBtn, final LinearLayout commentBtn, final View view, final Feed feed){

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
                                    loveImage.setImageResource(R.mipmap.zan);

                                    feed.setLikeCount(feed.getLikeCount()-1);
                                    feed.setLikeState(0);

                                    if(feed.getLikeCount() == 0){
                                        loveText.setText("赞");
                                    }else{
                                        loveText.setText(""+feed.getLikeCount() + "赞");
                                    }


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
                                    loveImage.setImageResource(R.mipmap.zan_click);
                                    feed.setLikeCount(feed.getLikeCount()+1);
                                    feed.setLikeState(1);
                                    if(feed.getLikeCount() == 0){
                                        loveText.setText("赞");
                                    }else{
                                        loveText.setText(""+feed.getLikeCount() + "赞");
                                    }
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
                                    favImage.setImageResource(R.mipmap.shoucang);
                                    feed.setFavoriteCount(feed.getFavoriteCount()-1);
                                    feed.setFavoriteState(0);
                                    if(feed.getFavoriteCount() == 0){
                                        favText.setText(" · 收藏");
                                    }else{
                                        favText.setText(" · "+feed.getFavoriteCount() + "收藏");
                                    }
                                    NoticeTransfer.refresh();
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
                                    favImage.setImageResource(R.mipmap.shoucang_click);
                                    feed.setFavoriteCount(feed.getFavoriteCount()+1);
                                    feed.setFavoriteState(1);
                                    if(feed.getFavoriteCount() == 0){
                                        favText.setText(" · 收藏");
                                    }else{
                                        favText.setText(" · "+feed.getFavoriteCount() + "收藏");
                                    }
                                    NoticeTransfer.refresh();
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


    public static void toolbarClickDetail(final ImageView favImage, final ImageView loveImage, final ImageView shareImage, final View view, final Feed feed){

        if(feed == null)
            return;
        loveImage.setOnClickListener(new Button.OnClickListener(){//创建监听
            public void onClick(View v) {

                if(App.isLogin()){
                    if(feed.getLikeState() == 1){
                        HttpAPI.disLoveFeed(feed.getId(),new HttpAPI.Callback<BaseModel>() {
                            @Override
                            public void onSuccess(BaseModel baseModel) {
                                if(baseModel.getState() == 0){
                                    loveImage.setImageResource(R.mipmap.zan1);

                                    feed.setLikeCount(feed.getLikeCount()-1);
                                    feed.setLikeState(0);
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
                                    loveImage.setImageResource(R.mipmap.zan_click1);
                                    feed.setLikeCount(feed.getLikeCount()+1);
                                    feed.setLikeState(1);
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

        shareImage.setOnClickListener(new Button.OnClickListener(){//创建监听
            public void onClick(View v) {
                goToShare(view,feed);
            }

        });

        favImage.setOnClickListener(new Button.OnClickListener(){//创建监听
            public void onClick(View v) {
                if(App.isLogin()){
                    if(feed.getFavoriteState() == 1){
                        HttpAPI.disFavFeed(feed.getId(),feed.getFeedCategory(),new HttpAPI.Callback<BaseModel>() {
                            @Override
                            public void onSuccess(BaseModel baseModel) {

                                if(baseModel.getState() == 0){
                                    favImage.setImageResource(R.mipmap.shoucang1);
                                    feed.setFavoriteCount(feed.getFavoriteCount()-1);
                                    feed.setFavoriteState(0);
                                    NoticeTransfer.refresh();
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
                                    favImage.setImageResource(R.mipmap.shoucang_click1);
                                    feed.setFavoriteCount(feed.getFavoriteCount()+1);
                                    feed.setFavoriteState(1);
                                    NoticeTransfer.refresh();
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



    }

}
