package com.quxin.freshfun.mongodb.impl;

import java.util.List;

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

	@Override
	public List<Comment> findComment(Query query) {
		// TODO Auto-generated method stub
		return mongoTemplate.find(query, Comment.class, "comment");
	}

	@Override
	public void addComment(Comment comment) {
		// TODO Auto-generated method stub
		mongoTemplate.save(comment, "comment");
	}

}
