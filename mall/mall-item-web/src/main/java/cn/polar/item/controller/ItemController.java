package cn.polar.item.controller;

import cn.polar.item.pojo.Item;
import cn.polar.pojo.TbItem;
import cn.polar.pojo.TbItemDesc;
import cn.polar.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Description : TODO
 * Created By Polar on 2017/8/16
 */

@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;

    @RequestMapping("/item/{itemId}")
    public String showItemInfo(@PathVariable Long itemId, Model model) {
        TbItem tbItem = itemService.getItemById(itemId);
        TbItemDesc tbItemDesc = itemService.getItemDescById(itemId);
        // 将TbItem转成Item对象，取图
        Item item = new Item(tbItem);

        model.addAttribute("item", item);
        model.addAttribute("itemDesc", tbItemDesc);
        return "item";
    }
}
