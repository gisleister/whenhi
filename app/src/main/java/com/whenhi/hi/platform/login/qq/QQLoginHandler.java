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

package com.whenhi.hi.platform.login.qq;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.tencent.connect.UserInfo;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.UiError;
import com.whenhi.hi.platform.QQHelper;
import com.whenhi.hi.platform.login.AuthResult;
import com.whenhi.hi.platform.login.BaseLoginHandler;
import com.whenhi.hi.platform.login.ILoginListener;
import com.whenhi.hi.platform.model.Login;
import com.whenhi.hi.util.GsonUtil;


/**
 * QQ login handler.
 *
 * @author markmjw
 * @since 1.0.0
 */
public class QQLoginHandler extends BaseLoginHandler {
    private QQHelper mManager;
    private Context mContext;
    private Login mLogin;

    public QQLoginHandler(Context context) {
        mContext = context.getApplicationContext();
        mManager = QQHelper.getInstance(context);
        mLogin = new Login();
        mLogin.setType(1);
    }

    /**
     * login
     *
     * @param activity activity
     * @param listener callback listener
     */
    public void login(Activity activity, ILoginListener listener) {
        Log.d(TAG, "QQ login insert");
        setCallBack(listener);
        mManager.getTencent().login(activity, "all", mAuthListener);
    }

    /**
     * logout
     *
     * @param activity activity
     */
    public void logout(Activity activity) {
        mManager.getTencent().logout(activity);
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
        mManager.getTencent().onActivityResultData(requestCode, resultCode, data, mAuthListener);
        //mManager.getTencent().onActivityResult(requestCode, resultCode, data);
    }

    private IUiListener mAuthListener = new IUiListener() {
        @Override
        public void onComplete(Object response) {
            Log.d(TAG, "QQ login complete");
            if (null == response) {
                callBack(ILoginListener.CODE_AUTH_FAILED, "");
                // release resource
                mManager.getTencent().releaseResource();
                return;
            }

            QQLoginResult result = GsonUtil.fromJson(response + "", QQLoginResult.class);
            AuthResult auth = new AuthResult();
            auth.from = AuthResult.TYPE_QQ;
            auth.id = result.openid;
            auth.accessToken = result.access_token;
            auth.expiresIn = result.expires_in;

            mManager.getTencent().setAccessToken(auth.accessToken, auth.expiresIn + "");
            mManager.getTencent().setOpenId(auth.id);
            long expiresIn = mManager.getTencent().getExpiresIn();
            log("QQ authorize success!" +
                    "\nOpenId: " + auth.id +
                    "\nAccess token: " + auth.accessToken +
                    "\nExpires in: " + formatDate(expiresIn));

            mLogin.setAuthResult(auth);
            callBack(ILoginListener.CODE_AUTH_SUCCESS, mLogin);
            if (mRequestInfoEnable) {
                callBack(ILoginListener.CODE_LOGIN_ING, mLogin);
                // request user info
                new UserInfo(mContext, mManager.getTencent().getQQToken())
                        .getUserInfo(mGetInfoListener);
            }
        }

        @Override
        public void onError(UiError e) {
            mLogin.setErrorMessage(e.errorMessage);
            callBack(ILoginListener.CODE_AUTH_EXCEPTION, mLogin);
            // release resource
            mManager.getTencent().releaseResource();
        }

        @Override
        public void onCancel() {
            callBack(ILoginListener.CODE_CANCEL_AUTH, mLogin);
            // release resource
            mManager.getTencent().releaseResource();
        }
    };

    private IUiListener mGetInfoListener = new IUiListener() {
        @Override
        public void onComplete(Object response) {
            if (null == response) {
                callBack(ILoginListener.CODE_AUTH_FAILED, mLogin);
                // release resource
                mManager.getTencent().releaseResource();
                return;
            }

            // request user info success
            QQUserInfo info = GsonUtil.fromJson(response + "", QQUserInfo.class);

            mLogin.setqQUserInfo(info);
            callBack(ILoginListener.CODE_SUCCESS, mLogin);

            // release resource
            mManager.getTencent().releaseResource();
        }

        @Override
        public void onError(UiError e) {
            mLogin.setErrorMessage(e.errorMessage);
            callBack(ILoginListener.CODE_FAILED, mLogin);
            // release resource
            mManager.getTencent().releaseResource();
        }

        @Override
        public void onCancel() {
            callBack(ILoginListener.CODE_CANCEL_AUTH, mLogin);
            // release resource
            mManager.getTencent().releaseResource();
        }
    };
}
