package com.whenhi.hi.model;

import java.io.Serializable;

/**
 * Created by 王雷 on 2017/1/19.
 */

public class UserIncomeRecord implements Serializable {
    private int userId;
    private long createTime;
    private String categoryTitle;
    private int growValue;
    private int score;
    private int growCategory;
    private String createTimeNice;


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

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }

    public int getGrowValue() {
        return growValue;
    }

    public void setGrowValue(int growValue) {
        this.growValue = growValue;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int getGrowCategory() {
        return growCategory;
    }

    public void setGrowCategory(int growCategory) {
        this.growCategory = growCategory;
    }
}
