package cn.polar.content.service;

import cn.polar.common.pojo.EasyUITreeNode;
import cn.polar.common.util.MallResult;

import java.util.List;

public interface ContentCategoryService {
    /**
     * 获取分类树
     * @param parentId
     * @return
     */
    List<EasyUITreeNode> getCategoryList(Long parentId);

    /**
     * 添加新分类
     * @param parentId
     * @param name
     * @return
     */
    MallResult createCategory(Long parentId, String name);

    /**
     * 通过id修改分类信息
     * @param id
     * @param name
     * @return
     */
    MallResult updateCategoryById(Long id, String name);

    /**
     * 删除节点  级联删除
     * @param id
     * @return
     */
    MallResult deleteCategoryById(Long id);
}
