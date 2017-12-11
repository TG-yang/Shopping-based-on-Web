package cn.polar.search.mapper;

import cn.polar.common.pojo.SearchItem;

import java.util.List;

public interface ItemSearchMapper {
    List<SearchItem> getItemList();
    SearchItem getItemById(Long id);

}
