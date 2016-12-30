package com.quxin.freshfun.service.impl.manage;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.quxin.freshfun.common.Constant;
import com.quxin.freshfun.common.FreshFunEncoder;
import com.quxin.freshfun.model.goods.PromotionGoodsPOJO;
import com.quxin.freshfun.model.outparam.UserInfoOutParam;
import com.quxin.freshfun.model.param.GoodsParam;
import com.quxin.freshfun.model.param.OrderParam;
import com.quxin.freshfun.model.pojo.PromotionPOJO;
import com.quxin.freshfun.model.pojo.address.AddressPOJO;
import com.quxin.freshfun.model.pojo.goods.GoodsBasePOJO;
import com.quxin.freshfun.service.address.AddressService;
import com.quxin.freshfun.service.address.AddressUtilService;
import com.quxin.freshfun.service.impl.mail.MailSender;
import com.quxin.freshfun.service.impl.mail.MailSenderFactory;
import com.quxin.freshfun.service.impl.wechat.ClientRequestHandler;
import com.quxin.freshfun.service.impl.wechat.PrepayIdRequestHandler;
import com.quxin.freshfun.dao.*;
import com.quxin.freshfun.model.*;
import com.quxin.freshfun.model.outparam.WxPayInfo;
import com.quxin.freshfun.service.order.OrderService;
import com.quxin.freshfun.service.promotion.PromotionService;
import com.quxin.freshfun.service.user.UserBaseService;
import com.quxin.freshfun.service.wechat.WeChatService;
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
import org.springframework.util.StringUtils;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
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
	private UserBaseMapper userBaseMapper;
	@Autowired
	private GoodsMapper goodsMapper;
	@Autowired
	private GoodsBaseMapper goodsBaseMapper;
	@Autowired
	private ShoppingCartMapper shoppingCartMapper;
	@Autowired
	private MerchantAgentMapper merchantAgentMapper;
	@Autowired
	private PromotionService promotionService;
	@Autowired
	private UserAddressMapper userAddress;
	@Autowired
	private AddressUtilService addressUtilService;
	@Autowired
	private AddressService addressService;
	@Autowired
	private WeChatService weChatService;


	private Logger resultLogger = LoggerFactory.getLogger("info_log");
	private Logger billLogger = LoggerFactory.getLogger("BILL");
	private final Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 添加订单
	 * @throws BusinessException
	 */
	@Override
	public WxPayInfo addOrder(OrderInfo orderInfo,HttpServletRequest request) throws BusinessException, JSONException {
		if(orderInfo == null || orderInfo.getGoodsInfo() == null || orderInfo.getGoodsInfo().get(0) == null)
			throw new BusinessException("添加订单时参数错误");
//		OrderPayInfo [] orderPayInfos = new OrderPayInfo[orderInfo.getGoodsInfo().size()];
		//订单总价格
		Integer orderSumPrice = 0;
		//订单编号
		Long uId = orderInfo.getUserId();

		List<OrderPayInfo> goodsBaseList = null;
		if(orderInfo.getGoodsInfo().get(0).getScId() == null || 0 == orderInfo.getGoodsInfo().get(0).getScId()){
			goodsBaseList = getPayGoodsInfo(orderInfo);
			//设置商品活动信息
			setGoodsDiscountInfo(goodsBaseList);
		} else {
			//购物车
			goodsBaseList = getShoppingCartInfo(orderInfo);
			//设置商品活动信息
			setGoodsDiscountInfo(goodsBaseList);
		}

		OrdersPOJO orderPOJO = makeOrderPOJO(orderInfo,goodsBaseList);
		orderSumPrice = orderPOJO.getActualPrice();
		//添加父级订单
		insertOrderParent(orderInfo, orderPOJO);
		//订单父级编号
		Long orderId = orderPOJO.getId();

		List<OrderDetailsPOJO> orderDetailList = makeOrderDetail(goodsBaseList, orderInfo, orderId);
		insertOrder(orderDetailList);

		//获取用户openId
        String openId = userBaseService.queryUserInfoByUserId(uId).getOpenId();
		//生成订单后 调用支付
		StringBuilder payId = new StringBuilder();
		payId.append("Z");
		payId.append(orderId);
		//微信支付
		WxPayInfo wxPayInfo = weChatService.wzPay(request, payId.toString(), orderSumPrice.toString(), openId);
		wxPayInfo.setOrderId(orderDetailList.get(0).getId());
		//保存二维码支付url
		savePayUrl(wxPayInfo,orderId);
		//修改支付状态
		modifyShoppingCartState(orderInfo);
		return wxPayInfo;
	}

	/**
	 * 查询商品信息
	 * @param orderInfo 订单信息
	 */
	private List<OrderPayInfo> getShoppingCartInfo(OrderInfo orderInfo) {
		List<OrderPayInfo> goodsBaseList = new ArrayList<>();
		if(orderInfo != null){
			for (GoodsInfo info : orderInfo.getGoodsInfo()) {
				ShoppingCartPOJO shoppingCart = shoppingCartMapper.selectShoppingCart(info.getScId());
				if(shoppingCart != null) {
					OrderPayInfo payInfo = new OrderPayInfo();
					GoodsParam goodsParam = goodsBaseMapper.selectGoodsByGoodsId(shoppingCart.getGoodsId().longValue());
					payInfo.setGoodsPrice(goodsParam.getShopPrice());
					payInfo.setGoodsId(shoppingCart.getGoodsId().longValue());
					payInfo.setTotal(shoppingCart.getGoodsTotals());
					payInfo.setGoodsName(shoppingCart.getGoodsName());
					goodsBaseList.add(payInfo);
				}
			}
		}
		return goodsBaseList;
	}

	/**
	 * 设置订单优惠信息
	 * @param goodsBaseList 订单商品信息集合
	 */
	private void setGoodsDiscountInfo(List<OrderPayInfo> goodsBaseList) throws BusinessException {
		List<PromotionPOJO> promotionList = promotionService.selectStockByGoodsList(goodsBaseList);
		checkStock(goodsBaseList, promotionList);
		for (OrderPayInfo payInfo : goodsBaseList) {
			switch (payInfo.getIsDiscount()){
				case 1:
					//修改商品库存
					updateGoodsStock(payInfo);
					//修改销量
					updateSaleNum(payInfo);
					break;
				case 2:
					//修改限量购库存
					updateDiscountStock(payInfo);
					break;
				case 3:
					throw new BusinessException("商品库存不足");
			}
		}
		//设置商品优惠价格
		List<PromotionGoodsPOJO> promotionGoods = promotionService.queryLimitedGoods(goodsBaseList);
		for (OrderPayInfo payInfo: goodsBaseList) {
			for (PromotionGoodsPOJO promotion : promotionGoods) {
				if(payInfo.getIsDiscount() == 2) {
					if(payInfo.getGoodsId().equals(promotion.getGoodsId())){
						payInfo.setGoodsPrice(promotion.getDiscountPrice().intValue());
					}
				}
			}
		}
	}

	/**
	 * 增加销量
	 * @param payInfo 支付信息
	 */
	private void updateSaleNum(OrderPayInfo payInfo) {
		Integer saleStatus = goodsBaseMapper.updateGoodsSaleNum(payInfo.getGoodsId());
		if (saleStatus <= 0) {
            logger.error("修改商品销量失败");
        }
	}

	/**
	 * 修改优惠商品库存
	 * @param payInfo 支付信息
 	 * @throws BusinessException
	 */
	private void updateDiscountStock(OrderPayInfo payInfo) throws BusinessException {
		Map<String,Object> map = new HashMap<>();
		map.put("stock",payInfo.getTotal());
		map.put("promotionId",payInfo.getPromotionId());
		int state = promotionService.updateStockById(map);
		if(state <= 0){
            throw new BusinessException("批量修改限量库存失败");
        }
	}

	/**
	 * 修改商品库存
	 * @param payInfo 支付信息
	 */
	private void updateGoodsStock(OrderPayInfo payInfo) throws BusinessException {
		Map<String,Object> map = new HashMap<>();
		map.put("stock",payInfo.getTotal());
		map.put("goodsId",payInfo.getGoodsId());
		int state = goodsBaseMapper.updateGoodsStock(map);
		if(state <= 0)
			throw new BusinessException("修改商品库存失败");
	}

	/**
	 * 判断库存
	 * @param goodsBaseList 商品集合
	 * @param promotionList	优惠商品集合
	 * @return
	 */
	private void checkStock(List<OrderPayInfo> goodsBaseList, List<PromotionPOJO> promotionList) {
		if(promotionList != null && promotionList.size() > 0) {
			for (OrderPayInfo payInfo : goodsBaseList) {
					for (PromotionPOJO promotion : promotionList) {
						if (payInfo.getGoodsId().equals(promotion.getObjectId())) {
							if ((payInfo.getTotal() - promotion.getStock()) <= 0) {
								payInfo.setIsDiscount(2);
								payInfo.setPromotionId(promotion.getId());
							} else {
								payInfo.setIsDiscount(3);
							}
							if(promotion.getStock() == 0){
								findGoodsStock(payInfo);
							}
						}
					}
			}
			for (OrderPayInfo payInfo : goodsBaseList) {
				if(payInfo.getIsDiscount() == 0){
					//查询单个商品库存
					findGoodsStock(payInfo);
				}
			}
		} else {
			List<GoodsBasePOJO> orderPayList = goodsBaseMapper.selectBatchStock(goodsBaseList);
			if(orderPayList != null){
				for (OrderPayInfo orderPayInfo : goodsBaseList) {
					for (GoodsBasePOJO goodsBase: orderPayList) {
						if(orderPayInfo.getGoodsId().equals(goodsBase.getId())) {
							if (orderPayInfo.getTotal() - goodsBase.getStockNum() < 0) {
								orderPayInfo.setIsDiscount(1);
							} else {
								orderPayInfo.setIsDiscount(3);
							}
						}
					}
				}
			}
		}
	}

	/**
	 * 查询普通商品库存
	 * @param payInfo 订单支付信息
	 */
	private void findGoodsStock(OrderPayInfo payInfo) {
		GoodsBasePOJO goodsBase = goodsBaseMapper.selectStockByGoodsId(payInfo.getGoodsId());
		if(payInfo.getGoodsId().equals(goodsBase.getId())) {
            if (payInfo.getTotal() - goodsBase.getStockNum() <= 0) {
                payInfo.setIsDiscount(1);
            } else {
                payInfo.setIsDiscount(3);
            }
        }
	}

	/**
	 * 添加订单
	 * @param orderDetailList 订单数据
	 */
	private void insertOrder(List<OrderDetailsPOJO> orderDetailList) throws BusinessException {
		for (OrderDetailsPOJO order : orderDetailList) {
			int state = orderDetailsMapper.insertSelective(order);
			if (state <= 0){
				throw new BusinessException("订单添加失败");
			}
		}
	}

	/**
	 * 添加父级订单
	 * @param orderInfo 客户单提交支付信息
	 * @param orderPOJO 订单数据
	 * @throws BusinessException
	 */
	private void insertOrderParent(OrderInfo orderInfo, OrdersPOJO orderPOJO) throws BusinessException {
		int insertStatus = ordersMapper.insertSelective(orderPOJO);
		if(insertStatus <= 0){
			resultLogger.error(orderInfo.getUserId()+"添加订单失败");
			throw new BusinessException("父级订单添加失败");
		}
	}

	/**
	 * 获取支付商品信息
	 * @param orderInfo
	 */
	private List<OrderPayInfo> getPayGoodsInfo(OrderInfo orderInfo) {
		List<OrderPayInfo> goodsBaseList = new ArrayList<>();
		if(orderInfo != null) {
			//获取支付商品信息
			goodsBaseList = goodsBaseMapper.selectGoodsInfoByGoodsList(orderInfo.getGoodsInfo());
			if (goodsBaseList != null) {
				for (OrderPayInfo orderPayInfo : goodsBaseList) {
					for (GoodsInfo goodsInfo : orderInfo.getGoodsInfo()) {
						if (orderPayInfo.getGoodsId().equals(goodsInfo.getGoodsId().longValue())) {
							orderPayInfo.setTotal(goodsInfo.getCount());
						}
					}
				}
			}
		}
		return goodsBaseList;
	}

	/**
	 * 修改订单状态
	 * @param orderInfo
	 * @throws BusinessException 业务异常
	 */
	private void modifyShoppingCartState(OrderInfo orderInfo) throws BusinessException {
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
	 * 保存二维码支付url
	 * @param wxPayInfo	返回客户端支付信息
	 */
	private void savePayUrl(WxPayInfo wxPayInfo,Long orderId) {
		Map<String,Object> map = new HashMap<>();
		map.put("payUrl",wxPayInfo.getPayUrl());
		map.put("orderId",orderId);
		Integer state = orderDetailsMapper.updatePayUrl(map);
		if(state <= 0)
			logger.error("保存二维码支付url失败");
	}

	/**
	 * 设置商品价格
	 * @param goodsBase  商品实体
	 */
	private void getShopPrice(GoodsBasePOJO goodsBase) throws BusinessException {
		PromotionGoodsPOJO promotionGoods = promotionService.queryDiscountPriceByGoodsId(goodsBase.getId());
		//是否打折商品
		if(promotionGoods != null) {
			if (promotionGoods.getDiscount()) {
				goodsBase.setShopPrice(promotionGoods.getDiscountPrice().intValue());
			}
		}
	}
	/**
	 * 根据用户提交的信息生成订单详情
	 */
	private List<OrderDetailsPOJO> makeOrderDetail(List<OrderPayInfo> goodsBaseList,OrderInfo orderInfo,Long orderId) throws BusinessException {
		long currentTime = DateUtils.getCurrentDate();
		long uid = orderInfo.getUserId();
		List<OrderDetailsPOJO> orderList = new ArrayList<>();
		for (OrderPayInfo orderPayInfo : goodsBaseList) {
			OrderDetailsPOJO od = new OrderDetailsPOJO();
			//订单编号
			od.setOrderId(orderId);
			od.setUserId(uid);
			od.setGoodsId(orderPayInfo.getGoodsId().intValue());
			od.setAddressId(orderInfo.getAddressId());
			od.setPaymentMethod(orderInfo.getPaymentMethod());
			/**
			 * 订单支付金额
			 */
			Integer orderActualPrice = orderPayInfo.getGoodsPrice()*orderPayInfo.getTotal();
			od.setActualPrice(orderActualPrice);
			od.setPayPrice(orderPayInfo.getGoodsPrice());
			od.setPayTime(currentTime);
			od.setOrderStatus(10);
			od.setOrderType(orderPayInfo.getIsDiscount());
			//生成平台标识
			generateAppIdentify(orderInfo, uid, od);
			//查询用户绑定公众平台
			UserInfoOutParam userInfo = userBaseService.queryUserInfoByUserId(uid);
			if(userInfo != null){
				od.setFansAppId(userInfo.getAppId());
			}
			od.setCount(orderPayInfo.getTotal());
			od.setCreateDate(currentTime);
			od.setUpdateDate(currentTime);
			//添加地址
			generateOrderAddress(orderInfo, od);
			orderList.add(od);
		}

		return orderList;
	}

	/**
	 * 生成平台标识
	 * @param orderInfo
	 * @param uid
	 * @param od
	 * @throws BusinessException
	 */
	private void generateAppIdentify(OrderInfo orderInfo, long uid, OrderDetailsPOJO od) throws BusinessException {
		if(!StringUtils.isEmpty(orderInfo.getAppId())){
            Long urlId = FreshFunEncoder.urlToId(orderInfo.getAppId());
            od.setAppId(urlId);
            // 设置订单来源
            setOrderSource(uid, urlId);
        } else {
            // 设置订单来源
            setOrderSource(uid,888888L);
			od.setAppId(888888L);
        }
	}

	/**
	 * 生成订单地址
	 * @param orderInfo
	 * @param od
	 * @throws BusinessException
	 */
	private void generateOrderAddress(OrderInfo orderInfo, OrderDetailsPOJO od) throws BusinessException {
		AddressPOJO address = addressService.queryAddressById(orderInfo.getAddressId());
		if(address != null) {
            od.setName(address.getName());
            od.setTel(address.getTel());
            od.setAddress(address.getAddress());
            if (address.getIsNew() == 1) {
                od.setProvCode(address.getProvCode());
                od.setCityCode(address.getCityCode());
                od.setDistCode(address.getDistCode());
            } else {
                od.setCity(address.getCity());
            }
        } else{
            throw new BusinessException("下单时查询地址为空");
        }
	}

	/**
	 * 根据用户提交的信息生成订单详情
	 * @param payInfo
	 * @param goodsInfo
	 */
	private OrderDetailsPOJO makeOrderDetail(OrderPayInfo payInfo, GoodsInfo goodsInfo,OrderInfo orderInfo,Long orderId) throws BusinessException {
		long currentTime = DateUtils.getCurrentDate();
        long uid = orderInfo.getUserId();
		//查询捕手信息
//		Long fetcherId = null;
//		UserInfoOutParam userInfo = userBaseService.queryUserInfoByUserId(uid);
//		if(userInfo != null){
//			fetcherId = userInfo.getFetcherId();
//		}
		OrderDetailsPOJO od = new OrderDetailsPOJO();
		//订单编号
		od.setOrderId(orderId);
		od.setUserId(uid);
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
		//生成平台标识
		generateAppIdentify(orderInfo, uid, od);
		//查询用户绑定公众平台
		UserInfoOutParam userInfo = userBaseService.queryUserInfoByUserId(uid);
		if(userInfo != null){
			od.setFansAppId(userInfo.getAppId());
		}

		//判断是否有上级
//		if(fetcherId !=null && fetcherId != 0) {
//			od.setFetcherId(fetcherId);
//			//计算捕手需要获取的提成
//			Double fetcherMoney = orderActualPrice * Constant.FECTHER_COMPONENT;
//			od.setFetcherPrice(fetcherMoney.intValue());
//			od.setPayPlateform(1);
//		}else{
//			String sign = orderInfo.getFetcherId();
//			if (sign != null && !"".equals(sign)){
//				Long id = FreshFunEncoder.urlToId(sign);
//				if(id != null) {
//                    //判断是否是捕手
//                    boolean bool = userBaseService.checkIsFetcherByUserId(id);
//                    if(bool){
//                        int status = userBaseService.modifyFetcherForUser(uid,id);
//                        if (status <= 0) {
//							logger.error("添加分享标记失败");
//                        }else{
//                            //添加分享提成
//                            od.setFetcherId(id);
//                            //计算捕手需要获取的提成
//                            Double fetcherMoney = orderActualPrice * Constant.FECTHER_COMPONENT;
//                            od.setFetcherPrice(fetcherMoney.intValue());
//							od.setPayPlateform(1);
//                        }
//                    }
//				}
//			}
//		}
		od.setCount(goodsInfo.getCount());
		od.setCreateDate(currentTime);
		od.setUpdateDate(currentTime);
		//添加地址
		generateOrderAddress(orderInfo, od);
		return od;
	}

	/**
	 * 查询订单来源
	 * @param userId 用户编号
	 * @param appId appId
	 */
	private void setOrderSource(Long userId, Long appId) {
		userBaseService.modifyUserAppId(userId,appId);
	}

	/**
	 * 订单实体
	 * @param orderInfo
	 * @return
	 */
	private OrdersPOJO makeOrderPOJO(OrderInfo orderInfo,List<OrderPayInfo> goodsBaseList) {
		int sumPrice = 0;
		for (OrderPayInfo payInfo : goodsBaseList) {
			sumPrice += payInfo.getGoodsPrice()*payInfo.getTotal();
		}
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
	public OrderDetailsPOJO getOrderLogistic(Long orderId) {
		OrderDetailsPOJO orderDetail = orderDetailsMapper.getLogistic(orderId);
		return orderDetail;
	}

	@Override
	public String findPayUrl(Long orderId) throws BusinessException {
		if(orderId == null)
			throw new BusinessException("查询二维码支付url订单编号不能为null");
		OrderDetailsPOJO order = orderDetailsMapper.selectParentOrderId(orderId);
		return orderDetailsMapper.selectPayUrl(order.getOrderId());
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
			if("SUCCESS".equals(map.get("result_code"))){
				int payStatus = 0;
				Long currentDate = DateUtils.getCurrentDate();
				String orderId = map.get("attach");
				//微信回传的订单编号
				String transactionId = map.get("transaction_id");
				if("W".equals(orderId.subSequence(0, 1))){
					Long id = Long.parseLong(orderId.substring(1,orderId.length()));
					payStatus = orderDetailsMapper.updateOrderDetailPayStatus(currentDate,id,transactionId);
					if(payStatus > 0) {
						OrderDetailsPOJO orderDetails = orderDetailsMapper.selectPayOrderInfoById(id);
						if (orderDetails != null) {
							//发送邮件
							senderMail(orderDetails,1);
							//推送消息
							pushMessage(orderDetails,1);
						} else {
							logger.error("订单回调查询商品失败");
						}
					}
				} else if("B".equals(orderId.subSequence(0, 1))){
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
				} else if ("Z".equals(orderId.subSequence(0,1))){
					String oId = orderId.substring(1,orderId.length());
					List<OrderDetailsPOJO> orderDetailsList = orderDetailsMapper.selectPayId(Long.parseLong(oId));
					if(orderDetailsList != null) {
						for (OrderDetailsPOJO orderDetails : orderDetailsList) {
							payStatus = orderDetailsMapper.updateOrderDetailPayStatus(currentDate, orderDetails.getId(), transactionId);
							//修改库存
							if(payStatus > 0) {
								//发送邮件
								senderMail(orderDetails,0);
								//推送消息
								pushMessage(orderDetails,0);
							}
						}
					}else{
						logger.error("订单回调根据父级订单号查询订单编号失败");
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
	@Override
	public void senderMail(OrderDetailsPOJO order,int sign){
		if(order == null) {
			logger.error("订单发送Mail订单不能为空");
			return;
		}
		getAddress(order);
		if(sign == 1) {
			GoodsParam goods = goodsBaseMapper.selectGoodsByGoodsId(order.getGoodsId().longValue());
			order.setGoods(goods);
		}
		OrderParam orderParam = new OrderParam();
		orderParam.setOrderId(order.getId());
		orderParam.setGoodsName(order.getGoods().getGoodsName());
		orderParam.setPayPrice(MoneyFormat.priceFormatString(order.getActualPrice()));
		//成交时间
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		orderParam.setPayTime(dateFormat.format(order.getPayTime()*1000));
		orderParam.setCount(order.getCount());
		orderParam.setUnitPrice(MoneyFormat.priceFormatString(order.getPayPrice()));
		orderParam.setCostPrice(MoneyFormat.priceFormatString(order.getGoodsCost()));
		orderParam.setOrderSource(setOrderSource(order));
		orderParam.setConsignee(order.getName());
		orderParam.setTel(order.getTel());
		orderParam.setAddress(order.getCity()+order.getAddress());
		String [] titles = new String[]{"订单编号","商品名","支付金额","成交时间","数量","单价","成本价","订单来源","收货人","联系电话","收货地址"};
		//titles = new String[]{"orderNumber","goodsName","payMoney","payTime","amount","price","costPrice","orderSource","consignee","tel","address"};
		String htmlStr = ObjectToHtml.getHtmlStr(orderParam, titles);
		//发送
		MailSender sender = MailSenderFactory.getSender();
		try {
			sender.send("order@freshfun365.com",new StringBuilder().append("支付订单：").append(order.getId()).toString(),htmlStr);
		} catch (MessagingException e) {
			logger.error("发送邮件出现异常",e);
		}
	}

	/**
	 * 修改粉丝订单来源
	 * @param map
	 * @return
	 */
	@Override
	public int updateFansAppId(Map<String, Object> map) {
		return orderDetailsMapper.updateFansAppId(map);
	}

	@Override
	public List<OrderDetailsPOJO> selectAllAppId() {
		return orderDetailsMapper.selectAllAppId();
	}

	/**
	 * 设置订单来源
	 * @param order
	 */
	private String setOrderSource(OrderDetailsPOJO order) {
		if(order.getAppId() != null) {
			return userBaseMapper.selectAppNameByAppId(order.getAppId());
		}
		return "";
	}

	/**
	 * 往微信推送消息
	 */
	private void pushMessage(OrderDetailsPOJO order,int sign){
		if(sign == 1) {
			GoodsParam goods = goodsBaseMapper.selectGoodsByGoodsId(order.getGoodsId().longValue());
			order.setGoods(goods);
		}
		//设置地址信息
		getAddress(order);
		weChatService.sendWxOrderMessage(order);
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
	 * 订单支付
	 */
	@Override
	public WxPayInfo awaitPayOrder(HttpServletRequest request,OrderPayPOJO order,Long userId) throws BusinessException, JSONException {
		if(order.getOrderId() == null || userId == null)
			throw new BusinessException("订单支付订单编号不能为空");
		OrderDetailsPOJO detailsPOJO = orderDetailsMapper.selectPayOrder(order.getOrderId());
		WxPayInfo wxPayInfo = null;
		if(detailsPOJO != null) {
			//获取用户openId
			String openId = userBaseService.queryUserInfoByUserId(userId).getOpenId();
			StringBuilder sb = new StringBuilder();
			sb.append("W");
			sb.append(order.getOrderId());
			wxPayInfo = weChatService.wzPay(request, sb.toString(), detailsPOJO.getActualPrice().toString(), openId);
			wxPayInfo.setOrderId(Long.parseLong(order.getOrderId()));
			saveOrderPayUrl(wxPayInfo);
		}
		return wxPayInfo;
	}

	private void saveOrderPayUrl(WxPayInfo wxPayInfo) {
		Map<String,Object> map = new HashMap<>();
		map.put("payUrl",wxPayInfo.getPayUrl());
		map.put("orderId",wxPayInfo.getOrderId());
		Integer state = orderDetailsMapper.updateOrderUrl(map);
		if(state <= 0)
			logger.error("保存二维码支付url失败");
	}

	/**
	 * 二维码支付
	 * @param productId 商品或订单编号
	 * @param openId 用户凭证
	 * @return
	 */
	@Override
	public String QRCodePay(HttpServletRequest request,String productId, String openId) throws BusinessException, JSONException {
		if(productId == null || openId == null)
			throw new BusinessException("二维码支付参数不能为null");
		String payResult = null;
		String payOrderId = productId.subSequence(0, 1).toString();
		switch (payOrderId){
			case "Z":
				OrdersPOJO ordersPOJO = ordersMapper.selectById(productId.substring(1,productId.length()));
				if (ordersPOJO != null) {
					payResult = weChatService.QRCodePay(request, productId, ordersPOJO.getActualPrice().toString(), openId);
				}
				break;
			case "W":
				OrderDetailsPOJO orderDetails = orderDetailsMapper.selectPayOrder(productId.substring(1, productId.length()));
				if(orderDetails != null){
					payResult = weChatService.QRCodePay(request, productId, orderDetails.getActualPrice().toString(), openId);
				}
				break;
		}
		if(StringUtils.isEmpty(payResult))
			throw new BusinessException("二维码支付提交预支付异常");
		return payResult;
	}

	/**
     * App订单支付
     * @param orderId
     * @return
     */
    @Override
    public WxPayInfo appOrderPay(String orderId,HttpServletRequest request) throws UnsupportedEncodingException, JSONException {
        OrderDetailsPOJO detailsPOJO = orderDetailsMapper.selectPayOrder(orderId);
        //String payMoney = MoneyFormat.priceFormatString(detailsPOJO.getActualPrice());
        StringBuilder sb = new StringBuilder();
        sb.append("W");
        sb.append(orderId);
        WxPayInfo payInfo = appPay(request, sb.toString(), detailsPOJO.getActualPrice().toString());
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

	/**
	 * 原生支付
	 * @param orderInfo
	 * @param request
	 * @return
	 */
	@Override
	public WxPayInfo addWeixinAppPay(OrderInfo orderInfo, HttpServletRequest request) throws BusinessException, UnsupportedEncodingException, JSONException {
		OrderPayInfo [] orderPayInfos = new OrderPayInfo[orderInfo.getGoodsInfo().size()];
		//订单总价格
		Integer orderSumPrice = 0;
		//订单编号
		Long uId = orderInfo.getUserId();
		//获取订单信息
		orderSumPrice = getOrderInfo(orderInfo, orderPayInfos, orderSumPrice);

		OrdersPOJO orderPOJO = makeOrderPOJO(orderInfo,orderSumPrice);
		//添加父级订单
		insertOrderParent(orderInfo, orderPOJO);
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
		WxPayInfo info = appPay(request, payId.toString(), orderSumPrice.toString());
		//修改购物车状态
		modifyShoppingCartState(orderInfo);
		return info;
	}

	@Override
	public List<OrderDetailsPOJO> selectPayId(Long parentOrderId) {
		return orderDetailsMapper.selectPayId(parentOrderId);
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
	private int getOrderInfo(OrderInfo orderInfo, OrderPayInfo[] orderPayInfos, int orderSumPrice) throws BusinessException {
		if(orderInfo.getGoodsInfo() != null && orderInfo.getGoodsInfo().size() > 0) {
			for (int i = 0; i < orderInfo.getGoodsInfo().size(); i++) {
				GoodsInfo goodsInfo = orderInfo.getGoodsInfo().get(i);
				OrderPayInfo orderPayInfo = null;
				//查询购物车
				if (null == goodsInfo.getScId() || 0 == goodsInfo.getScId()) {
					GoodsBasePOJO goodsBase = goodsBaseMapper.selectOrderPayInfo(goodsInfo.getGoodsId().longValue());
					getShopPrice(goodsBase);
					orderPayInfo = new OrderPayInfo(goodsBase.getTitle(), goodsBase.getShopPrice(),goodsInfo.getCount());
				} else {
					ShoppingCartPOJO shoppingCart = shoppingCartMapper.selectShoppingCart(goodsInfo.getScId());
					GoodsBasePOJO goodsBase = goodsBaseMapper.selectOrderPayInfo(shoppingCart.getGoodsId().longValue());
					getShopPrice(goodsBase);
					orderPayInfo = new OrderPayInfo(goodsBase.getTitle(), goodsBase.getShopPrice(),shoppingCart.getGoodsTotals());
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

	public WxPayInfo appPay(HttpServletRequest request,String payId,String payMoney) throws JSONException, UnsupportedEncodingException {
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

		PrepayIdRequestHandler prepayReqHandler = new PrepayIdRequestHandler();//获取prepayid的请求类
		ClientRequestHandler clientHandler = new ClientRequestHandler();//返回客户端支付参数的请求类

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
		String sign = prepayReqHandler.createMD5Sign(ConstantUtil.PARTNER_KEY);
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
			sign = clientHandler.createMD5Sign(ConstantUtil.PARTNER_KEY);

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

}