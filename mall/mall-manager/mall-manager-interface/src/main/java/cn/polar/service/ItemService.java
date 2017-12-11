package cn.polar.service;

import cn.polar.common.pojo.EasyUIDataGridResult;
import cn.polar.common.util.MallResult;
import cn.polar.pojo.TbItem;
import cn.polar.pojo.TbItemDesc;

public interface ItemService {
    /**
     * 通过商品id获取商品信息
     * @param itemId
     * @return
     */
    TbItem getItemById(Long itemId);

    /**
     * 分页查询商品信息
     * @param page 当前页
     * @param rows 每页记录数
     * @return
     */
    EasyUIDataGridResult<TbItem> getItemList(Integer page, Integer rows);

    /**
     * 添加新商品
     * @param tbItem
     * @param desc
     * @return
     */

    MallResult saveItem(TbItem tbItem, String desc);

    /**
     * 通过商品id查询商品描述
     * @param id
     * @return
     */
    MallResult getTbItemDescById(Long id);

    /**
     * 更新商品
     * @param tbitem
     * @param desc
     * @return
     */
    MallResult updateItem(TbItem tbitem, String desc);

    /**
     * 删除商品
     * @param ids
     * @return
     */
    MallResult deleteByIds(Long[] ids);

    /**
     * 下架商品
     * @param ids
     * @return
     */
    MallResult instockItemById(Long[] ids);

    /**
     * 上架商品
     * @param ids
     * @return
     */
    MallResult reshelfItemById(Long[] ids);

    TbItemDesc getItemDescById(Long itemId);
}
