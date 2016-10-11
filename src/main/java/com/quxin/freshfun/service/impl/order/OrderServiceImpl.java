package com.quxin.freshfun.service.impl.order;

import com.alibaba.fastjson.JSON;
import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quxin.freshfun.common.Constant;
import com.quxin.freshfun.controller.wxpay.AccessTokenRequestHandler;
import com.quxin.freshfun.controller.wxpay.ClientRequestHandler;
import com.quxin.freshfun.controller.wxpay.PackageRequestHandler;
import com.quxin.freshfun.controller.wxpay.PrepayIdRequestHandler;
import com.quxin.freshfun.controller.wxpay.client.TenpayHttpClient;
import com.quxin.freshfun.dao.*;
import com.quxin.freshfun.model.*;
import com.quxin.freshfun.model.outparam.WxPayInfo;
import com.quxin.freshfun.service.order.OrderService;
import com.quxin.freshfun.utils.*;
import com.quxin.freshfun.utils.weixinPayUtils.ConstantUtil;
import com.quxin.freshfun.utils.weixinPayUtils.JsonUtil;
import com.quxin.freshfun.utils.weixinPayUtils.TenpayUtil;
import com.quxin.freshfun.utils.weixinPayUtils.WXUtil;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.*;

@Service("orderService")
public class OrderServiceImpl implements OrderService {
	@Autowired
	private OrdersMapper ordersMapper;
	@Autowired
	private UserOutcomeMapper userOutcomeMapper;
	@Autowired
	private OrderDetailsMapper orderDetailsMapper;
	@Autowired
	private GoodsDeliveryTypeMapper goodsDeliveryTypeMapper;
	@Autowired
	private UsersMapper usersMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private GoodsLimitMapper goodsLimitMapper;
	@Autowired
	private ShoppingCartMapper shoppingCartMapper;
	@Autowired
	private UserRevenueMapper userRevenueMapper;
	@Autowired
	private MerchantAgentMapper merchantAgentMapper;
	@Autowired
	private UserAddressMapper userAddress;

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
		OrderPayInfo [] orderPayInfos = new OrderPayInfo[orderInfo.getGoodsInfo().size()];
		//订单总价格
		int orderSumPrice = 0;
		//订单编号
		Long uId = Long.parseLong(orderInfo.getUserId().replace("\"", ""));

		for(int i = 0;i < orderInfo.getGoodsInfo().size();i++){
			GoodsInfo goodsInfo = orderInfo.getGoodsInfo().get(i);
			OrderPayInfo orderPayInfo = null;
			//查询购物车
			if(null == goodsInfo.getScId() || 0 == goodsInfo.getScId()){
				GoodsPOJO goodsPOJO = goodsMapper.selectShoppingInfo(goodsInfo.getGoodsId());
				orderPayInfo = new OrderPayInfo(goodsPOJO.getGoodsName(), goodsPOJO.getShopPrice()*goodsInfo.getCount());
			}else{
				ShoppingCartPOJO shoppingCart = shoppingCartMapper.selectShoppingCart(goodsInfo.getScId());
				orderPayInfo = new OrderPayInfo(shoppingCart.getGoodsName(), shoppingCart.getGoodsTotalsPrice()*shoppingCart.getGoodsTotals());
			}
			orderPayInfos[i] = orderPayInfo;
			orderSumPrice += orderPayInfo.getGoodsPrice();
		}

		OrdersPOJO orderPOJO = makeOrderPOJO(orderInfo,orderSumPrice);
		int insertStatus = ordersMapper.insertSelective(orderPOJO);
		if(insertStatus <= 0){
			resultLogger.error(orderInfo.getUserId()+"添加订单失败");
			throw new BusinessException("订单添加失败");
		}
		//订单父级编号
		Long orderId = orderPOJO.getId();
		for (int i = 0;i < orderPayInfos.length;i++){
			GoodsInfo goodsInfo = orderInfo.getGoodsInfo().get(i);
			OrderDetailsPOJO orderDetail = makeOrderDetail(orderPayInfos[i],goodsInfo,orderInfo,orderId);
			//生成订单详情
			int orderDetailStatus = orderDetailsMapper.insertSelective(orderDetail);
			if(orderDetailStatus <= 0){
				resultLogger.error(orderInfo.getUserId()+"添加订单详情失败");
				throw new BusinessException("订单详情生成失败");
			}
		}

		//生成订单后 调用支付
		String payMoney = MoneyFormat.priceFormatString(orderSumPrice);
		StringBuilder payId = new StringBuilder();
		payId.append("Z");
		payId.append(orderId);
		ResponseResult payResult = orderPay(payId.toString(),payMoney,orderInfo.getCode(),orderInfo.getOpenid());
		//修改订单状态
		//int payStatus = order.updateOrderPayStatus(orderPOJO.getId());
		//修改支付状态
		if(orderInfo.getGoodsInfo().get(0).getScId() != null){
			for(int i = 0;i < orderInfo.getGoodsInfo().size();i++){
				Integer status = shoppingCartMapper.updateOrderPayStatus(orderInfo.getGoodsInfo().get(i).getScId());
				if(status <= 0){
					resultLogger.error(orderInfo.getUserId()+"修改购物车状态失败");
					throw new BusinessException("修改购物车状态");
				}
			}
		}
		return payResult;
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
		for(int i = 0;i < orderInfo.getGoodsInfo().size();i++){
			GoodsInfo goodsInfo = orderInfo.getGoodsInfo().get(i);
			OrderPayInfo payInfo = null;

			GoodsLimit goodsPOJO = goodsLimitMapper.selectShoppingInfo(goodsInfo.getGoodsId());
			payInfo = new OrderPayInfo(goodsPOJO.getGoodsName(), goodsPOJO.getShopPrice());

			goodsPrice[i] = payInfo.getGoodsPrice();
			OrderDetailsPOJO orderDetail = makeOrderDetail(payInfo,goodsInfo,orderInfo,orderId);
			orderDetail.setIsLimit(1);
			//生成订单详情
			int orderDetailStatus = orderDetailsMapper.insertSelective(orderDetail);
			if(orderDetailStatus <= 0){
				resultLogger.error(orderInfo.getUserId()+"添加订单详情失败");
				throw new RuntimeException();
			}
			//生成收入账单
			//generateBill(payInfo,goodsInfo,orderInfo.getUserId(),orderDetail.getOrderDetailsId());
			//生成支出账单
			generateExpensesBill(payInfo,goodsInfo,Long.parseLong(orderInfo.getUserId().replace("\"", "")),orderDetail);
		}
		OrdersPOJO orderPOJO = makeOrderPOJO(orderInfo,0);
		orderPOJO.setIsLimit(1);
		int insertStatus = ordersMapper.insertSelective(orderPOJO);
		if(insertStatus <= 0){
			resultLogger.error(orderInfo.getUserRedId()+"添加订单失败");
			throw new RuntimeException();
		}
		String money = "0.01";
		//生成订单后 调用支付
		ResponseResult payResult = orderPay(orderPOJO.getOrderId().toString(),money,orderInfo.getCode(),orderInfo.getOpenid());

		return payResult;
	}
	/**
	 * 添加用户支出账单
	 * @param payInfo
	 * @param goodsInfo
	 * @param userId
	 * @param orderDetail
	 */
	private void generateExpensesBill(OrderPayInfo payInfo, GoodsInfo goodsInfo,
									  Long userId, OrderDetailsPOJO orderDetail) {
		Long currentDate = DateUtils.getCurrentDate();
		UserOutcome outCome = new UserOutcome();
		outCome.setUserId(userId);
		outCome.setOrderId(orderDetail.getId().toString());
		outCome.setOutPrice(orderDetail.getActualPrice());
		outCome.setCreateDate(currentDate);
		outCome.setUpdateDate(currentDate);
		outCome.setOutcomeName(payInfo.getGoodsName());
		int status = addUserOutcome(outCome);
		if(status <= 0){
			resultLogger.error(userId+"生成用户支出账单失败");
			throw new RuntimeException();
		}
	}

	/**
	 * 根据用户提交的信息生成订单详情
	 * @param payInfo
	 * @param goodsInfo
	 */
	private OrderDetailsPOJO makeOrderDetail(OrderPayInfo payInfo, GoodsInfo goodsInfo,OrderInfo orderInfo,Long orderId) {
		long currentTime = DateUtils.getCurrentDate();
        long uid = Long.parseLong(orderInfo.getUserId().replace("\"",""));
		//根据地址编号查询地址信息
		UserAddress address = userAddress.selectAddressById(orderInfo.getAddressId());
		//查询捕手信息
		UsersPOJO userPOJO = usersMapper.selectParentIdByUserId(uid);

		OrderDetailsPOJO od = new OrderDetailsPOJO();
		//订单编号
		od.setOrderId(orderId);
		od.setUserId(Long.parseLong(orderInfo.getUserId().replace("\"", "")));
		od.setGoodsId(goodsInfo.getGoodsId());
		od.setAddressId(orderInfo.getAddressId());
		od.setPaymentMethod(orderInfo.getPaymentMethod());
		od.setActualPrice(payInfo.getGoodsPrice());
		od.setPayTime(currentTime);
		od.setOrderStatus(10);
		//判断是否是捕手
		if(userPOJO.getParentId() !=null && userPOJO.getParentId()!=0) {

			od.setFetcherId(userPOJO.getParentId());
			//计算捕手需要获取的提成
			Double fetcherMoney = payInfo.getGoodsPrice() * Constant.FECTHER_COMPONENT;
			od.setFetcherPrice(fetcherMoney.intValue());
		}
		//查询是否有代理商户
		GoodsPOJO gp = goodsMapper.selectProxyMerchantByGoodsId(goodsInfo.getGoodsId());
		if(gp.getMerchantProxyId()!=null && gp.getMerchantProxyId()!=0){
			od.setAgentId(gp.getMerchantProxyId());
			//计算商户获取的提成
			Double agentMoney = payInfo.getGoodsPrice()*Constant.AGENT_COMPONENT;
			od.setAgentPrice(agentMoney.intValue());
		}
		if(null == orderInfo.getPaySign() || "".equals(orderInfo.getPaySign())){
			if(userPOJO.getParentId() !=null && userPOJO.getParentId()!=0){
				od.setPayPlateform(1);
			}else{
				od.setPayPlateform(0);
			}
		}else{
			od.setPayPlateform(2);
		}
		od.setCount(goodsInfo.getCount());
		od.setPayPlateform(orderInfo.getPaySign());
		od.setCreateDate(currentTime);
		od.setUpdateDate(currentTime);
		od.setName(address.getName());
		od.setTel(address.getTel());
		od.setCity(address.getCity());
		od.setAddress(address.getAddress());
		return od;
	}

	/**
	 * 订单实体
	 * @param orderInfo
	 * @return
	 */
	private OrdersPOJO makeOrderPOJO(OrderInfo orderInfo,Integer sumPrice) {
		long currentDate = DateUtils.getCurrentDate();
		OrdersPOJO order = new OrdersPOJO();
		order.setUserId(Long.parseLong(orderInfo.getUserId().replace("\"", "")));
		order.setGmtCreate(currentDate);
		order.setGmtModified(currentDate);
		order.setPaymentMethod(orderInfo.getPaymentMethod());
		order.setOrderCount(orderInfo.getGoodsInfo().size());
		order.setOrderStatus(0);
		order.setPayStatus(0);
		order.setUserId(Long.parseLong(orderInfo.getUserId().replace("\"","")));
		order.setActualPrice(sumPrice);
		order.setCode(orderInfo.getCode());
		return order;
	}
	/**
	 * 生成支付
	 * @return
	 */
	private ResponseResult orderPay(String orderId,String moneyPaid,String code,String openid){
		StringBuffer sb = new StringBuffer();
		sb.append("https://www.freshfun365.com/wz_pay/wx/wx_pay.php?");
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
		List<OrdersPOJO> orders = ordersMapper.findAllOrders();
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
		orderInPiecesBean.setDeliveryDate(order.getGmtCreate());
		//orderInpieces.addOrderDetail(orderInPiecesBean);
		//区分是否是分享用户
		if(order.getCode().trim() == "" || order.getCode() == null){
			//不是分享用户
			//createBill(order,orderInPiecesBean.getOrderPieceId(),order.getActualPrice());
		}else{
			//分享用户
			createBill(order,orderInPiecesBean.getOrderPieceId());
		}
	}
	/**
	 * 生成账单
	 * @param payInfo
	 */
	private void generateBill(OrderPayInfo payInfo,OrderDetailsPOJO orderDetail,Long userId,Integer goodsId) {
		//查询用户信息
		UsersPOJO userPOJO = usersMapper.selectParentIdByUserId(userId);
		//解析
		if(null != userPOJO.getParentId() && userPOJO.getParentId() != 0){
			//修改用户标记

			//查询是否有代理商户
			GoodsPOJO gp = goodsMapper.selectProxyMerchantByGoodsId(goodsId);
			if(null != gp.getMerchantProxyId() && gp.getMerchantProxyId() != 0){
				int shareMoney = (int) (orderDetail.getActualPrice() * 0.1);
				int proxyMerchantMoney = (int) (orderDetail.getActualPrice() * 0.2);
				int merchantMoney = (int) (orderDetail.getActualPrice() * 0.7);
				generateBillDetail(payInfo,orderDetail,userPOJO.getParentId(),shareMoney,1);
				generateBillDetail(payInfo,orderDetail,gp.getMerchantProxyId(),proxyMerchantMoney,2);
				generateBillDetail(payInfo,orderDetail,gp.getStoreId(),merchantMoney,3);
			}else{
				int shareMoney = (int) (orderDetail.getActualPrice() * 0.1);
				int merchantMoney = (int) (orderDetail.getActualPrice() * 0.9);
				generateBillDetail(payInfo,orderDetail,userPOJO.getParentId(),shareMoney,1);
				generateBillDetail(payInfo,orderDetail,gp.getStoreId(),merchantMoney,3);
			}
		}else{
			//查询是否有代理商户
			GoodsPOJO gp = goodsMapper.selectProxyMerchantByGoodsId(goodsId);
			if(null != gp.getMerchantProxyId() && gp.getMerchantProxyId() != 0){
				int proxyMerchantMoney = (int) (orderDetail.getActualPrice() * 0.2);
				int merchantMoney = (int) (orderDetail.getActualPrice() * 0.8);
				generateBillDetail(payInfo,orderDetail,gp.getMerchantProxyId(),proxyMerchantMoney,2);
				generateBillDetail(payInfo,orderDetail,gp.getStoreId(),merchantMoney,3);
			}else{
				generateBillDetail(payInfo,orderDetail,gp.getStoreId(),orderDetail.getActualPrice(),3);
			}
		}
		//}
	}
	/**
	 * 生成订单详情
	 * @param payInfo
	 * @param orderDetail
	 * @param userId
	 */
	private void generateBillDetail(OrderPayInfo payInfo,OrderDetailsPOJO orderDetail,Long userId,int money,int deliveryType) {
		long currentDate = DateUtils.getCurrentDate();
		UserRevenue revenue = new UserRevenue();
		revenue.setUserId(userId);
		revenue.setOrderId(orderDetail.getId().toString());
		revenue.setPrice(money);
		revenue.setInState(0);
		revenue.setCreateDate(currentDate);
		revenue.setUpdateDate(currentDate);
		revenue.setDeliveryType(deliveryType);
		revenue.setRevenueName(payInfo.getGoodsName());
		int status = addUserRevenue(revenue);
		resultLogger.info("生成订单状态：revenue："+status);
		if(status <= 0){
			resultLogger.error(userId+"生成订单详情失败");
			throw new RuntimeException();
		}
	}

	/**
	 * 生成分享用户账单
	 * @param order
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
	 * 添加用户支出信息
	 * @param out
	 */
	private int addUserOutcome(UserOutcome out) {
		return userOutcomeMapper.insertSelective(out);
	}

	/**
	 * 添加用户收入信息
	 * @param UserRevenue
	 */
	private int addUserRevenue(UserRevenue UserRevenue) {
		return userRevenueMapper.insertSelective(UserRevenue);
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
		GoodsDeliveryTypePOJO deliveryTypePOJO = goodsDeliveryTypeMapper.selectByDeliveryId(4);
		//发货次数
		int deliveryCount = deliveryTypePOJO.getDeliveryCount();
		//单次价格
		int singlePrice = order.getActualPrice() / deliveryCount;
		Long[] deliveryDates = makeDeliveryDate(order.getGmtCreate(),deliveryTypePOJO);
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
		OrderInPieces op = new OrderInPieces();
		//订单编号
		op.setOrderPieceId(UUID.randomUUID().toString());
		op.setPayTime(order.getGmtCreate());
		op.setOrderStatus(0);
		op.setCommentStatus(0);
		op.setActualPrice(order.getActualPrice());
		return op;
	}

	/**
	 * 生成订单发货时间
	 * @param deliveryTypePOJO
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
		OrderDetailsPOJO orderDetail = orderDetailsMapper.getLogistic(orderDetailId);
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
					payStatus = orderDetailsMapper.updateOrderDetailPayStatus(currentDate,Long.parseLong(orderId.substring(1,orderId.length())));
					billLogger.info("订单详情支付"+map.get("attach"));
					//根据订单查询账单详情
//					List<Integer> ids = userRevenueMapper.selectIdByOrderId(orderId.substring(1, orderId.length()));
//					for(Integer id : ids){
//						//修改账单状态
//						int status = userRevenueMapper.updateBillPayStatus(id);
//						billLogger.info("修改订单状态："+status);
//					}
				}else if("B".equals(orderId.subSequence(0, 1))){
					//商户代理费支付回调
					Integer id = Integer.parseInt(orderId.substring(1,orderId.length()));
					payStatus = merchantAgentMapper.updatePayStatus(id);
					//查询商户代理信息
					MerchantAgent m = merchantAgentMapper.selectMerchantInfo(id);
					//修改商品的代理状态
					int goodsStatus = goodsMapper.updateGoodsAgent(m.getMerchantId(),m.getGoodsId());
					if(goodsStatus <= 0){
						billLogger.error("修改商品代理状态失败");
					}
					billLogger.info("商户编号："+map.get("attach")+"支付状态："+payStatus);
				}else if("Z".equals(orderId.subSequence(0,1))){
					String oId = orderId.substring(1,orderId.length());
					billLogger.info("订单编号："+oId);
					List<Long> payIdList = orderDetailsMapper.selectPayId(Long.parseLong(oId));
					for (Long id : payIdList) {
						payStatus = orderDetailsMapper.updateOrderDetailPayStatus(currentDate, id);
						billLogger.info("用户编号："+map.get("attach")+"修改订单状态结果："+payStatus);
						//根据订单查询账单详情
//						List<Integer> ids = userRevenueMapper.selectIdByOrderId(id.toString());
//						for(Integer i : ids){
//							//修改账单状态
//							int status = userRevenueMapper.updateBillPayStatus(i);
//							billLogger.info("修改账单状态："+status);
//						}
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
		OrderDetailsPOJO detailsPOJO = orderDetailsMapper.selectPayOrder(order.getOrderId());
		String payMoney = MoneyFormat.priceFormatString(detailsPOJO.getActualPrice());
		StringBuilder sb = new StringBuilder();
		sb.append("W");
		sb.append(order.getOrderId());
		ResponseResult orderPay = orderPay(sb.toString(),payMoney,order.getCode(),order.getOpenId());
		return orderPay;
	}


	@Override
	public ResponseResult addQuanMingPay(QuanMingPayInfo info) throws BusinessException {
		String userId = info.getUserId().replace("\"","");
		String [] users = {"556686","556682","556681"};
		//商户支付金额
		Double agentPrice = 30000.00;
		for (String str: users) {
			if(str.equals(userId)){
				agentPrice = 0.01;
			}
		}
		Long date = DateUtils.getCurrentDate();
		MerchantAgent agent = new MerchantAgent();
		agent.setMerchantId(Long.parseLong(userId));
		Double price = agentPrice*100;
		agent.setPrice(price.intValue());
		agent.setGoodsId(info.getGoodsId());
		agent.setPayStatus(0);
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
		return orderDetailsMapper.selectAllIncome(id);
	}
	@Override
	public Integer queryEarnedIncome(Long id) {
		return orderDetailsMapper.selectEarnedIncome(id);
	}

	/**
	 * 原生支付
	 * @param orderInfo
	 * @param request
	 * @param response
	 * @return
	 */
	@Override
	public WxPayInfo addWeixinAppPay(OrderInfo orderInfo, HttpServletRequest request, HttpServletResponse response) throws BusinessException, UnsupportedEncodingException, JSONException {
		OrderPayInfo [] orderPayInfos = new OrderPayInfo[orderInfo.getGoodsInfo().size()];
		//订单总价格
		int orderSumPrice = 0;
		//订单编号
		Long uId = Long.parseLong(orderInfo.getUserId().replace("\"", ""));
		//获取订单信息
		orderSumPrice = getOrderInfo(orderInfo, orderPayInfos, orderSumPrice);

		OrdersPOJO orderPOJO = makeOrderPOJO(orderInfo,orderSumPrice);
		int insertStatus = ordersMapper.insertSelective(orderPOJO);
		if(insertStatus <= 0){
			resultLogger.error(orderInfo.getUserId()+"添加订单失败");
			throw new BusinessException("订单添加失败");
		}
		//订单父级编号
		Long orderId = orderPOJO.getId();
		/**
		 * 生成订单详情
		 */
		generateOrderDetails(orderInfo, orderPayInfos, orderId);
		//生成订单后 调用支付
		String payMoney = MoneyFormat.priceFormatString(orderSumPrice);
		StringBuilder payId = new StringBuilder();
		payId.append("Z");
		payId.append(orderId);
		//订单支付
		WxPayInfo info = appPay(request, response, payId.toString(), payMoney, orderInfo.getCode(), orderInfo.getOpenid());
		//修改购物车状态
		updateGoodsCartStatus(orderInfo);
		return info;
	}

	/**
	 * 生成订单后修改购物车状态
	 * @param orderInfo
	 * @throws BusinessException
	 */
	private void updateGoodsCartStatus(OrderInfo orderInfo) throws BusinessException {
		if(orderInfo.getGoodsInfo().get(0).getScId() != null){
			for(int i = 0;i < orderInfo.getGoodsInfo().size();i++){
				Integer status = shoppingCartMapper.updateOrderPayStatus(orderInfo.getGoodsInfo().get(i).getScId());
				if(status <= 0){
					resultLogger.error(orderInfo.getUserId()+"修改购物车状态失败");
					throw new BusinessException("修改购物车状态");
				}
			}
		}
	}

	/**
	 * 根据订单信息生成订单详情
	 * @param orderInfo
	 * @param orderPayInfos
	 * @param orderId
	 * @throws BusinessException
	 */
	private void generateOrderDetails(OrderInfo orderInfo, OrderPayInfo[] orderPayInfos, Long orderId) throws BusinessException {
		for (int i = 0;i < orderPayInfos.length;i++){
			GoodsInfo goodsInfo = orderInfo.getGoodsInfo().get(i);
			OrderDetailsPOJO orderDetail = makeOrderDetail(orderPayInfos[i],goodsInfo,orderInfo,orderId);
			//生成订单详情
			int orderDetailStatus = orderDetailsMapper.insertSelective(orderDetail);
			if(orderDetailStatus <= 0){
				resultLogger.error(orderInfo.getUserId()+"添加订单详情失败");
				throw new BusinessException("订单详情生成失败");
			}
		}
	}

	/**
	 * 获取订单需要的信息
	 * @param orderInfo
	 * @param orderPayInfos
	 * @param orderSumPrice
	 * @return
	 */
	private int getOrderInfo(OrderInfo orderInfo, OrderPayInfo[] orderPayInfos, int orderSumPrice) {
		for(int i = 0;i < orderInfo.getGoodsInfo().size();i++){
			GoodsInfo goodsInfo = orderInfo.getGoodsInfo().get(i);
			OrderPayInfo orderPayInfo = null;
			//查询购物车
			if(null == goodsInfo.getScId() || 0 == goodsInfo.getScId()){
				GoodsPOJO goodsPOJO = goodsMapper.selectShoppingInfo(goodsInfo.getGoodsId());
				orderPayInfo = new OrderPayInfo(goodsPOJO.getGoodsName(), goodsPOJO.getShopPrice()*goodsInfo.getCount());
			}else{
				ShoppingCartPOJO shoppingCart = shoppingCartMapper.selectShoppingCart(goodsInfo.getScId());
				orderPayInfo = new OrderPayInfo(shoppingCart.getGoodsName(), shoppingCart.getGoodsTotalsPrice()*shoppingCart.getGoodsTotals());
			}
			orderPayInfos[i] = orderPayInfo;
			orderSumPrice += orderPayInfo.getGoodsPrice();
		}
		return orderSumPrice;
	}

	private String generateOrder(OrderInfo orderInfo, HttpServletRequest request, HttpServletResponse response, Integer orderSumPrice, Long orderId) throws JSONException, UnsupportedEncodingException {
		String payStr;
		StringBuilder payId = new StringBuilder();
		payId.append("Z");
		payId.append(orderId);
		//生成支付订单
		//payStr = appPay(request, response,payId.toString(),orderSumPrice.toString(),orderInfo.getCode(),orderInfo.getOpenid());
		return "";
	}

	/**
	 * 根据用户提交的信息生成单笔订单详情
	 * @param
	 * @param
	 */
	private OrderDetailsPOJO makeOrderDetail(GoodsPOJO goodsPOJO,OrderInfo orderInfo,GoodsInfo goodsInfo) {
		long currentTime = DateUtils.getCurrentDate();
		//根据地址编号查询地址信息
		UserAddress address = userAddress.selectAddressById(orderInfo.getAddressId());
		//查询捕手信息
		UsersPOJO userPOJO = usersMapper.selectParentIdByUserId(Long.parseLong(orderInfo.getUserId().replace("\"","")));

		OrderDetailsPOJO od = new OrderDetailsPOJO();

		od.setUserId(Long.parseLong(orderInfo.getUserId().replace("\"", "")));
		od.setGoodsId(goodsPOJO.getId());
		od.setAddressId(orderInfo.getAddressId());
		od.setPaymentMethod(orderInfo.getPaymentMethod());
		//单笔订单支付的价格
		Integer orderMoney = goodsPOJO.getShopPrice()*goodsInfo.getCount();
		od.setActualPrice(orderMoney);
		od.setPayTime(currentTime);
		od.setOrderStatus(10);
		//判断是否是捕手
		if(userPOJO.getParentId() !=null && userPOJO.getParentId()!=0) {
			od.setFetcherId(userPOJO.getParentId());
			//计算捕手需要获取的提成
			Double fetcherMoney = orderMoney * Constant.FECTHER_COMPONENT;
			od.setFetcherPrice(fetcherMoney.intValue());
		}
		//查询是否有代理商户
		GoodsPOJO gp = goodsMapper.selectProxyMerchantByGoodsId(goodsPOJO.getId());
		if(gp.getMerchantProxyId()!=null && gp.getMerchantProxyId()!=0){
			od.setAgentId(gp.getMerchantProxyId());
			//计算商户获取的提成
			Double agentMoney = orderMoney*Constant.AGENT_COMPONENT;
			od.setAgentPrice(agentMoney.intValue());
		}
		if(null == orderInfo.getPaySign() || "".equals(orderInfo.getPaySign())){
			if(userPOJO.getParentId() !=null && userPOJO.getParentId()!=0){
				od.setPayPlateform(1);
			}else{
				od.setPayPlateform(0);
			}
		}else{
			od.setPayPlateform(2);
		}
		od.setCount(goodsInfo.getCount());
		od.setCreateDate(currentTime);
		od.setUpdateDate(currentTime);
		od.setName(address.getName());
		od.setTel(address.getTel());
		od.setCity(address.getCity());
		od.setAddress(address.getAddress());
		return od;
	}

	/**
	 * 根据购物车信息生成订单详情
	 * @param
	 * @param
	 */
	private OrderDetailsPOJO makeOrderDetail(ShoppingCartPOJO cart,OrderInfo orderInfo,Long orderId) {
		long currentTime = DateUtils.getCurrentDate();
		//根据地址编号查询地址信息
		UserAddress address = userAddress.selectAddressById(orderInfo.getAddressId());
		//查询捕手信息
		UsersPOJO userPOJO = usersMapper.selectParentIdByUserId(Long.parseLong(orderInfo.getUserId().replace("\"","")));

		OrderDetailsPOJO od = new OrderDetailsPOJO();
		//订单编号
		od.setOrderId(orderId);
		od.setUserId(Long.parseLong(orderInfo.getUserId().replace("\"", "")));
		od.setGoodsId(cart.getGoodsId());
		od.setAddressId(orderInfo.getAddressId());
		od.setPaymentMethod(orderInfo.getPaymentMethod());
		//单笔订单支付的价格
		Integer orderMoney = cart.getGoodsTotalsPrice()*cart.getGoodsTotals();
		od.setActualPrice(orderMoney);
		od.setPayTime(currentTime);
		od.setOrderStatus(10);
		//判断是否是捕手
		if(userPOJO.getParentId() !=null && userPOJO.getParentId()!=0) {
			od.setFetcherId(userPOJO.getParentId());
			//计算捕手需要获取的提成
			Double fetcherMoney = orderMoney * Constant.FECTHER_COMPONENT;
			od.setFetcherPrice(fetcherMoney.intValue());
		}
		//查询是否有代理商户
		GoodsPOJO gp = goodsMapper.selectProxyMerchantByGoodsId(cart.getGoodsId());
		if(gp.getMerchantProxyId()!=null && gp.getMerchantProxyId()!=0){
			od.setAgentId(gp.getMerchantProxyId());
			//计算商户获取的提成
			Double agentMoney = orderMoney*Constant.AGENT_COMPONENT;
			od.setAgentPrice(agentMoney.intValue());
		}
		if(null == orderInfo.getPaySign() || "".equals(orderInfo.getPaySign())){
			if(userPOJO.getParentId() !=null && userPOJO.getParentId()!=0){
				od.setPayPlateform(1);
			}else{
				od.setPayPlateform(0);
			}
		}else{
			od.setPayPlateform(2);
		}
		od.setCount(cart.getGoodsTotals());
		od.setCreateDate(currentTime);
		od.setUpdateDate(currentTime);
		od.setName(address.getName());
		od.setTel(address.getTel());
		od.setCity(address.getCity());
		od.setAddress(address.getAddress());
		return od;
	}

	public WxPayInfo appPay(HttpServletRequest request, HttpServletResponse response,String payId,String payMoney,String code,String openId) throws JSONException, UnsupportedEncodingException {
		//Map<Object,Object> resInfo = new HashMap<Object, Object>();
		WxPayInfo info = new WxPayInfo();
		//接收财付通通知的URL
		String notify_url = "https://freshfun.meiguoyouxian.com/FreshFun/payCallback.do";

		//---------------生成订单号 开始------------------------
		//当前时间 yyyyMMddHHmmss
		String currTime = TenpayUtil.getCurrTime();
		//8位日期
		String strTime = currTime.substring(8, currTime.length());
		//四位随机数
		String strRandom = TenpayUtil.buildRandom(4) + "";
		//10位序列号,可以自行调整。
		String strReq = strTime + strRandom;
		//订单号，此处用时间加随机数生成，商户根据自己情况调整，只要保持全局唯一就行
		String out_trade_no = strReq;
		//---------------生成订单号 结束------------------------

		PackageRequestHandler packageReqHandler = new PackageRequestHandler(request, response);//生成package的请求类
		PrepayIdRequestHandler prepayReqHandler = new PrepayIdRequestHandler(request, response);//获取prepayid的请求类
		ClientRequestHandler clientHandler = new ClientRequestHandler(request, response);//返回客户端支付参数的请求类
		//packageReqHandler.setKey(ConstantUtil.PARTNER_KEY);

		int retcode ;
		String retmsg = "";
		String xml_body = "";
		//获取token值

		String token = AccessTokenRequestHandler.getAccessToken();

		resultLogger.info("获取token------值 " + token);

		if (!"".equals(token)) {
			//设置package订单参数
			//packageReqHandler.setParameter("bank_type", "WX");//银行渠道
			/*packageReqHandler.setParameter("body", "悦选美食"); //商品描述
			packageReqHandler.setParameter("notify_url", notify_url); //接收财付通通知的URL

			packageReqHandler.setParameter("out_trade_no", payId); //商家订单号
			packageReqHandler.setParameter("total_fee", payMoney); //商品金额,以分为单位
            packageReqHandler.setParameter("spbill_create_ip",request.getRemoteAddr()); //订单生成的机器IP，指用户浏览器端IP
			packageReqHandler.setParameter("fee_type", "1"); //币种，1人民币   66
			//packageReqHandler.setParameter("input_charset", "UTF-8"); //字符编码
            packageReqHandler.setParameter("trade_type","APP");*/


			//获取package包
			//String packageValue = packageReqHandler.getRequestURL();
			//resInfo.put("package", packageValue);

			//resultLogger.info("获取package------值 " + packageValue);



			String noncestr = WXUtil.getNonceStr();
			String timestamp = WXUtil.getTimeStamp();
			String traceid = "";
			////设置获取prepayid支付参数
			prepayReqHandler.setParameter("appid",ConstantUtil.APP_ID);
			//prepayReqHandler.setParameter("appkey", ConstantUtil.APP_KEY);
            prepayReqHandler.setParameter("mch_id", ConstantUtil.PARTNER); //商户号
			prepayReqHandler.setParameter("nonce_str", noncestr);
            prepayReqHandler.setParameter("body", "sss");
            prepayReqHandler.setParameter("notify_url", notify_url);
            prepayReqHandler.setParameter("out_trade_no", payId);
            prepayReqHandler.setParameter("total_fee", "1000"); //商品金额,以分为单位
            prepayReqHandler.setParameter("spbill_create_ip",request.getRemoteAddr()); //订单生成的机器IP，指用户浏览器端IP
            prepayReqHandler.setParameter("fee_type", "1"); //币种，1人民币   66
            prepayReqHandler.setParameter("trade_type","APP");
			//生成获取预支付签名
			String sign = prepayReqHandler.createMD5Sign();
			//增加非参与签名的额外参数
			prepayReqHandler.setParameter("sign", sign);
			/*prepayReqHandler.setParameter("sign_method",
					ConstantUtil.SIGN_METHOD);*/
			String gateUrl = ConstantUtil.GATEURL;
			prepayReqHandler.setGateUrl(gateUrl);

			//获取prepayId
			String prepayid = prepayReqHandler.sendPrepay();

			resultLogger.info("获取prepayid------值 " + prepayid);

			//吐回给客户端的参数
			if (null != prepayid && !"".equals(prepayid)) {
				//输出参数列表
				clientHandler.setParameter("appid", ConstantUtil.APP_ID);
				clientHandler.setParameter("partnerid", ConstantUtil.PARTNER);
				clientHandler.setParameter("prepayid", prepayid);
				clientHandler.setParameter("package", "Sign=WXPay");
				clientHandler.setParameter("noncestr", noncestr);
				clientHandler.setParameter("timestamp", timestamp);
				//生成签名
				sign = clientHandler.createMD5Sign();
				//clientHandler.setParameter("sign", sign);

				//xml_body = clientHandler.getXmlBody();
				//resInfo.put("entity", xml_body);

				info.setAppid(ConstantUtil.APP_ID);
				info.setPartnerid(ConstantUtil.PARTNER);
				info.setPrepayid(prepayid);
				info.setPackageInfo("Sign=WXPay");
				info.setNoncestr(noncestr);
				info.setTimestamp(timestamp);
				info.setSign(sign);
				retcode = 0;
				retmsg = "OK";
			} else {
				retcode = -2;
				retmsg = "错误：获取prepayId失败";
			}
		} else {
			retcode = -1;
			retmsg = "错误：获取不到Token";
		}

//		resInfo.put("retcode", retcode);
//		resInfo.put("retmsg", retmsg);
//		String strJson = JSON.toJSONString(resInfo);
		return info;
	}
}