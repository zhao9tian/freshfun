package com.quxin.freshfun.controller.task;

import com.quxin.freshfun.model.OrderDetailsPOJO;
import com.quxin.freshfun.service.bill.BillService;
import com.quxin.freshfun.service.order.OrderManager;
import com.quxin.freshfun.service.user.IdentifiedCodeService;
import com.quxin.freshfun.utils.BusinessException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.List;

@Controller
public class AutoConfirmTask {
    @Autowired
    private OrderManager orderManager;
    @Autowired
    private BillService billService;
    @Autowired
    private IdentifiedCodeService identifiedCodeService;

    /**
     * 14天自动确认收货时添加账单信息
     * 每天凌晨1,2,3,4点触发
     */
    @Scheduled(cron="0 0 1,2,3,4 * * ? ")
    public void autoConfirmDelivery() throws BusinessException {
        System.out.println("开始执行");
        //查询订单未确认收货信息
        List<OrderDetailsPOJO> orders = orderManager.autoConfirmDelivery();
        if(orders != null){
            for (OrderDetailsPOJO order: orders) {
                //修改订单状态
                int status = orderManager.autoConfirmDelivery(order.getId().toString());
                if(status <= 0){
                    throw new BusinessException("修改7点自动确认收货失败");
                }
            }
        }
    }

    /**
     * 7天自动走账单流程
     * 每天凌晨1.2.3.4点执行
     */
    @Scheduled(cron="0 0 1,2,3,4 * * ? ")
    public void autoConfirmRecording() throws BusinessException {
        billService.addAutoConfirmRecording();
    }

    /**
     * 删除验证码
     * 每天凌晨1点执行
     */
    @Scheduled(cron="0 0 1 * * ? ")
    public void deleteVerifyCode(){
        identifiedCodeService.removeIdentifiedCode();
    }

    /**
     * 扫描超过一天没支付订单
     */
    @Scheduled(cron="0 0 *／1 * * ? ")
    public void scanningOvertimeOrder(){
        orderManager.scanningOvertimeOrder();
    }

    /**
     * 扫描限时购订单超半小时
     */
    @Scheduled(cron = "0 * * * * ?")
    public void scanningOverLimitedOrder(){
        orderManager.selectOverTimeLimitedOrder();
    }
}
