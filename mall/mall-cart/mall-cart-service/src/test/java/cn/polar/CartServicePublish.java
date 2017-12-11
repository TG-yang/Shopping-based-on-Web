package cn.polar;

import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import java.util.Stack;

/**
 * Description : TODO
 * Created By Polar on 2017/8/18
 */
public class CartServicePublish {
    @Test
    public void cartService() throws Exception {
        ApplicationContext context = new ClassPathXmlApplicationContext(
                "classpath:/spring/*.xml");

        System.err.println("服务已启动");
        System.in.read();
    }

    @Test
    public void testInteger() throws Exception {
        Integer a = 129;
        Integer b = 129;
        System.out.println(a == b);


        Long c = -129L;
        Long d = -129L;
        System.out.println(c == d.longValue());
    }

    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<Character>();
        for (char c : s.toCharArray()) {
            if (c == '(')
                stack.push(')');
            else if (c == '{')
                stack.push('}');
            else if (c == '[')
                stack.push(']');
            else if (stack.isEmpty() || stack.pop() != c)
                return false;
        }
        return stack.isEmpty();
    }

    @Test
    public void testS() throws Exception {
        String s = "({[]}())";


        System.out.println(isValid(s));
    }

    @Test
    public void hash() throws Exception {
        byte a = 127;
        byte b = 127;
        b += a;
        System.out.println(b);


    }
}
