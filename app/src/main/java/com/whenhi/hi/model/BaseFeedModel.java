package com.whenhi.hi.model;

/**
 * Created by 王雷 on 2017/1/6.
 */

public class BaseFeedModel {
    private int state;
    private Feed data;
    private String msgText;
    private int msgCode;


    public int getMsgCode() {
        return msgCode;
    }

    public void setMsgCode(int msgCode) {
        this.msgCode = msgCode;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public Feed getData() {
        return data;
    }

    public void setData(Feed data) {
        this.data = data;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

}
