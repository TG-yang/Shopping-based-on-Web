package cn.polar.search.service;

import cn.polar.common.pojo.SearchResult;

public interface SearchService {
    SearchResult getSearchItems(String keyWord, Integer page, Integer rows) throws Exception;
}
