package com.quxin.freshfun.mongodb;

import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import com.quxin.freshfun.model.ShoppingCart;
@Repository
public interface MongoShoppingCart {
	
	
	public List<ShoppingCart> findShoppingCart(Query query);

}
