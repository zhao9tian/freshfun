package com.quxin.freshfun.service.impl.user;

import com.alibaba.fastjson.JSON;
import com.quxin.freshfun.dao.WalletMapper;
import com.quxin.freshfun.model.user.WalletPOJO;
import com.quxin.freshfun.service.user.WalletService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 钱包实现类
 * Created by qucheng on 17/1/18.
 */
@Service("walletService")
public class WalletServiceImpl implements WalletService {

    @Autowired
    private WalletMapper walletMapper;

    private Logger logger = LoggerFactory.getLogger(WalletServiceImpl.class);

    @Override
    public Boolean addWallet(WalletPOJO walletPOJO) {
        //1.校验入参
        if (!validate(walletPOJO)) {
            logger.error("新增红包数据时入参有误:" + JSON.toJSONString(walletPOJO));
            return false;
        } else {
            //2.查询数据表查看用户余额
            Long oldBalance = walletMapper.selectWalletBalanceByUserId(walletPOJO.getUserId());
            if(oldBalance == null){
                oldBalance = 0L;
            }
            //3.根据type计算加入流水之后的余额
            if(walletPOJO.getType().equals(1)){
                if(Math.abs(walletPOJO.getFlowMoney()) > oldBalance){
                    logger.error("支出金额大于余额");
                    return false ;
                }
                walletPOJO.setFlowMoney(-Math.abs(walletPOJO.getFlowMoney()));
            }
            Long newBalance = oldBalance + walletPOJO.getFlowMoney();
            walletPOJO.setBalance(newBalance);
            //4.插入记录
            walletMapper.insertWallet(walletPOJO);
        }
        return true;
    }

    @Override
    public Long queryBalanceByUserId(Long userId) {
        if(userId == null){
            logger.error("用户id为空");
        }
        return walletMapper.selectWalletBalanceByUserId(userId);
    }

    /**
     * 校验钱包入参
     *
     * @param walletPOJO 钱包对象
     * @return 是否通过校验
     */
    private boolean validate(WalletPOJO walletPOJO) {

        if (walletPOJO == null) {
            return false;
        } else {
            if (walletPOJO.getUserId() == null || walletPOJO.getOrderId() == null
                    || walletPOJO.getType() == null || walletPOJO.getFlowMoney() == null) {
                return false;
            }
        }
        return true;
    }
}
