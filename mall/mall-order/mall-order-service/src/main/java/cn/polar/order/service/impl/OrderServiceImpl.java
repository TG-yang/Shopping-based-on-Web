package cn.polar.order.service.impl;

import cn.polar.common.jedis.JedisClient;
import cn.polar.common.util.MallResult;
import cn.polar.mapper.TbOrderItemMapper;
import cn.polar.mapper.TbOrderMapper;
import cn.polar.mapper.TbOrderShippingMapper;
import cn.polar.order.pojo.OrderDTO;
import cn.polar.order.service.OrderService;
import cn.polar.pojo.TbOrderItem;
import cn.polar.pojo.TbOrderShipping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Description : TODO
 * Created By Polar on 2017/8/19
 */
@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private TbOrderMapper orderMapper;
    @Autowired
    private TbOrderItemMapper orderItemMapper;
    @Autowired
    private TbOrderShippingMapper orderShippingMapper;
    @Autowired
    private JedisClient jedisClient;

    @Value("${ORDER_ID_GEN_KEY}")
    private String ORDER_ID_GEN_KEY;

    @Value("${ORDER_ID_START}")
    private String ORDER_ID_START;
    @Value("${ORDER_DETAIL_ID_GEN_KEY}")
    private String ORDER_DETAIL_ID_GEN_KEY;

    @Override
    public MallResult createOrder(OrderDTO orderDTO) {
        //生成订单号。使用redis的incr生成。
        if (!jedisClient.exists(ORDER_ID_GEN_KEY)) {
            jedisClient.set(ORDER_ID_GEN_KEY, ORDER_ID_START);
        }
        String orderId = jedisClient.incr(ORDER_ID_GEN_KEY).toString();
        //补全orderDTO的属性
        orderDTO.setOrderId(orderId);
        //1、未付款，2、已付款，3、未发货，4、已发货，5、交易成功，6、交易关闭
        orderDTO.setStatus(1);
        orderDTO.setCreateTime(new Date());
        orderDTO.setUpdateTime(new Date());
        //插入订单表
        orderMapper.insert(orderDTO);
        //向订单明细表插入数据。
        List<TbOrderItem> orderItems = orderDTO.getOrderItems();
        for (TbOrderItem tbOrderItem : orderItems) {
            //生成明细id
            String odId = jedisClient.incr(ORDER_DETAIL_ID_GEN_KEY).toString();
            //补全pojo的属性
            tbOrderItem.setId(odId);
            tbOrderItem.setOrderId(orderId);
            //向明细表插入数据
            orderItemMapper.insert(tbOrderItem);
        }
        //向订单物流表插入数据
        TbOrderShipping orderShipping = orderDTO.getOrderShipping();
        orderShipping.setOrderId(orderId);
        orderShipping.setCreated(new Date());
        orderShipping.setUpdated(new Date());
        orderShippingMapper.insert(orderShipping);
        //返回MallResult，包含订单号
        return MallResult.ok(orderId);
    }
}

