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

import com.sina.weibo.sdk.share.WbShareHandler;


/**
 * Weibo helper.
 *
 * @author markmjw
 * @since 1.0.0
 */
public class WeiboHelper {
    private static WeiboHelper sInstance;

    private final WbShareHandler mWbShareHandler;

    private WeiboHelper(Context context) {

        // 创建微博分享接口实例
        mWbShareHandler = new WbShareHandler((Activity)context);
        mWbShareHandler.registerApp();
    }

    public synchronized static WeiboHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new WeiboHelper(context);
        }
        return sInstance;
    }



    public WbShareHandler getAPI(){return mWbShareHandler;}




}
