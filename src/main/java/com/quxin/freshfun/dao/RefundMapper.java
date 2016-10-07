package com.quxin.freshfun.dao;

import com.quxin.freshfun.model.RefundPOJO;

import java.util.Map;

public interface RefundMapper {

	/**
	 * 保存退款信息
	 * @param refund 退款对象
	 * @return 返回保存的条数
	 */
	Integer save(RefundPOJO refund);
	
	/**
	 * 根据订单Id修改退款状态
	 * @param map 订单id, 订单状态
	 * @return 返回修改的数目
	 */
	Integer updateRefundStatus(Map<String , Object> map);
}
