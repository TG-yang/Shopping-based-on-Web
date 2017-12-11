package cn.polar.service.impl;

import cn.polar.common.pojo.EasyUITreeNode;
import cn.polar.mapper.TbItemCatMapper;
import cn.polar.pojo.TbItemCat;
import cn.polar.pojo.TbItemCatExample;
import cn.polar.service.ItemCatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ItemCatServiceImpl implements ItemCatService {
    @Autowired
    private TbItemCatMapper itemCatMapper;

    @Override
    public List<EasyUITreeNode> getCatList(Long parentId) {
        TbItemCatExample example = new TbItemCatExample();
        TbItemCatExample.Criteria criteria = example.createCriteria();
        // 查询parentId的所有分类列表
        criteria.andParentIdEqualTo(parentId);

        List<TbItemCat> tbItemCats = itemCatMapper.selectByExample(example);

        List<EasyUITreeNode> treeNodeList = new ArrayList<>();
        for (TbItemCat tbItemCat : tbItemCats
                ) {
            EasyUITreeNode easyUITreeNode = new EasyUITreeNode();
            // 此处设置treeNode的id为类目id 主键值
            easyUITreeNode.setId(tbItemCat.getId());
            easyUITreeNode.setText(tbItemCat.getName());
            easyUITreeNode.setState(tbItemCat.getIsParent() ? "closed":"open");

            //将treeNode添加到列表中
            treeNodeList.add(easyUITreeNode);
        }
        return treeNodeList;
    }
}
