package com.whenhi.hi.platform.model;

import com.whenhi.hi.platform.login.AuthResult;
import com.whenhi.hi.platform.login.qq.QQUserInfo;
import com.whenhi.hi.platform.login.wechat.WechatUserInfo;
import com.whenhi.hi.platform.login.weibo.WeiboUserInfo;

/**
 * Created by 王雷 on 2017/1/12.
 */

public class Login {
    private int type; //1 代表qq登录 2代表微信登录 3代表微博登录
    private WechatUserInfo wechatUserInfo;
    private QQUserInfo qQUserInfo;
    private AuthResult authResult;
    private String errorMessage;
    private WeiboUserInfo weiboUserInfo;

    public WeiboUserInfo getWeiboUserInfo() {
        return weiboUserInfo;
    }

    public void setWeiboUserInfo(WeiboUserInfo weiboUserInfo) {
        this.weiboUserInfo = weiboUserInfo;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public WechatUserInfo getWechatUserInfo() {
        return wechatUserInfo;
    }

    public void setWechatUserInfo(WechatUserInfo wechatUserInfo) {
        this.wechatUserInfo = wechatUserInfo;
    }

    public QQUserInfo getqQUserInfo() {
        return qQUserInfo;
    }

    public void setqQUserInfo(QQUserInfo qQUserInfo) {
        this.qQUserInfo = qQUserInfo;
    }

    public AuthResult getAuthResult() {
        return authResult;
    }

    public void setAuthResult(AuthResult authResult) {
        this.authResult = authResult;
    }
}
