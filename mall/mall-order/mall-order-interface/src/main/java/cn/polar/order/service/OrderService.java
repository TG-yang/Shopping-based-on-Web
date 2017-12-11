package cn.polar.order.service;

import cn.polar.common.util.MallResult;
import cn.polar.order.pojo.OrderDTO;

/**
 * Description : TODO
 * Created By Polar on 2017/8/19
 */
public interface OrderService {
    /**
     * 添加订单
     * @param orderDTO
     * @return
     */
    MallResult createOrder(OrderDTO orderDTO);
}
