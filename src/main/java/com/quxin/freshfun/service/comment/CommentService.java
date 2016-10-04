package com.quxin.freshfun.service.comment;

import com.quxin.freshfun.model.pojo.CommentPOJO;

public interface CommentService {
	
	public Integer addComment(CommentPOJO commentPOJO);

	public CommentPOJO queryCommentDetailByOrderId(Long orderId);

}
