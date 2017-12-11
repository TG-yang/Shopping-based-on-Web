package cn.polar.search.service;

import cn.polar.common.util.MallResult;

public interface ImportSearchItemService {
    MallResult importItems();
    void importItemById(Long id) throws Exception;
}
