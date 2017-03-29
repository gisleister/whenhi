package com.whenhi.hi.model;

/**
 * Created by 王雷 on 2017/2/28.
 */

public class ChargeRecord {
    private int cardValue;
    private int status;

    private String createTime;
    private String finishTime;
    private String createTimeNice;


    public int getCardValue() {
        return cardValue;
    }

    public void setCardValue(int cardValue) {
        this.cardValue = cardValue;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public String getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(String finishTime) {
        this.finishTime = finishTime;
    }

    public String getCreateTimeNice() {
        return createTimeNice;
    }

    public void setCreateTimeNice(String createTimeNice) {
        this.createTimeNice = createTimeNice;
    }
}
