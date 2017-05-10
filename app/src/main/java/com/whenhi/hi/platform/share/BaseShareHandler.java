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

package com.whenhi.hi.platform.share;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * The abstract login handler.
 *
 * @author markmjw
 * @since 1.0.0
 */
public abstract class BaseShareHandler {
    protected static final String TAG = "BaseShareHandler";


    private IShareListener mIShareListener;

    public BaseShareHandler() {

    }


    /**
     * set login callback
     *
     * @param listener the callback
     */
    protected void setCallBack(IShareListener listener) {
        mIShareListener = listener;
    }

    /**
     * callback
     *
     * @param statusCode status code
     * @param data       callback data
     */
    public synchronized void callBack(int statusCode,Object data) {
        if (null != mIShareListener) {
            mIShareListener.shareStatus(statusCode,data);
        }
    }

    /**
     * format date with yyyy-MM-dd hh:MM:ss
     *
     * @param time the date time
     * @return format string
     */
    protected String formatDate(long time) {
        return new SimpleDateFormat("yyyy-MM-dd hh:MM:ss", Locale.CHINA).format(new Date(time));
    }

    /**
     *
     * @param message the log message
     */
    protected void log(String message) {
        Log.i(TAG, message);
    }
}
