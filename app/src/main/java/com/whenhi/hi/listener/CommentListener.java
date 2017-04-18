package com.whenhi.hi.listener;

import com.whenhi.hi.model.Comment;

/**
 * Created by 王雷 on 2017/2/21.
 */

public interface CommentListener<C> {
    void commentSuccess(Comment comment);
}
