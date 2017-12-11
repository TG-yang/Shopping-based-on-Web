package cn.polar.sso.controller;

import cn.polar.common.util.CookieUtils;
import cn.polar.common.util.JsonUtils;
import cn.polar.common.util.MallResult;
import cn.polar.pojo.TbUser;
import cn.polar.sso.service.UserService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Description : TODO
 * Created By Polar on 2017/8/17
 */
@Controller
public class UserController {
    @Autowired
    private UserService userService;

    /**
     * logout
     */

    @RequestMapping("/user/logout/{token}")
    public String logout(@PathVariable String token, HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && StringUtils.isNotBlank(token)) {

            for (Cookie cookie : cookies
                    ) {
                if ("token".equals(cookie.getName())) {
                    // 删除此cookie
                    cookie.setValue(null);
                    cookie.setMaxAge(0);
                    break;
                }
            }
        }

        // 删除缓存中的cookie
        userService.deleteUserByToken(token);

        return "redirect:http://localhost:8082/";
    }

    /**
     * register
     *
     * @param user
     * @return
     */
    @RequestMapping(value = "/user/register", method = RequestMethod.POST)
    @ResponseBody
    public MallResult register(TbUser user) {
        MallResult result = userService.addUser(user);
        return result;
    }


    /**
     * check register data
     *
     * @param param
     * @param type
     * @return
     */
    @RequestMapping("/user/check/{param}/{type}")
    @ResponseBody
    public MallResult checkData(@PathVariable String param, @PathVariable Integer type) {
        MallResult result = userService.checkData(param, type);
        return result;

    }

    /**
     * login
     *
     * @param username
     * @param password
     * @param request
     * @param response
     * @return
     */
    @RequestMapping(value = "/user/login", method = RequestMethod.POST)
    @ResponseBody
    public MallResult login(String username, String password,
                            HttpServletRequest request, HttpServletResponse response) {
        MallResult result = userService.login(username, password);

        // 从返回结果中取token，写入cookie。Cookie要跨域。
        String token = (String) result.getData();

        // set cookies
        CookieUtils.setCookie(request, response, "token", token);
        return result;
    }

    /**
     * 通过token获取登录的用户信息
     *
     * @param token
     * @param callback jsonp ：执行 JSONP 请求时需要制定的回调函数名称，默认值是“callback”。
     * @return
     */
    @RequestMapping(value = "/user/token/{token}", produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public String getUserByToken(@PathVariable String token, String callback) {


        MallResult result = userService.getUserByToken(token);
        // 判断是否是跨域请求数据

        if (StringUtils.isNotBlank(callback)) {

            return callback + "(" + JsonUtils.objectToJson(result) + ");";

            // spring 4.0 加入如下jsonp请求处理方式
/*            MappingJacksonValue mappingJacksonValue = new MappingJacksonValue(result);
            mappingJacksonValue.setJsonpFunction(callback);
            return JsonUtils.objectToJson(mappingJacksonValue);*/
        }


        return JsonUtils.objectToJson(result);
    }


}
