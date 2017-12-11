package cn.polar.content.service;

import cn.polar.common.pojo.EasyUIDataGridResult;
import cn.polar.common.util.MallResult;
import cn.polar.pojo.TbContent;

import java.util.List;

public interface ContentService {
    /**
     * 新增内容
     * @param tbContent
     * @return
     */
    MallResult addContent(TbContent tbContent);

    /**
     * 获取内容分页列表
     * @param categoryId
     * @param page
     * @param rows
     * @return
     */
    EasyUIDataGridResult<TbContent> getContentList(Long categoryId, Integer page, Integer rows);

    List<TbContent> getContentListByCid(Long categoryId);
}
