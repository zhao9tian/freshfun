package com.quxin.freshfun.utils;

public class MoneyFormat {
	public static String priceFormatString(Integer price){
        String ratea = String.format("%.2f", price/100d);
        return ratea;
	}
}
