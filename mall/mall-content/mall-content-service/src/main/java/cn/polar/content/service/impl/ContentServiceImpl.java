package cn.polar.content.service.impl;

import cn.polar.common.jedis.JedisClientPool;
import cn.polar.common.pojo.EasyUIDataGridResult;
import cn.polar.common.util.JsonUtils;
import cn.polar.common.util.MallResult;
import cn.polar.content.service.ContentService;
import cn.polar.mapper.TbContentMapper;
import cn.polar.pojo.TbContent;
import cn.polar.pojo.TbContentExample;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ContentServiceImpl implements ContentService {
    @Autowired
    private TbContentMapper tbContentMapper;

    @Autowired
    private JedisClientPool jedisClientPool;

    @Value("${CONTENT_LIST}")
    private String CONTENT_LIST;

    /**
     * 添加内容信息
     * @param tbContent
     * @return
     */
    @Override
    public MallResult addContent(TbContent tbContent) {
        tbContent.setCreated(new Date());
        tbContent.setUpdated(new Date());
        tbContentMapper.insertSelective(tbContent);
        // 缓存同步
        try {
            jedisClientPool.hdel(CONTENT_LIST, tbContent.getCategoryId().toString());
        } catch (Exception e) {
            e.printStackTrace();
        }

        return MallResult.ok();
    }

    /**
     * 获取指定分类下的分页内容信息
     *
     * @param categoryId
     * @param page
     * @param rows
     * @return
     */
    @Override
    public EasyUIDataGridResult<TbContent> getContentList(Long categoryId, Integer page, Integer rows) {
        PageHelper.startPage(page, rows);

        // 执行查询
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);
        List<TbContent> tbContents = tbContentMapper.selectByExample(example);

        //创建一个返回值对象
        EasyUIDataGridResult<TbContent> result = new EasyUIDataGridResult<>();
        result.setRows(tbContents);

        //获取总记录数
        PageInfo<TbContent> pageInfo = new PageInfo<>(tbContents);
        result.setTotal(pageInfo.getTotal());
        return result;
    }

    /**
     * 获取指定分类的内容列表
     *
     * @param categoryId
     * @return
     */
    @Override
    public List<TbContent> getContentListByCid(Long categoryId) {

        // 缓存中即使发生异常也会正常从数据库中查询结果
        try {
            // 先查询缓存
            String json = jedisClientPool.hget(CONTENT_LIST, categoryId.toString());
            // 判断是否为空
            if (StringUtils.isNotBlank(json)) {
                // 把json转为为对象
                List<TbContent> tbContents = JsonUtils.jsonToList(json, TbContent.class);
                return tbContents;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        TbContentExample example = new TbContentExample();
        TbContentExample.Criteria criteria = example.createCriteria();
        criteria.andCategoryIdEqualTo(categoryId);

        List<TbContent> tbContents = tbContentMapper.selectByExampleWithBLOBs(example);

        try {
            // 将查询内容放入缓存中
            jedisClientPool.hset(CONTENT_LIST, categoryId.toString(), JsonUtils.objectToJson(tbContents));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return tbContents;
    }
}
