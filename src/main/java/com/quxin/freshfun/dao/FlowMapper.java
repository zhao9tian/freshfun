package com.quxin.freshfun.dao;

import com.quxin.freshfun.model.parm.FlowParam;
import com.quxin.freshfun.model.pojo.FlowPOJO;

import java.util.List;
import java.util.Map;

public interface FlowMapper {


    /**
     * 添加财务流水
     * @param flowParam
     * @return
     */
    Integer insert(FlowParam flowParam);

    /**
     * 通过用户ID查流水
     * @param map
     * @return
     */
    List<FlowPOJO> selectFlowListByUserId(Map map);

    /**
     * 通过单号查询流水明细
     * @param orderId
     * @return
     */
    FlowPOJO selectFlowByOrderId(Long orderId);

    /**
     * 获取最新一条的用户id
     * @param userId
     * @return
     */
    int selectLastedFlowByUserId(Long userId);

    /**
     * 通过主键查询流水明细
     * @param id
     * @return
     */
    FlowPOJO selectFlowById(Integer id);

    /**
     * 查询总数
     * @param userId
     * @return
     */
    Integer getCount(Long userId);
}