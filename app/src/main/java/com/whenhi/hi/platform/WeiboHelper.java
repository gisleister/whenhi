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

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.sina.weibo.sdk.api.ImageObject;
import com.sina.weibo.sdk.api.TextObject;
import com.sina.weibo.sdk.api.WebpageObject;
import com.sina.weibo.sdk.api.WeiboMultiMessage;
import com.sina.weibo.sdk.api.share.IWeiboHandler;
import com.sina.weibo.sdk.api.share.IWeiboShareAPI;
import com.sina.weibo.sdk.api.share.SendMultiMessageToWeiboRequest;
import com.sina.weibo.sdk.api.share.WeiboShareSDK;
import com.sina.weibo.sdk.utils.Utility;
import com.whenhi.hi.util.ImageUtil;


/**
 * Weibo helper.
 *
 * @author markmjw
 * @since 1.0.0
 */
public class WeiboHelper {
    private static WeiboHelper sInstance;

    private final IWeiboShareAPI mWeiboShareAPI;

    private WeiboHelper(Context context) {
        // 创建微博分享接口实例
        mWeiboShareAPI = WeiboShareSDK.createWeiboAPI(context.getApplicationContext(),
                PlatformConfig.getInstance().getWeiboKey());
        mWeiboShareAPI.registerApp();
    }

    public synchronized static WeiboHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new WeiboHelper(context);
        }
        return sInstance;
    }

    /**
     * 是否安装微博APP
     *
     * @return is installed
     */
    public boolean isInstalled() {
        return mWeiboShareAPI.isWeiboAppInstalled();
    }

    public IWeiboShareAPI getAPI(){return mWeiboShareAPI;}

    /**
     * 处理分享结果
     *
     * @param intent   {@link Intent}
     * @param response IWeiboHandler.Response
     */
    public void handleResponse(Intent intent, IWeiboHandler.Response response) {
        mWeiboShareAPI.handleWeiboResponse(intent, response);
    }


}
