package com.quxin.freshfun.service.impl.order;

import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.quxin.freshfun.dao.*;
import com.quxin.freshfun.model.*;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quxin.freshfun.mongodb.AddressManagerMongo;
import com.quxin.freshfun.service.order.OrderService;
import com.quxin.freshfun.utils.AESUtil;
import com.quxin.freshfun.utils.BusinessException;
import com.quxin.freshfun.utils.DateUtils;
import com.quxin.freshfun.utils.HttpClientUtil;
import com.quxin.freshfun.utils.IdGenerate;
import com.quxin.freshfun.utils.MoneyFormat;

@Service
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrdersMapper order;
	@Autowired
	private UserOutcomeMapper outcome;
	@Autowired
	private UserOutcomeDetailsMapper outcomeDetail;
	@Autowired
	private AddressManagerMongo orderAddress;
	@Autowired
	private OrderDetailsMapper orderDetails;
	@Autowired
	private GoodsDeliveryTypeMapper deliveryType;
	@Autowired
	private UsersMapper user;
	@Autowired
	private GoodsMapper goods;
	@Autowired
	private GoodsLimitMapper goodsLimit;
	@Autowired
	private ShoppingCartMapper cart;
	@Autowired
	private UserRevenueMapper revenue;
	@Autowired
	private UserRevenueDetailsMapper revenueDetail;
	@Autowired
	private MerchantAgentMapper merchantAgentMapper;
	
	//周
	public static final int FREQUENCY_WEEK = 0;
	public static final int FREQUENCY_WEEK_VALUE = 7;
	//月
	public static final int FREQUENCY_MONTH = 1;
	public static final int FREQUENCY_MONTH_VALUE = 30;
	
	public static final int COMMON_ORDER_TYPE = 11;
	
	private Logger resultLogger = LoggerFactory.getLogger("info_log");
	private Logger billLogger = LoggerFactory.getLogger("BILL");
	
	/**
	 * 添加订单
	 * @throws BusinessException 
	 */
	@Override
	public ResponseResult addOrder(OrderInfo orderInfo) throws BusinessException {
		Integer [] goodsPrice = new Integer[orderInfo.getGoodsInfo().size()];
		//订单编号
		IdGenerate idGenerate = new IdGenerate();
		Long uId = Long.parseLong(orderInfo.getUser_id().replace("\"", ""));
		Long orderId = idGenerate.nextId();
		// #TODO 根据购物车数量循环
		for(int i = 0;i < orderInfo.getGoodsInfo().size();i++){
			GoodsInfo goodsInfo = orderInfo.getGoodsInfo().get(i);
			OrderPayInfo payInfo = null;
			//查询购物车
			if(null == goodsInfo.getSc_id() || 0 == goodsInfo.getSc_id()){
				GoodsPOJO goodsPOJO = goods.selectShoppingInfo(goodsInfo.getGoodsId());
				payInfo = new OrderPayInfo(goodsPOJO.getGoods_name(), goodsPOJO.getShop_price());
			}else{
				ShoppingCartPOJO shoppingCart = cart.selectShoppingCart(goodsInfo.getSc_id());
				payInfo = new OrderPayInfo(shoppingCart.getGoodsName(), shoppingCart.getGoodsTotalsPrice());
			}
			goodsPrice[i] = payInfo.getGoodsPrice();
			OrderDetailsPOJO orderDetail = makeOrderDetail(payInfo,goodsInfo,orderInfo,orderId);
			//生成订单详情
			int orderDetailStatus = orderDetails.insertSelective(orderDetail);
			if(orderDetailStatus <= 0){
				resultLogger.error(orderInfo.getUser_id()+"添加订单详情失败");
				throw new BusinessException("订单详情生成失败");
			}
			//生成收入账单
			generateBill(payInfo,orderDetail,uId,goodsInfo.getGoodsId());
			//生成支出账单
			generateExpensesBill(payInfo,goodsInfo,uId,orderDetail);
		}
		if(orderInfo.getGoodsInfo().get(0).getSc_id() != null){
			OrdersPOJO orderPOJO = makeOrderPOJO(orderInfo,goodsPrice,orderId);
			int insertStatus = order.insertSelective(orderPOJO);
			if(insertStatus <= 0){
				resultLogger.error(orderInfo.getUser_red_id()+"添加订单失败");
				throw new BusinessException("订单添加失败");
			}
		}
		//生成订单后 调用支付
		String payMoney = MoneyFormat.priceFormatString(payMoney(goodsPrice));
		ResponseResult payResult = orderPay(orderId.toString(),payMoney,orderInfo.getCode(),orderInfo.getOpenid());
		//修改订单状态 
		//int payStatus = order.updateOrderPayStatus(orderPOJO.getId());
		//修改支付状态
		if(orderInfo.getGoodsInfo().get(0).getSc_id() != null){
			for(int i = 0;i < orderInfo.getGoodsInfo().size();i++){
				Integer status = cart.updateOrderPayStatus(orderInfo.getGoodsInfo().get(i).getSc_id());
				if(status <= 0){
					resultLogger.error(orderInfo.getUser_id()+"修改购物车状态失败");
					throw new BusinessException("修改购物车状态");
				}
			}
		}
//		System.out.println("修改状态:"+payStatus);
//		int status = orderDetails.updatePayStatus(orderId);
//		System.out.println(status);
		return payResult;
	}
	
	private synchronized Integer payMoney(Integer[] goodsPrice) {
		int sumPrice = 0;
		for (int i = 0; i < goodsPrice.length; i++) {
			sumPrice = sumPrice + goodsPrice[i];
		}
		return sumPrice;
	}
	/**
	 * 添加限时购订单
	 * @param orderInfo
	 */
	public ResponseResult addLimitOrder(OrderInfo orderInfo) {
		Integer [] goodsPrice = new Integer[orderInfo.getGoodsInfo().size()];
		//订单编号
		IdGenerate idGenerate = new IdGenerate();
		Long orderId = idGenerate.nextId();
		// #TODO 根据购物车数量循环
		for(int i = 0;i < orderInfo.getGoodsInfo().size();i++){
			GoodsInfo goodsInfo = orderInfo.getGoodsInfo().get(i);
			OrderPayInfo payInfo = null;
			
			GoodsLimit goodsPOJO = goodsLimit.selectShoppingInfo(goodsInfo.getGoodsId());
			payInfo = new OrderPayInfo(goodsPOJO.getGoods_name(), goodsPOJO.getShop_price());

			goodsPrice[i] = payInfo.getGoodsPrice();
			OrderDetailsPOJO orderDetail = makeOrderDetail(payInfo,goodsInfo,orderInfo,orderId);
			orderDetail.setIsLimit(1);
			//生成订单详情
			int orderDetailStatus = orderDetails.insertSelective(orderDetail);
			if(orderDetailStatus <= 0){
				resultLogger.error(orderInfo.getUser_id()+"添加订单详情失败");
				throw new RuntimeException();
			}
			//生成收入账单
			//generateBill(payInfo,goodsInfo,orderInfo.getUser_id(),orderDetail.getOrderDetailsId());
			//生成支出账单
			generateExpensesBill(payInfo,goodsInfo,Long.parseLong(orderInfo.getUser_id().replace("\"", "")),orderDetail);
		}
		OrdersPOJO orderPOJO = makeOrderPOJO(orderInfo,goodsPrice,orderId);
		orderPOJO.setIs_limit(1);
		int insertStatus = order.insertSelective(orderPOJO);
		if(insertStatus <= 0){
			resultLogger.error(orderInfo.getUser_red_id()+"添加订单失败");
			throw new RuntimeException();
		}
		String money = "0.01";
		//生成订单后 调用支付
		ResponseResult payResult = orderPay(orderPOJO.getOrder_id().toString(),money,orderInfo.getCode(),orderInfo.getOpenid());

		return payResult;
	}
	/**
	 * 添加用户支出账单
	 * @param goodsPOJO
	 * @param goodsInfo
	 * @param user_id
	 * @param orderDetailsId
	 */
	private void generateExpensesBill(OrderPayInfo payInfo, GoodsInfo goodsInfo,
			Long user_id, OrderDetailsPOJO orderDetail) {
		Long currentDate = DateUtils.getCurrentDate();
		UserOutcome outCome = new UserOutcome();
		outCome.setUser_id(user_id);
		outCome.setOrder_id(orderDetail.getOrderDetailsId());
		outCome.setOut_price(orderDetail.getActualPrice());
		outCome.setCreate_date(currentDate);
		outCome.setUpdate_date(currentDate);
		outCome.setOutcome_name(payInfo.getGoodsName());
		int status = addUserOutcome(outCome);
		if(status <= 0){
			resultLogger.error(user_id+"生成用户支出账单失败");
			throw new RuntimeException();
		}
	}

	/**
	 * 根据用户提交的信息生成订单详情
	 * @param goodsPOJO
	 * @param goodsInfo
	 */
	private OrderDetailsPOJO makeOrderDetail(OrderPayInfo payInfo, GoodsInfo goodsInfo,OrderInfo orderInfo,Long orderId) {
		long currentTime = DateUtils.getCurrentDate();
		//根据订单地址编号查询地址信息
		OrderDetailsPOJO od = new OrderDetailsPOJO();
		//订单编号
		IdGenerate idGenerate = new IdGenerate();
		String orderDetailId = getOrderDetailId(idGenerate,orderInfo);
		od.setOrderId(orderId);
		od.setUserId(Long.parseLong(orderInfo.getUser_id().replace("\"", "")));
		od.setOrderDetailsId(orderDetailId);
		od.setGoodsId(goodsInfo.getGoodsId());
		od.setAddressId(orderInfo.getAddress_id());
		od.setPaymentMethod(orderInfo.getPayment_method());
		if(null != goodsInfo.getCount() && goodsInfo.getCount() > 0)
			od.setActualPrice(payInfo.getGoodsPrice()*goodsInfo.getCount());
		else
			od.setActualPrice(payInfo.getGoodsPrice());
		od.setPayTime(currentTime);
		od.setOrderStatus(0);
		od.setPayStatus(0);
		od.setCommentStatus(0);
		od.setCount(goodsInfo.getCount());
		od.setPayPlateform(orderInfo.getPay_sign());
		od.setCreateDate(currentTime);
		od.setUpdateDate(currentTime);
		return od;
	}
	private String getOrderDetailId(IdGenerate idGenerate,OrderInfo orderInfo) {
		StringBuilder sb = new StringBuilder();
		if(orderInfo.getPay_sign() == 2){
			sb.append("WA");
			sb.append(idGenerate.nextId());
			return sb.toString();
		}else if( orderInfo.getPay_sign() == 1){
			sb.append("WI");
			sb.append(idGenerate.nextId());
			return sb.toString();
		}else {
			sb.append("WZ");
			sb.append(idGenerate.nextId());
			return sb.toString();
		}
	}

	/**
	 * 订单实体
	 * @param orderInfo
	 * @return
	 */
	private OrdersPOJO makeOrderPOJO(OrderInfo orderInfo,Integer [] goodsPrice,Long orderId) {
		Integer sumPrice = 0;
		for(int i = 0;i < goodsPrice.length;i++){
			sumPrice += goodsPrice[i];
		}
		long currentDate = DateUtils.getCurrentDate();
		OrdersPOJO order = new OrdersPOJO();
		order.setUser_id(Long.parseLong(orderInfo.getUser_id().replace("\"", "")));
		order.setOrder_id(orderId);
		order.setGmt_create(currentDate);
		order.setGmt_modified(currentDate);
		order.setPayment_method(orderInfo.getPayment_method());
		order.setOrder_count(orderInfo.getGoodsInfo().size());
		order.setOrder_status(0);
		order.setPay_status(0);
		order.setUser_red_id(orderInfo.getUser_red_id());
		order.setActual_price(sumPrice);
		order.setCode(orderInfo.getCode());
		return order;
	}
	/**
	 * 生成支付
	 * @return
	 */
	private ResponseResult orderPay(String orderId,String moneyPaid,String code,String openid){
		StringBuffer sb = new StringBuffer();
		sb.append("https://meiguoyouxian.com/wz_pay/wx/wx_pay.php?");
		sb.append("method=getH5PrepayId");
		sb.append("&");
		sb.append("total_amount=");
		sb.append(moneyPaid);
		sb.append("&");
		sb.append("order_number=");
		sb.append(orderId);
		sb.append(DateUtils.getCurrentDate());
		sb.append("&");
		sb.append("attach=");
		sb.append(orderId);
		sb.append("&");
		sb.append("code=");
		sb.append(code);
		sb.append("&");
		sb.append("openid=");
		sb.append(openid);
		
		Object o = new Object();
		Gson gson = new GsonBuilder().disableHtmlEscaping().create();
		String StrJson = gson.toJson(o);
		String str = HttpClientUtil.jsonToPost(sb.toString(), StrJson);
		billLogger.info("支付返回结果："+str);
		Gson g = new Gson();
		ResponseResult json = g.fromJson(str,ResponseResult.class);
		return json;
	}
	/**
	 *查询所有订单信息
	 */
	@Override
	public List<OrdersPOJO> findAllOrders() {
		List<OrdersPOJO> orders = order.findAllOrders();
		OrdersPOJO order = orders.get(0);
		
		createOrderDetails(order);
		return null;
	}
	private void createOrderDetails(OrdersPOJO order) {
		if(11 == COMMON_ORDER_TYPE){
			createCommonOrderDetail(order);
		}else{
			createSplitOrder(order);
		}
	}
	/**
	 * 普通订单
	 * @param order
	 */
	private void createCommonOrderDetail(OrdersPOJO order) {
		OrderInPieces orderInPiecesBean = getOrderInPiecesBean(order);
		orderInPiecesBean.setDeliveryDate(order.getGmt_create());
		//orderInpieces.addOrderDetail(orderInPiecesBean);
		//区分是否是分享用户
		if(order.getCode().trim() == "" || order.getCode() == null){
			//不是分享用户
			//createBill(order,orderInPiecesBean.getOrderPieceId(),order.getActual_price());
		}else{
			//分享用户
			createBill(order,orderInPiecesBean.getOrderPieceId());
		}
		
	}
	/**
	 * 生成账单
	 * @param goodsPOJO
	 */
	private void generateBill(OrderPayInfo payInfo,OrderDetailsPOJO orderDetail,Long userId,Integer goodsId) {
//		if(null == goodsInfo.getSignCode() || "".equals(goodsInfo.getSignCode())){
//			//查询是否有代理商户
//			GoodsPOJO gp = goods.selectProxyMerchantByGoodsId(goodsInfo.getGoodsId());
//			if(null != gp.getMerchant_proxy_id() && gp.getMerchant_proxy_id() != 0){
//				int proxyMerchantMoney = (int) (payInfo.getGoodsPrice() * 0.2);
//				int merchantMoney = (int) (payInfo.getGoodsPrice() * 0.8);
//				generateBillDetail(payInfo,goodsInfo,gp.getMerchant_proxy_id(),orderInpieces,proxyMerchantMoney,2);
//				generateBillDetail(payInfo,goodsInfo,gp.getStore_id(),orderInpieces,merchantMoney,3);
//				
//			}else{
//				generateBillDetail(payInfo,goodsInfo,gp.getStore_id(),orderInpieces,payInfo.getGoodsPrice(),3);
//			}
//		}else{
			//查询用户信息
			UsersPOJO userPOJO = user.selectParentIdByUserId(userId);
			//解析
			//String codeVal = getCodeVal(goodsInfo.getSignCode());
			//String[] splitCode = getSplitCode(codeVal);
			//Long shareId = Long.parseLong(splitCode[0]);
			if(null != userPOJO.getParent_id() && userPOJO.getParent_id() != 0){
				//修改用户标记
				
				//查询是否有代理商户
				GoodsPOJO gp = goods.selectProxyMerchantByGoodsId(goodsId);
				if(null != gp.getMerchant_proxy_id() && gp.getMerchant_proxy_id() != 0){
					int shareMoney = (int) (orderDetail.getActualPrice() * 0.1);
					int proxyMerchantMoney = (int) (orderDetail.getActualPrice() * 0.2);
					int merchantMoney = (int) (orderDetail.getActualPrice() * 0.7);
					generateBillDetail(payInfo,orderDetail,userPOJO.getParent_id(),shareMoney,1);
					generateBillDetail(payInfo,orderDetail,gp.getMerchant_proxy_id(),proxyMerchantMoney,2);
					generateBillDetail(payInfo,orderDetail,gp.getStore_id(),merchantMoney,3);
				}else{
					int shareMoney = (int) (orderDetail.getActualPrice() * 0.1);
					int merchantMoney = (int) (orderDetail.getActualPrice() * 0.9);
					generateBillDetail(payInfo,orderDetail,userPOJO.getParent_id(),shareMoney,1);
					generateBillDetail(payInfo,orderDetail,gp.getStore_id(),merchantMoney,3);
				}
			}else{
				//查询是否有代理商户
				GoodsPOJO gp = goods.selectProxyMerchantByGoodsId(goodsId);
				if(null != gp.getMerchant_proxy_id() && gp.getMerchant_proxy_id() != 0){
					int proxyMerchantMoney = (int) (orderDetail.getActualPrice() * 0.2);
					int merchantMoney = (int) (orderDetail.getActualPrice() * 0.8);
					generateBillDetail(payInfo,orderDetail,gp.getMerchant_proxy_id(),proxyMerchantMoney,2);
					generateBillDetail(payInfo,orderDetail,gp.getStore_id(),merchantMoney,3);
				}else{
					generateBillDetail(payInfo,orderDetail,gp.getStore_id(),orderDetail.getActualPrice(),3);
				}
			}
		//}
	}
	/**
	 * 生成订单详情
	 * @param goodsPOJO
	 * @param goodsInfo
	 * @param orderInpieces
	 */
	private void generateBillDetail(OrderPayInfo payInfo,OrderDetailsPOJO orderDetail,Long userId,int money,int deliveryType) {
		long currentDate = DateUtils.getCurrentDate();
		UserRevenue revenue = new UserRevenue();
		revenue.setUser_id(userId);
		revenue.setOrder_id(orderDetail.getOrderDetailsId());
		revenue.setPrice(money);
		revenue.setIn_state(0);
		revenue.setCreate_date(currentDate);
		revenue.setUpdate_date(currentDate);
		revenue.setDelivery_type(deliveryType);
		revenue.setRevenue_name(payInfo.getGoodsName());
		int status = addUserRevenue(revenue);
		if(status <= 0){
			resultLogger.error(userId+"生成订单详情失败");
			throw new RuntimeException();
		}
	}

	/**
	 * 生成分享用户账单
	 * @param order2
	 * @param orderPieceId
	 */
	private void createBill(OrdersPOJO order, String orderPieceId) {
		//解析code
		String codeVal = getCodeVal(order.getCode());
		String[] splitVal = getSplitCode(codeVal);
		if(splitVal.length <= 0)
			throw new IllegalArgumentException();
		Integer userId = Integer.parseInt(splitVal[0]);
		Integer goodsId = Integer.parseInt(splitVal[1]);
		if(goodsId != 1){
			// TODO 记录日志
		}else{
			//生成用户标记
			//updateUserParentId(userId,goodsId);
			//生成分享账单
			//createShareBill(order,userId,orderPieceId);
		}
	}
	/**
	 * 添加用户支出详情信息
	 * @param outDetail
	 */
	private void addUserOutcomeDetail(UserOutcomeDetails outDetail) {
		outcomeDetail.insertSelective(outDetail);
	}
	/**
	 * 添加用户支出信息
	 * @param outcome
	 */
	private int addUserOutcome(UserOutcome out) {
		return outcome.insertSelective(out);
	}
	/**
	 * 添加用户收入详情信息
	 * @param UserrevenueDetail
	 */
	private void addUserRevenueDetail(UserRevenueDetails UserrevenueDetail) {
		revenueDetail.insertSelective(UserrevenueDetail);
	}
	/**
	 * 添加用户收入信息
	 * @param UserRevenue
	 */
	private int addUserRevenue(UserRevenue UserRevenue) {
		return revenue.insertSelective(UserRevenue);
	}
	/**
	 * 根据订单商品编号查询商品商户编号
	 * @param goods_id
	 */
	private GoodsPOJO findMerchantInfoByGoodsId(Integer goodsId) {
		return goods.selectMerchantByGoodsId(goodsId);
	}
	/**
	 * 修改用户分享标记
	 * @param userId
	 * @param goodsId
	 */
	private Integer modifyUserParentId(Long userId, Long parentId) {
		Map<String, Long> map = new HashMap<>();
		map.put("parentId", parentId);
		map.put("userId", userId);
		int sign = user.updateParentIdByUserId(map);
		return sign;
	}
	/**
	 * 分割code
	 * @param codeVal
	 */
	private String [] getSplitCode(String codeVal) {
		return codeVal.split("\\|");
	}
	/**
	 * 解析经过Base64编码的coed
	 * @param code
	 */
	private String getCodeVal(String code) {
		return AESUtil.decodeStr(code);
	}
	/**
	 * 拆分订单
	 * @param order
	 */
	private void createSplitOrder(OrdersPOJO order) {
		//查询订单的套餐类型
		GoodsDeliveryTypePOJO deliveryTypePOJO = deliveryType.selectByDeliveryId(4);
		//发货次数
		int deliveryCount = deliveryTypePOJO.getDeliveryCount();
		//单次价格
		int singlePrice = order.getActual_price() / deliveryCount;
		Long[] deliveryDates = makeDeliveryDate(order.getGmt_create(),deliveryTypePOJO);
		for(int i = 0;i < deliveryCount;i++){
			//生成商品详情
			OrderInPieces orderInPiecesBean = getOrderInPiecesBean(order);
			orderInPiecesBean.setDeliveryDate(deliveryDates[i]);
		}
	}
	
	/**
	 * 根据订单地址 查询订单详情实体
	 * @param order
	 */
	private OrderInPieces getOrderInPiecesBean(OrdersPOJO order) {
		//根据订单地址编号查询地址信息
//		OrderAddress address = getAddressByOrderId(order.getAddress_id());
		OrderInPieces op = new OrderInPieces();
		//订单编号
	//	op.setUserId(order.getUser_id());
		//op.setOrderId(order.getId());
		op.setOrderPieceId(UUID.randomUUID().toString());
//		op.setAddress(address);
		//op.setPaymentMethod(order.getPayment_method());
		op.setPayTime(order.getGmt_create());
		op.setOrderStatus(0);
		op.setCommentStatus(0);
		op.setActualPrice(order.getActual_price());
		return op;
	}
	
	/**
	 * 根据订单编号查询地址信息
	 * @param id
	 */
	private OrderAddress getAddressByOrderId(String addressId) {
		return orderAddress.findAddressById(addressId);
	}
	
	/**
	 * 生成订单发货时间
	 * @param goodsDelivery
	 */
	private Long[] makeDeliveryDate(Long createDate,GoodsDeliveryTypePOJO deliveryTypePOJO) {
		Long [] dates = new Long [deliveryTypePOJO.getDeliveryCount()];
		int frequencyCycleWeek = FREQUENCY_WEEK_VALUE;
		int frequencyCycleMonth = FREQUENCY_MONTH_VALUE;
		switch(deliveryTypePOJO.getFrequency()){
		case FREQUENCY_WEEK:
			for(int i = 0;i < deliveryTypePOJO.getDeliveryCount();i++){
				Long time = getDeliveryDate(createDate, frequencyCycleWeek);
				dates[i] = time;
				frequencyCycleWeek += FREQUENCY_WEEK_VALUE;
			}
			break;
		case FREQUENCY_MONTH:
			for(int i = 0;i < deliveryTypePOJO.getDeliveryCount();i++){
				Long time = getDeliveryDate(createDate, frequencyCycleMonth);
				dates[i] = time;
				frequencyCycleMonth += FREQUENCY_MONTH_VALUE;
			}
			break;
		}
		return dates;
	}
	
	/**
	 * 根据创建订单时间生成发货时间
	 * @param time
	 * @param d
	 * @return
	 */
	private Long getDeliveryDate(Long time,int d){
		 Calendar cal = Calendar.getInstance();
		 Date date = new Date(time*1000);
		 cal.setTime(date);
	     cal.set(Calendar.DATE, cal.get(Calendar.DATE) + d);
	     cal.set(Calendar.HOUR_OF_DAY, 0);
	     cal.set(Calendar.MINUTE, 0);
	     cal.set(Calendar.SECOND, 0);
	     cal.set(Calendar.MILLISECOND, 0);
	     return cal.getTimeInMillis();
	}
	@Override
	public OrderDetailsPOJO getOrderLogistic(String orderDetailId) {
		OrderDetailsPOJO orderDetail = orderDetails.getLogistic(orderDetailId);
		return orderDetail;
	}
	/**
	 * 回调
	 */
	@SuppressWarnings("unchecked")
	@Override
	public int PayCallback(InputStream in) {
		try{
			SAXReader sr = new SAXReader();
			Document document = sr.read(in);
			Element root = document.getRootElement();
			List<Element> elements = root.elements();
			Map<String, String> map = Maps.newHashMap();
			for (Element e : elements) {
				map.put(e.getName(), e.getText());
			}
			billLogger.info("用户编号："+map.get("attach")+"支付回调结果："+map.toString());
			billLogger.info(map.get("result_code"));
			billLogger.info("SUCCESS".equals(map.get("result_code"))+"");
			if("SUCCESS".equals(map.get("result_code"))){
				int payStatus = 0;
				Long currentDate = DateUtils.getCurrentDate();
				String orderId = map.get("attach");
				if("W".equals(orderId.subSequence(0, 1))){
					Integer id = orderDetails.selectPayIdByOrderDetailsId(orderId);
					payStatus = orderDetails.updateOrderDetailPayStatus(currentDate, id);
					billLogger.info("订单详情支付"+map.get("attach"));
				}else if("B".equals(orderId.subSequence(0, 1))){
					//商户代理费支付回调
					Integer id = Integer.parseInt(orderId.substring(1,orderId.length()));
					payStatus = merchantAgentMapper.updatePayStatus(id);
					billLogger.info("商户编号："+map.get("attach")+"支付状态："+payStatus);
				}else{
					List<Integer> payIdList = orderDetails.selectPayId(Long.parseLong(orderId));
					for (Integer id : payIdList) {
						payStatus = orderDetails.updateOrderDetailPayStatus(currentDate, id);
						billLogger.info("用户编号："+map.get("attach")+"修改订单状态结果：");
					}
				}
				return payStatus;
			}
			
		} catch (DocumentException e1) {
			e1.printStackTrace();
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return 0;
	}
	/**
	 * 订单支付
	 */
	@Override
	public ResponseResult awaitPayOrder(OrderPayPOJO order) {
		OrderDetailsPOJO detailsPOJO = orderDetails.selectPayOrder(order.getOrderId());
		String payMoney = MoneyFormat.priceFormatString(detailsPOJO.getActualPrice());
		ResponseResult orderPay = orderPay(order.getOrderId(),payMoney,order.getCode(),order.getOpenId());
		return orderPay;
	}


	@Override
	public ResponseResult addQuanMingPay(QuanMingPayInfo info) throws BusinessException {
		//商户支付金额
		Integer agentPrice = 30000;
		Long date = DateUtils.getCurrentDate();
		MerchantAgent agent = new MerchantAgent();
		agent.setMerchantId(Long.parseLong(info.getUserId().replace("\"","")));
		agent.setPrice(agentPrice*100);
		agent.setCreateDate(date);
		agent.setUpdateDate(date);
		int status = merchantAgentMapper.insertSelective(agent);
		if(status <= 0){
			billLogger.error("商户："+info.getUserId()+"添加商户代理信息失败");
			throw new BusinessException("添加商户代理信息失败");
		}else{
			//支付
			StringBuilder sb = new StringBuilder();
			sb.append("B");
			sb.append(agent.getId());
			//发起支付请求
			ResponseResult payResult = orderPay(sb.toString(),agentPrice.toString(),info.getCode(),info.getOpenId());
			return payResult;
		}
	}

	@Override
	public Integer queryAllIncome(Long id) {
		return orderDetails.selectAllIncome(id);
	}
	@Override
	public Integer queryEarnedIncome(Long id) {
		return orderDetails.selectEarnedIncome(id);
	}
}