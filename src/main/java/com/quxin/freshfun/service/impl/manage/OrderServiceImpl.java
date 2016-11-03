package com.quxin.freshfun.service.impl.manage;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quxin.freshfun.common.Constant;
import com.quxin.freshfun.common.FreshFunEncoder;
import com.quxin.freshfun.model.outparam.UserInfoOutParam;
import com.quxin.freshfun.service.impl.wechat.ClientRequestHandler;
import com.quxin.freshfun.service.impl.wechat.PrepayIdRequestHandler;
import com.quxin.freshfun.dao.*;
import com.quxin.freshfun.model.*;
import com.quxin.freshfun.model.outparam.WxPayInfo;
import com.quxin.freshfun.service.order.OrderService;
import com.quxin.freshfun.service.user.UserBaseService;
import com.quxin.freshfun.utils.*;
import com.quxin.freshfun.utils.weixinPayUtils.ConstantUtil;
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
    private UserBaseService userBaseService;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private GoodsLimitMapper goodsLimitMapper;
	@Autowired
	private ShoppingCartMapper shoppingCartMapper;
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
	private final Logger logger = LoggerFactory.getLogger(getClass());

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
		Long uId = orderInfo.getUserId();

		for(int i = 0;i < orderInfo.getGoodsInfo().size();i++){
			GoodsInfo goodsInfo = orderInfo.getGoodsInfo().get(i);
			OrderPayInfo orderPayInfo = null;
			//查询购物车
			if(null == goodsInfo.getScId() || 0 == goodsInfo.getScId()){
				GoodsPOJO goodsPOJO = goodsMapper.selectShoppingInfo(goodsInfo.getGoodsId());
				orderPayInfo = new OrderPayInfo(goodsPOJO.getGoodsName(), goodsPOJO.getShopPrice(),goodsInfo.getCount());
			}else{
				ShoppingCartPOJO shoppingCart = shoppingCartMapper.selectShoppingCart(goodsInfo.getScId());
				orderPayInfo = new OrderPayInfo(shoppingCart.getGoodsName(), shoppingCart.getGoodsTotalsPrice(),shoppingCart.getGoodsTotals());
			}
			orderPayInfos[i] = orderPayInfo;
			orderSumPrice += orderPayInfo.getGoodsPrice()*orderPayInfo.getTotal();
		}

		OrdersPOJO orderPOJO = makeOrderPOJO(orderInfo,orderSumPrice);
		int insertStatus = ordersMapper.insertSelective(orderPOJO);
		if(insertStatus <= 0){
			resultLogger.error(orderInfo.getUserId()+"添加订单失败");
			throw new BusinessException("订单添加失败");
		}
		//订单父级编号
		Long orderId = orderPOJO.getId();
		//订单详情编号
		Long orderDetailsId = null;
		for (int i = 0;i < orderPayInfos.length;i++){
			GoodsInfo goodsInfo = orderInfo.getGoodsInfo().get(i);
			OrderDetailsPOJO orderDetail = makeOrderDetail(orderPayInfos[i],goodsInfo,orderInfo,orderId);
			//生成订单详情
			int orderDetailStatus = orderDetailsMapper.insertSelective(orderDetail);
			if(orderDetailStatus <= 0){
				resultLogger.error(orderInfo.getUserId()+"添加订单详情失败");
				throw new BusinessException("订单详情生成失败");
			}else{
				orderDetailsId = orderDetail.getId();
			}
		}
		//获取用户openId
        String openId = userBaseService.queryUserInfoByUserId(uId).getOpenId();
		//生成订单后 调用支付
		String payMoney = MoneyFormat.priceFormatString(orderSumPrice);
		StringBuilder payId = new StringBuilder();
		payId.append("Z");
		payId.append(orderId);
		ResponseResult payResult = orderPay(payId.toString(),payMoney,orderInfo.getCode(),openId);
		payResult.setOrderId(orderDetailsId);
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
	@Override
	public String getOpenId(Long userId){
        return userBaseService.queryUserInfoByUserId(userId).getOpenId();
	}

	/**
	 * 添加限时购订单
	 * @param orderInfo
	 */
	public ResponseResult addLimitOrder(OrderInfo orderInfo) {

		return null;
	}

	/**
	 * 根据用户提交的信息生成订单详情
	 * @param payInfo
	 * @param goodsInfo
	 */
	private OrderDetailsPOJO makeOrderDetail(OrderPayInfo payInfo, GoodsInfo goodsInfo,OrderInfo orderInfo,Long orderId) {
		long currentTime = DateUtils.getCurrentDate();
        long uid = orderInfo.getUserId();
		//根据地址编号查询地址信息
		UserAddress address = userAddress.selectAddressById(orderInfo.getAddressId());
		//查询捕手信息
		Long fetcherId = null;
		UserInfoOutParam userInfo = userBaseService.queryUserInfoByUserId(uid);
		if(userInfo != null){
			fetcherId = userInfo.getFetcherId();
		}
		OrderDetailsPOJO od = new OrderDetailsPOJO();
		//订单编号
		od.setOrderId(orderId);
		od.setUserId(orderInfo.getUserId());
		od.setGoodsId(goodsInfo.getGoodsId());
		od.setAddressId(orderInfo.getAddressId());
		od.setPaymentMethod(orderInfo.getPaymentMethod());
		/**
		 * 订单支付金额
		 */
		Integer orderActualPrice = payInfo.getGoodsPrice()*payInfo.getTotal();
		od.setActualPrice(orderActualPrice);
		od.setPayPrice(payInfo.getGoodsPrice());
		od.setPayTime(currentTime);
		od.setOrderStatus(10);
		//判断是否有上级
		if(fetcherId !=null && fetcherId != 0) {
			od.setFetcherId(fetcherId);
			//计算捕手需要获取的提成
			Double fetcherMoney = orderActualPrice * Constant.FECTHER_COMPONENT;
			od.setFetcherPrice(fetcherMoney.intValue());
			od.setPayPlateform(1);
		}else{
			String sign = orderInfo.getFetcherId();
			if (sign != null && !"".equals(sign)){
				Long id = FreshFunEncoder.urlToId(sign);
				if(id != null) {
                    //判断是否是捕手
                    boolean bool = userBaseService.checkIsFetcherByUserId(id);
                    if(bool){
                        int status = userBaseService.modifyFetcherForUser(uid,id);
                        if (status <= 0) {
							logger.error("添加分享标记失败");
                        }else{
                            //添加分享提成
                            od.setFetcherId(id);
                            //计算捕手需要获取的提成
                            Double fetcherMoney = orderActualPrice * Constant.FECTHER_COMPONENT;
                            od.setFetcherPrice(fetcherMoney.intValue());
							od.setPayPlateform(1);
                        }
                    }
				}
			}
		}
		//查询是否有代理商户
		GoodsPOJO gp = goodsMapper.selectProxyMerchantByGoodsId(goodsInfo.getGoodsId());
		if(gp.getMerchantProxyId()!=null && gp.getMerchantProxyId()!=0){
			od.setAgentId(gp.getMerchantProxyId());
			//计算商户获取的提成
			Double agentMoney = orderActualPrice*Constant.AGENT_COMPONENT;
			od.setAgentPrice(agentMoney.intValue());
		}
		if(null != orderInfo.getPaySign() && orderInfo.getPaySign() != 0){
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
	 * 订单实体
	 * @param orderInfo
	 * @return
	 */
	private OrdersPOJO makeOrderPOJO(OrderInfo orderInfo,Integer sumPrice) {
		long currentDate = DateUtils.getCurrentDate();
		OrdersPOJO order = new OrdersPOJO();
		order.setUserId(orderInfo.getUserId());
		order.setGmtCreate(currentDate);
		order.setGmtModified(currentDate);
		order.setPaymentMethod(orderInfo.getPaymentMethod());
		order.setOrderCount(orderInfo.getGoodsInfo().size());
		order.setOrderStatus(0);
		order.setPayStatus(0);
		order.setUserId(orderInfo.getUserId());
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
	 * 添加用户支出信息
	 * @param out
	 */
	private int addUserOutcome(UserOutcome out) {
		return userOutcomeMapper.insertSelective(out);
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
			logger.info("用户编号："+map.get("attach")+"支付回调结果："+map.toString());
			if("SUCCESS".equals(map.get("result_code"))){
				int payStatus = 0;
				Long currentDate = DateUtils.getCurrentDate();
				String orderId = map.get("attach");
				//微信回传的订单编号
				String transactionId = map.get("transaction_id");
				if("W".equals(orderId.subSequence(0, 1))){
					payStatus = orderDetailsMapper.updateOrderDetailPayStatus(currentDate,Long.parseLong(orderId.substring(1,orderId.length())),transactionId);
					billLogger.info("订单详情支付"+map.get("attach"));
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
						payStatus = orderDetailsMapper.updateOrderDetailPayStatus(currentDate, id,transactionId);
						billLogger.info("用户编号："+map.get("attach")+"修改订单状态结果："+payStatus);
					}
				}
				return payStatus;
			}

		} catch (DocumentException e1) {
			logger.error("获取微信回调信息失败",e1);
		}finally{
			try {
				in.close();
			} catch (IOException e) {
				logger.error("微信回调流关闭失败",e);
			}
		}
		return 0;
	}
	/**
	 * 订单支付
	 */
	@Override
	public ResponseResult awaitPayOrder(OrderPayPOJO order,Long userId) {
		OrderDetailsPOJO detailsPOJO = orderDetailsMapper.selectPayOrder(order.getOrderId());
		String payMoney = MoneyFormat.priceFormatString(detailsPOJO.getActualPrice());
		//获取用户openId
        String openId = userBaseService.queryUserInfoByUserId(userId).getOpenId();
		StringBuilder sb = new StringBuilder();
		sb.append("W");
		sb.append(order.getOrderId());
		ResponseResult orderPay = orderPay(sb.toString(),payMoney,order.getCode(),openId);
		return orderPay;
	}

    /**
     * App订单支付
     * @param orderId
     * @return
     */
    @Override
    public WxPayInfo appOrderPay(String orderId,HttpServletRequest request,HttpServletResponse response) throws UnsupportedEncodingException, JSONException {
        OrderDetailsPOJO detailsPOJO = orderDetailsMapper.selectPayOrder(orderId);
        //String payMoney = MoneyFormat.priceFormatString(detailsPOJO.getActualPrice());
        StringBuilder sb = new StringBuilder();
        sb.append("W");
        sb.append(orderId);
        WxPayInfo payInfo = appPay(request, response, sb.toString(), detailsPOJO.getActualPrice().toString());
        return payInfo;
    }


	@Override
	public ResponseResult addQuanMingPay(QuanMingPayInfo info,Long userId) throws BusinessException {
		//商户支付金额
		Double agentPrice = 30000.00;
		Long date = DateUtils.getCurrentDate();
		MerchantAgent agent = new MerchantAgent();
		agent.setMerchantId(userId);
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
			//获取用户openId
            String openId = userBaseService.queryUserInfoByUserId(userId).getOpenId();
			//支付
			StringBuilder sb = new StringBuilder();
			sb.append("B");
			sb.append(agent.getId());
			//发起支付请求
			ResponseResult payResult = orderPay(sb.toString(),agentPrice.toString(),info.getCode(),openId);
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
		Integer orderSumPrice = 0;
		//订单编号
		Long uId = orderInfo.getUserId();
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
		StringBuilder payId = new StringBuilder();
		payId.append("Z");
		payId.append(orderId);
		//订单支付
		WxPayInfo info = appPay(request, response, payId.toString(), orderSumPrice.toString());
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
					logger.error(orderInfo.getUserId()+"修改购物车状态失败");
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
		if(orderPayInfos != null && orderPayInfos.length > 0) {
			for (int i = 0; i < orderPayInfos.length; i++) {
				GoodsInfo goodsInfo = orderInfo.getGoodsInfo().get(i);
				OrderDetailsPOJO orderDetail = makeOrderDetail(orderPayInfos[i], goodsInfo, orderInfo, orderId);
				//生成订单详情
				int orderDetailStatus = orderDetailsMapper.insertSelective(orderDetail);
				if (orderDetailStatus <= 0) {
					logger.error(orderInfo.getUserId() + "添加订单详情失败");
					throw new BusinessException("订单详情生成失败");
				}
			}
		}else{
			logger.error("用户生成订单详情失败");
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
		if(orderInfo.getGoodsInfo() != null && orderInfo.getGoodsInfo().size() > 0) {
			for (int i = 0; i < orderInfo.getGoodsInfo().size(); i++) {
				GoodsInfo goodsInfo = orderInfo.getGoodsInfo().get(i);
				OrderPayInfo orderPayInfo = null;
				//查询购物车
				if (null == goodsInfo.getScId() || 0 == goodsInfo.getScId()) {
					GoodsPOJO goodsPOJO = goodsMapper.selectShoppingInfo(goodsInfo.getGoodsId());
					orderPayInfo = new OrderPayInfo(goodsPOJO.getGoodsName(), goodsPOJO.getShopPrice(),goodsInfo.getCount());
				} else {
					ShoppingCartPOJO shoppingCart = shoppingCartMapper.selectShoppingCart(goodsInfo.getScId());
					orderPayInfo = new OrderPayInfo(shoppingCart.getGoodsName(), shoppingCart.getGoodsTotalsPrice(),shoppingCart.getGoodsTotals());
				}
				if(orderPayInfo != null) {
					orderPayInfos[i] = orderPayInfo;
					orderSumPrice += orderPayInfo.getGoodsPrice()*orderPayInfo.getTotal();
				}else{
					logger.error("生成订单时获取商品的信息为空");
				}
			}
		}else{
			logger.error("生成订单时用户传递商品信息为空");
		}
		return orderSumPrice;
	}

	public WxPayInfo appPay(HttpServletRequest request, HttpServletResponse response,String payId,String payMoney) throws JSONException, UnsupportedEncodingException {
		WxPayInfo info = new WxPayInfo();
		//接收财付通通知的URL
		String notify_url = "https://www.freshfun365.com/FreshFun/payCallback.do";

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

		PrepayIdRequestHandler prepayReqHandler = new PrepayIdRequestHandler(request, response);//获取prepayid的请求类
		ClientRequestHandler clientHandler = new ClientRequestHandler(request, response);//返回客户端支付参数的请求类

		int retcode ;
		String retmsg = "";
		String xml_body = "";
		//获取token值

		//String token = AccessTokenRequestHandler.getAccessToken();

		String noncestr = WXUtil.getNonceStr();
		String timestamp = WXUtil.getTimeStamp();
		String traceid = "";
		////设置获取prepayid支付参数
		prepayReqHandler.setParameter("appid",ConstantUtil.APP_ID);
		prepayReqHandler.setParameter("body", "悦选美食"); //商品描述
		prepayReqHandler.setParameter("attach", payId);
		prepayReqHandler.setParameter("mch_id", ConstantUtil.PARTNER); //商户号
		prepayReqHandler.setParameter("nonce_str", noncestr);
		prepayReqHandler.setParameter("notify_url", notify_url);
		prepayReqHandler.setParameter("out_trade_no", payId);
		prepayReqHandler.setParameter("total_fee",payMoney); //商品金额,以分为单位
		prepayReqHandler.setParameter("spbill_create_ip",request.getRemoteAddr()); //订单生成的机器IP，指用户浏览器端IP
		prepayReqHandler.setParameter("fee_type", "1"); //币种，1人民币   66
		prepayReqHandler.setParameter("trade_type","APP");
		//生成获取预支付签名
		String sign = prepayReqHandler.createMD5Sign();
		//增加非参与签名的额外参数
		prepayReqHandler.setParameter("sign", sign);
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
		return info;
	}


    @Override
    public Integer updatePayPrice() {
        return null;
    }
}