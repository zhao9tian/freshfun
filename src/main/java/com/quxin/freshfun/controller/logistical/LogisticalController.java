package com.quxin.freshfun.controller.logistical;

import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.quxin.freshfun.model.LogisticalContent;
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

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
		Logistical logistical = new Logistical();
		String result=GtexMd5B32Util.insertPostHttp(waybill,company);

		Map<String,Object> map = JSON.parseObject(result,java.util.Map.class);
		JSONArray jsonArray = (JSONArray) map.get("gtex_traces");
		Object[] objArr = jsonArray.toArray();

		List<LogisticalContent> list = new ArrayList<LogisticalContent>();
		for(int i=0;i<objArr.length;i++) {
			LogisticalContent p = new LogisticalContent();
			Map<String,Object> ma =  (Map)objArr[i];
			p.setContent(ma.get("content").toString());
			p.setTime(ma.get("time").toString());
			list.add(p);
		}
		logistical.setGtexTraces(list);
		return logistical;
	}
	
}
