package com.quxin.freshfun.controller.refund;

import com.quxin.freshfun.model.OrderDetailsPOJO;
import com.quxin.freshfun.model.RefundPOJO;
import com.quxin.freshfun.model.param.RefundParam;
import com.quxin.freshfun.service.order.OrderManager;
import com.quxin.freshfun.service.refund.RefundService;
import com.quxin.freshfun.utils.ResultUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Map;

@RequestMapping("/refund")
@Controller
public class RefundController {


	@Autowired
	private RefundService refundSerivce;

	@Autowired
	private OrderManager orderManager;

	/**
	 * 保存退款信息,返回是否退款成功
	 * @param refundParam 退款申请入参
	 * @return 返回请求结果
	 */
	@RequestMapping(value="/saveRefund" , method = RequestMethod.POST)
	@ResponseBody
	public Map<String , Object> insertRefund(@RequestBody RefundParam refundParam){
		Map<String , Object> result ;
		if(refundParam != null){
			if(refundParam.getOrderDetailsId() != null && !"".equals(refundParam.getOrderDetailsId())){
				Long orderId = Long.valueOf(refundParam.getOrderDetailsId());
				if(orderId != 0){
					OrderDetailsPOJO orderDetailsPOJO = orderManager.selectSigleOrder(orderId);
					if(orderDetailsPOJO == null){
						return ResultUtil.fail(1004 , "该订单不存在");
					}
				}else{
					return ResultUtil.fail(1004 ,"订单号为空");
				}
				String refundMoney = refundParam.getReturnMoney();
				String serverType = refundParam.getServiceType();
				String refundReason = refundParam.getReturnReason();
				if(refundMoney == null || "".equals(refundMoney) ){
					return ResultUtil.fail(1004 , "退款金额不能为空");
				}
				if(serverType == null || "".equals(serverType) || "请选择申请服务".equals(serverType)){
					return ResultUtil.fail(1004 , "服务类型未选择");
				}
				if(refundReason == null || "".equals(refundReason) || "请选择退货原因".equals(refundReason)){
					return ResultUtil.fail(1004, "退款原因未选择");
				}
				RefundPOJO refund = new RefundPOJO();
				Integer money = (int)(Float.parseFloat(refundMoney)*100);
				refund.setReturnMoney(money);
				refund.setReturnReason(refundReason);
				refund.setReturnDes(refundParam.getRefundDes());
				refund.setServiceType(serverType);
				refund.setOrderId(orderId);
				refund.setGmtCreate(System.currentTimeMillis()/1000);
				refund.setGmtModified(System.currentTimeMillis()/1000);
				Boolean isSuc = refundSerivce.save(refund);
				if(isSuc){
					result = ResultUtil.success(1);
				}else{
					result = ResultUtil.fail(1004 ,"申请退款失败");
				}
			}else{
				Long orderId = refundParam.getOrderId();
				if(orderId != null && orderId != 0){
					OrderDetailsPOJO orderDetailsPOJO = orderManager.selectSigleOrder(orderId);
					if(orderDetailsPOJO == null){
						return ResultUtil.fail(1004 , "该订单不存在");
					}
				}else{
					return ResultUtil.fail(1004 ,"订单号为空");
				}
				String refundMoney = refundParam.getRefundMoney();
				String serverType = refundParam.getServerType();
				String refundReason = refundParam.getRefundReason();
				if(refundMoney == null || "".equals(refundMoney) ){
					return ResultUtil.fail(1004 , "退款金额不能为空");
				}
				if(serverType == null || "".equals(serverType) || "请选择申请服务".equals(serverType)){
					return ResultUtil.fail(1004 , "服务类型未选择");
				}
				if(refundReason == null || "".equals(refundReason) || "请选择退货原因".equals(refundReason)){
					return ResultUtil.fail(1004, "退款原因未选择");
				}
				RefundPOJO refund = new RefundPOJO();
				Integer money = (int)(Double.parseDouble(refundMoney)*100);
				refund.setReturnMoney(money);
				refund.setReturnReason(refundReason);
				refund.setReturnDes(refundParam.getRefundDes());
				refund.setServiceType(serverType);
				refund.setOrderId(refundParam.getOrderId());
				refund.setGmtCreate(System.currentTimeMillis()/1000);
				refund.setGmtModified(System.currentTimeMillis()/1000);
				Boolean isSuc = refundSerivce.save(refund);
				if(isSuc){
					result = ResultUtil.success(1);
				}else{
					result = ResultUtil.fail(1004 ,"申请退款失败");
				}
			}
		}else{
			result = ResultUtil.fail(1004 , "退款对象不能为null");
		}
		return result;
	}



}
