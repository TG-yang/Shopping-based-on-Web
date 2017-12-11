package cn.polar.controller;

import cn.polar.common.pojo.EasyUITreeNode;
import cn.polar.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class ItemCatController {
    @Autowired
    private ItemCatService itemCatService;

    /**
     * 获取商品分类信息
     * @param parentId 分类信息父结点
     * @return 分类信息列表
     */
    @RequestMapping("/item/cat/list")
    @ResponseBody
    public List<EasyUITreeNode> getItemCatList(@RequestParam(value = "id", defaultValue = "0") Long parentId) {
        return itemCatService.getCatList(parentId);
    }

}
