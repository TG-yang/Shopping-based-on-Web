package cn.polar.portal.controller;

import cn.polar.content.service.ContentService;
import cn.polar.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class IndexController {

    @Value("${CONTENT_LUNBO_ID}")
    private Long CONTENT_LUNBO_ID;
    @Autowired
    private ContentService contentService;
    @RequestMapping("/index")
    public String showIndex(Model model) {
        // 查询页面内容
        List<TbContent> ad1List = contentService.getContentListByCid(CONTENT_LUNBO_ID);
        // 将结果传递给页面
        model.addAttribute("ad1List" , ad1List);
        return "index";

    }


}
