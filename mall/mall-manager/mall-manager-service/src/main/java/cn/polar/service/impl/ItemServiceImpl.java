package cn.polar.service.impl;

import cn.polar.common.jedis.JedisClientPool;
import cn.polar.common.pojo.EasyUIDataGridResult;
import cn.polar.common.util.IDUtils;
import cn.polar.common.util.JsonUtils;
import cn.polar.common.util.MallResult;
import cn.polar.mapper.TbItemDescMapper;
import cn.polar.mapper.TbItemMapper;
import cn.polar.pojo.TbItem;
import cn.polar.pojo.TbItemDesc;
import cn.polar.pojo.TbItemExample;
import cn.polar.service.ItemService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.jms.*;
import java.util.Date;
import java.util.List;

/**
 * 商品管理Service
 */
@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private TbItemMapper tbItemMapper;
    @Autowired
    private TbItemDescMapper tbItemDescMapper;

    @Autowired
    private JmsTemplate jmsTemplate;
    @Resource
    private Destination topicDestination;
    @Autowired
    private JedisClientPool jedisClientPool;

    @Value("${REDIS_ITEM_PRE}")
    private String REDIS_ITEM_PRE;

    @Value("${ITEM_CACHE_EXPIRE}")
    private Integer ITEM_CACHE_EXPIRE;

    /**
     * 查询商品信息
     * @param itemId
     * @return
     */
    @Override
    public TbItem getItemById(Long itemId) {
        // 查询缓存
        try {
            String json = jedisClientPool.get(REDIS_ITEM_PRE + ":" + itemId + ":BASE");
            if (StringUtils.isNotBlank(json)) {
                TbItem tbItem = JsonUtils.jsonToPojo(json, TbItem.class);
                return tbItem;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        criteria.andIdEqualTo(itemId);
        List<TbItem> tbItems = tbItemMapper.selectByExample(example);
        if (tbItems != null && tbItems.size() > 0) {
            TbItem tbItem = tbItems.get(0);
            try {
                // 添加到缓存中
                jedisClientPool.set(REDIS_ITEM_PRE + ":" + itemId + ":BASE", JsonUtils.objectToJson(tbItem));
                // 设置过期时间，减少数据库压力，同时不会导致缓存中存放过多数据
                jedisClientPool.expire(REDIS_ITEM_PRE + ":" + itemId + ":BASE", ITEM_CACHE_EXPIRE);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return tbItem;
        }
        return null;
    }

    /**
     * 查询商品描述信息
     * @param itemId
     * @return
     */
    @Override
    public TbItemDesc getItemDescById(Long itemId) {
        // 查询缓存
        try {
            String json = jedisClientPool.get(REDIS_ITEM_PRE + ":" + itemId + ":DESC");
            if (StringUtils.isNotBlank(json)) {
                TbItemDesc tbItemDesc = JsonUtils.jsonToPojo(json, TbItemDesc.class);
                return tbItemDesc;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(itemId);

        try {
            String json = JsonUtils.objectToJson(tbItemDesc);
            // 添加到缓存中
            jedisClientPool.set(REDIS_ITEM_PRE + ":" + itemId + ":DESC", json);
            // 设置过期时间，减少数据库压力，同时不会导致缓存中存放过多数据
            jedisClientPool.expire(REDIS_ITEM_PRE + ":" + itemId + ":DESC", ITEM_CACHE_EXPIRE);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tbItemDesc;
    }

    @Override
    public EasyUIDataGridResult<TbItem> getItemList(Integer page, Integer rows) {
        // 设置分页信息
        PageHelper.startPage(page, rows);
        // 执行查询
        TbItemExample example = new TbItemExample();
        TbItemExample.Criteria criteria = example.createCriteria();
        List<TbItem> tbItems = tbItemMapper.selectByExample(example);

        //创建一个返回值对象
        EasyUIDataGridResult<TbItem> result = new EasyUIDataGridResult<>();
        result.setRows(tbItems);

        //获取总记录数
        PageInfo<TbItem> pageInfo = new PageInfo<TbItem>(tbItems);
        result.setTotal(pageInfo.getTotal());
        return result;
    }

    /**
     * 添加商品
     * @param tbItem
     * @param desc
     * @return
     */
    @Override
    public MallResult saveItem(TbItem tbItem, String desc) {
        // 添加商品id
        final long itemId = IDUtils.genItemId();
        tbItem.setId(itemId);
        // 添加商品状态 1-正常 2-下架 3-删除
        tbItem.setStatus((byte) 1);
        tbItem.setCreated(new Date());
        tbItem.setUpdated(new Date());

        // 商品描述对象
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(itemId);
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setCreated(new Date());
        tbItemDesc.setUpdated(new Date());

        tbItemMapper.insert(tbItem);

        tbItemDescMapper.insert(tbItemDesc);

        // 发送一个商品添加信息
        jmsTemplate.send(topicDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(String.valueOf(itemId));
                return textMessage;
            }
        });
        return MallResult.ok();
    }

    @Override
    public MallResult getTbItemDescById(Long id) {
        TbItemDesc tbItemDesc = tbItemDescMapper.selectByPrimaryKey(id);
        // 回写的数据位MallResult 将商品描述信息对象放入
        return MallResult.ok(tbItemDesc);

    }

    @Override
    public MallResult updateItem(final TbItem tbitem, String desc) {
        tbitem.setUpdated(new Date());
        // 商品描述对象
        TbItemDesc tbItemDesc = new TbItemDesc();
        tbItemDesc.setItemId(tbitem.getId());
        tbItemDesc.setItemDesc(desc);
        tbItemDesc.setUpdated(new Date());

        tbItemMapper.updateByPrimaryKeySelective(tbitem);
        tbItemDescMapper.updateByPrimaryKeySelective(tbItemDesc);
        // 删除缓存
        deleteCacheById(tbitem.getId());

        // 发送一个商品同步索引库
        jmsTemplate.send(topicDestination, new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                TextMessage textMessage = session.createTextMessage(String.valueOf(tbitem.getId()));
                return textMessage;
            }
        });

        return MallResult.ok();
    }

    /**
     * 删除多个商品
     * @param ids
     * @return
     */
    @Override
    public MallResult deleteByIds(Long[] ids) {
        for (Long id : ids
                ) {
            // 先删除商品描述信息 再删除商品信息
//            tbItemDescMapper.deleteByPrimaryKey(id);
//            tbItemMapper.deleteByPrimaryKey(id);
            // 非真删除 数据无价
            TbItem tbItem = new TbItem();
            tbItem.setId(id);
            tbItem.setStatus((byte) 3);
            // 3表示商品删除ß
            tbItemMapper.updateByPrimaryKeySelective(tbItem);
            // 删除缓存
            deleteCacheById(id);

        }
        return MallResult.ok();
    }

    /**
     * 下架商品
     * @param ids
     * @return
     */
    @Override
    public MallResult instockItemById(Long[] ids) {
        for (Long id : ids
                ) {

            TbItem tbItem = new TbItem();
            tbItem.setId(id);
            tbItem.setStatus((byte) 2);
            // 1表示商品上架
            tbItemMapper.updateByPrimaryKeySelective(tbItem);

        }
        return MallResult.ok();
    }

    /**
     * 上架商品
     * @param ids
     * @return
     */
    @Override
    public MallResult reshelfItemById(Long[] ids) {
        for (Long id : ids
                ) {

            TbItem tbItem = new TbItem();
            tbItem.setId(id);
            tbItem.setStatus((byte) 1);
            // 1表示商品下架
            tbItemMapper.updateByPrimaryKeySelective(tbItem);

        }
        return MallResult.ok();
    }

    /**
     * 删除缓存(包含商品信息和商品描述信息的缓存)
     * @param id
     */
    public void deleteCacheById(Long id) {
        jedisClientPool.del(REDIS_ITEM_PRE + ":" + id + ":BASE");
        jedisClientPool.del(REDIS_ITEM_PRE + ":" + id + ":DESC");
    }


}
