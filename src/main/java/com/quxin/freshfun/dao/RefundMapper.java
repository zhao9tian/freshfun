package com.quxin.freshfun.dao;

import com.quxin.freshfun.model.RefundPOJO;

public interface RefundMapper {

	/**
	 * 保存退款信息
	 * @param refund
	 * @return
	 */
	Integer save(RefundPOJO refund);
	
	/**
	 * 根据订单Id修改退款状态
	 * @param orderDetailId
	 * @return
	 */
	Integer updateRefundStatus(String orderDetailId);
}
