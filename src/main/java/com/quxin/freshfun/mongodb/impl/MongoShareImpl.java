package com.quxin.freshfun.mongodb.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.quxin.freshfun.model.Share;
import com.quxin.freshfun.mongodb.MongoShare;
@Repository
public class MongoShareImpl implements MongoShare {
	@Autowired
	private MongoTemplate mongoTemplate;
	@Override
	public int addShareLink(Share share) {
		mongoTemplate.save(share, "share");
		return 1;
	}

}
