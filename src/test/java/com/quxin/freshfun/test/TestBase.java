package com.quxin.freshfun.test;

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


    public static void main(String[] args) throws ParseException {
        String a ="a";
        String b = new String("b");
        System.out.println(a);
        System.out.println(b);
    }
}
