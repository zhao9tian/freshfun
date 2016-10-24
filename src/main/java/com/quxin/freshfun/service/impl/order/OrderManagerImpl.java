package com.quxin.freshfun.service.impl.order;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.quxin.freshfun.model.outparam.UserInfoOutParam;
import com.quxin.freshfun.model.param.FlowParam;
import com.quxin.freshfun.service.flow.FlowService;
import com.quxin.freshfun.service.user.UserBaseService;
import com.quxin.freshfun.utils.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.quxin.freshfun.dao.GoodsMapper;
import com.quxin.freshfun.dao.OrderDetailsMapper;
import com.quxin.freshfun.dao.ShoppingCartMapper;
import com.quxin.freshfun.model.GoodsPOJO;
import com.quxin.freshfun.model.OrderDetailsPOJO;
import com.quxin.freshfun.model.OrderStatusInfo;
import com.quxin.freshfun.model.ShoppingCartPOJO;
import com.quxin.freshfun.service.order.OrderManager;
import com.quxin.freshfun.utils.DateUtils;
import com.quxin.freshfun.utils.MoneyFormat;
import org.springframework.util.StringUtils;

@Service("orderManager")
public class OrderManagerImpl implements OrderManager {
	@Autowired
	private OrderDetailsMapper orderDetailsMapper;
	@Autowired
	private ShoppingCartMapper shoppingCartMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private UserBaseService userBaseService;
	@Autowired
	private FlowService flowService;

	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 根据订单编号删除订单信息
	 */
	@Override
	public int delOrder(String orderId) {
		Long date = DateUtils.getCurrentDate();
		return orderDetailsMapper.delOrder(date,orderId);
	}
	/**
	 * 根据用户编号查询所有订单信息
	 */
	@Override
	public List<OrderDetailsPOJO> findAll(Long userId, int currentPage,
										  int pageSize) {
		if (currentPage <= 0)
			currentPage = 1;
		int start = (currentPage - 1) * pageSize;
		List<OrderDetailsPOJO> orderList = orderDetailsMapper.selectAll(start, pageSize, userId);
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
		List<OrderDetailsPOJO> orderDetailsList = orderDetailsMapper.selectPendingPaymentOrder(start, pageSize,userId);
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
		List<OrderDetailsPOJO> goodsReceipt = orderDetailsMapper.selectAwaitGoodsReceipt(start, pageSize,userId);
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
		List<OrderDetailsPOJO> awaitDeliverOrder = orderDetailsMapper.selectAwaitDeliverOrder(start, pageSize,userId);
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
		List<OrderDetailsPOJO> awaitComment = orderDetailsMapper.selectAwaitComment(start, pageSize,userId);
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
		List<OrderDetailsPOJO> cancelOrder = orderDetailsMapper.selectCancelOrder(start, pageSize,userId);
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
		List<ShoppingCartPOJO> carts = shoppingCartMapper.selectShoppingCartByUserId(userId);
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
		List<GoodsPOJO> recommendGoods = goodsMapper.selectRecommendGoods();
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
		List<Integer> list = shoppingCartMapper.selectExistedOrder(userId, goodsId);
		if(list.size() > 0){
			//修改购物车数量
			for (Integer id: list) {
				System.out.println(id);
				int status = shoppingCartMapper.addGoodsTotals(id);
				if(status <= 0){
					return 0;
				}else{
					return 1;
				}
			}
		}
		Long currentDate = DateUtils.getCurrentDate();
		//查询单个商品价格
		GoodsPOJO goodsInfo = goodsMapper.selectShoppingInfo(goodsId);
		ShoppingCartPOJO shoppingCart = new ShoppingCartPOJO();
		shoppingCart.setUserId(userId);
		shoppingCart.setGoodsId(goodsId);
		shoppingCart.setGoodsTotals(1);
		shoppingCart.setGoodsTotalsPrice(goodsInfo.getShopPrice());
		shoppingCart.setGoodsName(goodsInfo.getGoodsName());
		shoppingCart.setCreateDate(currentDate);
		shoppingCart.setUpdateDate(currentDate);
		int status = shoppingCartMapper.insertSelective(shoppingCart);
		return status;
	}
	@Override
	public Integer selectGoodsTotals(Integer scId) {
		return shoppingCartMapper.selectGoodsTotals(scId);
	}
	@Override
	public Integer addGoodsTotals(Integer scId) {
		Integer totals = shoppingCartMapper.selectGoodsTotals(scId);
		if(totals >= 99){
			return 99;
		}else{
			shoppingCartMapper.addGoodsTotals(scId);
			totals = totals + 1;
		}
		return totals;
	}
	@Override
	public Integer reduceGoodsTotals(Integer scId) {
		Integer totals = shoppingCartMapper.selectGoodsTotals(scId);
		if(totals <= 1){
			return 1;
		}else{
			shoppingCartMapper.reduceGoodsTotals(scId);
			totals = totals - 1;
		}
		return totals;
	}

	@Override
	public OrderDetailsPOJO selectSigleOrder(Long orderDetailsId) {
		List<OrderDetailsPOJO> sigleOrder = orderDetailsMapper.selectSigleOrder(orderDetailsId);
		if(sigleOrder != null && sigleOrder.size() > 0){
			OrderDetailsPOJO detailsPOJO = sigleOrder.get(0);
			detailsPOJO.setActualMoney(MoneyFormat.priceFormatString(detailsPOJO.getActualPrice()));
			detailsPOJO.setActualPrice(null);
			return detailsPOJO;
		}
		return null;
	}

	/**
	 * 确认收货
	 * @param orderId
	 * @return
	 */
	@Override
	public Integer addConfirmGoodsReceipt(String orderId) throws BusinessException {
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("reciveTime", System.currentTimeMillis()/1000);
		map.put("orderDetailId", orderId);
		//修改订单状态
		Integer returnNum = orderDetailsMapper.updateOrderStatus(map);
		if(returnNum <= 0){
			throw new BusinessException("修改确认收货状态失败");
		}
		//查询订单信息
		/*OrderDetailsPOJO orderDetails = orderDetailsMapper.selectConfirmOrderInfo(orderId);
		if(orderDetails.getAgentId() !=0){
			//添加账单信息
			Long currentDate = DateUtils.getCurrentDate();
			FlowParam flowParam = new FlowParam();
			flowParam.setOrderId(orderDetails.getId());
			flowParam.setUserId(orderDetails.getAgentId());
			flowParam.setCreated(currentDate);
			flowParam.setUpdated(currentDate);
			flowParam.setAgentFlow(orderDetails.getAgentPrice().longValue());
			flowService.add(flowParam);
		}
		if(orderDetails.getFetcherId() != 0){
			//添加账单信息
			Long currentDate = DateUtils.getCurrentDate();
			FlowParam flowParam = new FlowParam();
			flowParam.setOrderId(orderDetails.getId());
			flowParam.setUserId(orderDetails.getFetcherId());
			flowParam.setCreated(currentDate);
			flowParam.setUpdated(currentDate);
//			flowParam.setAgentFlow(orderDetails.getFetcherPrice().longValue());
			flowParam.setFetcherFlow(orderDetails.getFetcherPrice().longValue());
			flowService.add(flowParam);
		}*/

		return returnNum;
	}

	@Override
	public Integer autoConfirmDelivery(String orderId) throws BusinessException {
		logger.info("订单自动进入确认收货流程");
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("reciveTime", System.currentTimeMillis()/1000);
		map.put("orderDetailId", orderId);
		//修改订单状态
		Integer returnNum = orderDetailsMapper.updateOrderStatus(map);
		if(returnNum <= 0){
			throw new BusinessException("修改确认收货状态失败");
		}
		return returnNum;
	}

	/**
	 * 确认评价
	 * @param orderId
	 * @return
	 */
	@Override
	public int confirmGoodsComment(String orderId) {
		return orderDetailsMapper.confirmGoodsComment(orderId);
	}

	/**
	 * 查询待评价数量
	 * @param userId
	 * @return
	 */
	@Override
	public Integer selectCommentCount(Long userId) {
		return orderDetailsMapper.selectCommentCount(userId);
	}
	/**
	 * 查询所有未退款订单状态数量
	 * @param userId
	 * @return
	 */

	@Override
	public List<OrderStatusInfo> selectStatusCounts(Long userId) {
		return orderDetailsMapper.selectStatusCounts(userId);
	}

	/**
	 * 查询退款订单数量
	 * @param userId
	 * @return
	 */

	@Override
	public List<OrderStatusInfo> selectRefundCounts(Long userId) {
		return orderDetailsMapper.selectRefundCounts(userId);
	}


	/**
	 * 查询未付款订单数量
	 * @param userId
	 * @return
	 */
	@Override
	public Integer selectPayCounts(Long userId) {
		return orderDetailsMapper.selectPayCounts(userId);
	}
	@Override
	public Integer confirmReceipt(String orderDetailId) {
		Map<String , Object> map = new HashMap<String, Object>();
		map.put("reciveTime", System.currentTimeMillis()/1000);
		map.put("orderDetailId", orderDetailId);
		return orderDetailsMapper.updateOrderStatus(map);
	}

	@Override
	public Integer delShoppingCartOrder(Integer scId) {
		Long currentDate = DateUtils.getCurrentDate();
		return shoppingCartMapper.delShoppingCartOrder(scId, currentDate);
	}
	@Override
	public Long applyRefund(String orderDetailId) {
		return orderDetailsMapper.applyRefund(orderDetailId);
	}



	//订单后台
	@Override
	public List<OrderDetailsPOJO> selectBackstageOrderClose(int currentPage, int pageSize) {
		return orderDetailsMapper.selectBackstageOrderClose(currentPage,pageSize);
	}

	@Override
	public List<OrderDetailsPOJO> selectBackstageOrders(int currentPage, int pageSize) {
		return orderDetailsMapper.selectBackstageOrders(currentPage,pageSize);
	}

	@Override
	public List<OrderDetailsPOJO> selectBackstagePendingPaymentOrder(int currentPage, int pageSize) {
		return orderDetailsMapper.selectBackstagePendingPaymentOrder(currentPage,pageSize);
	}

	@Override
	public List<OrderDetailsPOJO> selectBackstageAwaitDeliverOrder(int currentPage, int pageSize) {
		return orderDetailsMapper.selectBackstageAwaitDeliverOrder(currentPage,pageSize);
	}

	@Override
	public List<OrderDetailsPOJO> selectBackstageAwaitGoodsReceipt(int currentPage, int pageSize) {
		return orderDetailsMapper.selectBackstageAwaitGoodsReceipt(currentPage,pageSize);
	}

	@Override
	public List<OrderDetailsPOJO> selectFinishOrder(int currentPage, int pageSize) {
		return orderDetailsMapper.selectFinishOrder(currentPage,pageSize);
	}

	@Override
	public Integer selectBackstageOrderCloseCount() {
		return orderDetailsMapper.selectBackstageOrderCloseCount();
	}

	@Override
	public Integer selectBackstageOrdersCount() {
		return orderDetailsMapper.selectBackstageOrdersCount();
	}

	@Override
	public Integer selectBackstagePendingPaymentOrderCount() {
		return orderDetailsMapper.selectBackstagePendingPaymentOrderCount();
	}

	@Override
	public Integer selectBackstageAwaitDeliverOrderCount() {
		return orderDetailsMapper.selectBackstageAwaitDeliverOrderCount();
	}

	@Override
	public Integer selectBackstageAwaitGoodsReceiptCount() {
		return orderDetailsMapper.selectBackstageAwaitGoodsReceiptCount();
	}

	@Override
	public Integer selectFinishOrderCount() {
		return orderDetailsMapper.selectFinishOrderCount();
	}

	@Override
	public Integer deliverOrder(OrderDetailsPOJO order) {
		if(StringUtils.isEmpty(order.getActualMoney()) || StringUtils.isEmpty(order.getOrderId())) {
			return 0;
		}
		Long currentDate = DateUtils.getCurrentDate();
		Map<String,Object> map = Maps.newHashMap();
		map.put("orderId",order.getOrderId());
		map.put("deliveryNum",order.getDeliveryNum());
		map.put("deliveryName",order.getDeliveryName());
		map.put("deliveryTime",currentDate);
		Double goodsCost = Double.parseDouble(order.getActualMoney())*100;
		map.put("goodsCost",goodsCost.intValue());
		map.put("deliveryRemark",order.getDeliveryRemark());
		return orderDetailsMapper.deliverOrder(map);
	}

	@Override
	public Integer orderRemark(Long orderId,String remark) {
		Map<String,Object> map = Maps.newHashMap();
		map.put("remark",remark);
		map.put("orderId",orderId);
		return orderDetailsMapper.orderRemark(map);
	}

	@Override
	public List<OrderDetailsPOJO> autoConfirmDelivery() {
		return orderDetailsMapper.selectUnConfirmOrder();
	}

	@Override
	public OrderDetailsPOJO selectConfirmOrderInfo(String orderId) {
		return orderDetailsMapper.selectConfirmOrderInfo(orderId);
	}


}
