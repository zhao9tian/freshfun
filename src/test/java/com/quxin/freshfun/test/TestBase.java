package com.quxin.freshfun.test;

import com.quxin.freshfun.model.pojo.goods.GoodsBasePOJO;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.text.ParseException;

public class TestBase {
    @SuppressWarnings("resource")
    private static AbstractApplicationContext CONTEXT;

    @BeforeClass
    public static void beforeClass() {
        CONTEXT = new ClassPathXmlApplicationContext("classpath:/applicationContext.xml");
    }

    protected static AbstractApplicationContext getContext() {
        return CONTEXT;
    }

    @AfterClass
    public static void afterClass() {
        CONTEXT.close();
    }

    private static  String a ;

    public static void main(String[] args) throws ParseException {
//        a ="aa";
        String aa = null ;
        Integer bb = 0;
//        final String vv ;
//        String s = aa + bb;
        System.out.println(bb);
//        String a = "abc";
//        System.out.println(1+2+a);
//        Integer
//        String b = "a" +a;
//        b.length();
//        System.out.println(b == "aabc".intern());

    }
}
