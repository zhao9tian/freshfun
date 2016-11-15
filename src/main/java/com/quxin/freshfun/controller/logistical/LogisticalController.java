package com.quxin.freshfun.controller.logistical;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.google.common.collect.Maps;
import com.quxin.freshfun.model.OrderDetailsPOJO;
import com.quxin.freshfun.service.order.OrderService;
import com.quxin.freshfun.utils.KdniaoTrackQueryAPI;
import com.quxin.freshfun.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 修改物流接口
 * updated by qucheng on 2016/11/09
 */
@Controller
@RequestMapping("/logistical")
public class LogisticalController {

	@Autowired
	private OrderService orderService;
	
 	/**
	 * 根据订单明细ID查询出物流信息
	 * @param orderId 订单id
	 * @return 返回请求是否成功
	 */
	@RequestMapping("/queryLogisticalByOrderId")
	@ResponseBody
	public Map<String , Object> getLogistical(@RequestParam Integer orderId) throws Exception {
		Map<String , Object> result;
		if(orderId != null){
			OrderDetailsPOJO orderDetails = orderService.getOrderLogistic(Long.valueOf(orderId));
			if(orderDetails == null){
				result = ResultUtil.fail(1004 , "没有订单编号为"+ orderId +"的订单");
				return result;
			}else{
				String waybill = orderDetails.getDeliveryNum();//物流单号
				String company = orderDetails.getDeliveryName();//物流公司编号
				if(waybill == null || "".equals(waybill) || company == null || "".equals(company)){
					return ResultUtil.fail(1004 , "订单编号为"+ orderId +"的订单物流信息不完整");
				}
				//获取物流信息
				String logisticalInfo = KdniaoTrackQueryAPI.getOrderTracesByJson(company , waybill);
				Map map = JSON.parseObject(logisticalInfo,Map.class);
				Object traces = map.get("Traces");
				if(traces == null){
					return ResultUtil.fail(1004 , "订单编号为"+ orderId +"的订单未发货");
				}
				JSONArray jsonArray = (JSONArray) map.get("Traces");
				Object[] objArr = jsonArray.toArray();
				List<Map> list = new ArrayList<>();
				for (Object anObjArr : objArr) {
					Map<String , Object> trace = Maps.newHashMap();
					Map ma = (Map) anObjArr;
					trace.put("content" , ma.get("AcceptStation").toString());
					trace.put("time",ma.get("AcceptTime").toString());
					list.add(trace);
				}
				Collections.reverse(list);//倒序物流信息
				Map<String , Object> data = Maps.newHashMap();
				data.put("shipperCode" , map.get("ShipperCode"));
				data.put("logisticCode" , map.get("LogisticCode"));
				data.put("traces" , list);
				result = ResultUtil.success(data);
			}
		}else{
			result = ResultUtil.fail(1004 , "订单Id为空");
		}
		return result;
	}


	/**
	 * 根据订单明细ID查询出物流信息
	 * @param orderDetailId 订单id
	 * @return 返回请求是否成功
	 */
	@RequestMapping("/getLogistical")
	@ResponseBody
	public Map<String , Object> getLogisticalToIOS(@RequestParam String orderDetailId) throws Exception {//订单编号 物流公司
		Map<String , Object> result;
		if(orderDetailId != null){
			OrderDetailsPOJO orderDetails = orderService.getOrderLogistic(Long.valueOf(orderDetailId));
			if(orderDetails == null){
				result = ResultUtil.fail(1004 , "订单号为"+orderDetailId+"的订单不存在");
			}else{
				String waybill = orderDetails.getDeliveryNum();
				String company = orderDetails.getDeliveryName();
				if(waybill == null || "".equals(waybill) || company == null || "".equals(company)){
					result = ResultUtil.fail(1004 , "订单编号为"+orderDetailId+"的订单未填写物流信息,或者是填写不完整");
				}else{
					//获取物流信息
					String logisticalInfo = KdniaoTrackQueryAPI.getOrderTracesByJson(company , waybill);
					Map map = JSON.parseObject(logisticalInfo,Map.class);
					Object traces = map.get("Traces");
					if(traces == null){
						return ResultUtil.fail(1004 , "订单编号为"+orderDetailId+"的订单没有物流信息");
					}
					JSONArray jsonArray = (JSONArray) map.get("Traces");
					Object[] arr = jsonArray.toArray();
					List<Map> list = new ArrayList<>();
					for (Object anObjArr : arr) {
						Map ma = (Map) anObjArr;
						Map<String , Object> trace1 = Maps.newHashMap();
						trace1.put("content" , ma.get("AcceptStation").toString());
						trace1.put("time",ma.get("AcceptTime").toString());
						list.add(trace1);
					}
					Collections.reverse(list);//倒序物流信息
					Map<String , Object> data = Maps.newHashMap();
					data.put("getTraces" , list);
					result = ResultUtil.success(data);
				}
			}
		}else{
			result = ResultUtil.fail(1004 , "订单Id不能为空");
		}
		return result;
	}


}
