package com.quxin.freshfun.test;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

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

}
