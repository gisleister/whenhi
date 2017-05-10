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

import com.tencent.open.utils.SystemUtils;
import com.tencent.tauth.Tencent;
import com.whenhi.hi.platform.share.BaseShareHandler;

/**
 * QQ helper.
 *
 * @author 1.0.0
 * @since 2015-03-02
 */
public class QQHelper extends BaseShareHandler {
    public static final String TAG = "QQHelper";

    private int SHARE_TYPE = 0;//0代表qq1代表qqzone

    private static QQHelper sInstance;

    private Tencent mTencent;

    private QQHelper(Context context) {
        mTencent = Tencent.createInstance(PlatformConfig.getInstance().getQQId(),
                context.getApplicationContext());
    }

    public synchronized static QQHelper getInstance(Context context) {
        if (sInstance == null) {
            sInstance = new QQHelper(context);
        }
        return sInstance;
    }

    /**
     * 是否安装QQ客户端
     *
     * @param context the context
     * @return is installed
     */
    public static boolean isInstalled(Context context) {
        return SystemUtils.checkMobileQQ(context);
    }

    /**
     * 获取QQ API
     *
     * @return Tencent api object
     */
    public Tencent getTencent() {
        return mTencent;
    }


}
