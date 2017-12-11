package cn.polar.cart.interceptor;

import cn.polar.common.util.CookieUtils;
import cn.polar.common.util.MallResult;
import cn.polar.pojo.TbUser;
import cn.polar.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Description : TODO
 * Created By Polar on 2017/8/18
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o) throws Exception {
        // 执行handler方法前 执行此方法

        // 判断用户是否登录
        String token = CookieUtils.getCookieValue(httpServletRequest, "token");
        MallResult result = userService.getUserByToken(token);
        if(result.getStatus() != 200) {
            // 无用户登录
            return true;
        }
        // 已经有用户登录 将用户信息放入到request域中
        TbUser user = (TbUser) result.getData();
        httpServletRequest.setAttribute("user", user);

        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {
        // 执行handler方法之后 返回ModelAndView前 执行此方法
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {
        // 返回ModelAndView执行此方法
    }
}
