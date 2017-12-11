package cn.polar.controller;

import cn.polar.common.pojo.EasyUITreeNode;
import cn.polar.common.util.MallResult;
import cn.polar.content.service.ContentCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class CategoryController {
    @Autowired
    private ContentCategoryService contentCategoryService;

    /**
     * 展示内容分类管理树
     * @param parentId
     * @return
     */
    @RequestMapping("/content/category/list")
    @ResponseBody
    public List<EasyUITreeNode> getCategoryList(
             @RequestParam(name = "id", defaultValue = "0") Long parentId) {
        List<EasyUITreeNode> treeNodes = contentCategoryService.getCategoryList(parentId);

        return treeNodes;
    }

    /**
     * 增加分类
     * @param parentId
     * @param name
     * @return
     */
    @RequestMapping("/content/category/create")
    @ResponseBody
    public MallResult createCategory(Long parentId, String name) {
        MallResult result = contentCategoryService.createCategory(parentId, name);
        return result;
    }

    /**
     * 修改分类信息
     * @param id
     * @param name
     * @return
     */
    @RequestMapping("/content/category/update")
    @ResponseBody
    public MallResult updateCategoryById(Long id, String name) {
        MallResult result = contentCategoryService.updateCategoryById(id, name);
        return result;
    }

    @RequestMapping("/content/category/delete")
    @ResponseBody
    public MallResult deleteCategoryById(Long id) {
        MallResult result = contentCategoryService.deleteCategoryById(id);
        return result;
    }
}
