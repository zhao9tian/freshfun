package com.quxin.freshfun.service.refund;

import com.quxin.freshfun.model.RefundPOJO;

public interface RefundService {

	/**
	 * 保存退款信息
	 * 返回自增id
	 * @param refund
	 */
 	Boolean save(RefundPOJO refund);
	
	
	
}
