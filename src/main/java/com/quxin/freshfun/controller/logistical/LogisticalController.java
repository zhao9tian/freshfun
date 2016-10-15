package com.quxin.freshfun.controller.logistical;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.quxin.freshfun.model.Logistical;
import com.quxin.freshfun.model.LogisticalContent;
import com.quxin.freshfun.model.OrderDetailsPOJO;
import com.quxin.freshfun.service.order.OrderService;
import com.quxin.freshfun.utils.GtexMd5B32Util;
import com.quxin.freshfun.utils.ResultUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 物流接口
 * @author qucheng
 * @time 2016年9月9日下午9:02:27
 */
@Controller
@RequestMapping("/logistical")
public class LogisticalController {

	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	private OrderService orderService;
	
 	/**
	 * 根据订单明细ID查询出物流信息
	 * @param orderDetailId 订单id
	 * @return 返回请求是否成功
	 */
	@RequestMapping("/getLogistical")
	@ResponseBody
	public Map<String , Object> getLogistical(@RequestParam String orderDetailId){//订单编号 物流公司
		OrderDetailsPOJO orderDetails = orderService.getOrderLogistic(orderDetailId);
		Map<String , Object> result;
		if(orderDetails == null){
			logger.error("没有订单编号为"+orderDetailId+"的订单");
			result = ResultUtil.fail(1004 , "没有订单编号为"+orderDetailId+"的订单");
			return result;
		}else{
			//物流单号
			String waybill = orderDetails.getDeliveryNum();
			//物流公司编号
			String company = orderDetails.getDeliveryName();
			if(waybill == null || "".equals(waybill) || company == null || "".equals(company)){
				logger.error("订单编号为"+orderDetailId+"的订单未填写物流信息,或者是填写不完整");
				result = ResultUtil.fail(1004 , "订单编号为"+orderDetailId+"的订单未填写物流信息,或者是填写不完整");
				return result;
			}
			Logistical logistical = new Logistical();
			//获取物流信息
			String logisticalInfo = GtexMd5B32Util.insertPostHttp(waybill,company);
			Map map = JSON.parseObject(logisticalInfo,Map.class);
			Object traces = map.get("gtex_traces");
			if(traces == null){
				logger.error("订单编号为"+orderDetailId+"的订单没有物流信息");
				result = ResultUtil.fail(1004 , "订单编号为"+orderDetailId+"的订单没有物流信息");
				return result;
			}
			JSONArray jsonArray = (JSONArray) map.get("gtex_traces");
			Object[] objArr = jsonArray.toArray();

			List<LogisticalContent> list = new ArrayList<>();
			for (Object anObjArr : objArr) {
				LogisticalContent p = new LogisticalContent();
				Map ma = (Map) anObjArr;
				p.setContent(ma.get("content").toString());
				p.setTime(ma.get("time").toString());
				list.add(p);
			}
			logistical.setGtexTraces(list);
			result = ResultUtil.success(logistical);
		}
		return result;
	}
	
}
