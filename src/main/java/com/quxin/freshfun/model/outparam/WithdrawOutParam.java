package com.quxin.freshfun.model.outparam;

/**
 * Created by qucheng on 2016/9/30.
 */
public class WithdrawOutParam {
    private String withdrawName ;
    private String price ;
    private String time ;

    public String getWithdrawName() {
        return withdrawName;
    }

    public void setWithdrawName(String withdrawName) {
        this.withdrawName = withdrawName;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @Override
    public String toString() {
        return "WithdrawOutParam{" +
                "withdrawName='" + withdrawName + '\'' +
                ", price='" + price + '\'' +
                ", time='" + time + '\'' +
                '}';
    }
}
