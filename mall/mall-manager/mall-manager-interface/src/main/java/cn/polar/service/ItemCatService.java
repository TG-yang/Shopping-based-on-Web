package cn.polar.service;

import cn.polar.common.pojo.EasyUITreeNode;

import java.util.List;

public interface ItemCatService {
    /**
     * 获取分类列表
     * @param parentId
     * @return easyUITreeNode_pojo列表
     */
    List<EasyUITreeNode> getCatList(Long parentId);
}
