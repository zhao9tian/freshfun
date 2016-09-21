package com.quxin.freshfun.controller.goods;

import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quxin.freshfun.model.ShoppingCart;
import com.quxin.freshfun.service.ShoppingCartService;




@Controller
@RequestMapping("/shoppingcart")
public class ShoppingCartPage {
	@Autowired
	private ShoppingCartService shoppingCart;
	
	/**
	 * 查询购物车信息
	 * @return 
	 */
	
	@RequestMapping("/show")
	@ResponseBody
	public List<ShoppingCart> findAllGoods(String userID,HttpServletResponse response){
		response.setHeader("Access-Control-Allow-Origin", "*");
		System.out.println(userID);
		return shoppingCart.findShoppingCart(userID);
	}
	

}
