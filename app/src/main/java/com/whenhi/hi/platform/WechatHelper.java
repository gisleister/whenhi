/*
 * Copyright (C) 2015 MarkMjw
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.whenhi.hi.platform;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;

import com.tencent.mm.sdk.modelmsg.SendMessageToWX;
import com.tencent.mm.sdk.modelmsg.WXImageObject;
import com.tencent.mm.sdk.modelmsg.WXMediaMessage;
import com.tencent.mm.sdk.modelmsg.WXWebpageObject;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.whenhi.hi.util.ImageUtil;


/**
 * Wechat helper.
 *
 * @author markmjw
 * @since 1.0.0
 */
public class WechatHelper {


    /** Min supported version. */
    private static final int MIN_SUPPORTED_VERSION = 0x21020001;


    private static WechatHelper sInstance;

    private IWXAPI mApi;

    private WechatHelper(Context context) {
        mApi = WXAPIFactory.createWXAPI(context.getApplicationContext(),
                PlatformConfig.getInstance().getWechatId(),true);
        mApi.registerApp(PlatformConfig.getInstance().getWechatId());
    }

    public synchronized static WechatHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new WechatHelper(context);
        }
        return sInstance;
    }

    /**
     * 微信是否已安装
     *
     * @return is installed
     */
    public boolean isInstalled() {
        return mApi.isWXAppInstalled();
    }

    /**
     * 是否支持发送朋友圈
     *
     * @return is supported
     */
    public boolean isSupported() {
        return mApi.isWXAppSupportAPI();
    }

    /**
     * 是否支持发送朋友圈
     *
     * @return is supported
     */
    public boolean isSupportedTimeline() {
        return mApi.getWXAppSupportAPI() >= MIN_SUPPORTED_VERSION;
    }


    /**
     * 获取微信API
     *
     * @return IWXAPI
     */
    public IWXAPI getAPI() {
        return mApi;
    }

    /**
     * 处理分享结果
     *
     * @param intent  {@link Intent}
     * @param handler IWXAPIEventHandler
     */
    public void handleResponse(Intent intent, IWXAPIEventHandler handler) {
        mApi.handleIntent(intent, handler);
    }


}
