package com.whenhi.hi.platform.share;

/**
 * Created by 王雷 on 2017/1/6.
 */

public interface IShareListener {

    /** Success. */
    int CODE_SUCCESS = 0x00;
    /** Failed. */
    int CODE_FAILED = 0x01;
    /** Shareing. */
    int CODE_SHARE_ING = 0x02;
    /** Cancel . */
    int CODE_CANCEL_SHARE = 0x03;


    /**
     *
     * @param code
     * @param data
     */
    void shareStatus(int code, Object data);
}
