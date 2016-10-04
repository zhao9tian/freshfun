package com.quxin.freshfun.controller.order;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.quxin.freshfun.model.*;
import com.quxin.freshfun.model.param.FlowParam;
import com.quxin.freshfun.service.flow.FlowService;
import com.quxin.freshfun.utils.DateUtils;
import com.quxin.freshfun.utils.MoneyFormat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.common.collect.Maps;
import com.quxin.freshfun.dao.GoodsLimitMapper;
import com.quxin.freshfun.dao.ResponseResult;
import com.quxin.freshfun.service.goods.GoodsService;
import com.quxin.freshfun.service.order.OrderManager;
import com.quxin.freshfun.service.order.OrderService;
import com.quxin.freshfun.utils.BusinessException;

@Controller
@RequestMapping("/")
public class OrderController {
	@Autowired
	private OrderService orderService;
	@Autowired
	private OrderManager orderManager;
	@Autowired
	private FlowService flowService;
	private static Integer orderOutTime;


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
	public List<OrderDetailsPOJO> findAllOrders(String userId,Integer currentPage,Integer pageSize){
		Long ui = Long.parseLong(userId.replace("\"", ""));
		List<OrderDetailsPOJO> orders =setGoodsList(orderManager.findAll(ui, currentPage, pageSize) , 1);
		orders = setGoodsMoney(orders);
		return orders;
	}
	/**`
	 * 查询单个商品
	 * @param orderDetailsId
	 * @return
	 */
	@RequestMapping("/selectSigleOrder")
	@ResponseBody
	public OrderDetailsPOJO selectSigleOrder(String orderDetailsId){
		OrderDetailsPOJO orderDetail = orderManager.selectSigleOrder(orderDetailsId);
		//限时页面倒计时
		Long createTime = orderDetail.getCreateDate();
		Long now = System.currentTimeMillis();
		Long outTimeStamp = createTime*1000 + orderOutTime*60000 - now;
		orderDetail.setOutTimeStamp(outTimeStamp);
		GoodsPOJO goods = orderDetail.getGoods();
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
	public List<OrderDetailsPOJO> selectPendingPaymentOrder(String userId,Integer currentPage,Integer pageSize){
		Long ui = Long.parseLong(userId.replace("\"", ""));
		List<OrderDetailsPOJO> orders = orderManager.selectPendingPaymentOrder(ui, currentPage, pageSize);
		orders = setGoodsMoney(orders);
		return setGoodsList(orders,null);
	}
	/**
	 * 分页查询待发货信息
	 * @param userId
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/selectAwaitDeliverOrder")
	@ResponseBody
	public List<OrderDetailsPOJO> selectAwaitDeliverOrder(String userId,Integer currentPage,Integer pageSize){
		Long ui = Long.parseLong(userId.replace("\"", ""));
		List<OrderDetailsPOJO> orders = setGoodsList(orderManager.selectAwaitDeliverOrder(ui,currentPage,pageSize),null);
		orders = setGoodsMoney(orders);
		return orders;
	}

	/**
	 * 查询待收货订单
	 * @param userId
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/selectAwaitGoodsReceipt")
	@ResponseBody
	public List<OrderDetailsPOJO> selectAwaitGoodsReceipt(String userId,Integer currentPage,Integer pageSize){
		Long ui = Long.parseLong(userId.replace("\"", ""));
		List<OrderDetailsPOJO> orders = setGoodsList(orderManager.selectAwaitGoodsReceipt(ui, currentPage, pageSize),null);
		orders = setGoodsMoney(orders);
		return orders;
	}
	/**
	 * 查询待评价订单
	 * @param userId
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/selectStayAssessOrder")
	@ResponseBody
	public List<OrderDetailsPOJO> selectAwaitComment(String userId,Integer currentPage,Integer pageSize){
		Long ui = Long.parseLong(userId.replace("\"", ""));
		List<OrderDetailsPOJO> orders = setGoodsList(orderManager.selectAwaitComment(ui, currentPage, pageSize),null);
		orders = setGoodsMoney(orders);
		return orders;
	}
	/**
	 * 查询退货订单
	 * @param userId
	 * @param currentPage
	 * @param pageSize
	 * @return
	 */
	@RequestMapping("/selectCancelOrder")
	@ResponseBody
	public List<OrderDetailsPOJO> selectCancelOrder(String userId,Integer currentPage,Integer pageSize){
		Long ui = Long.parseLong(userId.replace("\"", ""));
		List<OrderDetailsPOJO> orders = setGoodsList(orderManager.selectCancelOrder(ui, currentPage, pageSize),null);
		orders = setGoodsMoney(orders);
		return orders;
	}
	/**
	 * 根据用户编号查询购物车信息
	 * @param userId
	 * @return
	 */
	@RequestMapping("/selectShoppingCartByUserId")
	@ResponseBody
	public Map<String,Object> selectShoppingCartByUserId(String userId){
		Long ui = Long.parseLong(userId.replace("\"", ""));
		return orderManager.selectShoppingCartByUserId(ui);
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
	 * 删除订单
	 * @return
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
	public Map<String, Integer> addShoppingCart(String userId,Integer goodsId ){
		Long ui = Long.parseLong(userId.replace("\"", ""));
		Integer addStatus = orderManager.addShoppingCart(ui, goodsId);
		Map<String, Integer> map = new HashMap<>();
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
	public Map<String, ResponseResult> createOrder(@RequestBody OrderInfo orderInfo){
		Map<String, ResponseResult> map = Maps.newHashMap();
		ResponseResult payResult = null;
		try {
			payResult = orderService.addOrder(orderInfo);
		} catch (BusinessException e) {
			resultLogger.error("添加订单失败",e);
		}
		map.put("payResult", payResult);
		return map;
	}



	@RequestMapping(value="/quanMingPay",method={RequestMethod.POST})
	@ResponseBody
	public ResponseResult quanMingPay(@RequestBody QuanMingPayInfo payInfo){
		ResponseResult payResult = null;
		try{
			payResult = orderService.addQuanMingPay(payInfo);
		}catch (BusinessException e){
			resultLogger.error("商户代理信息添加失败",e);
		}

		return payResult;
	}

	@RequestMapping(value="/awaitPayOrder",method={RequestMethod.POST})
	@ResponseBody
	public Map<String, ResponseResult> awaitPayOrder(@RequestBody OrderPayPOJO orderPay){
		Map<String, ResponseResult> map = Maps.newHashMap();
		ResponseResult result = orderService.awaitPayOrder(orderPay);
		map.put("payResult", result);
		return map;
	}
	/**
	 * 添加限时购订单
	 */
	@RequestMapping(value="/createLimitOrder",method={RequestMethod.POST})
	@ResponseBody
	public Map<String, ResponseResult> createLimitOrder(@RequestBody OrderInfo orderInfo){
		Map<String, ResponseResult> map = Maps.newHashMap();
		ResponseResult payResult = orderService.addLimitOrder(orderInfo);
		map.put("payResult", payResult);
		return map;
	}

	@RequestMapping(value="/payCallback",produces = "application/xml")
	@ResponseBody
	public String payCallback(HttpServletRequest request,HttpServletResponse response){
		InputStream in = null;
		try {
			in = request.getInputStream();
			int callbackStatus = orderService.PayCallback(in);
			if(callbackStatus <= 0){
				return "FAIL";
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return "SUCCESS";
	}

	/**
	 * 查询订单数量
	 * @param userId
	 * @return
	 */
	@RequestMapping("/selectordercounts")
	@ResponseBody
	public Map<String, Object> selectOrderCounts(String userId){
		Long ui = Long.parseLong(userId.replace("\"", ""));
		List<OrderStatusInfo> statusCounts = orderManager.selectStatusCounts(ui);

		int awaitPaymentCount =  orderManager.selectPayCounts(ui);
        int commentCount = orderManager.selectCommentCount(ui);
		Map<String, Object> orderMap = new HashMap<String, Object>(5);
		orderMap.put("daishouhuo","");
		orderMap.put("daipingjia","");
		orderMap.put("daifahuo","");
		orderMap.put("tuihuo","");
		orderMap.put("daifukuan","");

		for(OrderStatusInfo orderStatus : statusCounts){
			if(orderStatus.getOrderStatus() == 10){
				if(awaitPaymentCount <= 0){
					orderMap.put("daifukuan","");
				}else{
					orderMap.put("daifukuan", awaitPaymentCount);
				}
			}else if(orderStatus.getOrderStatus() == 30){
				orderMap.put("daifahuo", orderStatus.getStatusCounts());
			}else if(orderStatus.getOrderStatus() == 50){
				orderMap.put("daishouhuo", orderStatus.getStatusCounts());
			}else if(orderStatus.getOrderStatus() == 70){
                if(commentCount <= 0){
                    orderMap.put("daipingjia", "");
                }else{
                    orderMap.put("daipingjia", commentCount);
                }
			}else if(orderStatus.getOrderStatus() == 40){
				orderMap.put("tuihuo", orderStatus.getStatusCounts());
			}
		}



		System.out.println(orderMap);

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
				OrderDetailsPOJO od = orderManager.selectSigleOrder(order.getId().toString());
				//限时页面倒计时
				Long createTime = od.getCreateDate();
				Long now = System.currentTimeMillis();
				Long outTimeStamp = createTime*1000 + orderOutTime*60000 - now;
				order.setOutTimeStamp(outTimeStamp);
			}
		}
		/*for(OrderDetailsPOJO order : orders){
			setGoods(order);
		}*/
		return orders;
	}

	/***********************************确认收货************************************************/
	/**
	 * 修改订单状态
	 * @param orderDetailId
	 * @return
	 */
	@RequestMapping("/confirmReceipt")
	@ResponseBody
	public ReturnStatus confirmReceipt(@RequestParam String orderDetailId){
		ReturnStatus rs = new ReturnStatus();
		Integer num = orderManager.confirmReceipt(orderDetailId);
		if(num != null){
			rs.setStatus(1);
		}else{
			rs.setStatus(0);
		}
		return rs;
	}


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
			GoodsPOJO goods = o.getGoods();
			goods.setMarketMoney(MoneyFormat.priceFormatString(goods.getMarketPrice()));
			goods.setGoodsMoney(MoneyFormat.priceFormatString(goods.getShopPrice()));
			goods.setMarketMoney(MoneyFormat.priceFormatString(goods.getShopPrice()));
		}
		return order;
	}

	/**
	 * 后台设置金额格式
	 * @param order
	 * @return
	 */
	public List<OrderDetailsPOJO> setBackstageMoney(List<OrderDetailsPOJO> order){
		for (OrderDetailsPOJO o: order) {
			GoodsPOJO goods = o.getGoods();
			goods.setMarketMoney(MoneyFormat.priceFormatString(goods.getShopPrice()));
		}
		return order;
	}


	//后台订单系统管理

	/**
	 * 查询关闭订单
	 * @param currentPage
	 * @return
	 */
	@RequestMapping("/selectBackstageOrderClose")
	@ResponseBody
	public Map<String, Object> selectBackstageOrderClose(Integer currentPage) {
		Map<String, Object>  map = new HashMap<String, Object>();
		if(currentPage == null || currentPage <= 0){
			map.put("code",1004);
			map.put("msg","传入当前页码不正确");
			Map<String, Object>  resultMap = new HashMap<String, Object>();
			resultMap.put("status",map);
			resultMap.put("data","");
			return resultMap;
		}
		int page = (currentPage - 1) * PAGE_SIZE;
		List<OrderDetailsPOJO> order = orderManager.selectBackstageOrderClose(page,PAGE_SIZE);
		order = setBackstageMoney(order);

		map.put("code",1001);
		map.put("msg","请求成功");
		Map<String, Object>  resultMap = new HashMap<String, Object>();
		resultMap.put("status",map);
		resultMap.put("data",order);
		return resultMap;
	}
	/**
	 * 所有订单
	 * @param currentPage
	 * @return
	 */
	@RequestMapping("/selectBackstageOrders")
	@ResponseBody
	public Map<String, Object> selectBackstageOrders(Integer currentPage){
		Map<String, Object>  map = new HashMap<String, Object>();
		Map<String, Object>  resultMap = new HashMap<String, Object>();
		if(currentPage == null || currentPage <= 0){
			map.put("code",1004);
			map.put("msg","传入当前页码不正确");
			resultMap.put("status",map);
			resultMap.put("data","");
			return resultMap;
		}
		int page = (currentPage - 1) * PAGE_SIZE;
		List<OrderDetailsPOJO> order = orderManager.selectBackstageOrders(page,PAGE_SIZE);
		order = setBackstageMoney(order);
		map.put("code",1001);
		map.put("msg","请求成功");
		resultMap.put("status",map);
		resultMap.put("data",order);
		return resultMap;
	}

	/**
	 * 待支付订单
	 * @param currentPage
	 * @return
	 */
	@RequestMapping("/selectBackstagePendingPaymentOrder")
	@ResponseBody
	public Map<String, Object>  selectBackstagePendingPaymentOrder(Integer currentPage){
		Map<String, Object>  map = new HashMap<String, Object>();
		Map<String, Object>  resultMap = new HashMap<String, Object>();
		if(currentPage == null || currentPage <= 0){
			map.put("code",1004);
			map.put("msg","传入当前页码不正确");
			resultMap.put("status",map);
			resultMap.put("data","");
			return resultMap;
		}else{
			int page = (currentPage - 1) * PAGE_SIZE;
			List<OrderDetailsPOJO> order = orderManager.selectBackstagePendingPaymentOrder(page,PAGE_SIZE);
			order = setBackstageMoney(order);
			map.put("code",1001);
			map.put("msg","请求成功");
			resultMap.put("status",map);
			resultMap.put("data",order);
		}



		return resultMap;
	}
	/**
	 * 待发货
	 * @param currentPage
	 * @return
	 */
	@RequestMapping("/selectBackstageAwaitDeliverOrder")
	@ResponseBody
	public Map<String, Object> selectBackstageAwaitDeliverOrder(Integer currentPage){
		Map<String, Object>  map = new HashMap<String, Object>();
		Map<String, Object>  resultMap = new HashMap<String, Object>();
		if(currentPage == null || currentPage <= 0){
			map.put("code",1004);
			map.put("msg","传入当前页码不正确");
			resultMap.put("status",map);
			resultMap.put("data","");
			return resultMap;
		}else{
			int page = (currentPage - 1) * PAGE_SIZE;
			List<OrderDetailsPOJO> order = orderManager.selectBackstageAwaitDeliverOrder(page,PAGE_SIZE);
			order = setBackstageMoney(order);
			map.put("code",1001);
			map.put("msg","请求成功");
			resultMap.put("status",map);
			resultMap.put("data",order);
		}
		return resultMap;
	}

	/**
	 * 待收货订单
	 * @param currentPage
	 * @return
	 */
	@RequestMapping("/selectBackstageAwaitGoodsReceipt")
	@ResponseBody
	public Map<String, Object> selectAwaitGoodsReceipt(Integer currentPage){
		Map<String, Object>  map = new HashMap<String, Object>();
		Map<String, Object>  resultMap = new HashMap<String, Object>();
		if(currentPage == null || currentPage <= 0){
			map.put("code",1004);
			map.put("msg","传入当前页码不正确");
			resultMap.put("status",map);
			return resultMap;
		}else{
			int page = (currentPage - 1) * PAGE_SIZE;
			List<OrderDetailsPOJO> order = orderManager.selectBackstageAwaitGoodsReceipt(page,PAGE_SIZE);
			order = setBackstageMoney(order);
			map.put("code",1001);
			map.put("msg","请求成功");
			resultMap.put("status",map);
			resultMap.put("data",order);
		}
		return resultMap;
	}

	/**
	 * 已完成订单
	 * @param currentPage
	 * @return
	 */
	@RequestMapping("/selectFinishOrder")
	@ResponseBody
	public Map<String, Object>  selectFinishOrder(Integer currentPage){
		Map<String, Object>  map = new HashMap<String, Object>();
		Map<String, Object>  resultMap = new HashMap<String, Object>();
		if(currentPage == null || currentPage <= 0){
			map.put("code",1004);
			map.put("msg","传入当前页码不正确");
			resultMap.put("status",map);
			resultMap.put("data","");
			return resultMap;
		}else{
			int page = (currentPage - 1) * PAGE_SIZE;
			List<OrderDetailsPOJO> order = orderManager.selectFinishOrder(page,PAGE_SIZE);
			order = setBackstageMoney(order);
			map.put("code",1001);
			map.put("msg","请求成功");
			resultMap.put("status",map);
			resultMap.put("data",order);
		}
		return resultMap;
	}

	/**
	 * 查询关闭订单数量
	 * @return
	 */
	@RequestMapping("/selectBackstageOrderCloseCount")
	@ResponseBody
	public Map<String, Object> selectBackstageOrderCloseCount(){
		Integer count = orderManager.selectBackstageOrderCloseCount()/PAGE_SIZE;
		if(count <= 0)
			count = 1;
		Map<String, Object>  map = new HashMap<String, Object>();
		Map<String, Object>  resultMap = new HashMap<String, Object>();
		map.put("code",1001);
		map.put("msg","请求成功");
		resultMap.put("status",map);
		resultMap.put("data",count);
		return resultMap;
	}

	/**
	 * 查询所有订单总数量
	 * @return
	 */
	@RequestMapping("/selectBackstageOrdersCount")
	@ResponseBody
	public Map<String, Object> selectBackstageOrdersCount(){
		Integer count = orderManager.selectBackstageOrdersCount()/PAGE_SIZE;
		if(count <= 0)
			count = 1;
		Map<String, Object>  map = new HashMap<String, Object>();
		Map<String, Object>  resultMap = new HashMap<String, Object>();
		map.put("code",1001);
		map.put("msg","请求成功");
		resultMap.put("status",map);
		resultMap.put("data",count);
		return resultMap;
	}
	/**
	 * 查询待付款数量
	 * @return
	 */
	@RequestMapping("/selectBackstagePendingPaymentOrderCount")
	@ResponseBody
	public Map<String, Object> selectBackstagePendingPaymentOrderCount(){
		Integer count = orderManager.selectBackstagePendingPaymentOrderCount()/PAGE_SIZE;
		if(count <= 0)
			count = 1;
		Map<String, Object>  map = new HashMap<String, Object>();
		Map<String, Object>  resultMap = new HashMap<String, Object>();
		map.put("code",1001);
		map.put("msg","请求成功");
		resultMap.put("status",map);
		resultMap.put("data",count);
		return resultMap;
	}
	/**
	 * 查询代发货数量
	 * @return
	 */
	@RequestMapping("/selectBackstageAwaitDeliverOrderCount")
	@ResponseBody
	public Map<String, Object> selectBackstageAwaitDeliverOrderCount(){
		Integer count = orderManager.selectBackstageAwaitDeliverOrderCount()/PAGE_SIZE;
		if(count == 0){
			count = 1;
		}
		Map<String, Object>  map = new HashMap<String, Object>();
		Map<String, Object>  resultMap = new HashMap<String, Object>();
		map.put("code",1001);
		map.put("msg","请求成功");
		resultMap.put("status",map);
		resultMap.put("data",count);
		return resultMap;
	}
	/**
	 * 查询待收货数量
	 * @return
	 */
	@RequestMapping("/selectBackstageAwaitGoodsReceiptCount")
	@ResponseBody
	public Map<String, Object> selectBackstageAwaitGoodsReceiptCount(){
		Integer count = orderManager.selectBackstageAwaitGoodsReceiptCount();
		if(count <= 0){
			count = 1;
		}
		Map<String, Object>  map = new HashMap<String, Object>();
		Map<String, Object>  resultMap = new HashMap<String, Object>();
		map.put("code",1001);
		map.put("msg","请求成功");
		resultMap.put("status",map);
		resultMap.put("data",count);
		return resultMap;
	}
	/**
	 * 查询已完成订单数量
	 * @return
	 */
	@RequestMapping("/selectFinishOrderCount")
	@ResponseBody
	public Map<String, Object> selectFinishOrderCount(){
		Integer count = orderManager.selectFinishOrderCount()/PAGE_SIZE;
		if(count <= 0)
			count = 1;
		Map<String, Object>  map = new HashMap<String, Object>();
		Map<String, Object>  resultMap = new HashMap<String, Object>();
		map.put("code",1001);
		map.put("msg","请求成功");
		resultMap.put("status",map);
		resultMap.put("data",count);
		return resultMap;
	}

	/**
	 * 发货
	 * @param order
	 * @return
	 */
	@RequestMapping(value="/selectFinishOrderCount",method={RequestMethod.POST})
	@ResponseBody
	public Map<String, Object> deliverOrder(@RequestBody OrderDetailsPOJO order){
		Map<String, Object>  map = new HashMap<String, Object>();
		Map<String, Object>  resultMap = new HashMap<String, Object>();
		if(order.getOrderId() == null){
			map.put("code",1004);
			map.put("msg","发货错误");
			resultMap.put("status",map);
			return resultMap;
		}else{
			int orderStatus = orderManager.deliverOrder(order);
			if(orderStatus <= 0){
				map.put("code",1004);
				map.put("msg","发货失败");
				resultMap.put("status",map);
			}else{
				map.put("code",1001);
				map.put("msg","请求成功");
				resultMap.put("status",map);
			}
			resultMap.put("data",orderStatus);
		}
		return resultMap;
	}

	/**
	 * 订单备注
	 * @return
	 */
	@RequestMapping("/orderRemark")
	@ResponseBody
	public Map<String, Object> orderRemark(Long orderId,String remark){
		Map<String, Object>  map = new HashMap<String, Object>();
		Map<String, Object>  resultMap = new HashMap<String, Object>();
		if(orderId == null || remark == null){
			map.put("code",1004);
			map.put("msg","参数错误");
			resultMap.put("status",map);
			return resultMap;
		}else{
			int status = orderManager.orderRemark(orderId,remark);
			if(status <= 0){
				map.put("code",1004);
				map.put("msg","备注失败");
				resultMap.put("status",map);
			}else{
				map.put("code",1001);
				map.put("msg","请求成功");
				resultMap.put("status",map);
			}

			resultMap.put("data",status);
		}
		return resultMap;
	}
}