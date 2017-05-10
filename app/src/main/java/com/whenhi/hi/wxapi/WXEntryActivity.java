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

package com.whenhi.hi.wxapi;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.tencent.mm.sdk.modelbase.BaseReq;
import com.tencent.mm.sdk.modelbase.BaseResp;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.whenhi.hi.R;
import com.whenhi.hi.activity.LoginActivity;
import com.whenhi.hi.activity.ShareActivity;
import com.whenhi.hi.platform.WechatHelper;
import com.whenhi.hi.platform.login.wechat.WechatLoginHandler;
import com.whenhi.hi.platform.share.wechat.WechatShareHandler;


public class WXEntryActivity extends Activity implements IWXAPIEventHandler {
    private static String TAG = WXEntryActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);
        handleIntent();
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
        handleIntent();
    }

    private void handleIntent() {
        WechatHelper.getInstance(this).handleResponse(getIntent(), this);
    }

    @Override
    public void onReq(BaseReq baseReq) {

    }

    @Override
    public void onResp(BaseResp resp) {

        switch (resp.errCode) {
            case BaseResp.ErrCode.ERR_OK:
                sucess(resp);
                break;
            case BaseResp.ErrCode.ERR_USER_CANCEL:
                break;
            case BaseResp.ErrCode.ERR_AUTH_DENIED:
                break;
            default:
                break;
        }
        finish();

    }

    private void sucess(BaseResp resp){
        int type = resp.getType();
        switch (type) {
            case 1: // SendAuth.Resp
                WechatLoginHandler wechatLoginHandler = LoginActivity.getWechatLoginHandler();
                if (null != wechatLoginHandler) {
                    wechatLoginHandler.handleResponse((SendAuth.Resp) resp);
                }
                break;

            case 2: // SendMessageToWX.Resp
                WechatShareHandler wechatShareHandler = ShareActivity.getWechatShareHandler();
                if (null != wechatShareHandler) {
                    Log.d(TAG,"分享完成，准备回调"+wechatShareHandler);
                    wechatShareHandler.handleResponse(resp);
                    Log.d(TAG,"分享完成，回调完成");
                }
                break;

            case 3: // GetMessageFromWX.Resp

                break;

            case 4: // ShowMessageFromWX.Resp

                break;
        }
    }

}
