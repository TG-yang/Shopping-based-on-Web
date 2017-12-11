package cn.polar.publish;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Description : TODO
 * Created By Polar on 2017/8/17
 */
public class SsoServicePublish {
    @Test
    public void ssoService() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:/spring/*");
        System.err.println("服务器已启动");
        System.in.read();



    }

}
