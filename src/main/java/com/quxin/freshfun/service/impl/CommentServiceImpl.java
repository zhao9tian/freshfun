package com.quxin.freshfun.service.impl;

import com.quxin.freshfun.dao.CommentDetailsMapper;
import com.quxin.freshfun.model.Comment;
import com.quxin.freshfun.service.CommentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service("commentService")
public class CommentServiceImpl implements CommentService {

	@Autowired
	private CommentDetailsMapper commentDetailsMapper;

	@Override
	public void addComment(Comment comment) {
		commentDetailsMapper.insertComment(comment);
	}

}
