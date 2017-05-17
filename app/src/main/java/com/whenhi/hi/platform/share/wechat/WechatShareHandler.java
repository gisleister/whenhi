package com.whenhi.hi.platform.share.wechat;

import android.content.Context;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.whenhi.hi.platform.WechatHelper;
import com.whenhi.hi.platform.model.Share;
import com.whenhi.hi.platform.share.BaseShareHandler;
import com.whenhi.hi.platform.share.IShareListener;
import com.whenhi.hi.util.ImageUtil;

/**
 * Created by 王雷 on 2017/1/6.
 */

public class WechatShareHandler extends BaseShareHandler {
    public static final String TAG = WechatShareHandler.class.getSimpleName();

    public static final int TYPE_WECHAT_FRIEND = 0;
    public static final int TYPE_WECHAT_TIMELINE = 1;

    private static final int MAX_IMAGE_LENGTH = 32 * 1024;
    private static final int DEFAULT_MAX_SIZE = 150;

    private WechatHelper mManager;
    private Share mShare;

    public WechatShareHandler(Context context) {
        mManager = WechatHelper.getInstance(context);
        mShare = new Share();
    }

    /**
     * handle resp from wechat.
     *
     * @param resp the resp
     */
    public void handleResponse(BaseResp resp) {

        switch (resp.errCode) {
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
     * 分享给微信朋友
     *
     * @param title 标题
     * @param des   描述信息
     * @param url   分享的链接
     * @param image 图片
     */
    public void sendFriend(String title, String des, String url, Bitmap image,IShareListener listener, int type) {
        mShare.setType(type);
        setCallBack(listener);
        WXWebpageObject page = new WXWebpageObject();
        page.webpageUrl = url;

        if(TextUtils.isEmpty(des)){
            des = "全球最大的娱乐内容汇集地...";
        }

        WXMediaMessage msg = new WXMediaMessage(page);
        msg.title = title;
        msg.description = des;
        if (null != image) {
            // 缩略图的二进制数据
            msg.thumbData = ImageUtil.bitmapToBytes(image, true);
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        // 用分享的时间来标识唯一的请求
        req.transaction = TYPE_WECHAT_FRIEND + String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        mManager.getAPI().sendReq(req);
    }

    /**
     * 分享给微信朋友
     *
     * @param title 标题
     * @param des   描述信息
     * @param url   分享链接
     * @param image 图片
     */
    public void sendFriend(String title, String des, String url, byte[] image) {
        WXWebpageObject page = new WXWebpageObject();
        page.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(page);
        msg.title = title;
        msg.description = des;
        if (null != image) {
            // 缩略图的二进制数据
            msg.thumbData = image;
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        // 用分享的时间来标识唯一的请求
        req.transaction = TYPE_WECHAT_FRIEND + String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        mManager.getAPI().sendReq(req);
    }

    /**
     * 分享给微信朋友
     *
     * @param filePath 文件路径
     * @param image    图片
     */
    public void sendFriend(String filePath, byte[] image) {
        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(filePath);

        final WXMediaMessage msg = new WXMediaMessage();
        if (null != image) {
            // 缩略图的二进制数据
            msg.thumbData = image;
        }
        msg.mediaObject = imgObj;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = TYPE_WECHAT_FRIEND + String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneSession;
        mManager.getAPI().sendReq(req);
    }

    /**
     * 分享给微信朋友
     *
     * @param filePath 文件路径
     * @param image    图片
     */
    public void sendTimeLine(String filePath, byte[] image) {
        WXImageObject imgObj = new WXImageObject();
        imgObj.setImagePath(filePath);

        final WXMediaMessage msg = new WXMediaMessage();
        if (null != image) {
            // 缩略图的二进制数据
            msg.thumbData = image;
        }
        msg.mediaObject = imgObj;

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        req.transaction = TYPE_WECHAT_TIMELINE + String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        mManager.getAPI().sendReq(req);
    }

    /**
     * 分享到微信朋友圈
     *
     * @param title 标题
     * @param des   描述信息
     * @param url   分享链接
     * @param image 图片
     */
    public void sendTimeLine(String title, String des, String url, Bitmap image,IShareListener listener, int type) {
        mShare.setType(type);
        setCallBack(listener);
        WXWebpageObject page = new WXWebpageObject();
        page.webpageUrl = url;
        WXMediaMessage msg = new WXMediaMessage(page);
        msg.title = title;
        msg.description = des;
        if (null != image) {
            // 缩略图的二进制数据
            msg.thumbData = ImageUtil.bitmapToBytes(image, true);
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        // 用分享的时间来标识唯一的请求
        req.transaction = TYPE_WECHAT_TIMELINE + String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        mManager.getAPI().sendReq(req);
    }

    /**
     * 分享到微信朋友圈
     *
     * @param title 标题
     * @param des   描述信息
     * @param url   分享的链接
     * @param image 图片
     */
    public void sendTimeLine(String title, String des, String url, byte[] image) {
        WXWebpageObject page = new WXWebpageObject();
        page.webpageUrl = url;

        WXMediaMessage msg = new WXMediaMessage(page);
        msg.title = title;
        msg.description = des;
        if (null != image) {
            // 缩略图的二进制数据
            msg.thumbData = image;
        }

        SendMessageToWX.Req req = new SendMessageToWX.Req();
        // 用分享的时间来标识唯一的请求
        req.transaction = TYPE_WECHAT_TIMELINE + String.valueOf(System.currentTimeMillis());
        req.message = msg;
        req.scene = SendMessageToWX.Req.WXSceneTimeline;
        mManager.getAPI().sendReq(req);
    }

    /**
     * 根据微信的要求缩放缩略图
     *
     * @param bitmap 图片
     * @return 图片
     */
    public Bitmap zoomOut(Bitmap bitmap) {
        Bitmap dstBitmap = null;
        if (null != bitmap) {
            int width = bitmap.getWidth();
            int height = bitmap.getHeight();

            if (width <= 0 || height <= 0) return null;

            int w, h;
            float scale = height * 1.0f / width;
            if (width > height) {
                w = DEFAULT_MAX_SIZE;
                h = (int) (w * scale);
            } else {
                h = DEFAULT_MAX_SIZE;
                w = (int) (h / scale);
            }

            dstBitmap = ImageUtil.zoom(bitmap, w, h);
            byte[] data = ImageUtil.bitmapToBytes(dstBitmap, false);

            while (data.length > MAX_IMAGE_LENGTH) {
                dstBitmap.recycle();

                w -= 10;
                h = (int) (w * scale);

                dstBitmap = ImageUtil.zoom(bitmap, w, h);
                data = ImageUtil.bitmapToBytes(dstBitmap, false);
            }
        }

        return dstBitmap;
    }
}
