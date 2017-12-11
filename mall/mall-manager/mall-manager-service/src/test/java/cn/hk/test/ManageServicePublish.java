package cn.hk.test;

import cn.polar.mapper.TbItemMapper;
import cn.polar.pojo.TbItem;
import cn.polar.pojo.TbItemExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.List;

public class ManageServicePublish {
    @Test
    public void managementService() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:/spring/applicationContext-*.xml");
        System.err.println("服务已启动");
        System.in.read();
        System.out.println("服务已关闭");
    }

    @Test
    public void f1() {
        ApplicationContext context = new ClassPathXmlApplicationContext("/spring/applicationContext-dao.xml");
        TbItemMapper tbItemMapper = context.getBean(TbItemMapper.class);
        PageHelper.startPage(1, 10);
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andIdBetween(927779L, 1000000L);
        List<TbItem> tbItems = tbItemMapper.selectByExample(example);

        PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(tbItems);

        System.out.println(pageInfo.getTotal());

    }


}
