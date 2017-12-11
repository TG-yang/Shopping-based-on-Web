package cn.polar.searchTest;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class SearchServicePublish {
    @Test
    public void searchService() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:/spring/applicationContext-*.xml");
        System.err.println("服务已启动");
        System.in.read();
        System.out.println("服务已关闭");
    }
}
