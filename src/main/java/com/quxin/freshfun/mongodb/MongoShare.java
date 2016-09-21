package com.quxin.freshfun.mongodb;

import org.springframework.stereotype.Repository;

import com.quxin.freshfun.model.Share;
@Repository
public interface MongoShare {
	int addShareLink(Share share);
}
