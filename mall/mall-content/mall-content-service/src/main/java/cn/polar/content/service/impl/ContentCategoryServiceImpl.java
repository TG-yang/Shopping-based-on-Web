package cn.polar.content.service.impl;

import cn.polar.common.pojo.EasyUITreeNode;
import cn.polar.common.util.MallResult;
import cn.polar.content.service.ContentCategoryService;
import cn.polar.mapper.TbContentCategoryMapper;
import cn.polar.pojo.TbContentCategory;
import cn.polar.pojo.TbContentCategoryExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 递归删除资源树存问题，目前只可以删除叶子节点
 * 删除存在问题，待解决
 */

@Service
public class ContentCategoryServiceImpl implements ContentCategoryService {
    @Autowired
    private TbContentCategoryMapper tbContentCategoryMapper;

    @Override
    public List<EasyUITreeNode> getCategoryList(Long parentId) {
        TbContentCategoryExample example = new TbContentCategoryExample();
        TbContentCategoryExample.Criteria criteria = example.createCriteria();
        criteria.andParentIdEqualTo(parentId);
        List<TbContentCategory> categories = tbContentCategoryMapper.selectByExample(example);

        List<EasyUITreeNode> treeNodeList = new ArrayList<>();
        for (TbContentCategory tbContentCategory : categories
                ) {
            EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
            easyUITreeNode.setText(tbContentCategory.getName());
            easyUITreeNode.setState(tbContentCategory.getIsParent() ? "closed" : "open");
            easyUITreeNode.setId(tbContentCategory.getId());

            treeNodeList.add(easyUITreeNode);

        }

        return treeNodeList;
    }

    @Override
    public MallResult createCategory(Long parentId, String name) {
        // 1插入一条分类信息
        TbContentCategory category = new TbContentCategory();
        category.setIsParent(false);
        category.setName(name);
        category.setUpdated(new Date());
        category.setCreated(new Date());
        category.setParentId(parentId);
        //排列序号，表示同级类目的展现次序，如数值相等则按名称次序排列。取值范围:大于零的整数
        category.setSortOrder(1);
        //状态。可选值:1(正常),2(删除)
        category.setStatus(1);

        tbContentCategoryMapper.insert(category);

        // 2、判断父节点的isparent是否为true，不是true需要改为true
        TbContentCategory tbContentCategory = tbContentCategoryMapper.selectByPrimaryKey(parentId);
        if (!tbContentCategory.getIsParent()) {
            tbContentCategory.setIsParent(true);
            tbContentCategory.setUpdated(new Date());
            tbContentCategoryMapper.updateByPrimaryKeySelective(tbContentCategory);
        }

        // 返回包装对象
        return MallResult.ok(category);
    }

    @Override
    public MallResult updateCategoryById(Long id, String name) {
        TbContentCategory category = new TbContentCategory();
        category.setId(id);
        category.setName(name);
        category.setUpdated(new Date());
        tbContentCategoryMapper.updateByPrimaryKeySelective(category);
        return MallResult.ok();
    }

    @Override
    public MallResult deleteCategoryById(Long id) {
        // 先获取该节点
        TbContentCategory category = tbContentCategoryMapper.selectByPrimaryKey(id);

        /*// 先获取当前节点的子节点
        TbContentCategoryExample example = new TbContentCategoryExample();
        example.createCriteria().andParentIdEqualTo(category.getId());
        List<TbContentCategory> tbContentCategories = tbContentCategoryMapper.selectByExample(example);

        // 判断条件应为  size是否大于0
        while (tbContentCategories != null && tbContentCategories.size() > 0) {
            for (TbContentCategory contentCategory : tbContentCategories
                    ) {
                //递归调用
                deleteCategoryById(contentCategory.getId());
            }

        }*/

        // 删除当前节点

        tbContentCategoryMapper.deleteByPrimaryKey(id);

        // 查询当前节点父节点下的所有节点
        TbContentCategoryExample example = new TbContentCategoryExample();
        example.createCriteria().andParentIdEqualTo(category.getId());
        List<TbContentCategory> tbContentCategories = tbContentCategoryMapper.selectByExample(example);

        // 只有当前节点的父节点无子节点时，修改其isParent值
        if(tbContentCategories == null) {

            // 删除所有子节点后，将父节点isParent改为false
            TbContentCategory parentCategory = tbContentCategoryMapper.selectByPrimaryKey(category.getParentId());
            parentCategory.setIsParent(false);
            tbContentCategoryMapper.updateByPrimaryKeySelective(parentCategory);
        }


        return MallResult.ok();
    }
}
