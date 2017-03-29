package com.whenhi.hi.model;

import java.io.Serializable;
import java.util.List;

/**
 * Created by 王雷 on 2017/1/13.
 */

public class NotificationModel implements Serializable {
    private int launchApp;
    private int uid;
    List<NotificationData> data;

    public int getLaunchApp() {
        return launchApp;
    }

    public void setLaunchApp(int launchApp) {
        this.launchApp = launchApp;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public List<NotificationData> getData() {
        return data;
    }

    public void setData(List<NotificationData> data) {
        this.data = data;
    }
}
