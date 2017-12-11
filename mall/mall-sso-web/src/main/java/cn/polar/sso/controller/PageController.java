package cn.polar.sso.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * Description : TODO
 * Created By Polar on 2017/8/17
 */
@Controller
public class PageController {
    @RequestMapping("/page/register")
    public String showRegister() {
        return "register";
    }
    @RequestMapping("/page/login")
    public String showLogin(String redirect, Model model) {

        model.addAttribute("redirect", redirect);
        return "login";
    }
}
