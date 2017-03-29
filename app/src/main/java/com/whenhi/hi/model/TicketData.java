package com.whenhi.hi.model;

/**
 * Created by 王雷 on 2017/3/6.
 */

public class TicketData {
    private int userId;
    private String token;
    private String adPicUrl;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getAdPicUrl() {
        return adPicUrl;
    }

    public void setAdPicUrl(String adPicUrl) {
        this.adPicUrl = adPicUrl;
    }
}
