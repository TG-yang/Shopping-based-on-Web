package cn.polar.cart.service.impl;

import cn.polar.cart.service.CartService;
import cn.polar.common.jedis.JedisClient;
import cn.polar.common.util.JsonUtils;
import cn.polar.common.util.MallResult;
import cn.polar.mapper.TbItemMapper;
import cn.polar.pojo.TbItem;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Description : 购物车服务层
 * Created By Polar on 2017/8/18
 */
@Service
public class CartServiceImpl implements CartService {
    @Autowired
    private JedisClient jedisClient;

    @Value("${REDIS_CART_PRE}")
    private String REDIS_CART_PRE;
    @Autowired
    private TbItemMapper itemMapper;

    @Override
    public MallResult add2Cart(Long userId, Long itemId, Integer num) {
        // 查看redis中是否存在该商品
        Boolean isExist = jedisClient.hexists(REDIS_CART_PRE + ":" + userId, itemId + "");
        if (isExist) {
            // 存在该商品
            String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
            TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
            tbItem.setNum(tbItem.getNum() + num);

            // 回写redis
            jedisClient.hset(REDIS_CART_PRE + ":" + userId,
                    itemId + "", JsonUtils.objectToJson(tbItem));
            return MallResult.ok();
        }

        // redis中不存在该商品
        // 查询该商品
        TbItem tbItem = itemMapper.selectByPrimaryKey(itemId);

        // set number of the item
        tbItem.setNum(num);
        // 修改图片信息
        String image = tbItem.getImage();
        if (StringUtils.isNotBlank(image)) {
            tbItem.setImage(image.split(",")[0]);

        }
        // 将该商品放入到redis购物车缓存中
        jedisClient.hset(REDIS_CART_PRE + ":" + userId,
                itemId + "", JsonUtils.objectToJson(tbItem));

        return MallResult.ok();
    }

    @Override
    public MallResult mergeCart(long userId, List<TbItem> itemList) {
        //遍历商品列表
        for (TbItem tbItem : itemList) {
            add2Cart(userId, tbItem.getId(), tbItem.getNum());
        }
        return MallResult.ok();
    }

    @Override
    public List<TbItem> getCartList(long userId) {

        //从redis中根据用户id查询商品列表
        List<String> list = jedisClient.hvals(REDIS_CART_PRE + ":" + userId);
        List<TbItem> tbItemList = new ArrayList<>();
        //把json列表转换成TbItem列表
        for (String json : list
                ) {
            TbItem item = JsonUtils.jsonToPojo(json, TbItem.class);
            tbItemList.add(item);

        }
        return tbItemList;
    }

    @Override
    public MallResult updateCartNum(long userId, long itemId, int num) {
        //从hash中取商品信息
        String json = jedisClient.hget(REDIS_CART_PRE + ":" + userId, itemId + "");
        if (StringUtils.isNotBlank(json)) {
            //转换成java对象
            TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
            //更新数量
            tbItem.setNum(num);
            //写入hash
            jedisClient.hset(REDIS_CART_PRE + ":" + userId, itemId + "", JsonUtils.objectToJson(tbItem));
        }
        return MallResult.ok();
    }

    @Override
    public MallResult deleteCartItem(long userId, long itemId) {
        jedisClient.hdel(REDIS_CART_PRE + ":" + userId, itemId + "");

        return MallResult.ok();
    }

    @Override
    public MallResult clearCartItem(Long userId) {
        //删除购物车信息
        jedisClient.del(REDIS_CART_PRE + ":" + userId);
        return MallResult.ok();



    }
}
