package cn.polar;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Description : TODO
 * Created By Polar on 2017/8/19
 */

public class OrderServicePublish {
    @Test
    public void orderService() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:/spring/*");
        System.err.println("服务器已启动");
        System.in.read();

    }


}
