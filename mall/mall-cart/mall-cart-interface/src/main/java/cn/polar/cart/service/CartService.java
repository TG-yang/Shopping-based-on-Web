package cn.polar.cart.service;

import cn.polar.common.util.MallResult;
import cn.polar.pojo.TbItem;

import java.util.List;

/**
 * Description : TODO
 * Created By Polar on 2017/8/18
 */
public interface CartService {
    /**
     * 添加购物项到缓存中
     * @param userId
     * @param itemId
     * @param num
     * @return
     */
    MallResult add2Cart(Long userId, Long itemId, Integer num);

    /**
     * 合并购物项
     * @param userId
     * @param itemList 存在于cookie中的购物列表
     * @return
     */
    MallResult mergeCart(long userId, List<TbItem> itemList);

    /**
     * 获取购物列表
     * @param userId
     * @return
     */
    List<TbItem> getCartList(long userId);

    /**
     * 更新购物车内容（数量）
     * @param userId
     * @param itemId
     * @param num
     * @return
     */
    MallResult updateCartNum(long userId, long itemId, int num);

    /**
     * 删除购物项
     * @param userId
     * @param itemId
     * @return
     */
    MallResult deleteCartItem(long userId, long itemId);

    MallResult clearCartItem(Long id);
}
