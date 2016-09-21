package com.quxin.freshfun.controller.logistical;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.quxin.freshfun.model.Logistical;
import com.quxin.freshfun.model.OrderDetailsPOJO;
import com.quxin.freshfun.service.order.OrderService;
import com.quxin.freshfun.utils.GtexMd5B32Util;

/**
 * 物流接口
 * @author TuZl
 * @time 2016年9月9日下午9:02:27
 */
@Controller
@RequestMapping("/logistical")
public class LogisticalController {

	@Autowired
	private OrderService orderService;
	
	/**
	 * 根据订单明细ID查询出物流信息
	 * @param orderDetailId
	 * @param response
	 * @return
	 */
	@RequestMapping("/getLogistical")
	@ResponseBody
	public Logistical getLogistical(@RequestParam String orderDetailId , HttpServletResponse response){//订单编号 物流公司
		OrderDetailsPOJO orderDetails = orderService.getOrderLogistic(orderDetailId);
		String waybill = orderDetails.getDeliveryNum();
		String company = orderDetails.getDeliveryName();
		
		String result=GtexMd5B32Util.insertPostHttp(waybill,company);
		Gson gson = new Gson();
		Logistical logistical = new Logistical();
		logistical = gson.fromJson(result, Logistical.class);
		return logistical;
	}
	
}
