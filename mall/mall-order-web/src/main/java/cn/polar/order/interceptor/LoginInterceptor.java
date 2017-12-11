package cn.polar.order.interceptor;

import cn.polar.cart.service.CartService;
import cn.polar.common.jedis.JedisClient;
import cn.polar.common.util.CookieUtils;
import cn.polar.common.util.JsonUtils;
import cn.polar.common.util.MallResult;
import cn.polar.pojo.TbItem;
import cn.polar.pojo.TbUser;
import cn.polar.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Description : TODO
 * Created By Polar on 2017/8/19
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Autowired
    private UserService userService;

    @Value("${SESSION}")
    private String SESSION;

    @Value("${SSO_URL}")
    private String SSO_URL;


    @Autowired
    private CartService cartService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object o) throws Exception {
        // 判断用户是否登录
        String token = CookieUtils.getCookieValue(request, "token");
        if (StringUtils.isBlank(token)) {
            response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
            return false;
        }
        // query user from redis
        MallResult result = userService.getUserByToken(token);
        if (result.getStatus() != 200) {
            // 无用户登录页面
            response.sendRedirect(SSO_URL + "/page/login?redirect=" + request.getRequestURL());
            return false;
        }

        TbUser user = (TbUser) result.getData();
        request.setAttribute("user", user);

        //合并购物车
        String jsonCart = CookieUtils.getCookieValue(request, "cart", true);
        if (StringUtils.isNotBlank(jsonCart)) {
            cartService.mergeCart(user.getId(), JsonUtils.jsonToList(jsonCart, TbItem.class));
            //删除cookie中的购物车数据
            CookieUtils.setCookie(request, response, "cart", "");

        }


        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o, Exception e) throws Exception {

    }
}
