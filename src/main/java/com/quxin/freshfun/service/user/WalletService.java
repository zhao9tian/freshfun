package com.quxin.freshfun.service.user;

import com.quxin.freshfun.model.user.WalletPOJO;

/**
 * 钱包service
 * Created by qucheng on 17/1/18.
 */
public interface WalletService {

    /**
     * 新增一条钱包记录
     * @param walletPOJO 钱包实体
     * @return 是否新增成功
     */
    Boolean addWallet(WalletPOJO walletPOJO);


    /**
     * 根据用户id查询用户余额
     * @param userId 用户id
     * @return 是否新增成功
     */
    Long queryBalanceByUserId(Long userId);

}
