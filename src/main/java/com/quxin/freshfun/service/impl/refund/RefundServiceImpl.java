package com.quxin.freshfun.service.impl.refund;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.quxin.freshfun.dao.RefundMapper;
import com.quxin.freshfun.model.RefundPOJO;
import com.quxin.freshfun.service.refund.RefundService;

@Service
public class RefundServiceImpl implements RefundService{

	@Autowired
	private RefundMapper refundDao;
	
	@Override
	public Integer save(RefundPOJO refund) {
		refundDao.save(refund);
		refundDao.updateRefundStatus(refund.getOrderDetailsId());
		return refund.getId();
	}

}
