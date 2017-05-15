package com.whenhi.hi.model;

/**
 * Created by 王雷 on 2017/1/6.
 */

public class LuckModel {
    private int state;
    private Go data;
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

    public Go getData() {
        return data;
    }

    public void setData(Go data) {
        this.data = data;
    }

    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

}
