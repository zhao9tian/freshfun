package com.quxin.freshfun.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.quxin.freshfun.model.ShoppingCart;


@Service
public interface ShoppingCartService {
	
	/**
	 * 查询购物车数据数据
	 */
	public List<ShoppingCart> findShoppingCart(String userId);

}
