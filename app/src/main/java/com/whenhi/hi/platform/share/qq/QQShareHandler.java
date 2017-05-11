package com.whenhi.hi.platform.share.qq;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;

import com.tencent.connect.common.Constants;
import com.tencent.connect.share.QQShare;
import com.tencent.connect.share.QzoneShare;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.whenhi.hi.platform.QQHelper;
import com.whenhi.hi.platform.model.Share;
import com.whenhi.hi.platform.share.BaseShareHandler;
import com.whenhi.hi.platform.share.IShareListener;

import java.util.ArrayList;

/**
 * Created by 王雷 on 2017/1/6.
 */

public class QQShareHandler extends BaseShareHandler {
    public static final String TAG = "QQShareHandler";

    private QQHelper mManager;
    private Context mContext;
    private Share mShare;
    public QQShareHandler(Context context) {
        mContext = context.getApplicationContext();
        mManager = QQHelper.getInstance(context);
        mShare = new Share();
    }



    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        mManager.getTencent().onActivityResultData(requestCode, resultCode, data, mQQShareListener);
        if (requestCode == Constants.REQUEST_API) {
            if (resultCode == Constants.REQUEST_QQ_SHARE ||
                    resultCode == Constants.REQUEST_QZONE_SHARE ||
                    resultCode == Constants.REQUEST_OLD_SHARE) {
                Tencent.handleResultData(data, mQQShareListener);
            }
        }
    }


    /**
     * 分享到qq好友(本地图片，注：如果没有安装qq客户端，有问题，坑啊)
     *
     * @param activity  activity
     * @param title     标题
     * @param summary   分享内容、摘要
     * @param targetUrl 点击跳转的url
     * @param imagePath 图片路径
     */
    public void shareToQQWithLocalImage(Activity activity, String title, String summary,
                                        String targetUrl, String imagePath) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imagePath);
        mManager.getTencent().shareToQQ(activity, params, mQQShareListener);
    }

    /**
     * 分享到qq好友（网络图片）
     *
     * @param activity  activity
     * @param title     标题
     * @param summary   分享内容、摘要
     * @param targetUrl 点击跳转的url
     * @param imageUrl  图片url
     */
    public void shareToQQWithNetworkImage(Activity activity, String title, String summary,
                                          String targetUrl, String imageUrl, IShareListener listener) {

        if(TextUtils.isEmpty(summary)){
            summary = "全球最大的娱乐内容汇集地...";
        }
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_DEFAULT);
        params.putString(QQShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QQShare.SHARE_TO_QQ_SUMMARY, summary);
        params.putString(QQShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_URL, imageUrl);
        setCallBack(listener);
        mManager.getTencent().shareToQQ(activity, params, mQQShareListener);
    }

    /**
     * 分享本地图片到QQ
     *
     * @param activity activity
     * @param imageUrl 图片URL
     */
    public void shareToQQWithImage(Activity activity, String imageUrl) {
        final Bundle params = new Bundle();
        params.putInt(QQShare.SHARE_TO_QQ_KEY_TYPE, QQShare.SHARE_TO_QQ_TYPE_IMAGE);
        params.putString(QQShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imageUrl);
        mManager.getTencent().shareToQQ(activity, params, mQQShareListener);
    }

    /**
     * 分享到qq空间（本地图文模式，注：有问题，坑啊，如果安装了qq客户端，分享不了；如果没有安装qq客户端，分享看不到图片）
     * QzoneShare.SHARE_TO_QQ_IMAGE_LOCAL_URL已废弃，分享本地图片统一使用QzoneShare.SHARE_TO_QQ_IMAGE_URL
     *
     * @param activity   activity
     * @param title      标题
     * @param summary    内容、摘要
     * @param targetUrl  跳转url
     * @param imagePaths 图片路径集合
     */
    public void shareToQzoneWithLocalImages(Activity activity, String title, String summary,
                                            String targetUrl, ArrayList<String> imagePaths, IShareListener listener) {
        shareToQzoneWithLocalImages(activity, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT, title,
                summary, targetUrl, imagePaths,listener);
    }

    /**
     * 分享到qq空间
     *
     * @param activity   activity
     * @param shareType  分享类型（本地图文、本地图片等，注：如果没有安装qq客户端，有问题，坑啊）
     * @param title      标题
     * @param summary    内容、摘要
     * @param targetUrl  跳转url
     * @param imagePaths 图片路径集合
     */
    public void shareToQzoneWithLocalImages(Activity activity, int shareType, String title,
                                            String summary, String targetUrl,
                                            ArrayList<String> imagePaths, IShareListener listener) {
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, shareType);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary);
        if (shareType != QzoneShare.SHARE_TO_QZONE_TYPE_APP) {
            params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        }
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_LOCAL_URL, imagePaths);
        mManager.getTencent().shareToQzone(activity, params, mQQZoneShareListener);
        setCallBack(listener);
    }

    /**
     * 分享到qq空间（图文模式）
     *
     * @param activity  activity
     * @param title     标题
     * @param summary   内容、摘要
     * @param targetUrl 跳转url
     * @param imageUrls 图片url集合
     */
    public void shareToQzoneWithNetWorkImages(Activity activity, String title, String summary,
                                              String targetUrl, ArrayList<String> imageUrls, IShareListener listener) {
        if(TextUtils.isEmpty(summary)){
            summary = "全球最大的娱乐内容汇集地...";
        }
        shareToQzoneWithNetWorkImages(activity, QzoneShare.SHARE_TO_QZONE_TYPE_IMAGE_TEXT, title,
                summary, targetUrl, imageUrls,listener);
    }

    /**
     * 分享到qq空间
     *
     * @param activity  activity
     * @param shareType 分享类型（图文、图等）
     * @param title     标题
     * @param summary   内容、摘要
     * @param targetUrl 跳转url
     * @param imageUrls 图片url集合
     */
    public void shareToQzoneWithNetWorkImages(Activity activity, int shareType, String title,
                                              String summary, String targetUrl,
                                              ArrayList<String> imageUrls, IShareListener listener) {
        final Bundle params = new Bundle();
        params.putInt(QzoneShare.SHARE_TO_QZONE_KEY_TYPE, shareType);
        params.putString(QzoneShare.SHARE_TO_QQ_TITLE, title);
        params.putString(QzoneShare.SHARE_TO_QQ_SUMMARY, summary);
        if (shareType != QzoneShare.SHARE_TO_QZONE_TYPE_APP) {
            params.putString(QzoneShare.SHARE_TO_QQ_TARGET_URL, targetUrl);
        }
        params.putStringArrayList(QzoneShare.SHARE_TO_QQ_IMAGE_URL, imageUrls);
        mManager.getTencent().shareToQzone(activity, params, mQQZoneShareListener);
        setCallBack(listener);
    }


    private IUiListener mQQShareListener = new IUiListener() {


        @Override
        public void onCancel() {
            mShare.setType(3);
            callBack(IShareListener.CODE_FAILED, mShare);
            mManager.getTencent().releaseResource();
        }

        @Override
        public void onComplete(Object response) {
            mShare.setType(3);
            if (null == response) {
                callBack(IShareListener.CODE_FAILED, mShare);
                mManager.getTencent().releaseResource();
            }

            callBack(IShareListener.CODE_SUCCESS, mShare);
            Log.d(TAG, "QQ share response: " + mShare.toString());
            mManager.getTencent().releaseResource();

        }

        @Override
        public void onError(UiError e) {
            mShare.setType(3);
            mShare.setErrorMessage(e.errorMessage);
            callBack(IShareListener.CODE_FAILED, mShare);
            Log.e(TAG, "QQ share error: " + e.errorMessage);
            mManager.getTencent().releaseResource();
        }
    };

    private IUiListener mQQZoneShareListener = new IUiListener() {


        @Override
        public void onCancel() {
            mShare.setType(4);
            callBack(IShareListener.CODE_FAILED, mShare);
            mManager.getTencent().releaseResource();
        }

        @Override
        public void onComplete(Object response) {
            mShare.setType(4);
            if (null == response) {
                callBack(IShareListener.CODE_FAILED, mShare);
                mManager.getTencent().releaseResource();
            }

            callBack(IShareListener.CODE_SUCCESS, mShare);
            Log.d(TAG, "QQ share response: " + mShare.toString());
            mManager.getTencent().releaseResource();

        }

        @Override
        public void onError(UiError e) {
            mShare.setType(4);
            mShare.setErrorMessage(e.errorMessage);
            callBack(IShareListener.CODE_FAILED, mShare);
            Log.e(TAG, "QQ share error: " + e.errorMessage);
            mManager.getTencent().releaseResource();
        }
    };


}
