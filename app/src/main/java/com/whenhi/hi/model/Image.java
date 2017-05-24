package com.whenhi.hi.model;

import java.io.Serializable;

/**
 * Created by 王雷 on 2017/1/6.
 */

public class Image implements Serializable {
    private int id;
    private int feedId;
    private int feedCategory;
    private int resType;
    private String content;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFeedId() {
        return feedId;
    }

    public void setFeedId(int feedId) {
        this.feedId = feedId;
    }

    public int getResType() {
        return resType;
    }

    public void setResType(int resType) {
        this.resType = resType;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getFeedCategory() {
        return feedCategory;
    }

    public void setFeedCategory(int feedCategory) {
        this.feedCategory = feedCategory;
    }
}
