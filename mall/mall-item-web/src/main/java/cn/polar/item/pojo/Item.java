package cn.polar.item.pojo;

import cn.polar.pojo.TbItem;

/**
 * Description : TbItem包装类
 * Created By Polar on 2017/8/16
 */
public class Item extends TbItem {
    // 多张图片用逗号隔开
    public String[] getImages() {
        String images = this.getImage();
        if (images != null && !"".equals(images)) {
            String[] strings = images.split(",");
            return strings;
        }
        return null;
    }

    public Item() {
    }

    public Item(TbItem tbItem) {
        this.setBarcode(tbItem.getBarcode());
        this.setCid(tbItem.getCid());
        this.setCreated(tbItem.getCreated());
        this.setId(tbItem.getId());
        this.setImage(tbItem.getImage());
        this.setNum(tbItem.getNum());
        this.setPrice(tbItem.getPrice());
        this.setSellPoint(tbItem.getSellPoint());
        this.setStatus(tbItem.getStatus());
        this.setTitle(tbItem.getTitle());
        this.setUpdated(tbItem.getUpdated());
    }


}
