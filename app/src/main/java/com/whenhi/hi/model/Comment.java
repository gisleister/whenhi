package com.whenhi.hi.model;

import java.io.Serializable;

/**
 * Created by 王雷 on 2016/12/27.
 */

public class Comment implements Serializable {

   private int id;
    private int targetId;
    private int feedId;
    private int targetType;
    private String content;
    private long createTime;
    private String targetUserName;
    private int userId;
    private String userName;
    private String userLogo;
    private String pubTimeNice;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getTargetId() {
        return targetId;
    }

    public void setTargetId(int targetId) {
        this.targetId = targetId;
    }

    public int getFeedId() {
        return feedId;
    }

    public void setFeedId(int feedId) {
        this.feedId = feedId;
    }

    public int getTargetType() {
        return targetType;
    }

    public void setTargetType(int targetType) {
        this.targetType = targetType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getTargetUserName() {
        return targetUserName;
    }

    public void setTargetUserName(String targetUserName) {
        this.targetUserName = targetUserName;
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

    public String getPubTimeNice() {
        return pubTimeNice;
    }

    public void setPubTimeNice(String pubTimeNice) {
        this.pubTimeNice = pubTimeNice;
    }
}
