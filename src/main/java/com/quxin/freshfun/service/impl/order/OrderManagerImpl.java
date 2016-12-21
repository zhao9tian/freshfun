package com.quxin.freshfun.service.impl.order;

import java.util.*;

import com.quxin.freshfun.dao.GoodsBaseMapper;
import com.quxin.freshfun.dao.PromotionMapper;
import com.quxin.freshfun.model.goods.PromotionGoodsPOJO;
import com.quxin.freshfun.model.param.GoodsParam;
import com.quxin.freshfun.model.pojo.PromotionPOJO;
import com.quxin.freshfun.model.pojo.goods.GoodsBasePOJO;
import com.quxin.freshfun.service.address.AddressUtilService;
import com.quxin.freshfun.service.promotion.PromotionService;
import com.quxin.freshfun.utils.BusinessException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.quxin.freshfun.dao.OrderDetailsMapper;
import com.quxin.freshfun.dao.ShoppingCartMapper;
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
	private GoodsBaseMapper goodsBaseMapper;
	@Autowired
	private AddressUtilService addressUtilService;
	@Autowired
	private PromotionService promotionService;

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
	 * 获取用户地址
	 * @param orderDetailsPOJO 订单实体
	 */
	private void getAddress(OrderDetailsPOJO orderDetailsPOJO) {
		if(StringUtils.isEmpty(orderDetailsPOJO.getCity())){
			String city = addressUtilService.queryNameByCode(orderDetailsPOJO.getProvCode(), orderDetailsPOJO.getCityCode(), orderDetailsPOJO.getDistCode());
			orderDetailsPOJO.setCity(city);
		}
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
	public Map<String,Object> selectShoppingCartByUserId(Long userId) throws BusinessException {
		List<ShoppingCartPOJO> carts = shoppingCartMapper.selectShoppingCartByUserId(userId);
		//判断商品是否属于限量购商品
		if(carts != null && carts.size() > 0) {
			List<PromotionGoodsPOJO> promotionList = promotionService.queryCartLimitedGoods(carts);
			for (ShoppingCartPOJO cartGoods : carts) {
				for (PromotionGoodsPOJO promotion : promotionList) {
					if (cartGoods.getGoodsId().equals(promotion.getGoodsId().intValue())) {
						cartGoods.getGoods().setShopPrice(promotion.getDiscountPrice().intValue());
					}
				}
			}
		}
		for (ShoppingCartPOJO sc : carts) {
			GoodsParam goods = sc.getGoods();
			String shoppingMoney = MoneyFormat.priceFormatString(goods.getShopPrice());
			String marketMoney = MoneyFormat.priceFormatString(goods.getMarketPrice());
			goods.setGoodsMoney(shoppingMoney);
			goods.setMarketMoney(marketMoney);
			String totalMoney = MoneyFormat.priceFormatString(sc.getGoodsTotalsPrice());
			sc.setGoodsTotalMoney(totalMoney);
		}
		//推荐商品
		List<GoodsBasePOJO> goodsBaseList = goodsBaseMapper.findRecommendGoods();
		List<GoodsParam> recommendGoods = new ArrayList<>();
		for (GoodsBasePOJO goodsBase: goodsBaseList) {
			GoodsParam goodsParam = new GoodsParam();
			goodsParam.setId(goodsBase.getId());
            goodsParam.setGoodsImg(goodsBase.getGoodsImg());
            goodsParam.setGoodsDes(goodsBase.getSubTitle());
			goodsParam.setGoodsName(goodsBase.getTitle());
            goodsParam.setShopPrice(goodsBase.getShopPrice());
            goodsParam.setMarketPrice(goodsBase.getOriginPrice());
			goodsParam.setGoodsMoney(MoneyFormat.priceFormatString(goodsBase.getShopPrice()));
			goodsParam.setMarketMoney(MoneyFormat.priceFormatString(goodsBase.getOriginPrice()));
			recommendGoods.add(goodsParam);
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
		//GoodsPOJO goodsInfo = goodsMapper.selectShoppingInfo(goodsId);
		GoodsBasePOJO goodsBaseInfo = goodsBaseMapper.selectOrderPayInfo(goodsId.longValue());
		ShoppingCartPOJO shoppingCart = new ShoppingCartPOJO();
		shoppingCart.setUserId(userId);
		shoppingCart.setGoodsId(goodsId);
		shoppingCart.setGoodsTotals(1);
		shoppingCart.setGoodsTotalsPrice(goodsBaseInfo.getShopPrice());
		shoppingCart.setGoodsName(goodsBaseInfo.getTitle());
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

	/**
	 * 查询单个商品信息
	 * @param orderDetailsId 订单编号
	 * @return
	 */
	@Override
	public OrderDetailsPOJO selectSigleOrder(Long orderDetailsId) {
		if(orderDetailsId == null)
			logger.error("查询订单详情订单编号不能为null");
		OrderDetailsPOJO sigleOrder = orderDetailsMapper.selectSigleOrder(orderDetailsId);
		if(sigleOrder != null){
			//根据订单编号查询商品信息
			GoodsParam goodsParam = goodsBaseMapper.selectGoodsByGoodsId(sigleOrder.getGoodsId().longValue());
			sigleOrder.setGoods(goodsParam);
			sigleOrder.setActualMoney(MoneyFormat.priceFormatString(sigleOrder.getActualPrice()));
			sigleOrder.setActualPrice(null);
			getAddress(sigleOrder);
			return sigleOrder;
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
	 * 修改超时订单状态
	 * @return
	 */
	@Override
	public void scanningOvertimeOrder() {
		List<OrderDetailsPOJO> orderIds = orderDetailsMapper.selectOvertimeOrder();
		int state = goodsBaseMapper.batchAddStock(orderIds);
		if(state <= 0){
			logger.error("超时订单返库存失败");
		}
		//批量修改订单状态
		int result = orderDetailsMapper.batchCloseOrder(orderIds);
		if(result <= 0){
			logger.error("批量修改订单状态失败");
		}
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

	@Override
	public List<OrderDetailsPOJO> autoConfirmDelivery() {
		return orderDetailsMapper.selectUnConfirmOrder();
	}

	@Override
	public OrderDetailsPOJO selectConfirmOrderInfo(String orderId) {
		return orderDetailsMapper.selectConfirmOrderInfo(orderId);
	}

	@Override
	public void selectOverTimeLimitedOrder()  {
		List<OrderDetailsPOJO> orderIdList = orderDetailsMapper.selectOverTimeLimitedOrder();
		if(orderIdList != null && orderIdList.size() > 0) {
			try {
				for (OrderDetailsPOJO order : orderIdList) {
					promotionService.updateLimitedStock(order);
				}
				//批量修改订单状态
				int result = orderDetailsMapper.batchCloseOrder(orderIdList);
				if (result <= 0) {
					logger.error("批量关闭订单状态失败");
				}

				//}
			} catch (BusinessException e){
				logger.error("扫描限量购超时订单异常",e);
			}
		}
	}

}
