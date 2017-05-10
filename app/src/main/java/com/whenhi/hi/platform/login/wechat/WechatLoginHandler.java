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

package com.whenhi.hi.platform.login.wechat;

import android.content.Context;
import android.text.TextUtils;

import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.whenhi.hi.network.HttpAPI;
import com.whenhi.hi.platform.PlatformConfig;
import com.whenhi.hi.platform.WechatHelper;
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
public class WechatLoginHandler extends BaseLoginHandler {

    private WechatHelper mManager;
    private Login mLogin;
    public WechatLoginHandler(Context context) {
        mManager = WechatHelper.getInstance(context);
    }

    /**
     * login
     *
     * @param listener callback listener
     */
    public void login(ILoginListener listener) {
        setCallBack(listener);
        SendAuth.Req request = new SendAuth.Req();
        request.scope = PlatformConfig.getInstance().getWechatScope();
        request.state = PlatformConfig.getInstance().getWechatState();
        mManager.getAPI().sendReq(request);
        mLogin = new Login();
        mLogin.setType(2);

    }

    /**
     * handle response from wechat.
     *
     * @param response the response
     */
    public void handleResponse(SendAuth.Resp response) {
        switch (response.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                String code = response.code;
                if (!TextUtils.isEmpty(code)) {
                    callBack(ILoginListener.CODE_LOGIN_ING, mLogin);
                    requestToken(code);
                } else {
                    callBack(ILoginListener.CODE_AUTH_FAILED, mLogin);
                }
                break;

            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                callBack(ILoginListener.CODE_AUTH_FAILED, mLogin);
                break;

            case BaseResp.ErrCode.ERR_USER_CANCEL:
                callBack(ILoginListener.CODE_CANCEL_AUTH, mLogin);
                break;
        }
    }

    /**
     * request token from wechat server.
     *
     * @param code the auth code
     */
    private void requestToken(String code) {
        HttpAPI.requestWechatToken(code, new HttpAPI.Callback<WechatLoginResult>() {
            @Override
            public void onSuccess(WechatLoginResult wechatLoginResult)  {
                AuthResult auth = new AuthResult();
                auth.from = AuthResult.TYPE_WECHAT;
                auth.id = wechatLoginResult.getOpenid();
                auth.accessToken = wechatLoginResult.getAccess_token();
                auth.expiresIn = wechatLoginResult.getExpires_in();
                auth.refreshToken = wechatLoginResult.getRefresh_token();

                mLogin.setAuthResult(auth);
                callBack(ILoginListener.CODE_AUTH_SUCCESS, mLogin);
                if (mRequestInfoEnable) {
                    callBack(ILoginListener.CODE_LOGIN_ING, mLogin);
                    // request user info
                    requestUserInfo(auth.id, auth.accessToken);
                }
            }

            @Override
            public void onFailure(Exception e) {
                mLogin.setErrorMessage(e.getMessage());
                callBack(ILoginListener.CODE_AUTH_FAILED, mLogin);
            }
        });

    }

    /**
     * request user information.
     *
     * @param openId      the app id
     * @param accessToken the access token
     */
    private void requestUserInfo(String openId, String accessToken) {


        HttpAPI.requestWechatUserInfo(openId, accessToken, new HttpAPI.Callback<WechatUserInfo>() {
            @Override
            public void onSuccess(WechatUserInfo wechatUserInfo){
                mLogin.setWechatUserInfo(wechatUserInfo);
                callBack(ILoginListener.CODE_SUCCESS, mLogin);

            }

            @Override
            public void onFailure(Exception e) {
                mLogin.setErrorMessage(e.getMessage());
                callBack(ILoginListener.CODE_FAILED, mLogin);
            }
        });


    }
}
