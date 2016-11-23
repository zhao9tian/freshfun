package com.quxin.freshfun.service.impl.refund;

import com.quxin.freshfun.dao.OrderDetailsMapper;
import com.quxin.freshfun.dao.RefundMapper;
import com.quxin.freshfun.model.RefundPOJO;
import com.quxin.freshfun.service.refund.RefundService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service("refundService")
public class RefundServiceImpl implements RefundService{

	private final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	private RefundMapper refundMapper;
	@Autowired
	private OrderDetailsMapper orderDetailsMapper;
	
	@Override
	public Boolean save(RefundPOJO refund) {
		Boolean b = false;
		//保存退款申请信息
		if(refund != null) {
			Integer refundMoney = refund.getReturnMoney();
			String serverType = refund.getServiceType();
			String refundReason = refund.getReturnReason();
			if (refundMoney == null || refundMoney == 0) {
				logger.error("退款金额不能为空");
				return false;
			}
			if (serverType == null || "".equals(serverType)) {
				logger.error("服务类型未选择");
				return false;
			}
			if (refundReason == null || "".equals(refundReason)) {
				logger.error("退款原因未选择");
				return false;
			}
			Integer isSuc1 = refundMapper.save(refund);
			if(isSuc1 == 1){
				//修改订单状态为退货,保存退款前的状态
				Integer orderStatus = orderDetailsMapper.selectSigleOrder(refund.getOrderId()).getOrderStatus();
				Map<String , Object> map = new HashMap<>();
				map.put("orderId" ,refund.getOrderId());
				map.put("orderStatus" , orderStatus);
				Integer isSuc2 = refundMapper.updateRefundStatus(map);
				if(isSuc2 == 1){
					b = true;
				}
			}else{
				logger.error("保存退款申请不成功");
				return false;
			}
		}else{
			logger.error("退款对象不能为空");
			return false;
		}
		return b;
	}

}
