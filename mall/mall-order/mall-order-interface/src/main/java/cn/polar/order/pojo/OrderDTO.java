package cn.polar.order.pojo;

import cn.polar.pojo.TbOrder;
import cn.polar.pojo.TbOrderItem;
import cn.polar.pojo.TbOrderShipping;

import java.io.Serializable;
import java.util.List;

/**
 * Description : 接收订单信息表
 * Created By Polar on 2017/8/19
 */
public class OrderDTO extends TbOrder implements Serializable {

    private List<TbOrderItem> orderItems;
    private TbOrderShipping orderShipping;

    public List<TbOrderItem> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(List<TbOrderItem> orderItems) {
        this.orderItems = orderItems;
    }

    public TbOrderShipping getOrderShipping() {
        return orderShipping;
    }

    public void setOrderShipping(TbOrderShipping orderShipping) {
        this.orderShipping = orderShipping;
    }
}
