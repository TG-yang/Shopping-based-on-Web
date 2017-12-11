package cn.polar.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class PageController {
    /**
     * 显示首页
     * @return
     */
    @RequestMapping("/")
    public String showIndex() {
        return "index";
    }

    /**
     * 显示相应地page页面
     * @param page
     * @return
     */
    @RequestMapping("/{page}")
    public String showPage(@PathVariable String page) {
        return page;
    }

    /**
     * 显示商品编辑页面
     * @return
     */
    @RequestMapping("/rest/page/item-edit")
    public String showEditPage() {
        return "item-edit";
    }
}
