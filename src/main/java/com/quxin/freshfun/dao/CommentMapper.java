package com.quxin.freshfun.dao;

import com.quxin.freshfun.model.pojo.CommentPOJO;

/**
 * 评价
 * created by paul
 */
public interface CommentMapper {

    /**
     * 插入评论
     * @param commentPOJO
     */
    Integer insert(CommentPOJO commentPOJO);

    /**
     * 获取评价详情
     * @param orderId
     * @return
     */
    CommentPOJO selectCommentDetailByOrderId(Long orderId);

}
