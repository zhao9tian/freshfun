package com.quxin.freshfun.controller.task;

import com.quxin.freshfun.model.OrderDetailsPOJO;
import com.quxin.freshfun.model.param.FlowParam;
import com.quxin.freshfun.service.bill.BillService;
import com.quxin.freshfun.service.flow.FlowService;
import com.quxin.freshfun.service.order.OrderManager;
import com.quxin.freshfun.service.user.VerifiedCodeService;
import com.quxin.freshfun.utils.BusinessException;
import com.quxin.freshfun.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

import java.util.List;

/**
 * Created by gsix on 2016/9/30.
 */
@Controller
public class AutoConfirmTask {
    @Autowired
    private OrderManager orderManager;
    @Autowired
    private BillService billService;
    @Autowired
    private FlowService flowService;

    @Autowired
    private VerifiedCodeService verifiedCodeService;

    /**
     * 7天自动确认收货时添加账单信息
     * 每天凌晨1点触发
     */
    @Scheduled(cron="0 0 1 * * ? ")
    public void autoConfirmDelivery() throws BusinessException {
        System.out.println("开始执行");
        //查询订单未确认收货信息
        List<OrderDetailsPOJO> orders = orderManager.autoConfirmDelivery();
        System.out.println(orders.toString());
        if(orders != null){
            for (OrderDetailsPOJO order: orders) {
                //修改订单状态
                int status = orderManager.confirmGoodsReceipt(order.getId().toString());
                if(status <= 0){
                    throw new BusinessException("修改7点自动确认收货失败");
                }
                Long currentDate = DateUtils.getCurrentDate();
                FlowParam flowParam = new FlowParam();
                flowParam.setOrderId(order.getId());
                flowParam.setUserId(order.getUserId());
                flowParam.setCreated(currentDate);
                flowParam.setUpdated(currentDate);
                flowParam.setAgentFlow(order.getAgentPrice().longValue());
                flowParam.setFetcherFlow(order.getFetcherPrice().longValue());
                flowService.add(flowParam);
            }
        }
    }

    /**
     * 7天自动走账单流程
     */
    @Scheduled(cron="0 0 1 * * ? ")
    public void autoConfirmRecording() throws BusinessException {
        billService.autoConfirmRecording();
    }

    /**
     * 删除验证码
     */
    @Scheduled(cron="0 0 1 * * ? ")
    public void deleteVerifyCode(){
        verifiedCodeService.removeVerifyCode();
    }

}