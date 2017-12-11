package cn.polar.cart.controller;

import cn.polar.cart.service.CartService;
import cn.polar.common.util.CookieUtils;
import cn.polar.common.util.JsonUtils;
import cn.polar.common.util.MallResult;
import cn.polar.pojo.TbItem;
import cn.polar.pojo.TbUser;
import cn.polar.service.ItemService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * Description : TODO
 * Created By Polar on 2017/8/18
 */
@Controller
public class CartController {
    @Autowired
    private ItemService itemService;

    @Value("${CART_EXPIRE}")
    private Integer CART_EXPIRE;
    @Autowired
    private CartService cartService;

    /**
     * 购物车项删除
     *
     * @param itemId
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/cart/delete/{itemId}")
    public String deleteCartItem(@PathVariable Long itemId, HttpServletRequest request,
                                 HttpServletResponse response) {
        TbUser user = isExistUser(request);
        if (null != user) {
            cartService.deleteCartItem(user.getId(), itemId);
            return "redirect:/cart/cart.html";
        }

        // 1、从url中取商品id
        // 2、从cookie中取购物车商品列表
        List<TbItem> cartList = getCartList(request);
        // 3、遍历列表找到对应的商品
        for (TbItem tbItem : cartList) {
            if (tbItem.getId() == itemId.longValue()) {
                // 4、删除商品。
                cartList.remove(tbItem);
                break;
            }
        }
        // 5、把商品列表写入cookie。
        CookieUtils.setCookie(request, response, "cart", JsonUtils.objectToJson(cartList), CART_EXPIRE, true);
        // 6、返回逻辑视图：在逻辑视图中做redirect跳转。
        return "redirect:/cart/cart.html";
    }

    /**
     * 购物车数量修改
     *
     * @param itemId
     * @param num
     * @param response
     * @param request
     * @return
     */
    @RequestMapping("/cart/update/num/{itemId}/{num}")
    @ResponseBody
    public MallResult updateCart(@PathVariable Long itemId, @PathVariable Integer num,
                                 HttpServletResponse response, HttpServletRequest request) {
        TbUser user = isExistUser(request);
        if (null != user) {
            // 存在用户 修改redis中的购物车数据
            MallResult result = cartService.updateCartNum(user.getId(), itemId, num);
            return result;

        }

        // 不存在用户 修改cookie中购物车的信息
        List<TbItem> cartList = getCartList(request);
        for (TbItem item : cartList
                ) {
            if (item.getId() == itemId.longValue()) {
                item.setNum(num);
            }

        }
        CookieUtils.setCookie(request, response, "cart",
                JsonUtils.objectToJson(cartList), CART_EXPIRE, true);

        return MallResult.ok();
    }

    /**
     * 跳转到购物车页面
     *
     * @return
     */
    @RequestMapping("/cart/cart")
    public String showCart(HttpServletRequest request, HttpServletResponse response, Model model) {
        // cookie中的cartList
        List<TbItem> cartList = getCartList(request);
        // 判断用户是否登录
        TbUser user = isExistUser(request);
        if (null != user) {
            // 判断cookie中的购物车列表是否为空
            if (!cartList.isEmpty()) {
                //合并购物车
                cartService.mergeCart(user.getId(), cartList);
                //删除cookie中的购物车
                CookieUtils.setCookie(request, response, "cart", "");

            }
            //从服务端取购物车列表
            List<TbItem> serviceCartList = cartService.getCartList(user.getId());
            model.addAttribute("cartList", serviceCartList);
            return "cart";
        }

        // 用户未登录 直接显示cookie中的cartList
        model.addAttribute("cartList", cartList);

        return "cart";
    }


    @RequestMapping("/cart/add/{itemId}")
    public String add2Cart(HttpServletRequest request, HttpServletResponse response,
                           @PathVariable Long itemId, Integer num) {
        // 判断用户是否登录
        TbUser user = isExistUser(request);
        if (null != user) {
            // 用户已登录 将购物项添加到缓存中
            Long userId = user.getId();
            cartService.add2Cart(userId, itemId, num);
            return "cartSuccess";

        }

        //用户没有登录 get cart from cookies
        List<TbItem> cartList = getCartList(request);

        // 1 判断商品是否在购物车中存在
        Boolean hasItem = false;
        for (TbItem item : cartList
                ) {
            // 存在 值得比较
            if (item.getId() == itemId.longValue()) {
                item.setNum(item.getNum() + num);
                // 包含此商品
                hasItem = true;
                break;
            }
        }

        // 2 如果不存在
        if (!hasItem) {
            // 查询商品信息，将商品放入到购物车中
            TbItem item = itemService.getItemById(itemId);
            if (item != null && StringUtils.isNotBlank(item.getImage())) {
                // 取出第一张照片
                String image = item.getImage().split(",")[0];
                item.setImage(image);
            }

            item.setNum(num);

            cartList.add(item);

        }

        // add cartList to cookies
        CookieUtils.setCookie(request, response, "cart",
                JsonUtils.objectToJson(cartList), CART_EXPIRE, true);


        return "cartSuccess";
    }

    /**
     * get cart list from cookies
     *
     * @param request
     * @return
     */
    public List<TbItem> getCartList(HttpServletRequest request) {
        // get cart list
        String json = CookieUtils.getCookieValue(request, "cart", true);

        if (StringUtils.isNotBlank(json)) {
            // transfer json to object
            List<TbItem> list = JsonUtils.jsonToList(json, TbItem.class);

            return list;
        }

        // if not exist
        return new ArrayList<>();
    }

    /**
     * 是否存在登录的用户
     *
     * @param request
     * @return
     */
    public TbUser isExistUser(HttpServletRequest request) {
        Object object = request.getAttribute("user");
        if (null != object) {
            return (TbUser) object;
        }

        return null;
    }
}
