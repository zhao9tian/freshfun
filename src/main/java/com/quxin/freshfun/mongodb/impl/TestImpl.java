package com.quxin.freshfun.mongodb.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Repository;

import com.quxin.freshfun.model.Share;
import com.quxin.freshfun.mongodb.TestDao;
@Repository
public class TestImpl implements TestDao {
	@Autowired
	private MongoTemplate mongoTemplate;
	@Override
	public List<Share> findAll() {
		List<Share> log = mongoTemplate.findAll(Share.class);
		return log;
	}
	

}
