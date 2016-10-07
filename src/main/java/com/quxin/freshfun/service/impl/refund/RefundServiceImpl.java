package com.quxin.freshfun.service.impl.refund;

import com.quxin.freshfun.dao.OrderDetailsMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quxin.freshfun.dao.RefundMapper;
import com.quxin.freshfun.model.RefundPOJO;
import com.quxin.freshfun.service.refund.RefundService;

import java.util.HashMap;
import java.util.Map;

@Service("refundService")
public class RefundServiceImpl implements RefundService{

	@Autowired
	private RefundMapper refundMapper;
	@Autowired
	private OrderDetailsMapper orderDetailsMapper;
	
	@Override
	public Integer save(RefundPOJO refund) {
		//保存退款申请信息
		refundMapper.save(refund);
		//修改订单状态为退货
		//保存退款前的状态
		Integer orderStatus = orderDetailsMapper.selectSigleOrder(refund.getOrderDetailsId()).get(0).getOrderStatus();
		Map<String , Object> map = new HashMap<>();
		map.put("orderId" ,refund.getOrderDetailsId());
		map.put("orderStatus" , orderStatus);
		refundMapper.updateRefundStatus(map);
		return refund.getId();
	}

}
