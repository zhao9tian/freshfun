package com.quxin.freshfun.mongodb;

import java.util.List;

import org.springframework.stereotype.Repository;

import com.quxin.freshfun.model.Share;
@Repository
public interface TestDao {
	public List<Share> findAll();
}
