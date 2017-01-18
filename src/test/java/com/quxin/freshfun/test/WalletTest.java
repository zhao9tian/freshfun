package com.quxin.freshfun.test;


import com.quxin.freshfun.model.user.WalletPOJO;
import com.quxin.freshfun.service.user.WalletService;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class WalletTest extends TestBase{


    private WalletService walletService;

    
    @Before
    public void setUp() throws Exception {
        walletService = getContext().getBean("walletService", WalletService.class);
    }

    
    @After
    public void tearDown() throws Exception {
        getContext().close();
    }

    @Test
    public void test() {
        WalletPOJO walletPOJO = new WalletPOJO();
        walletPOJO.setUserId(556677L);
        walletPOJO.setOrderId(17898932L);
        walletPOJO.setType(0);
        walletPOJO.setFlowMoney(200L);
        System.out.println(walletService.addWallet(walletPOJO));
    }

    @Test
    public void query() {
        System.out.println(walletService.queryBalanceByUserId(556677L));
    }

}

