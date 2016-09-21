package com.quxin.freshfun.mongodb;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.quxin.freshfun.model.Comment;

@Repository
public interface MongoComment {
	
	public List<Comment> findComment(Query query);
	

	public void addComment(Comment comment);

}
