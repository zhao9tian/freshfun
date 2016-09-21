package com.quxin.freshfun.controller.refund;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.quxin.freshfun.model.RefundPOJO;
import com.quxin.freshfun.model.ReturnStatus;
import com.quxin.freshfun.service.refund.RefundService;

@RequestMapping("/refund")
@Controller
public class RefundController {
	
	@Autowired
	private RefundService refundSerivce;
	
	/**
	 * 保存退款信息,返回是否退款成功
	 * @param refund
	 * @return
	 */
	@RequestMapping("/saveRefund")
	@ResponseBody
	public ReturnStatus insertRefund(@RequestBody RefundPOJO refund){
		ReturnStatus rs = new ReturnStatus();
		refund.setGmtCreate(System.currentTimeMillis()/1000);
		refund.setGmtModified(System.currentTimeMillis()/1000);
		Integer refundId = refundSerivce.save(refund);
		if(refundId !=null){
			rs.setStatus(1);//表示退款信息保存成功
			
		}else{
			rs.setStatus(0);//保存不成功
		}
		return rs;
	}
}
