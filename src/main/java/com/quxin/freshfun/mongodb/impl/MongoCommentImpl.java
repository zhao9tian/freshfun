package com.quxin.freshfun.mongodb.impl;

import java.util.List;

import com.quxin.freshfun.dao.CommentDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.quxin.freshfun.model.Comment;
import com.quxin.freshfun.mongodb.MongoComment;
@Repository
public class MongoCommentImpl implements MongoComment {

	@Autowired
	private MongoTemplate mongoTemplate;

	@Autowired
	private CommentDetailsMapper commentDetailsMapper;

	@Override
	public List<Comment> findComment(Query query) {
		return mongoTemplate.find(query, Comment.class, "comment");
	}

	@Override
	public void addComment(Comment comment) {
		commentDetailsMapper.insertComment(comment);
	}

}
