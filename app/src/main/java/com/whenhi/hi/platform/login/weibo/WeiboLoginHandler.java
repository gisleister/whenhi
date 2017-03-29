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

package com.whenhi.hi.platform.login.weibo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.sina.weibo.sdk.auth.AuthInfo;
import com.sina.weibo.sdk.auth.WeiboAuthListener;
import com.sina.weibo.sdk.auth.sso.SsoHandler;
import com.sina.weibo.sdk.exception.WeiboException;
import com.whenhi.hi.network.HttpAPI;
import com.whenhi.hi.platform.PlatformConfig;
import com.whenhi.hi.platform.login.AuthResult;
import com.whenhi.hi.platform.login.BaseLoginHandler;
import com.whenhi.hi.platform.login.ILoginListener;
import com.whenhi.hi.platform.model.Login;


/**
 * Weibo login handler.
 *
 * @author markmjw
 * @since 1.0.0
 */
public class WeiboLoginHandler extends BaseLoginHandler {


    private SsoHandler mSsoHandler;
    private Login mLogin;

    public WeiboLoginHandler() {
        mLogin = new Login();
        mLogin.setType(3);
    }

    /**
     * login
     *
     * @param activity the activity
     * @param listener callback listener
     */
    public void login(Activity activity, ILoginListener listener) {
        setCallBack(listener);
        PlatformConfig config = PlatformConfig.getInstance();
        AuthInfo weiboAuth = new AuthInfo(activity, config.getWeiboKey(), config.getWeiboCallback(),
                config.getWeiboScope());

        mSsoHandler = new SsoHandler(activity, weiboAuth);
        mSsoHandler.authorize(mAuthListener);
    }

    /**
     * should be called in {@link Activity#onActivityResult(int, int, Intent)}
     *
     * @param requestCode The integer request code originally supplied to
     *                    startActivityForResult(), allowing you to identify who this
     *                    result came from.
     * @param resultCode  The integer result code returned by the child activity
     *                    through its setResult().
     * @param data        An Intent, which can return result data to the caller
     *                    (various data can be attached to Intent "extras").
     */
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (null != mSsoHandler) {
            mSsoHandler.authorizeCallBack(requestCode, resultCode, data);
        }
    }

    /**
     * request user information.
     *
     * @param result the login result.
     */
    private void requestUserInfo(final WeiboLoginResult result) {

        HttpAPI.requestWeiboUserInfo(result.uid, result.access_token, new HttpAPI.Callback<WeiboUserInfo>() {
            @Override
            public void onSuccess(WeiboUserInfo weiboUserInfo){
                mLogin.setWeiboUserInfo(weiboUserInfo);
                callBack(ILoginListener.CODE_SUCCESS, mLogin);
            }

            @Override
            public void onFailure(Exception e) {
                mLogin.setErrorMessage(e.getMessage());
                callBack(ILoginListener.CODE_AUTH_FAILED, mLogin);
            }
        });


    }

    private WeiboAuthListener mAuthListener = new WeiboAuthListener() {

        @Override
        public void onComplete(Bundle values) {
            // parse authorize result
            WeiboLoginResult result = new WeiboLoginResult();
            result.access_token = values.getString("access_token");
            result.expires_in = values.getString("expires_in");
            result.remind_in = values.getString("remind_in");
            result.uid = values.getString("uid");
            result.userName = values.getString("userName");
            result.refresh_token = values.getString("refresh_token");

            // convert to common authorize result
            AuthResult auth = new AuthResult();
            auth.from = AuthResult.TYPE_WEIBO;
            auth.id = result.uid;
            auth.accessToken = result.access_token;
            auth.refreshToken = result.refresh_token;
            auth.expiresIn = System.currentTimeMillis() + Long.parseLong(result.expires_in) * 1000L;

            log("Weibo authorize success!" +
                    "\nUid: " + auth.id +
                    "\nAccess token: " + auth.accessToken +
                    "\nExpires in: " + formatDate(auth.expiresIn));

            mLogin.setAuthResult(auth);
            callBack(ILoginListener.CODE_AUTH_SUCCESS, mLogin);
            if (mRequestInfoEnable) {
                callBack(ILoginListener.CODE_LOGIN_ING, mLogin);
                // request user info
                requestUserInfo(result);
            }
        }

        @Override
        public void onWeiboException(WeiboException e) {
            mLogin.setErrorMessage(e.getMessage());
            callBack(ILoginListener.CODE_AUTH_EXCEPTION, mLogin);
        }

        @Override
        public void onCancel() {
            callBack(ILoginListener.CODE_CANCEL_AUTH, null);
        }
    };
}
