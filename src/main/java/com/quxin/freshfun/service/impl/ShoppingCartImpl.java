package com.quxin.freshfun.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import com.quxin.freshfun.model.ShoppingCart;
import com.quxin.freshfun.mongodb.MongoShoppingCart;
import com.quxin.freshfun.service.ShoppingCartService;

@Service
public class ShoppingCartImpl implements ShoppingCartService {
	
	@Autowired
	private MongoShoppingCart shoppingCart;

	@Override
	public List<ShoppingCart> findShoppingCart(String userID) {
		Query query = Query.query(Criteria.where("user_id").is(userID));
		System.out.println(query);
		return shoppingCart.findShoppingCart(query);
	}

}
