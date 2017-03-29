package com.whenhi.hi.model;

/**
 * Created by 王雷 on 2017/1/6.
 */

public class ChargeModel {
    private int state;
    private String msgText;
    private int msgCode;
    private ChargeData data;

    public ChargeData getData() {
        return data;
    }

    public void setData(ChargeData data) {
        this.data = data;
    }

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


    public String getMsgText() {
        return msgText;
    }

    public void setMsgText(String msgText) {
        this.msgText = msgText;
    }

}
