package com.whenhi.hi.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.orhanobut.dialogplus.DialogPlus;
import com.orhanobut.dialogplus.Holder;
import com.orhanobut.dialogplus.OnClickListener;
import com.orhanobut.dialogplus.OnDismissListener;
import com.orhanobut.dialogplus.ViewHolder;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.whenhi.hi.App;
import com.whenhi.hi.R;
import com.whenhi.hi.model.BaseModel;
import com.whenhi.hi.model.Feed;
import com.whenhi.hi.network.HttpAPI;
import com.whenhi.hi.platform.login.ILoginListener;
import com.whenhi.hi.platform.model.Share;
import com.whenhi.hi.platform.share.IShareListener;
import com.whenhi.hi.platform.share.qq.QQShareHandler;
import com.whenhi.hi.platform.share.wechat.WechatShareHandler;
import com.whenhi.hi.platform.share.weibo.WeiboShareHandler;
import com.whenhi.hi.receiver.NoticeTransfer;
import com.whenhi.hi.util.ImageUtil;

import java.util.ArrayList;

/**
 * Created by 王雷 on 2017/1/5.
 */

public class ShareActivity extends BaseActivity implements WbShareCallback {

    public static final String TAG = "ShareActivity";
    private static WechatShareHandler mWechatHandler;
    private WeiboShareHandler mWeiboShareHandler;
    private QQShareHandler mQQShareHandler;


    private ILifecycleListener mLifeListener;
    private Feed mFeed;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_share);


        Intent intent = getIntent();
        mFeed = (Feed) intent.getSerializableExtra("Feed");


        String title = mFeed.getTitle();
        String content = mFeed.getContent();

        if(TextUtils.isEmpty(title)){
            mFeed.setTitle("很嗨-没事偷着乐");
        }
        if(TextUtils.isEmpty(content)){
            mFeed.setContent("每天给自己一个开心的理由");
        }




        mWechatHandler = new WechatShareHandler(this);
        mWeiboShareHandler = new WeiboShareHandler(this);
        mQQShareHandler = new QQShareHandler(this);



        if(mFeed.getFeedCategory() == 4){
            ImageUtil.setShareBitmap(null);
        }
        ImageUtil.getBitmap(ShareActivity.this,ImageUtil.getShareImageUrl(mFeed));

        share(mFeed.getType());

    }


    @Override
    public void finish() {
        super.finish();
        //关闭窗体动画显示
        this.overridePendingTransition(R.anim.activity_close,0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (mLifeListener != null) {
            mLifeListener.onActivityResult(requestCode, resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    private void setLifecycleListener(ILifecycleListener lifeListener) {
        mLifeListener = lifeListener;
    }


    private void shareQQ() {

        this.setLifecycleListener(new ILifecycleListener() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                mQQShareHandler.onActivityResult(requestCode, resultCode, data);
                setLifecycleListener(null);
            }
        });
        String shareUrl = mFeed.getShareUrl();
        String shareImageUrl = ImageUtil.getShareImageUrl(mFeed);
        String title = mFeed.getTitle();
        mQQShareHandler.shareToQQWithNetworkImage(this, title,mFeed.getContent(), shareUrl,shareImageUrl,new ShareListener());
    }

    private void shareQQZone() {

        this.setLifecycleListener(new ILifecycleListener() {
            @Override
            public void onActivityResult(int requestCode, int resultCode, Intent data) {
                mQQShareHandler.onActivityResult(requestCode, resultCode, data);
                setLifecycleListener(null);
            }
        });

        String shareUrl = mFeed.getShareUrl();
        ArrayList<String>  images = ImageUtil.getShareImageUrls(mFeed);

        String title = mFeed.getTitle();
        mQQShareHandler.shareToQzoneWithNetWorkImages(this, title,mFeed.getContent(), shareUrl,images,new ShareListener());
    }

    public static WechatShareHandler getWechatShareHandler(){
        return mWechatHandler;
    }

    private void shareWechat() {
        String shareUrl = mFeed.getShareUrl();
        Bitmap bp = ImageUtil.zoomOut(ImageUtil.getShareBitmap());
        if(bp == null){
            bp = BitmapFactory.decodeResource(getResources(),R.mipmap.logo);
        }
        String title = mFeed.getTitle();
        mWechatHandler.sendFriend(title,mFeed.getContent(),shareUrl,bp,new ShareListener(),1);
    }
    private void shareWechatTimeline() {
        String shareUrl = mFeed.getShareUrl();

        Bitmap bp = ImageUtil.zoomOut(ImageUtil.getShareBitmap());
        if(bp == null){
            bp = BitmapFactory.decodeResource(getResources(),R.mipmap.logo);
        }

        String title = mFeed.getTitle();
        mWechatHandler.sendTimeLine(title,mFeed.getContent(),shareUrl,bp,new ShareListener(),2);
    }

    private void shareWeibo() {
        String shareUrl = mFeed.getShareUrl();

        Bitmap full = ImageUtil.getShareBitmap();
        Bitmap bp;
        if(full != null){
            bp = ImageUtil.zoomOut(full);
        }else{
            bp = BitmapFactory.decodeResource(getResources(),R.mipmap.logo);
            full = bp;
        }


        String title = mFeed.getTitle();

        mWeiboShareHandler.sendMultiMessage(true,true,true,false,ShareActivity.this,title,mFeed.getContent(),shareUrl,bp,full,new ShareListener());

    }





    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        mWeiboShareHandler.handleWeiboResponse(intent);

    }


    private void onShareFinish(int code, Object data) {

        Share shareResult = (Share) data;
        switch (code) {
            case ILoginListener.CODE_SUCCESS:
                HttpAPI.updateShareInfo(shareResult, mFeed, new HttpAPI.Callback<BaseModel>() {
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
                break;

            case ILoginListener.CODE_FAILED:
                break;


        }

        finish();
    }

    @Override
    public void onWbShareSuccess() {

    }

    @Override
    public void onWbShareCancel() {

    }

    @Override
    public void onWbShareFail() {

    }


    private class ShareListener implements IShareListener {
        @Override
        public void shareStatus(final int code, final Object data) {
            // running in main thread
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    onShareFinish(code, data);
                }
            });
        }
    }

    public interface ILifecycleListener {
        void onActivityResult(int requestCode, int resultCode, Intent data);
    }


    private void share(int type){
        Holder holder = new ViewHolder(R.layout.layout_dialog_share);
        DialogPlus dialog = DialogPlus.newDialog(this)
                .setContentHolder(holder)
                .setCancelable(true)
                .setGravity(Gravity.BOTTOM)
                .setOnDismissListener(new OnDismissListener() {
                    @Override
                    public void onDismiss(DialogPlus dialog) {
                        finish();
                    }
                })
                .setExpanded(false)//设置扩展模式可控制dialog的高度
                .setOnClickListener(new OnClickListener() {
                    @Override public void onClick(DialogPlus dialog, View view) {

                        switch (view.getId()) {
                            case R.id.share_image_wechat:
                                shareWechat();
                                break;
                            case R.id.share_image_moments:
                                shareWechatTimeline();
                                break;
                            case R.id.share_image_weibo:
                                shareWeibo();
                                break;
                            case R.id.share_image_qq:
                                shareQQ();
                                break;
                            case R.id.share_image_zone:
                                shareQQZone();
                                break;
                        }
                        //dialog.dismiss();
                    }

                })
                .create();
        dialog.show();

        if(type == 1){
            TextView title = (TextView)holder.getInflatedView().findViewById(R.id.share_title);
            title.setText("邀请好友一起领红包");
        }
    }
}


