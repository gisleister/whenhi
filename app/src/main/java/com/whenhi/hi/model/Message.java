package com.whenhi.hi.model;

/**
 * Created by 王雷 on 2016/12/25.
 */

public class Message {
    private int id;
    private long createTime;
    private String createTimeNice;
    private int userId;
    private String userName;
    private String userLogo;
    private int status;
    private String content;
    private int msgCategory;
    private String msgExtra;



    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getCreateTimeNice() {
        return createTimeNice;
    }

    public void setCreateTimeNice(String createTimeNice) {
        this.createTimeNice = createTimeNice;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserLogo() {
        return userLogo;
    }

    public void setUserLogo(String userLogo) {
        this.userLogo = userLogo;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getMsgCategory() {
        return msgCategory;
    }

    public void setMsgCategory(int msgCategory) {
        this.msgCategory = msgCategory;
    }

    public String getMsgExtra() {
        return msgExtra;
    }

    public void setMsgExtra(String msgExtra) {
        this.msgExtra = msgExtra;
    }
}
