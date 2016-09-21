package com.quxin.freshfun.mongodb.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.quxin.freshfun.model.ShoppingCart;
import com.quxin.freshfun.mongodb.MongoShoppingCart;
@Repository
public class MongoShoppingCartImpl implements MongoShoppingCart {
	
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public List<ShoppingCart> findShoppingCart(Query query) {
		return mongoTemplate.find(query, ShoppingCart.class, "shopping_cart");
	}

}
