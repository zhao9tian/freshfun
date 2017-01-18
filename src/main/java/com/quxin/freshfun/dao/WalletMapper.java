package com.quxin.freshfun.dao;

import com.quxin.freshfun.model.user.WalletPOJO;

/**
 * 钱包dao层
 * Created by qucheng on 17/1/18.
 */
public interface WalletMapper {

    /**
     * 查询钱包余额
     * @param userId 用户id
     * @return 钱包余额
     */
    Long selectWalletBalanceByUserId(Long userId);

    /**
     * 记录钱包金额
     * @param walletPOJO 钱包实体
     * @return 插入记录数
     */
    Integer insertWallet(WalletPOJO walletPOJO);
}
