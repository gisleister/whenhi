package com.whenhi.hi.model;

import java.io.Serializable;

/**
 * Created by 王雷 on 2017/1/21.
 */

public class NotificationData implements Serializable {
    private String type;
    private String subtype;
    private Notification data;


    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public Notification getData() {
        return data;
    }

    public void setData(Notification data) {
        this.data = data;
    }
}
