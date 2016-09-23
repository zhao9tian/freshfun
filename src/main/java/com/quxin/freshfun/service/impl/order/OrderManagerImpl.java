package com.quxin.freshfun.service.impl.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.quxin.freshfun.dao.GoodsMapper;
import com.quxin.freshfun.dao.OrderDetailsMapper;
import com.quxin.freshfun.dao.OrdersMapper;
import com.quxin.freshfun.dao.ShoppingCartMapper;
import com.quxin.freshfun.model.GoodsPOJO;
import com.quxin.freshfun.model.OrderDetailsPOJO;
import com.quxin.freshfun.model.OrderStatusInfo;
import com.quxin.freshfun.model.ShoppingCartPOJO;
import com.quxin.freshfun.service.order.OrderManager;
import com.quxin.freshfun.utils.DateUtils;
import com.quxin.freshfun.utils.MoneyFormat;
@Service
public class OrderManagerImpl implements OrderManager {
	@Autowired
	private OrdersMapper order;
	@Autowired
	private OrderDetailsMapper orderDetails;
	@Autowired
	private ShoppingCartMapper cart;
	@Autowired
	private GoodsMapper goods;

	/**
	 * 根据订单编号删除订单信息
	 */
	@Override
	public int delOrder(String orderId) {
		Long date = DateUtils.getCurrentDate();
		return orderDetails.delOrder(date,orderId);
	}
	/**
	 * 根据用户编号查询所有订单信息
	 */
	@Override
	public List<OrderDetailsPOJO> findAll(Long orderId, int currentPage,
			int pageSize) {
		if(currentPage <= 0)
			currentPage = 1;
		int start = (currentPage - 1) * pageSize;
		List<OrderDetailsPOJO> orderList = orderDetails.selectAll(start, pageSize, orderId);
		for (OrderDetailsPOJO orderDetailsPOJO : orderList) {
			orderDetailsPOJO.setActualMoney(MoneyFormat.priceFormatString(orderDetailsPOJO.getActualPrice()));
			orderDetailsPOJO.setActualPrice(null);
		}
		return orderList;
	}
	/**
	 * 根据用户编号查询待付款订单
	 */
	@Override
	public List<OrderDetailsPOJO> selectPendingPaymentOrder(Long userId,Integer currentPage,Integer pageSize) {
		if(currentPage <= 0)
			currentPage = 1;
		int start = (currentPage - 1) * pageSize;
		List<OrderDetailsPOJO> orderDetailsList = orderDetails.selectPendingPaymentOrder(start, pageSize,userId);
		for (OrderDetailsPOJO orderDetailsPOJO : orderDetailsList) {
			orderDetailsPOJO.setActualMoney(MoneyFormat.priceFormatString(orderDetailsPOJO.getActualPrice()));
			orderDetailsPOJO.setActualPrice(null);
		}
		return orderDetailsList;
	}
	/**
	 * 根据用户编号查询待收货订单
	 */
	@Override
	public List<OrderDetailsPOJO> selectAwaitGoodsReceipt(Long userId,Integer currentPage,Integer pageSize) {
		if(currentPage <= 0)
			currentPage = 1;
		int start = (currentPage - 1) * pageSize;
		List<OrderDetailsPOJO> goodsReceipt = orderDetails.selectAwaitGoodsReceipt(start, pageSize,userId);
		for (OrderDetailsPOJO orderDetailsPOJO : goodsReceipt) {
			orderDetailsPOJO.setActualMoney(MoneyFormat.priceFormatString(orderDetailsPOJO.getActualPrice()));
			orderDetailsPOJO.setActualPrice(null);
		}
		return goodsReceipt;
	}
	/**
	 * 根据用户编号查询待发货订单
	 */
	@Override
	public List<OrderDetailsPOJO> selectAwaitDeliverOrder(Long userId,Integer currentPage,Integer pageSize) {
		if(currentPage <= 0)
			currentPage = 1;
		int start = (currentPage - 1) * pageSize;
		List<OrderDetailsPOJO> awaitDeliverOrder = orderDetails.selectAwaitDeliverOrder(start, pageSize,userId);
		for (OrderDetailsPOJO orderDetailsPOJO : awaitDeliverOrder) {
			orderDetailsPOJO.setActualMoney(MoneyFormat.priceFormatString(orderDetailsPOJO.getActualPrice()));
			orderDetailsPOJO.setActualPrice(null);
		}
		return awaitDeliverOrder;
	}
	/**
	 * 根据用户编号查询待评价订单
	 */
	@Override
	public List<OrderDetailsPOJO> selectAwaitComment(Long userId,Integer currentPage,Integer pageSize) {
		if(currentPage <= 0)
			currentPage = 1;
		int start = (currentPage - 1) * pageSize;
		List<OrderDetailsPOJO> awaitComment = orderDetails.selectAwaitComment(start, pageSize,userId);
		for (OrderDetailsPOJO orderDetailsPOJO : awaitComment) {
			orderDetailsPOJO.setActualMoney(MoneyFormat.priceFormatString(orderDetailsPOJO.getActualPrice()));
			orderDetailsPOJO.setActualPrice(null);
		}
		return awaitComment;
	}
	/**
	 * 根据用户编号查询退货订单
	 */
	@Override
	public List<OrderDetailsPOJO> selectCancelOrder(Long userId,Integer currentPage,Integer pageSize) {
		if(currentPage <= 0)
			currentPage = 1;
		int start = (currentPage - 1) * pageSize;
		List<OrderDetailsPOJO> cancelOrder = orderDetails.selectCancelOrder(start, pageSize,userId);
		for (OrderDetailsPOJO orderDetailsPOJO : cancelOrder) {
			orderDetailsPOJO.setActualMoney(MoneyFormat.priceFormatString(orderDetailsPOJO.getActualPrice()));
			orderDetailsPOJO.setActualPrice(null);
		}
		return cancelOrder;
	}
	/**
	 * 根据用户编号查询购物车信息
	 */
	@Override
	public Map<String,Object> selectShoppingCartByUserId(Long userId) {
		List<ShoppingCartPOJO> carts = cart.selectShoppingCartByUserId(userId);
		for (ShoppingCartPOJO sc : carts) {
			GoodsPOJO goods = sc.getGoods();
			String shopingMoney = MoneyFormat.priceFormatString(goods.getShopPrice());
			String marketMoney = MoneyFormat.priceFormatString(goods.getMarketPrice());
			goods.setGoodsMoney(shopingMoney);
			goods.setMarketMoney(marketMoney);
			String totalMoney = MoneyFormat.priceFormatString(sc.getGoodsTotalsPrice());
			sc.setGoodsTotalMoney(totalMoney);
		}
		//推荐商品
		List<GoodsPOJO> recommendGoods = goods.selectRecommendGoods();
		for (GoodsPOJO goodsPOJO : recommendGoods) {
			goodsPOJO.setGoodsMoney(MoneyFormat.priceFormatString(goodsPOJO.getShopPrice()));
			goodsPOJO.setMarketMoney(MoneyFormat.priceFormatString(goodsPOJO.getMarketPrice()));
			goodsPOJO.setShopPrice(null);
			goodsPOJO.setMarketPrice(null);
		}
		Map<String,Object> map = Maps.newHashMap();
		map.put("cart", carts);
		map.put("recommendGoods", recommendGoods);
		
		return map;
	}
	@Override
	public Integer addShoppingCart(Long userId, Integer goodsId) {
		Integer existeStatus = cart.selectExistedOrder(userId, goodsId);
		if(existeStatus > 0){
			return 0;
		}
		Long currentDate = DateUtils.getCurrentDate();
		//查询单个商品价格
		GoodsPOJO goodsInfo = goods.selectShoppingInfo(goodsId);
		ShoppingCartPOJO shoppingCart = new ShoppingCartPOJO();
		shoppingCart.setUserId(userId);
		shoppingCart.setGoodsId(goodsId);
		shoppingCart.setGoodsTotals(1);
		shoppingCart.setGoodsTotalsPrice(goodsInfo.getShopPrice());
		shoppingCart.setGoodsName(goodsInfo.getGoodsName());
		shoppingCart.setCreateDate(currentDate);
		shoppingCart.setUpdateDate(currentDate);
		int status = cart.insertSelective(shoppingCart);
		return status;
	}
	@Override
	public Integer selectGoodsTotals(Integer scId) {
		return cart.selectGoodsTotals(scId);
	}
	@Override
	public Integer addGoodsTotals(Integer scId) {
		Integer totals = cart.selectGoodsTotals(scId);
		if(totals >= 99){
			return 99;
		}else{
			cart.addGoodsTotals(scId);
			totals = totals + 1;
		}
		return totals;
	}
	@Override
	public Integer reduceGoodsTotals(Integer scId) {
		Integer totals = cart.selectGoodsTotals(scId);
		if(totals <= 1){
			return 1;
		}else{
			cart.reduceGoodsTotals(scId);
			totals = totals - 1;
		}
		return totals;
	}
	
	@Override
	public OrderDetailsPOJO selectSigleOrder(String orderDetailsId) {
		List<OrderDetailsPOJO> sigleOrder = orderDetails.selectSigleOrder(orderDetailsId);
		if(sigleOrder != null && sigleOrder.size() > 0){
			OrderDetailsPOJO detailsPOJO = sigleOrder.get(0);
			detailsPOJO.setActualMoney(MoneyFormat.priceFormatString(detailsPOJO.getActualPrice()));
			detailsPOJO.setActualPrice(null);
			return detailsPOJO;
		}
		return null;
	}
	@Override
	public int confirmGoodsReceipt(String orderId) {
		return orderDetails.confirmGoodsReceipt(orderId);
	}
	
	/**
     * 确认评价
     * @param orderId
     * @return
     */
	@Override
	public int confirmGoodsComment(String orderId) {
		return orderDetails.confirmGoodsComment(orderId);
	}
	
	/**
     * 查询所有未退款订单状态数量
     * @param userID
     * @return
     */
	
	@Override
	public List<OrderStatusInfo> selectStatusCounts(Long userID) {
		// TODO Auto-generated method stub
		return orderDetails.selectStatusCounts(userID);
	}
	
	/**
     * 查询退款订单数量
     * @param userID
     * @return
     */
	
	@Override
	public List<OrderStatusInfo> selectRefundCounts(Long userID) {
		return orderDetails.selectRefundCounts(userID);
	}
	
	/**
     * 查询未付款订单数量
     * @param userID
     * @return
     */
	@Override
	public List<OrderStatusInfo> selectPayCounts(Long userID) {
		return orderDetails.selectPayCounts(userID);
	}
	@Override
	public Integer confirmReceipt(String orderDetailId) {
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("reciveTime", System.currentTimeMillis()/1000);
		map.put("orderDetailId", orderDetailId);
		return orderDetails.updateOrderStatus(map);
	}
	
	@Override
	public Integer delShoppingCartOrder(Integer scId) {
		Long currentDate = DateUtils.getCurrentDate();
		return cart.delShoppingCartOrder(scId, currentDate);
	}
	@Override
	public Integer applyRefund(String orderDetailId) {
		return orderDetails.applyRefund(orderDetailId);
	}

}
