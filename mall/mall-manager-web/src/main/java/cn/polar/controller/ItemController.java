package cn.polar.controller;

import cn.polar.common.pojo.EasyUIDataGridResult;
import cn.polar.common.util.MallResult;
import cn.polar.pojo.TbItem;
import cn.polar.service.ItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class ItemController {
    @Autowired
    private ItemService itemService;

    /**
     * 获取商品id
     * @param id
     * @return
     */
    @RequestMapping("/item/{id}")
    @ResponseBody
    public TbItem getItemById(@PathVariable Long id) {

        TbItem tbItem = itemService.getItemById(id);
        return tbItem;
    }

    @RequestMapping("/rest/item/query/item/desc/{id}")
    @ResponseBody public MallResult getTbItemDescById(@PathVariable Long id) {
        return itemService.getTbItemDescById(id);
    }

    /**
     * 分页查询商品列表
     * @param page 当前页
     * @param rows 每页的显示条数
     * @return 分页bean
     */
    @RequestMapping("/item/list")
    @ResponseBody
    public EasyUIDataGridResult<TbItem> getItemList(Integer page, Integer rows) {
        EasyUIDataGridResult<TbItem> result = itemService.getItemList(page, rows);
        return result;
    }

    /**
     * 添加商品
     * @param tbItem
     * @param desc
     * @return
     */
    @RequestMapping("/item/save")
    @ResponseBody
    public MallResult saveItem(TbItem tbItem, String desc) {

        return itemService.saveItem(tbItem, desc);
    }

    /**
     * 更新商品
     * @param tbitem
     * @param desc
     * @return
     */

    @RequestMapping("/rest/item/update")
    @ResponseBody
    public MallResult updateItemById(TbItem tbitem, String desc) {
        return itemService.updateItem(tbitem, desc);
    }

    /**
     * 删除多个商品
     * @param ids 商品id数组
     * @return
     */
    @RequestMapping("/rest/item/delete")
    @ResponseBody
    public MallResult deleteByIds(Long[] ids) {
        return itemService.deleteByIds(ids);
    }

    /**
     * 下架
     * @param ids
     * @return
     */
    @RequestMapping("/rest/item/instock")
    @ResponseBody
    public MallResult instockItemById(Long[] ids) {
        return itemService.instockItemById(ids);
    }

    /**
     * 上架
     * @param ids
     * @return
     */
    @RequestMapping("/rest/item/reshelf")
    @ResponseBody
    public MallResult reshelfItemById(Long[] ids) {
        return itemService.reshelfItemById(ids);
    }





}
