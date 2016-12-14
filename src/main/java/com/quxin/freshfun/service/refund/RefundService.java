package com.quxin.freshfun.service.refund;

import com.quxin.freshfun.model.RefundPOJO;

public interface RefundService {

	/**
	 * 保存退款信息
	 * 返回自增id
	 * @param refund 实体类
	 */
 	Boolean save(RefundPOJO refund);

	/**
	 * 查询退款详情
	 * @param orderId 订单id
	 * @return 订单详情
	 */
	RefundPOJO queryRefund(Long orderId);
	
}
