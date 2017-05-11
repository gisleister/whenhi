package com.whenhi.hi.platform.share.weibo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.share.WbShareCallback;
import com.sina.weibo.sdk.utils.Utility;
import com.whenhi.hi.platform.WeiboHelper;
import com.whenhi.hi.platform.model.Share;
import com.whenhi.hi.platform.share.BaseShareHandler;
import com.whenhi.hi.platform.share.IShareListener;

/**
 * Created by 王雷 on 2017/1/6.
 */

public class WeiboShareHandler extends BaseShareHandler implements WbShareCallback {
    public static final String TAG = WeiboShareHandler.class.getSimpleName();


    private WeiboHelper mManager;
    private Share mShare = new Share();


    public WeiboShareHandler(Context context) {
        mManager = WeiboHelper.getInstance(context);

        mShare.setType(5);

    }

    public void handleWeiboResponse(Intent intent) {
        mManager.getAPI().doResultIntent(intent,this);
    }


    @Override
    public void onWbShareSuccess() {
        callBack(IShareListener.CODE_SUCCESS, mShare);
    }

    @Override
    public void onWbShareCancel() {
        callBack(IShareListener.CODE_CANCEL_SHARE, mShare);
    }

    @Override
    public void onWbShareFail() {
        callBack(IShareListener.CODE_FAILED, mShare);
    }

    /**
     * 第三方应用发送请求消息到微博，唤起微博分享界面。
     * 同时可以分享文本、图片以及其它媒体资源（网页、音乐、视频、声音中的一种）。
     *
     * @param hasText    分享的内容是否有文本
     * @param hasImage   分享的内容是否有图片
     * @param hasWebpage 分享的内容是否有网页
     * @param hasVideo   分享的内容是否有视频
     */
    public void sendMultiMessage(boolean hasText, boolean hasImage, boolean hasWebpage,
                                   boolean hasVideo, Activity activity, String title, String des, String url, Bitmap
                                          bitmap, Bitmap fullbp,IShareListener listener) {

        // 1. 初始化微博的分享消息
        WeiboMultiMessage weiboMessage = new WeiboMultiMessage();
        if (hasText) {
            weiboMessage.textObject = getTextObject(des);
        }

        if (hasImage) {
            weiboMessage.imageObject = getImageObject(fullbp);
        }

        // 用户可以分享其它媒体资源（网页、音乐、视频、声音中的一种）
        if (hasWebpage) {
            weiboMessage.mediaObject = getWebPageObject(title,des,url,bitmap);
        }



        mManager.getAPI().shareMessage(weiboMessage, true);
        setCallBack(listener);
    }


    /**
     * 创建文本消息对象
     *
     * @param content 文本内容
     * @return 文本消息对象
     */
    private TextObject getTextObject(String content) {
        TextObject text = new TextObject();
        text.text = content;
        return text;
    }

    /**
     * 创建图片消息对象
     *
     * @return 图片消息对象
     */
    private ImageObject getImageObject( Bitmap bitmap) {
        ImageObject image = new ImageObject();
        if (bitmap != null) {
            image.setImageObject(bitmap);
        }
        return image;
    }

    /**
     * 创建多媒体（网页）消息对象
     *
     * @param title     标题
     * @param des       描述信息
     * @param url       分享的链接
     * @param bitmap 缩略图
     * @return 网页消息对象
     */
    private WebpageObject getWebPageObject(String title, String des, String url, Bitmap bitmap) {
        WebpageObject mediaObject = new WebpageObject();
        mediaObject.identify = Utility.generateGUID();
        mediaObject.title = "#" + title + "#" + " @很嗨爆笑君 ";
        mediaObject.description = des;

        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = url;
        //mediaObject.defaultText = title;
        return mediaObject;
    }


}
