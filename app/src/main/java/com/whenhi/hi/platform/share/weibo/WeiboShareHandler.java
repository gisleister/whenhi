package com.whenhi.hi.platform.share.weibo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.text.TextUtils;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.VideoObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.BaseResponse;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.constant.WBConstants;
import com.sina.weibo.sdk.utils.Utility;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.whenhi.hi.platform.WechatHelper;
import com.whenhi.hi.platform.WeiboHelper;
import com.whenhi.hi.platform.model.Share;
import com.whenhi.hi.platform.share.BaseShareHandler;
import com.whenhi.hi.platform.share.IShareListener;
import com.whenhi.hi.util.ImageUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Created by 王雷 on 2017/1/6.
 */

public class WeiboShareHandler extends BaseShareHandler {
    public static final String TAG = WeiboShareHandler.class.getSimpleName();


    private WeiboHelper mManager;
    private Share mShare = new Share();

    public WeiboShareHandler(Context context) {
        mManager = WeiboHelper.getInstance(context);

        mShare.setType(5);
    }

    public void handleWeiboResponse(Intent intent,IWeiboHandler.Response response) {

        // 从当前应用唤起微博并进行分享后，返回到当前应用时，需要在此处调用该函数
        // 来接收微博客户端返回的数据；执行成功，返回 true，并调用
        // {@link IWeiboHandler.Response#onResponse}；失败返回 false，不调用上述回调
         mManager.getAPI().handleWeiboResponse(intent, response);



    }

    public void handleResponse(BaseResponse baseResp) {

        switch (baseResp.errCode) {
            case BaseResp.ErrCode.ERR_OK:

                callBack(IShareListener.CODE_SUCCESS, mShare);
                break;

            case BaseResp.ErrCode.ERR_USER_CANCEL:
                callBack(IShareListener.CODE_CANCEL_SHARE, mShare);
                break;

            case BaseResp.ErrCode.ERR_SENT_FAILED:
                callBack(IShareListener.CODE_FAILED, mShare);
                break;
            default:
                callBack(IShareListener.CODE_CANCEL_SHARE, mShare);
                break;
        }
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
            weiboMessage.textObject = getTextObject(title);
        }

        if (hasImage) {
            weiboMessage.imageObject = getImageObject(fullbp);
        }

        // 用户可以分享其它媒体资源（网页、音乐、视频、声音中的一种）
        if (hasWebpage) {
            weiboMessage.mediaObject = getWebPageObject(title,des,url,bitmap);
        }

        if (hasVideo) {
            weiboMessage.mediaObject = getVideoObject(title,des,url,bitmap);
        }


        // 2. 初始化从第三方到微博的消息请求
        SendMultiMessageToWeiboRequest request = new SendMultiMessageToWeiboRequest();
        // 用transaction唯一标识一个请求
        request.transaction = String.valueOf(System.currentTimeMillis());
        request.multiMessage = weiboMessage;

        // 3. 发送请求消息到微博，唤起微博分享界面
        mManager.getAPI().sendRequest(activity, request);
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
        mediaObject.title = title;
        mediaObject.description = des;

        // 设置 Bitmap 类型的图片到视频对象里         设置缩略图。 注意：最终压缩过的缩略图大小不得超过 32kb。
        mediaObject.setThumbImage(bitmap);
        mediaObject.actionUrl = url;
        //mediaObject.defaultText = title;
        return mediaObject;
    }

    /**
     * 创建多媒体（视频）消息对象。
     *
     * @return 多媒体（视频）消息对象。
     */
    private VideoObject getVideoObject(String title, String des, String url, Bitmap bitmap) {
        // 创建媒体消息
        VideoObject videoObject = new VideoObject();
        videoObject.identify = Utility.generateGUID();
        videoObject.title = title;
        videoObject.description = des;



        ByteArrayOutputStream os = null;
        try {
            os = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 85, os);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        videoObject.setThumbImage(bitmap);
        videoObject.actionUrl = url;
        videoObject.dataUrl = url;
        videoObject.dataHdUrl = url;
        videoObject.duration = 10;
        videoObject.defaultText = title;
        return videoObject;
    }
}
