package com.whenhi.hi.receiver;

import com.whenhi.hi.listener.CommentListener;
import com.whenhi.hi.listener.LoginListener;
import com.whenhi.hi.listener.NoticeListener;

/**
 * Created by 王雷 on 2017/1/20.
 */

public class NoticeTransfer {

    private static NoticeListener mNoticeListener;
    private static LoginListener mLoginListener;
    private static CommentListener mCommentListener;



    public static void login(boolean is){
        if(mLoginListener != null){
            mLoginListener.login(is);
        }

    }

    public static void logout(boolean is){
        if(mLoginListener != null){
            mLoginListener.logout(is);
        }

    }

    public static void refresh(){
        if(mLoginListener != null){
            mLoginListener.refresh();
        }

    }

    public static void refreshMeesage(){
        if(mNoticeListener != null){
            mNoticeListener.refreshMeesage();//通知刷新list
        }

    }

    public static void commentSuccess(){
        if(mCommentListener != null){
            mCommentListener.commentSuccess();
        }

    }

    public static void mobile(String mobile){
        if(mLoginListener != null){
            mLoginListener.mobile(mobile);
        }
    }


    public void setNoticeListener(NoticeListener noticeListener) {
        this.mNoticeListener = noticeListener;
    }


    public void setLoginListener(LoginListener loginListener) {
        this.mLoginListener = loginListener;
    }

    public void setCommentListener(CommentListener commentListener) {
        this.mCommentListener = commentListener;
    }


}
