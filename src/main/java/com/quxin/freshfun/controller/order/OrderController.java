package com.quxin.freshfun.controller.order;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.quxin.freshfun.model.*;
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
	
	private static Integer orderOutTime;
	
	private Logger resultLogger = LoggerFactory.getLogger("info_log");
	
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
		return setGoodsList(orderManager.findAll(ui, currentPage, pageSize) , 1);
	}
	/**
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
		
		//控制付款成功页面申请退款按钮是否有效 isRefund 1是有效  0是无效
		int commenStatus = orderDetail.getCommentStatus();
		if (commenStatus == 1) {
			orderDetail.setIsRefund(0);
		} else {
			Long reciveTime = orderDetail.getReciveTime();
			if(reciveTime != null){
				Long now1 = System.currentTimeMillis();
				Long outTimeStamp1 = reciveTime * 1000 + 1000 * 60 * 60 * 24 * 5 - now1;
				if (outTimeStamp1 < 0) {
					orderDetail.setIsRefund(1);
				}else{
					orderDetail.setIsRefund(0);
				}
			}
		}
		return setGoods(orderDetail);
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
		return setGoodsList(orderManager.selectAwaitDeliverOrder(ui,currentPage,pageSize),null);
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
		return setGoodsList(orderManager.selectAwaitGoodsReceipt(ui, currentPage, pageSize),null);
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
		return setGoodsList(orderManager.selectAwaitComment(ui, currentPage, pageSize),null);
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
		return setGoodsList(orderManager.selectCancelOrder(ui, currentPage, pageSize),null);
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
    	int status = orderManager.confirmGoodsReceipt(orderId);
    	ResponseResult r = new ResponseResult();
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
	 * @param userID
	 * @return
	 */
	@RequestMapping("/selectordercounts")
	@ResponseBody
	public Map<String, Object> selectOrderCounts(String userID){
		Long ui = Long.parseLong(userID.replace("\"", ""));
		List<OrderStatusInfo> statusCounts = orderManager.selectStatusCounts(ui);
		
		List<OrderStatusInfo> refundCounts = orderManager.selectRefundCounts(ui);
		
		List<OrderStatusInfo> payCounts = orderManager.selectPayCounts(ui);
		
		Map<String, Object> orderMap = new HashMap<String, Object>(5);
		orderMap.put("daishouhuo","");
		orderMap.put("daipingjia","");
		orderMap.put("daifahuo","");
		orderMap.put("tuihuo","");
		orderMap.put("daifukuan","");
		
		int a = 0;
		int b = 0;
		for(OrderStatusInfo orderStatus : statusCounts){
			if(orderStatus.getOrderStatus() == 0){
				a = orderStatus.getStatusCounts();
			}else if(orderStatus.getOrderStatus() == 1){
				b = orderStatus.getStatusCounts();
			}else if(orderStatus.getOrderStatus() == 2){
				orderMap.put("daishouhuo", orderStatus.getStatusCounts());
			}else if(orderStatus.getOrderStatus() == 3){
				orderMap.put("daipingjia", orderStatus.getStatusCounts());
			}
		}
		a = a+b;
		if(a == 0){
			orderMap.put("daifahuo", "");
		}else{
		    orderMap.put("daifahuo", a);
		}
		
		for(OrderStatusInfo orderStatus : refundCounts){
			if(orderStatus.getRefundCounts() == 0){
				orderMap.put("tuihuo", "");
			}else{
				orderMap.put("tuihuo", orderStatus.getRefundCounts());
			}
			
		}
		
		for(OrderStatusInfo orderStatus : payCounts){
			if(orderStatus.getPayCounts() == 0){
				orderMap.put("daifukuan", "");
			}else{
				orderMap.put("daifukuan", orderStatus.getPayCounts());
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
	
	/**
	 * 给订单设置商品信息
	 * @param orderDetail
	 * @return
	 */
	public OrderDetailsPOJO setGoods(OrderDetailsPOJO orderDetail){
		Integer goodsId = orderDetail.getGoodsId();
		Integer isLimit = orderDetail.getIsLimit();//1是限时购
		if(isLimit == 0){
			orderDetail.setGoods(goodsService.findGoodsMysql(goodsId));
		}else{
			orderDetail.setGoods(goodsDao.findById(goodsId));
		}
		return orderDetail;
	}
	
	public List<OrderDetailsPOJO> setGoodsList(List<OrderDetailsPOJO> orders  , Integer rukou){
		if(rukou != null && rukou == 1){
			for(OrderDetailsPOJO order : orders){
				OrderDetailsPOJO od = orderManager.selectSigleOrder(order.getOrderDetailsId());
				//限时页面倒计时
				Long createTime = od.getCreateDate();
				Long now = System.currentTimeMillis();
				Long outTimeStamp = createTime*1000 + orderOutTime*60000 - now;
				order.setOutTimeStamp(outTimeStamp);
			}
		}
		for(OrderDetailsPOJO order : orders){
			setGoods(order);
		}
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
		Integer num = orderManager.applyRefund(orderDetailId);
		if(num != null){
			rs.setStatus(1);//不为空就表示未过期
		}else{
			rs.setStatus(0);//为空表示已过期
		}
		return rs;
	}
	
}