package com.whenhi.hi.model;

import java.io.Serializable;

/**
 * Created by 王雷 on 2017/1/18.
 */

public class UpdateData implements Serializable {
    private String versionDesc;
    private String downloadUrl;
    private int upgradeType;

    public String getVersionDesc() {
        return versionDesc;
    }

    public void setVersionDesc(String versionDesc) {
        this.versionDesc = versionDesc;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public int getUpgradeType() {
        return upgradeType;
    }

    public void setUpgradeType(int upgradeType) {
        this.upgradeType = upgradeType;
    }
}
