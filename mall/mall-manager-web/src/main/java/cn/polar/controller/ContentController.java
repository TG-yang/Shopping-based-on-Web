package cn.polar.controller;

import cn.polar.common.pojo.EasyUIDataGridResult;
import cn.polar.common.util.MallResult;
import cn.polar.content.service.ContentService;
import cn.polar.pojo.TbContent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ContentController {
    @Autowired
    private ContentService contentService;

    /**
     * 添加内容
     * @param content
     * @return
     */
    @RequestMapping("/content/save")
    @ResponseBody
    public MallResult addContent(TbContent content) {
        MallResult result = contentService.addContent(content);
        return result;
    }

    /**
     * 分页查询内容列表
     * @param categoryId
     * @param page
     * @param rows
     * @return
     */
    @RequestMapping("/content/query/list")
    @ResponseBody
    public EasyUIDataGridResult<TbContent> getContentList(
            Long categoryId, Integer page, Integer rows) {
        EasyUIDataGridResult<TbContent> result = contentService.getContentList(categoryId, page, rows);
        return result;

    }
}
