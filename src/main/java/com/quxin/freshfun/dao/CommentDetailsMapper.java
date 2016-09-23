package com.quxin.freshfun.dao;

import com.quxin.freshfun.model.Comment;

/**
 * Created by TuZl on 2016/9/23.
 */
public interface CommentDetailsMapper {

    /**
     * 插入评论
     * @param comment
     */
    void insertComment(Comment comment);
}
