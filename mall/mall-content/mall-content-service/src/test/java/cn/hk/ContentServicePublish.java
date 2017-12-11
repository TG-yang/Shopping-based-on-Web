package cn.hk;

import cn.polar.common.jedis.JedisClient;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.JedisPool;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class ContentServicePublish {
    @Test
    public void contentService() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext("classpath:/spring/applicationContext-*.xml");
        System.err.println("服务已启动");
        System.in.read();
        System.out.println("服务已关闭");
    }

    /**
     * 测试单机版Redis
     * @throws Exception
     */
    @Test
    public void testClientRedis() throws Exception {
        //创建一个Jedis对象。需要指定服务端的ip及端口。
        Jedis  jedis = new Jedis("172.16.81.129", 6379);
        jedis.set("key3", "luxury");
        System.out.println(jedis.get("key1"));
        Set<String> keys = jedis.keys("*");

        System.out.println(keys.toString());
        //关闭Jedis
        jedis.close();
    }

    /**
     * 测试单机连接池版
     * @throws Exception
     */
    @Test
    public void testClientPoolRedist() throws Exception {
        JedisPool jedisPool = new JedisPool();
        Jedis jedis = jedisPool.getResource();
        jedis.lpush("list", "a", "b", "c", "d");
        jedis.rpush("list", "1", "2", "3", "4");
        List<String> list = jedis.lrange("list", 0, -1);
        System.out.println(list.toString());
        jedis.close();
        jedisPool.close();


    }
}
