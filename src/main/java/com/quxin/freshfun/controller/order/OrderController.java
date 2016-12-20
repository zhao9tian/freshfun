package com.quxin.freshfun.controller.order;

import com.google.common.collect.Maps;
import com.quxin.freshfun.dao.GoodsLimitMapper;
import com.quxin.freshfun.dao.ResponseResult;
import com.quxin.freshfun.model.*;
import com.quxin.freshfun.model.outparam.WxPayInfo;
import com.quxin.freshfun.model.param.GoodsParam;
import com.quxin.freshfun.service.goods.GoodsService;
import com.quxin.freshfun.service.order.OrderManager;
import com.quxin.freshfun.service.order.OrderService;
import com.quxin.freshfun.service.user.UserBaseService;
import com.quxin.freshfun.utils.BusinessException;
import com.quxin.freshfun.utils.CookieUtil;
import com.quxin.freshfun.utils.FieldUtil;
import com.quxin.freshfun.utils.MoneyFormat;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/")
public class OrderController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderManager orderManager;
	@Autowired
	private UserBaseService userBaseService;
	private static Integer orderOutTime;
	/**
	 * 日志
	 */
	private final Logger logger = LoggerFactory.getLogger(getClass());


	private Logger resultLogger = LoggerFactory.getLogger("info_log");

	private static final Integer PAGE_SIZE = 15;

	@Value("${orderOutTime}")
	public void setRip(Integer value) {
		orderOutTime = value;
	}

	/**
	 * 查询所有订单
	 * @return
	 */
	@RequestMapping("/findAllOrders")
	@ResponseBody
	public List<OrderDetailsPOJO> findAllOrders(HttpServletRequest request,Integer currentPage,Integer pageSize){
		Long ui = CookieUtil.getUserIdFromCookie(request);
		List<OrderDetailsPOJO> orderList = orderManager.findAll(ui, currentPage, pageSize);
		List<OrderDetailsPOJO> orders =setGoodsList(orderList , 1);
		orders = setGoodsMoney(orders);
		return orders;
	}
	/**
	 * 查询单个商品
	 * @param orderDetailsId 订单Id
	 * @return 返回单个订单信息
	 */
	@RequestMapping("/selectSigleOrder")
	@ResponseBody
	public OrderDetailsPOJO selectSigleOrder(Long orderDetailsId){
		OrderDetailsPOJO orderDetail = orderManager.selectSigleOrder(orderDetailsId);
		//限时页面倒计时
		Long createTime = orderDetail.getCreateDate();
		Long now = System.currentTimeMillis();
		Long outTimeStamp = createTime*1000 + orderOutTime*60000 - now;
        if(orderDetail.getOrderType() == 2){
            outTimeStamp = createTime*1000 + 30 *60000 - now;
        }
		orderDetail.setOutTimeStamp(outTimeStamp);
		orderDetail.setPayMoney(MoneyFormat.priceFormatString(orderDetail.getPayPrice()));
		orderDetail.setPayPrice(null);
		GoodsParam goods = orderDetail.getGoods();
		goods.setMarketMoney(MoneyFormat.priceFormatString(goods.getMarketPrice()));
		goods.setGoodsMoney(MoneyFormat.priceFormatString(goods.getShopPrice()));
		goods.setMarketPrice(null);
		goods.setShopPrice(null);
		orderDetail.setGoods(goods);
		//控制付款成功页面申请退款按钮是否有效 isRefund 1是有效  0是无效
		return orderDetail;
	}




	/**
	 * 分页查待付款询订单信息
	 * @return
	 */
	@RequestMapping("/selectPendingPaymentOrder")
	@ResponseBody
	public List<OrderDetailsPOJO> selectPendingPaymentOrder(Integer currentPage,Integer pageSize,HttpServletRequest request){
		Long ui = CookieUtil.getUserIdFromCookie(request);
		List<OrderDetailsPOJO> orders = orderManager.selectPendingPaymentOrder(ui, currentPage, pageSize);
		orders = setGoodsMoney(orders);
		return setGoodsList(orders,null);
	}
	/**
	 * 分页查询待发货信息
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/selectAwaitDeliverOrder")
	@ResponseBody
	public List<OrderDetailsPOJO> selectAwaitDeliverOrder(Integer currentPage,Integer pageSize,HttpServletRequest request){
		Long ui = CookieUtil.getUserIdFromCookie(request);
		List<OrderDetailsPOJO> orders = setGoodsList(orderManager.selectAwaitDeliverOrder(ui,currentPage,pageSize),null);
		orders = setGoodsMoney(orders);
		return orders;
	}

	/**
	 * 查询待收货订单
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/selectAwaitGoodsReceipt")
	@ResponseBody
	public List<OrderDetailsPOJO> selectAwaitGoodsReceipt(HttpServletRequest request,Integer currentPage,Integer pageSize){
		Long ui = CookieUtil.getUserIdFromCookie(request);
		List<OrderDetailsPOJO> orders = setGoodsList(orderManager.selectAwaitGoodsReceipt(ui, currentPage, pageSize),null);
		orders = setGoodsMoney(orders);
		return orders;
	}
	/**
	 * 查询待评价订单
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/selectStayAssessOrder")
	@ResponseBody
	public List<OrderDetailsPOJO> selectAwaitComment(HttpServletRequest request,Integer currentPage,Integer pageSize){
		Long ui = CookieUtil.getUserIdFromCookie(request);
		List<OrderDetailsPOJO> orders = setGoodsList(orderManager.selectAwaitComment(ui, currentPage, pageSize),null);
		orders = setGoodsMoney(orders);
		return orders;
	}
	/**
	 * 查询退货订单
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/selectCancelOrder")
	@ResponseBody
	public List<OrderDetailsPOJO> selectCancelOrder(HttpServletRequest request,Integer currentPage,Integer pageSize){
		Long ui = CookieUtil.getUserIdFromCookie(request);
		List<OrderDetailsPOJO> orders = setGoodsList(orderManager.selectCancelOrder(ui, currentPage, pageSize),null);
		orders = setGoodsMoney(orders);
		return orders;
	}
	@RequestMapping("/cookieTest")
	@ResponseBody
	public String cookieTest(HttpServletResponse response){
		Long userId = 556677L;
		Cookie cookie = new Cookie("userId", CookieUtil.getCookieValueByUserId(userId));
		cookie.setMaxAge(CookieUtil.getCookieMaxAge());
		cookie.setDomain(".freshfun365.com");
		cookie.setPath("/");
		response.addCookie(cookie);
		return "ok";
	}

	/**
	 * 根据用户编号查询购物车信息
	 * @param
	 * @return
	 */
	@RequestMapping("/selectShoppingCartByUserId")
	@ResponseBody
	public Map<String,Object> selectShoppingCartByUserId(HttpServletRequest request){
		Long uId = CookieUtil.getUserIdFromCookie(request);
		return orderManager.selectShoppingCartByUserId(uId);
	}
	/**
	 * 添加商品数量
	 * @param scId
	 * @return
	 */
	@RequestMapping("/addGoodsTotals")
	@ResponseBody
	public Integer addGoodsTotals(Integer scId){
		return orderManager.addGoodsTotals(scId);
	}
	/**
	 * 确认收货
	 * @param orderId
	 * @return
	 */
	@RequestMapping("/confirmGoodsReceipt")
	@ResponseBody
	public ResponseResult confirmGoodsReceipt(String orderId){
		ResponseResult r = new ResponseResult();
		if(orderId == null || "".equals(orderId)){
			r.setSuccess("0");
			return  r;
		}
		int status = 0;
		try {
			status = orderManager.addConfirmGoodsReceipt(orderId);
		} catch (BusinessException e) {
			resultLogger.error("确认收货失败",e);
		}
		r.setSuccess(status+"");
		return r;
	}
	/**
	 * 订单关闭
	 * @return 关闭是否成功
	 */
	@RequestMapping("/delOrder")
	@ResponseBody
	public Map<String, Integer> delOrder(String orderId){
		int status = orderManager.delOrder(orderId);
		Map<String, Integer> map = Maps.newHashMap();
		map.put("status", status);
		return map;
	}
	/**
	 * 减少商品数量
	 * @param scId
	 * @return
	 */
	@RequestMapping("/reduceGoodsTotals")
	@ResponseBody
	public Integer reduceGoodsTotals(Integer scId){
		return orderManager.reduceGoodsTotals(scId);
	}
	/**
	 * 添加购物车
	 */
	@RequestMapping(value="/addShoppingCart")
	@ResponseBody
	public Map<String, Integer> addShoppingCart(HttpServletRequest request,Integer goodsId ){
		Long ui = CookieUtil.getUserIdFromCookie(request);
		Integer addStatus = orderManager.addShoppingCart(ui, goodsId);
		Map<String, Integer> map = new HashMap<String, Integer>();
		map.put("status", addStatus);
		return map;
	}
	/**
	 * 刪除購物車訂單
	 * @param scId
	 * @return
	 */
	@RequestMapping("/delShoppingCartOrder")
	@ResponseBody
	public ResponseResult delShoppingCartOrder(Integer scId){
		Integer delStatus = orderManager.delShoppingCartOrder(scId);
		ResponseResult result = new ResponseResult();
		if(delStatus <= 0){
			result.setSuccess("0");
		}else{
			result.setSuccess("1");
		}
		return result;
	}
	/*
	 * 添加订单
	 */
	@RequestMapping(value="/createOrder",method={RequestMethod.POST})
	@ResponseBody
	public Map<String, Object> createOrder(@RequestBody OrderInfo orderInfo,HttpServletRequest request) throws BusinessException {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> resultMap = new HashMap<>();
		WxPayInfo wxPayInfo = null;
		if(orderInfo == null){
			map.put("code", 1004);
			map.put("msg", "创建订单参数不能为空");
			resultMap.put("status", map);
			return resultMap;
		}
		try {
			Long userId = CookieUtil.getUserIdFromCookie(request);
			if(userId != null){
				orderInfo.setUserId(userId);
				wxPayInfo = orderService.addOrder(orderInfo, request);
				//判断用户是否绑定了手机
				String phoneNumber = userBaseService.queryUserInfoByUserId(userId).getPhoneNumber();
				if(phoneNumber!=null&&!"".equals(phoneNumber)){
					wxPayInfo.setIsPhone(1);
				}else{
					wxPayInfo.setIsPhone(0);
				}
			}else{
				logger.error("用户创建订单时userId为null");
			}
		} catch (JSONException e) {
			logger.error("订单支付Json转换异常",e);
		}
		map.put("code", 1001);
		map.put("msg", "请求成功");
		resultMap.put("status", map);
		resultMap.put("data",wxPayInfo);
		return resultMap;
	}

	/**
	 * 查询二维码支付url
	 * @param orderId	订单编号
	 * @return
	 */
	@RequestMapping("/findPayUrl")
	@ResponseBody
	public Map<String, Object> findPayUrl(Long orderId) throws BusinessException {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> resultMap = new HashMap<>();
		if(orderId == null){
			map.put("code", 1004);
			map.put("msg", "参数不能为空");
			resultMap.put("status", map);
			return resultMap;
		}
		String payUrl = orderService.findPayUrl(orderId);
		map.put("code", 1001);
		map.put("msg", "请求成功");
		resultMap.put("status", map);
		resultMap.put("data",payUrl);
		return resultMap;
	}

	@RequestMapping(value="/quanMingPay",method={RequestMethod.POST})
	@ResponseBody
	public ResponseResult quanMingPay(@RequestBody QuanMingPayInfo payInfo,HttpServletRequest httpServletRequest){
		ResponseResult payResult = null;
		try{
			Long userId = CookieUtil.getUserIdFromCookie(httpServletRequest);
			payInfo.setUserId(userId);
			payResult = orderService.addQuanMingPay(payInfo,userId);
		}catch (BusinessException e){
			resultLogger.error("商户代理信息添加失败",e);
		}

		return payResult;
	}

	/**
	 * 公众号订单支付
	 * @param orderPay	订单支付参数
	 * @param request	请求
	 * @return
	 * @throws BusinessException
	 */
	@RequestMapping(value="/awaitPayOrder",method={RequestMethod.POST})
	@ResponseBody
	public Map<String, Object> awaitPayOrder(@RequestBody OrderPayPOJO orderPay,HttpServletRequest request) throws BusinessException {
		Map<String, Object> map = new HashMap<>();
		Map<String, Object> resultMap = new HashMap<>();
		if(orderPay == null){
			map.put("code", 1004);
			map.put("msg", "订单支付参数不能为空");
			resultMap.put("status", map);
			return resultMap;
		}
		Long userId = CookieUtil.getUserIdFromCookie(request);
		WxPayInfo payInfo = null;
		try {
			payInfo = orderService.awaitPayOrder(request, orderPay, userId);
			if(payInfo == null){
				map.put("code", 1004);
				map.put("msg", "订单支付失败");
				resultMap.put("status", map);
				return resultMap;
			}
		} catch (JSONException e) {
			logger.error("订单支付Json转换异常",e);
		}
		map.put("code", 1001);
		map.put("msg", "请求成功");
		resultMap.put("status", map);
		resultMap.put("data",payInfo);
		return resultMap;
	}


	@RequestMapping(value="/payCallback")
	public void payCallback(HttpServletRequest request,HttpServletResponse response){
		response.setContentType("application/xml");
		InputStream in;
		try {
			in = request.getInputStream();
			int callbackStatus = orderService.PayCallback(in);
			if(callbackStatus <= 0){
				response.getWriter().write(FieldUtil.jsonToXml("{\"return_code\":\"FAIL\",\"return_msg\":\"FAIL\"}"));
			} else {
				response.getWriter().write(FieldUtil.jsonToXml("{\"return_code\":\"SUCCESS\",\"return_msg\":\"OK\"}"));
			}
		} catch (IOException e) {
			logger.error("支付回调IO异常",e);
		}
	}

	/**
	 * 查询订单数量
	 * @return 返回所有状态的订单数量
	 */
	@RequestMapping("/selectordercounts")
	@ResponseBody
	public Map<String, Object> selectOrderCounts(HttpServletRequest request){
		Long ui = CookieUtil.getUserIdFromCookie(request);
		List<OrderStatusInfo> statusCounts = orderManager.selectStatusCounts(ui);
		//查询出待付款且并未过期的订单数
		int awaitPaymentCount =  orderManager.selectPayCounts(ui);
		//查询出已确认收货且未评价的订单数
        int commentCount = orderManager.selectCommentCount(ui);
		Map<String, Object> orderMap = new HashMap<>(5);
		orderMap.put("daishouhuo","");
		orderMap.put("daipingjia","");
		orderMap.put("daifahuo","");
		orderMap.put("tuihuo","");
		orderMap.put("daifukuan","");
		for(OrderStatusInfo orderStatus : statusCounts){
			if(orderStatus.getOrderStatus() == 10){
				if(awaitPaymentCount > 0){
					orderMap.put("daifukuan", awaitPaymentCount);
				}
			}else if(orderStatus.getOrderStatus() == 30){
				orderMap.put("daifahuo", orderStatus.getStatusCounts());
			}else if(orderStatus.getOrderStatus() == 50){
				orderMap.put("daishouhuo", orderStatus.getStatusCounts());
			}else if(orderStatus.getOrderStatus() == 70){
				if(commentCount > 0){
					orderMap.put("daipingjia", commentCount);
				}
			}else if(orderStatus.getOrderStatus() == 40){
			   orderMap.put("tuihuo", orderStatus.getStatusCounts());
			}
		}
		return orderMap;
	}


	/***********************************查询限时商品或者普通商品************************************************/
	@Autowired
	private GoodsService goodsService;
	@Autowired
	private GoodsLimitMapper goodsDao;


	public List<OrderDetailsPOJO> setGoodsList(List<OrderDetailsPOJO> orders  , Integer rukou){
		if(rukou != null && rukou == 1){
			for(OrderDetailsPOJO order : orders){
				OrderDetailsPOJO od = orderManager.selectSigleOrder(order.getId());
				//限时页面倒计时
				Long createTime = od.getCreateDate();
				Long now = System.currentTimeMillis();
				Long outTimeStamp = createTime*1000 + orderOutTime*60000 - now;
				order.setOutTimeStamp(outTimeStamp);
			}
		}
		return orders;
	}

	/***********************************确认收货************************************************/

	/**
	 * 修改订单状态
	 * @param orderDetailId
	 * @return
	 */
	@RequestMapping("/applyRefund")
	@ResponseBody
	public ReturnStatus applyRefund(@RequestParam String orderDetailId){
		ReturnStatus rs = new ReturnStatus();
		Long num = orderManager.applyRefund(orderDetailId);
		if(num != null){
			rs.setStatus(1);//不为空就表示未过期
		}else{
			rs.setStatus(0);//为空表示已过期
		}
		return rs;
	}

	/**
	 * 设置金钱的格式
	 * @param order
	 * @return
	 */
	public List<OrderDetailsPOJO> setGoodsMoney(List<OrderDetailsPOJO> order){
		for (OrderDetailsPOJO o: order) {
			if(o.getPayPrice() != null){
				o.setPayMoney(MoneyFormat.priceFormatString(o.getPayPrice()));
				o.setPayPrice(null);
			}
			GoodsParam goods = o.getGoods();
			if(goods.getMarketPrice() != null) {
				goods.setMarketMoney(MoneyFormat.priceFormatString(goods.getMarketPrice()));
				goods.setMarketPrice(null);
			}
			if(goods.getShopPrice() != null) {
				goods.setGoodsMoney(MoneyFormat.priceFormatString(goods.getShopPrice()));
				goods.setShopPrice(null);
			}
		}
		return order;
	}
}